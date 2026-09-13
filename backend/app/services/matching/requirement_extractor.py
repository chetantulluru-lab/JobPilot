"""
Job Requirement Extractor for JobPilot Matching Engine.
Extracts structured requirements (Required Skills, Preferred Skills, Experience, Degree)
from job descriptions, requirements text, and explicit job model columns.
"""

import re
from dataclasses import dataclass, field
from typing import List, Optional, Set
from app.services.matching.skill_normalizer import SkillNormalizer


@dataclass
class JobRequirements:
    required_skills: List[str] = field(default_factory=list)
    preferred_skills: List[str] = field(default_factory=list)
    min_experience_years: Optional[int] = None
    min_education_degree: Optional[str] = None
    role_title: str = ""


class JobRequirementExtractor:
    # Regex patterns to detect section headings in job postings
    REQUIRED_SECTION_REGEX = re.compile(
        r"(?:required\s+skills?|minimum\s+qualifications?|must\s+haves?|what\s+you(?:'ll)?\s+need|requirements?|core\s+qualifications?):?",
        re.IGNORECASE
    )

    PREFERRED_SECTION_REGEX = re.compile(
        r"(?:preferred\s+qualifications?|preferred\s+skills?|nice\s+to\s+haves?|bonus\s+points?|pluses?|good\s+to\s+have|desired\s+skills?):?",
        re.IGNORECASE
    )

    EXPERIENCE_REGEX = re.compile(
        r"(\d+)\+?\s*(?:-\s*(\d+))?\s*years?(?:\s+of)?(?:\s+experience)?",
        re.IGNORECASE
    )

    DEGREE_REGEX = re.compile(
        r"\b(Bachelor(?:'s)?|B\.?S\.?|B\.?Tech|B\.?E\.?|Master(?:'s)?|M\.?S\.?|M\.?Tech|Ph\.?D|Doctorate)\b",
        re.IGNORECASE
    )

    @classmethod
    def extract_requirements(
        cls,
        description: str,
        requirements_text: Optional[str] = None,
        skills_required_str: Optional[str] = None,
        preferred_skills_str: Optional[str] = None,
        education_req_str: Optional[str] = None,
        role_title: str = ""
    ) -> JobRequirements:
        """
        Extracts classified required vs preferred competencies from job text and model attributes.
        """
        required_set: Set[str] = set()
        preferred_set: Set[str] = set()

        # 1. Ingest explicit job columns if present
        if skills_required_str:
            for s in skills_required_str.split(","):
                norm = SkillNormalizer.normalize_skill(s)
                if norm:
                    required_set.add(norm)

        if preferred_skills_str:
            for s in preferred_skills_str.split(","):
                norm = SkillNormalizer.normalize_skill(s)
                if norm:
                    preferred_set.add(norm)

        # 2. Segment job description and requirements text into blocks
        combined_text = f"{requirements_text or ''}\n\n{description or ''}"
        lines = combined_text.split("\n")

        current_mode = "GENERAL"  # "REQUIRED", "PREFERRED", "GENERAL"

        for line in lines:
            trimmed = line.strip()
            if not trimmed:
                continue

            if cls.REQUIRED_SECTION_REGEX.search(trimmed) and len(trimmed) < 60:
                current_mode = "REQUIRED"
                continue
            elif cls.PREFERRED_SECTION_REGEX.search(trimmed) and len(trimmed) < 60:
                current_mode = "PREFERRED"
                continue

            # Extract skills found in the current line
            extracted = SkillNormalizer.extract_skills_from_prose(trimmed)
            if extracted:
                if current_mode == "REQUIRED":
                    required_set.update(extracted)
                elif current_mode == "PREFERRED":
                    preferred_set.update(extracted)
                elif not required_set:
                    # If we haven't found a required section yet, initial skills lean to required
                    required_set.update(extracted)

        # 3. Disambiguation: Required skills take strict precedence over Preferred
        preferred_set = preferred_set - required_set

        # Fallback: If nothing was found, default to Python if empty
        if not required_set and not preferred_set:
            required_set.add("Python")

        # 4. Extract minimum experience years
        min_years = None
        exp_match = cls.EXPERIENCE_REGEX.search(combined_text)
        if exp_match:
            try:
                min_years = int(exp_match.group(1))
            except Exception:
                pass

        # 5. Extract degree requirement
        degree = education_req_str
        if not degree:
            degree_match = cls.DEGREE_REGEX.search(combined_text)
            degree = degree_match.group(0) if degree_match else None

        return JobRequirements(
            required_skills=sorted(list(required_set)),
            preferred_skills=sorted(list(preferred_set)),
            min_experience_years=min_years,
            min_education_degree=degree,
            role_title=role_title
        )
