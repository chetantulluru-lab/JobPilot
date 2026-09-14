"""
Main AI Service Orchestrator for JobPilot.
Coordinates prompt generation, LLM invocation via OpenRouter, anti-hallucination validation,
and seamless deterministic fallbacks when AI is offline or rate-limited.
"""

import re
import json
import logging
from typing import Dict, List, Optional, Any

from app.core.config import settings
from app.services.ai.base import AIProvider, AIRequest, AIResponse
from app.services.ai.openrouter_provider import OpenRouterProvider
from app.services.ai.prompt_builder import PromptBuilder
from app.services.ai.validation_service import AIValidationService
from app.services.matching.skill_normalizer import SkillNormalizer
from app.services.matching.requirement_extractor import JobRequirementExtractor

logger = logging.getLogger("jobpilot.ai.service")


class AIService:
    def __init__(self, provider: Optional[AIProvider] = None):
        self._provider = provider or OpenRouterProvider()

    @property
    def is_ai_online(self) -> bool:
        return self._provider.is_available

    def generate_chat_reply(
        self,
        user_message: str,
        history: List[Dict[str, str]],
        profile_data: Optional[Dict[str, Any]] = None,
        job_context: Optional[Dict[str, Any]] = None
    ) -> Dict[str, Any]:
        """
        Processes a career assistant chat query.
        Falls back to rule-based contextual responses if AI key is unconfigured or rate-limited.
        """
        # 1. Try real AI completion if available
        if self._provider.is_available:
            try:
                messages = PromptBuilder.build_assistant_messages(
                    user_message=user_message,
                    history=history,
                    profile_data=profile_data,
                    job_context=job_context
                )
                request = AIRequest(messages=messages, temperature=0.4, max_tokens=1000)
                response = self._provider.generate_chat_completion(request)

                validated_content = AIValidationService.validate_content_grounding(
                    generated_text=response.content,
                    profile_data=profile_data or {},
                    missing_skills=job_context.get("missing_skills", []) if job_context else []
                )

                return {
                    "content": validated_content,
                    "model": response.model,
                    "is_fallback": False,
                    "tokens_used": response.tokens_used
                }
            except Exception as e:
                logger.warning(f"AI Provider error during chat generation, falling back to deterministic response: {e}")

        # 2. Deterministic Fallback Engine (Zero crash, 100% grounded in real data)
        return {
            "content": self._generate_deterministic_assistant_reply(user_message, profile_data, job_context),
            "model": "offline-rule-engine",
            "is_fallback": True,
            "tokens_used": None
        }

    def _generate_deterministic_assistant_reply(
        self,
        user_message: str,
        profile_data: Optional[Dict[str, Any]],
        job_context: Optional[Dict[str, Any]]
    ) -> str:
        """Deterministic, grounded responses for standard career queries."""
        msg_lower = user_message.lower()
        skills = []
        if profile_data and profile_data.get("skills"):
            skills = [s.get("name") if isinstance(s, dict) else str(s) for s in profile_data["skills"]]

        # Query: "What skills do I have?" / empty profile query
        if ("what" in msg_lower or "my" in msg_lower or "list" in msg_lower) and "skill" in msg_lower and "missing" not in msg_lower:
            if not skills:
                return "I don't have confirmed career information yet. Upload your resume or complete your Career Profile and I can analyze it."
            skill_list = ", ".join(skills)
            return f"Based on your confirmed Career Profile, you currently have {len(skills)} verified competencies: {skill_list}."

        # Query: "Why is my match 58%?" / "Why is my match score X?"
        if "why" in msg_lower and ("match" in msg_lower or "score" in msg_lower):
            if job_context and job_context.get("match_score") is not None:
                score = job_context["match_score"]
                tier = job_context.get("match_tier", "Match")
                matched = ", ".join(job_context.get("matched_skills", [])) or "None detected"
                missing = ", ".join(job_context.get("missing_skills", [])) or "None"
                return (
                    f"Your match score is {score}% ({tier}).\n\n"
                    f"• Strengths Identified: You matched on key requirements: {matched}.\n"
                    f"• Skill Gaps: You are currently missing: {missing}.\n\n"
                    f"In JobPilot's scoring model, required technical skills account for 55% of the score. "
                    f"Acquiring and adding projects with {missing.split(',')[0]} will have the highest immediate impact on your score."
                )
            return "To analyze your match score, please select a target job posting from the Jobs tab."

        # Query: "What skills am I missing?"
        if "missing" in msg_lower and "skill" in msg_lower:
            if job_context and job_context.get("missing_skills"):
                missing = job_context["missing_skills"]
                bullets = "\n".join([f"  {i+1}. {s}" for i, s in enumerate(missing)])
                return (
                    f"Based on the requirements for {job_context.get('title', 'this role')}, you are missing the following competencies:\n\n"
                    f"{bullets}\n\n"
                    f"Check the Skill Gaps tab to see a step-by-step learning roadmap for each of these technologies."
                )
            return "You haven't selected a job yet, or your profile already covers all listed requirements!"

        # Query: "How can I improve my resume?" / "Improve my profile"
        if "improve" in msg_lower or "tips" in msg_lower or "resume" in msg_lower:
            points = []
            if not profile_data or not profile_data.get("projects"):
                points.append("• Add at least 2 detailed technical projects showcasing real problem solving.")
            if not profile_data or not profile_data.get("skills"):
                points.append("• List your core programming languages, frameworks, and databases.")
            if not profile_data or not profile_data.get("education"):
                points.append("• Complete your education details and field of study.")
            if not points:
                points.append("• Ensure all project descriptions use quantifiable action verbs (e.g. 'Reduced latency by 20%', 'Built REST APIs handling 500 req/s').")
                points.append("• Verify that every skill listed in your profile is corroborated in your project tech stacks.")
            return "Here are the top high-impact improvements for your profile:\n\n" + "\n".join(points)

        # Default fallback
        user_name = profile_data.get("full_name", "there") if profile_data else "there"
        skill_count = len(skills)
        return (
            f"Hello {user_name}! I'm your JobPilot AI Career Pilot. "
            f"You currently have {skill_count} verified skills on file. "
            f"You can ask me questions like:\n"
            f"• 'Why is my match score low for this job?'\n"
            f"• 'What skills am I missing?'\n"
            f"• 'How should I improve my project descriptions?'\n"
            f"• 'Generate a cover letter for my target job'"
        )

    def tailor_resume(
        self,
        profile_data: Dict[str, Any],
        target_job: Dict[str, Any]
    ) -> Dict[str, Any]:
        """Tailors candidate's resume for a specific job without fabricating experience."""
        # 1. Try AI generation
        if self._provider.is_available:
            try:
                messages = PromptBuilder.build_tailoring_messages(profile_data, target_job)
                request = AIRequest(messages=messages, temperature=0.3, max_tokens=1500)
                response = self._provider.generate_chat_completion(request)
                
                return {
                    "tailored_summary": self._extract_section(response.content, "TAILORED_SUMMARY") or self._generate_fallback_summary(profile_data, target_job),
                    "matched_keywords_to_emphasize": target_job.get("matched_skills", []),
                    "missing_skills_notice": target_job.get("missing_skills", []),
                    "full_tailored_text": response.content,
                    "is_fallback": False
                }
            except Exception as e:
                logger.warning(f"AI tailoring failed, using deterministic tailoring: {e}")

        # 2. Deterministic Fallback Tailoring
        return {
            "tailored_summary": self._generate_fallback_summary(profile_data, target_job),
            "matched_keywords_to_emphasize": target_job.get("matched_skills", []),
            "missing_skills_notice": target_job.get("missing_skills", []),
            "full_tailored_text": self._generate_fallback_summary(profile_data, target_job),
            "is_fallback": True
        }

    def _generate_fallback_summary(self, profile: Dict[str, Any], job: Dict[str, Any]) -> str:
        role = job.get("title", "Software Engineer")
        skills = profile.get("skills", [])
        top_skills = [s.get("name") if isinstance(s, dict) else str(s) for s in skills[:4]]
        skills_str = ", ".join(top_skills) if top_skills else "modern software technologies"
        return (
            f"Motivated engineer with proven experience in {skills_str}. "
            f"Passionate about applying rigorous engineering principles to contribute effectively as a {role}. "
            f"Demonstrated ability to build reliable applications, write clean code, and rapidly master new systems."
        )

    def generate_cover_letter(
        self,
        profile_data: Dict[str, Any],
        target_job: Dict[str, Any],
        tone: str = "Professional"
    ) -> Dict[str, Any]:
        """Generates a truthful, job-tailored cover letter."""
        if self._provider.is_available:
            try:
                messages = PromptBuilder.build_cover_letter_messages(profile_data, target_job, tone)
                request = AIRequest(messages=messages, temperature=0.4, max_tokens=1200)
                response = self._provider.generate_chat_completion(request)
                cleaned = AIValidationService.validate_and_clean_cover_letter(response.content, profile_data)
                return {"content": cleaned, "is_fallback": False, "tone": tone}
            except Exception as e:
                logger.warning(f"AI cover letter generation failed: {e}")

        # Deterministic Fallback
        company = target_job.get("company", "Hiring Team")
        role = target_job.get("title", "the position")
        candidate_name = profile_data.get("full_name") or "Candidate"
        skills = [s.get("name") if isinstance(s, dict) else str(s) for s in profile_data.get("skills", [])[:3]]
        skills_str = ", ".join(skills) if skills else "software development"

        content = (
            f"Dear {company} Hiring Team,\n\n"
            f"I am writing to express my strong interest in the {role} position. "
            f"With a strong foundation in {skills_str}, I am excited about the opportunity to contribute to your team's goals.\n\n"
            f"Throughout my projects and experience, I have developed a disciplined approach to building reliable software, "
            f"solving complex challenges, and collaborating across engineering workflows. I am eager to bring this same dedication "
            f"and problem-solving drive to {company}.\n\n"
            f"Thank you for your time and consideration. I welcome the opportunity to discuss how my background aligns with your needs.\n\n"
            f"Sincerely,\n{candidate_name}"
        )
        return {"content": content, "is_fallback": True, "tone": tone}

    def generate_recruiter_message(
        self,
        profile_data: Dict[str, Any],
        target_job: Dict[str, Any],
        recipient_name: Optional[str] = "Hiring Manager",
        platform: str = "LinkedIn",
        user_context: Optional[str] = None
    ) -> Dict[str, Any]:
        """Generates a short, effective outreach message."""
        if self._provider.is_available:
            try:
                messages = PromptBuilder.build_recruiter_message_messages(
                    profile_data, target_job, recipient_name, platform, user_context
                )
                request = AIRequest(messages=messages, temperature=0.3, max_tokens=1000)
                response = self._provider.generate_chat_completion(request)
                return {"content": response.content.strip(), "is_fallback": False}
            except Exception as e:
                logger.warning(f"AI outreach generation failed: {e}")

        # Deterministic Fallback
        name = recipient_name or "Hiring Manager"
        role = target_job.get("title", "the open role")
        company = target_job.get("company", "your company")
        candidate_name = profile_data.get("full_name") or "Candidate"
        skills = [s.get("name") if isinstance(s, dict) else str(s) for s in profile_data.get("skills", [])[:2]]
        skill_mention = f"working with {', '.join(skills)}" if skills else "engineering reliable systems"

        content = (
            f"Hi {name},\n\n"
            f"I hope you're having a productive week. I noticed the {role} opening at {company} and wanted to reach out. "
            f"Given my background {skill_mention}, I believe my skills are strongly aligned with your requirements.\n\n"
            f"I've formally applied, but would love to connect and briefly share how I can add immediate value to your team. "
            f"Best regards,\n{candidate_name}"
        )
        return {"content": content, "is_fallback": True}

    def analyze_job_description(self, raw_jd_text: str) -> Dict[str, Any]:
        """Analyzes unstructured job description into structured requirements."""
        if not raw_jd_text or not raw_jd_text.strip():
            raise ValueError("Job description text cannot be empty.")

        # 1. Try AI extraction
        if self._provider.is_available:
            try:
                messages = PromptBuilder.build_jd_analysis_messages(raw_jd_text)
                request = AIRequest(messages=messages, temperature=0.1, max_tokens=800)
                response = self._provider.generate_chat_completion(request)
                
                # Extract json from response
                raw = response.content.strip()
                if "```json" in raw:
                    raw = raw.split("```json")[1].split("```")[0].strip()
                elif "```" in raw:
                    raw = raw.split("```")[1].split("```")[0].strip()
                
                parsed = json.loads(raw)
                # Normalize skills
                if "required_skills" in parsed and isinstance(parsed["required_skills"], list):
                    parsed["required_skills"] = [SkillNormalizer.normalize_skill(s) for s in parsed["required_skills"] if s]
                if "preferred_skills" in parsed and isinstance(parsed["preferred_skills"], list):
                    parsed["preferred_skills"] = [SkillNormalizer.normalize_skill(s) for s in parsed["preferred_skills"] if s]
                parsed["is_ai_parsed"] = True
                return parsed
            except Exception as e:
                logger.warning(f"AI JD extraction failed, using deterministic extractor: {e}")

        # 2. Deterministic Fallback using Phase 3B RequirementExtractor
        reqs = JobRequirementExtractor.extract_requirements(
            description=raw_jd_text,
            role_title="Target Position"
        )
        return {
            "title": reqs.role_title or "Position Analyzed",
            "company": "Company (from JD)",
            "location": "Remote / Unspecified",
            "work_mode": "Remote",
            "min_experience_years": reqs.min_experience_years,
            "education_degree": reqs.min_education_degree,
            "required_skills": reqs.required_skills,
            "preferred_skills": reqs.preferred_skills,
            "responsibilities": ["Refer to full job posting text for detailed responsibilities."],
            "salary_range": None,
            "is_ai_parsed": False
        }

    def _extract_section(self, text: str, header: str) -> Optional[str]:
        """Helper to extract a tagged section from LLM output."""
        pattern = rf"\[{header}\]\s*([\s\S]*?)(?:\[[A-Z_]+\]|$)"
        match = re.search(pattern, text)
        return match.group(1).strip() if match else None


# Global AIService singleton
ai_service = AIService()
