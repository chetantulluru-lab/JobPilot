"""
Relevance evaluation engines for Experience and Education.
Assesses candidate project/work corroboration and degree field relevance without fabricating data.
"""

from typing import Dict, List, Set, Optional
import re

RELATED_CS_FIELDS: Set[str] = {
    "computer science", "information technology", "software engineering",
    "computer engineering", "data science", "artificial intelligence",
    "machine learning", "cybersecurity", "electrical engineering",
    "electronics and communication", "mathematics", "physics", "information systems"
}


class ExperienceMatcher:
    @staticmethod
    def calculate_experience_relevance(
        job_required_skills: List[str],
        job_preferred_skills: List[str],
        candidate_project_techs: Set[str],
        candidate_experience_techs: Set[str],
        corroborated_skills: Set[str]
    ) -> int:
        """
        Calculates experience relevance score (0 to 100) based on practical tech stack overlap
        and corroborated skill usage in real-world projects and roles.
        """
        if not job_required_skills:
            return 80

        all_practical_techs = candidate_project_techs.union(candidate_experience_techs)
        if not all_practical_techs:
            return 40  # Candidate has skills listed but no project/experience descriptions yet

        # 1. Overlap with required skills
        req_overlap = set(job_required_skills).intersection(all_practical_techs)
        req_ratio = len(req_overlap) / len(job_required_skills)

        # 2. Corroborated skills bonus (skills formally listed and used in projects)
        corr_overlap = set(job_required_skills).intersection(corroborated_skills)
        corr_ratio = len(corr_overlap) / len(job_required_skills)

        # 3. Overlap with preferred skills
        pref_ratio = 0.0
        if job_preferred_skills:
            pref_overlap = set(job_preferred_skills).intersection(all_practical_techs)
            pref_ratio = len(pref_overlap) / len(job_preferred_skills)

        # Weighted experience score
        score = (req_ratio * 65.0) + (corr_ratio * 25.0) + (pref_ratio * 10.0)
        return int(round(min(100.0, max(10.0, score))))


class EducationMatcher:
    @staticmethod
    def calculate_education_relevance(
        job_degree_req: Optional[str],
        candidate_degrees: List[Dict[str, str]]
    ) -> int:
        """
        Evaluates educational alignment, supporting related fields like AI, Data Science, and IT
        for Computer Science requirements.
        """
        if not candidate_degrees:
            return 60  # Degree information not provided in profile

        if not job_degree_req:
            # Job posting doesn't mandate a specific degree
            return 90 if candidate_degrees else 75

        job_req_lower = job_degree_req.lower()

        for cand_edu in candidate_degrees:
            raw_field = cand_edu.get("field_of_study") or cand_edu.get("field") or ""
            field = raw_field.lower()
            degree = cand_edu.get("degree", "").lower()

            # Check field match
            is_related_field = False
            if "computer science" in job_req_lower or "related" in job_req_lower or "engineering" in job_req_lower:
                if any(rf in field for rf in RELATED_CS_FIELDS):
                    is_related_field = True
            elif field and field in job_req_lower:
                is_related_field = True

            # Check degree level (Bachelor's, Master's, etc.)
            has_degree_level = False
            if "bachelor" in job_req_lower or "b.s" in job_req_lower or "b.tech" in job_req_lower:
                if any(b in degree for b in ["bachelor", "b.s", "b.tech", "b.e", "master", "m.s", "m.tech", "ph.d"]):
                    has_degree_level = True
            elif "master" in job_req_lower or "m.s" in job_req_lower:
                if any(m in degree for m in ["master", "m.s", "m.tech", "ph.d"]):
                    has_degree_level = True
            else:
                has_degree_level = True

            if is_related_field and has_degree_level:
                return 95
            elif is_related_field:
                return 85
            elif has_degree_level:
                return 50
            else:
                return 30

        return 40
