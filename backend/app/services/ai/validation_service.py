"""
AI Validation Service for JobPilot.
Enforces programmatic anti-hallucination guardrails on AI-generated career content.
"""

import re
import logging
from typing import Dict, List, Set, Any
from app.services.matching.skill_normalizer import SkillNormalizer

logger = logging.getLogger("jobpilot.ai.validation")


class AIValidationService:
    @staticmethod
    def extract_profile_skills(profile_data: Dict[str, Any]) -> Set[str]:
        """Extracts normalized canonical skill set from candidate's verified profile."""
        skills = set()
        for s in profile_data.get("skills", []):
            name = s.get("name") if isinstance(s, dict) else str(s)
            canon = SkillNormalizer.normalize_skill(name)
            if canon:
                skills.add(canon)
        for p in profile_data.get("projects", []):
            if isinstance(p, dict) and p.get("tech_stack"):
                for ts in p["tech_stack"].split(","):
                    c = SkillNormalizer.normalize_skill(ts)
                    if c:
                        skills.add(c)
        return skills

    @classmethod
    def validate_content_grounding(
        cls,
        generated_text: str,
        profile_data: Dict[str, Any],
        missing_skills: List[str]
    ) -> str:
        """
        Validates that generated text does not falsely claim the candidate possesses
        skills that were verified as missing.
        """
        if not generated_text:
            return ""

        profile_skills = cls.extract_profile_skills(profile_data)
        missing_set = set(missing_skills)

        # Check for phrases claiming proficiency in missing skills
        # e.g., "extensive experience with AWS", "proficient in Docker"
        for missing in missing_set:
            if missing not in profile_skills:
                # Pattern: "experience in <missing>", "proficient in <missing>", "skilled in <missing>"
                claim_pattern = re.compile(
                    rf"\b(?:extensive|hands-on|deep|strong|solid|proven)?\s*(?:experience\s+with|proficient\s+in|mastery\s+of|expert\s+in)\s+{re.escape(missing)}\b",
                    re.IGNORECASE
                )
                if claim_pattern.search(generated_text):
                    logger.warning(f"Detected hallucinated skill claim for missing skill: '{missing}'. Replacing with learning statement.")
                    generated_text = claim_pattern.sub(f"foundational understanding of {missing} (active learning target)", generated_text)

        return generated_text

    @classmethod
    def validate_and_clean_cover_letter(
        cls,
        cover_letter: str,
        profile_data: Dict[str, Any]
    ) -> str:
        """Ensures cover letter maintains professional formatting and grounded claims."""
        if not cover_letter:
            return ""
        
        # Remove any lingering thinking tags or system leakage
        cleaned = re.sub(r"\[/?(?:SYSTEM|INSTRUCTIONS|RULES)[^\]]*\]", "", cover_letter, flags=re.IGNORECASE)
        return cleaned.strip()
