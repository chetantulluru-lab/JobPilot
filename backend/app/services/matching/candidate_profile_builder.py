"""
Candidate Profile Builder for JobPilot Matching Engine.
Gathers, normalizes, and classifies candidate competencies strictly from the confirmed Career Profile.
"""

from dataclasses import dataclass, field
from typing import Dict, List, Optional, Set
from app.models.career_profile import CareerProfile
from app.services.matching.skill_normalizer import SkillNormalizer


@dataclass
class CandidateProfileData:
    canonical_skills: Set[str] = field(default_factory=set)
    corroborated_skills: Set[str] = field(default_factory=set)  # Skills verified in projects/experience
    weak_skills: Set[str] = field(default_factory=set)          # Skills marked beginner or low confidence
    project_techs: Set[str] = field(default_factory=set)
    experience_techs: Set[str] = field(default_factory=set)
    education_degrees: List[Dict[str, str]] = field(default_factory=list)
    profile_completeness: int = 50


class CandidateProfileBuilder:
    @classmethod
    def build_from_career_profile(cls, profile: Optional[CareerProfile]) -> CandidateProfileData:
        """
        Builds a structured competency profile from a user's confirmed CareerProfile.
        Does NOT invent information; if fields are missing, records them accurately.
        """
        if not profile:
            return CandidateProfileData(profile_completeness=20)

        canonical_skills: Set[str] = set()
        weak_skills: Set[str] = set()
        project_techs: Set[str] = set()
        experience_techs: Set[str] = set()

        # 1. Ingest confirmed skills
        for s in (profile.skills or []):
            canon = SkillNormalizer.normalize_skill(s.name)
            if canon:
                canonical_skills.add(canon)
                if getattr(s, "proficiency", "").lower() == "beginner":
                    weak_skills.add(canon)

        # 2. Extract technologies from projects
        for p in (profile.projects or []):
            if p.tech_stack:
                for ts in p.tech_stack.split(","):
                    c = SkillNormalizer.normalize_skill(ts)
                    if c:
                        project_techs.add(c)
            if p.description:
                extracted = SkillNormalizer.extract_skills_from_prose(p.description)
                project_techs.update(extracted)

        # 3. Extract technologies from experience
        for exp in (profile.experience or []):
            if exp.description:
                extracted = SkillNormalizer.extract_skills_from_prose(exp.description)
                experience_techs.update(extracted)

        # 4. Corroborated Skills: skills listed in profile AND actively demonstrated in projects/experience
        corroborated_skills = canonical_skills.intersection(project_techs.union(experience_techs))

        # 5. Degrees and Education
        education_degrees = []
        for edu in (profile.education or []):
            education_degrees.append({
                "degree": edu.degree or "",
                "field_of_study": edu.field_of_study or "",
                "institution": edu.institution or ""
            })

        completeness = profile.profile_strength if profile.profile_strength else 50

        return CandidateProfileData(
            canonical_skills=canonical_skills,
            corroborated_skills=corroborated_skills,
            weak_skills=weak_skills,
            project_techs=project_techs,
            experience_techs=experience_techs,
            education_degrees=education_degrees,
            profile_completeness=completeness
        )
