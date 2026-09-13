"""
Prompt Builder for JobPilot AI Engine.
Constructs safe, strictly grounded prompts with explicit anti-hallucination guardrails.
"""

from typing import Dict, List, Optional, Any
from app.services.ai.base import AIMessagePayload


SYSTEM_CAREER_SAFETY_PROMPT = """You are JobPilot AI, an elite, highly professional AI career pilot and career coach.
You assist candidates in landing rewarding roles through honest, high-impact career guidance.

CRITICAL INTEGRITY AND SAFETY RULES:
1. ZERO FABRICATION POLICY: You must NEVER invent or claim employment history, companies, job titles, dates, technologies, degrees, certifications, metrics, or achievements that do not exist in the candidate's confirmed profile.
2. MISSING INFORMATION HANDLING: If a job requires a skill or requirement that the candidate does not have, you MUST explicitly point out that it is missing. Never claim the candidate has experience in tools they do not possess.
3. ADAPTIVE COACHING: You may suggest better phrasing, highlight transferable strengths, quantify achievements if the candidate provides numbers, and format content professionally.
4. ATS & CLARITY OPTIMIZATION: Write clear, active, concise prose using strong action verbs without buzzword fluff.
5. GROUNDED ADVICE: When asked 'Why is my match 58%?' or similar, use the real match breakdown provided in the context to explain exactly which required skills are matched and which are missing.
"""


class PromptBuilder:
    @staticmethod
    def _format_profile_summary(profile_data: Optional[Dict[str, Any]]) -> str:
        if not profile_data:
            return "No verified profile information on file."
        
        lines = []
        if profile_data.get("headline"):
            lines.append(f"Headline: {profile_data['headline']}")
        if profile_data.get("summary"):
            lines.append(f"Summary: {profile_data['summary']}")
        
        skills = profile_data.get("skills", [])
        if skills:
            skill_strs = [s.get("name") if isinstance(s, dict) else str(s) for s in skills]
            lines.append(f"Verified Skills: {', '.join(skill_strs)}")
        
        exps = profile_data.get("experience", [])
        if exps:
            lines.append("Work Experience:")
            for exp in exps:
                company = exp.get("company", "Company")
                role = exp.get("title") or exp.get("role", "Role")
                start = exp.get("start_date", "Date unverified")
                end = "Present" if exp.get("is_current") else exp.get("end_date", "Date unverified")
                desc = exp.get("description", "")
                lines.append(f" - {role} at {company} ({start} - {end}): {desc}")
        
        projs = profile_data.get("projects", [])
        if projs:
            lines.append("Projects:")
            for p in projs:
                title = p.get("title") or p.get("name", "Project")
                tech = p.get("tech_stack") or ", ".join(p.get("technologies", []))
                desc = p.get("description", "")
                lines.append(f" - {title} (Tech: {tech}): {desc}")
        
        edus = profile_data.get("education", [])
        if edus:
            lines.append("Education:")
            for e in edus:
                deg = e.get("degree", "Degree")
                field = e.get("field_of_study") or e.get("field", "")
                inst = e.get("institution", "Institution")
                lines.append(f" - {deg} in {field} from {inst}")
        
        return "\n".join(lines) if lines else "Candidate profile is currently empty."

    @staticmethod
    def _format_job_context(job_context: Optional[Dict[str, Any]]) -> str:
        if not job_context:
            return "No target job currently selected."
        
        lines = [
            f"Target Job: {job_context.get('title', 'Role')} at {job_context.get('company', 'Company')}",
            f"Location: {job_context.get('location', 'Remote')}",
            f"Required Skills: {job_context.get('skills_required', '')}",
        ]
        if job_context.get("preferred_skills"):
            lines.append(f"Preferred Skills: {job_context['preferred_skills']}")
        if job_context.get("match_score") is not None:
            lines.append(f"Authoritative Deterministic Match Score: {job_context['match_score']}% ({job_context.get('match_tier', '')})")
        if job_context.get("matched_skills"):
            lines.append(f"Matched Skills: {', '.join(job_context['matched_skills'])}")
        if job_context.get("missing_skills"):
            lines.append(f"Missing Skills: {', '.join(job_context['missing_skills'])}")
        return "\n".join(lines)

    @classmethod
    def build_assistant_messages(
        cls,
        user_message: str,
        history: List[Dict[str, str]],
        profile_data: Optional[Dict[str, Any]] = None,
        job_context: Optional[Dict[str, Any]] = None
    ) -> List[AIMessagePayload]:
        """Constructs prompt for multi-turn career assistant chat with grounded context."""
        context_block = f"""--- CANDIDATE CONFIRMED PROFILE ---
{cls._format_profile_summary(profile_data)}

--- ACTIVE JOB / MATCH CONTEXT ---
{cls._format_job_context(job_context)}
"""
        messages = [
            AIMessagePayload(role="system", content=f"{SYSTEM_CAREER_SAFETY_PROMPT}\n\n{context_block}")
        ]

        # Append previous conversation history (last 8 messages for context window management)
        for h in history[-8:]:
            role = "user" if h.get("sender") == "user" or h.get("role") == "user" else "assistant"
            messages.append(AIMessagePayload(role=role, content=h.get("content", "")))

        messages.append(AIMessagePayload(role="user", content=user_message))
        return messages

    @classmethod
    def build_tailoring_messages(
        cls,
        profile_data: Dict[str, Any],
        target_job: Dict[str, Any],
        existing_resume_text: Optional[str] = None
    ) -> List[AIMessagePayload]:
        """Constructs prompt for tailoring resume strictly to target role."""
        prompt = f"""You are tailoring the candidate's verified resume for the position of {target_job.get('title')} at {target_job.get('company')}.

CANDIDATE VERIFIED DATA:
{cls._format_profile_summary(profile_data)}

TARGET JOB REQUIREMENTS:
{cls._format_job_context(target_job)}
Description: {target_job.get('description', '')}

INSTRUCTIONS:
1. Re-organize and highlight the candidate's existing projects and skills that directly match this role.
2. Craft an ATS-optimized professional summary tailored for this position based ONLY on candidate's real experience.
3. List the top matching keywords from the candidate's background to emphasize.
4. List which required skills the candidate is missing and cannot claim.
5. Provide improved bullet points for the candidate's real projects emphasizing relevant impact.

Return structured response with sections:
[TAILORED_SUMMARY]
[KEY_MATCHES_TO_EMPHASIZE]
[MISSING_SKILLS_NOTICE]
[PROJECT_BULLET_IMPROVEMENTS]
"""
        return [
            AIMessagePayload(role="system", content=SYSTEM_CAREER_SAFETY_PROMPT),
            AIMessagePayload(role="user", content=prompt)
        ]

    @classmethod
    def build_cover_letter_messages(
        cls,
        profile_data: Dict[str, Any],
        job_data: Dict[str, Any],
        tone: str = "Professional"
    ) -> List[AIMessagePayload]:
        """Constructs prompt for job-specific cover letter."""
        prompt = f"""Generate a compelling, honest cover letter for {job_data.get('title')} at {job_data.get('company')}.
Tone: {tone}

CANDIDATE DATA:
{cls._format_profile_summary(profile_data)}

JOB DETAILS:
{cls._format_job_context(job_data)}

RULES:
1. Ground every claim strictly in the candidate's actual projects, skills, or experience listed above.
2. Do not invent past employers, awards, or technical tools not found in the profile.
3. Keep the letter focused on value: how candidate's demonstrated skills align with the company's needs.
4. Standard professional format: 3-4 concise paragraphs with clear call-to-action.
"""
        return [
            AIMessagePayload(role="system", content=SYSTEM_CAREER_SAFETY_PROMPT),
            AIMessagePayload(role="user", content=prompt)
        ]

    @classmethod
    def build_recruiter_message_messages(
        cls,
        profile_data: Dict[str, Any],
        job_data: Dict[str, Any],
        recipient_name: Optional[str] = "Hiring Manager",
        platform: str = "LinkedIn",
        user_context: Optional[str] = None
    ) -> List[AIMessagePayload]:
        """Constructs prompt for high-response outreach message."""
        prompt = f"""Draft a short, professional {platform} outreach message to {recipient_name or 'the recruiter'} regarding {job_data.get('title')} at {job_data.get('company')}.

CANDIDATE DATA:
{cls._format_profile_summary(profile_data)}

RELEVANT CONTEXT:
{user_context or 'Inquiring about opening and briefly sharing alignment.'}

RULES:
1. Maximum 100-140 words. Very concise, polite, and memorable.
2. Mention 1-2 specific matching skills or projects from candidate's verified profile.
3. Zero fluff, zero fake metrics.
"""
        return [
            AIMessagePayload(role="system", content=SYSTEM_CAREER_SAFETY_PROMPT),
            AIMessagePayload(role="user", content=prompt)
        ]

    @classmethod
    def build_jd_analysis_messages(cls, raw_jd_text: str) -> List[AIMessagePayload]:
        """Constructs prompt to analyze and parse unformatted job descriptions."""
        prompt = f"""Analyze the following job description and extract structured attributes in clean JSON format:

JOB DESCRIPTION TEXT:
\"\"\"{raw_jd_text}\"\"\"

Extract and format JSON strictly matching this schema:
{{
  "title": "<Job Title>",
  "company": "<Company Name or 'Not specified'>",
  "location": "<Location or 'Remote'>",
  "work_mode": "<Remote / Hybrid / On-site>",
  "min_experience_years": <number or null>,
  "education_degree": "<bachelor / master / etc or null>",
  "required_skills": ["<skill1>", "<skill2>"],
  "preferred_skills": ["<skill1>", "<skill2>"],
  "responsibilities": ["<resp1>", "<resp2>"],
  "salary_range": "<salary or null>"
}}
Return ONLY valid raw JSON without extra markdown.
"""
        return [
            AIMessagePayload(role="system", content="You are a precise data extraction specialist. Output valid JSON only."),
            AIMessagePayload(role="user", content=prompt)
        ]
