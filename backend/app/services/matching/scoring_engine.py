"""
Multi-factor Weighted Scoring Engine for JobPilot Matching.
Implements explainable, weighted calculation where required skills dominate.
Generates tailored human-readable match explanations.
"""

from dataclasses import dataclass, field
from typing import Dict, List, Set, Any


@dataclass
class MatchScoreBreakdown:
    match_score: int
    match_tier: str
    required_skill_coverage: int
    preferred_skill_coverage: int
    experience_relevance: int
    education_relevance: int
    profile_completeness: int
    matched_required: List[str]
    missing_required: List[str]
    matched_preferred: List[str]
    missing_preferred: List[str]
    strong_matches: List[str]
    weak_skills: List[str]
    explanation: str


class ScoringEngine:
    # Configurable Scoring Weights
    REQUIRED_SKILLS_WEIGHT = 0.55
    PREFERRED_SKILLS_WEIGHT = 0.10
    EXPERIENCE_PROJECTS_WEIGHT = 0.20
    EDUCATION_WEIGHT = 0.05
    PROFILE_COMPLETENESS_WEIGHT = 0.10
 
    @staticmethod
    def determine_match_tier(score: int) -> str:
        if score >= 90:
            return "Excellent Match"
        elif score >= 75:
            return "Strong Match"
        elif score >= 60:
            return "Moderate Match"
        elif score >= 40:
            return "Low Match"
        else:
            return "Weak Match"

    @classmethod
    def calculate_score(
        cls,
        job_required_skills: List[str],
        job_preferred_skills: List[str],
        candidate_canonical_skills: Set[str],
        corroborated_skills: Set[str],
        weak_skills: Set[str],
        experience_relevance: int,
        education_relevance: int,
        profile_completeness: int,
        role_title: str = "this role"
    ) -> MatchScoreBreakdown:
        """
        Computes weighted score with required skills dominance and generates explainable rationale.
        """
        # 1. Required skills evaluation
        matched_required: List[str] = []
        missing_required: List[str] = []

        for req in job_required_skills:
            if req in candidate_canonical_skills:
                matched_required.append(req)
            else:
                missing_required.append(req)

        req_coverage_pct = int(round((len(matched_required) / len(job_required_skills)) * 100)) if job_required_skills else 100

        # 2. Preferred skills evaluation
        matched_preferred: List[str] = []
        missing_preferred: List[str] = []

        for pref in job_preferred_skills:
            if pref in candidate_canonical_skills:
                matched_preferred.append(pref)
            else:
                missing_preferred.append(pref)

        pref_coverage_pct = int(round((len(matched_preferred) / len(job_preferred_skills)) * 100)) if job_preferred_skills else 100

        # 3. Strong matches: required or preferred skills actively verified in projects/experience
        strong_matches = [s for s in matched_required if s in corroborated_skills]
        if not strong_matches:
            strong_matches = matched_required[:3]

        # Weak skills in the context of this job
        matched_weak = [s for s in matched_required if s in weak_skills]

        # 4. Multi-factor Weighted Calculation
        weighted_score = (
            (req_coverage_pct * cls.REQUIRED_SKILLS_WEIGHT) +
            (pref_coverage_pct * cls.PREFERRED_SKILLS_WEIGHT) +
            (experience_relevance * cls.EXPERIENCE_PROJECTS_WEIGHT) +
            (education_relevance * cls.EDUCATION_WEIGHT) +
            (profile_completeness * cls.PROFILE_COMPLETENESS_WEIGHT)
        )

        final_score = int(round(weighted_score))

        # 5. Required Skills Dominance Penalty:
        # A candidate who matches preferred skills but misses critical required skills
        # must NOT receive an artificially high score.
        req_ratio = len(matched_required) / len(job_required_skills) if job_required_skills else 1.0
        if req_ratio < 0.35:
            final_score = min(final_score, 45)  # Cap at Low/Weak Match
        elif req_ratio < 0.50:
            final_score = min(final_score, 59)  # Cap at Low Match
        elif req_ratio < 0.70:
            final_score = min(final_score, 74)  # Cap at Moderate Match

        final_score = max(0, min(100, final_score))

        # 6. Determine Match Tier
        match_tier = cls.determine_match_tier(final_score)

        # 7. Generate Explainable Human-Readable Rationale
        explanation = cls._generate_explanation(
            final_score=final_score,
            match_tier=match_tier,
            matched_required=matched_required,
            missing_required=missing_required,
            matched_preferred=matched_preferred,
            missing_preferred=missing_preferred,
            strong_matches=strong_matches,
            role_title=role_title
        )

        return MatchScoreBreakdown(
            match_score=final_score,
            match_tier=match_tier,
            required_skill_coverage=req_coverage_pct,
            preferred_skill_coverage=pref_coverage_pct,
            experience_relevance=experience_relevance,
            education_relevance=education_relevance,
            profile_completeness=profile_completeness,
            matched_required=matched_required,
            missing_required=missing_required,
            matched_preferred=matched_preferred,
            missing_preferred=missing_preferred,
            strong_matches=strong_matches,
            weak_skills=matched_weak,
            explanation=explanation
        )

    @classmethod
    def _generate_explanation(
        cls,
        final_score: int,
        match_tier: str,
        matched_required: List[str],
        missing_required: List[str],
        matched_preferred: List[str],
        missing_preferred: List[str],
        strong_matches: List[str],
        role_title: str
    ) -> str:
        parts = []

        if final_score >= 90:
            parts.append(f"Excellent match for {role_title}! You meet nearly all core requirements.")
            if strong_matches:
                parts.append(f"Strong competencies verified in your profile: {', '.join(strong_matches)}.")
            if missing_preferred or missing_required:
                gaps = missing_required + missing_preferred
                parts.append(f"Minor gap to round out your profile: {', '.join(gaps[:2])}.")
        elif final_score >= 75:
            parts.append(f"Strong match for {role_title}.")
            if matched_required:
                parts.append(f"You match core requirements including {', '.join(matched_required[:4])}.")
            if strong_matches:
                parts.append(f"Your practical experience is corroborated by projects involving {', '.join(strong_matches[:3])}.")
            if missing_required:
                parts.append(f"Your primary growth areas are {', '.join(missing_required[:2])}.")
        elif final_score >= 60:
            parts.append(f"Moderate match ({final_score}%) for {role_title}.")
            if matched_required:
                parts.append(f"You meet foundational skills: {', '.join(matched_required[:3])}.")
            if missing_required:
                parts.append(f"Key missing required skills: {', '.join(missing_required[:3])}.")
        else:
            parts.append(f"{match_tier} ({final_score}%) for {role_title}.")
            parts.append("Main reason for lower score: Several required core engineering skills are not yet present in your career profile.")
            if missing_required:
                parts.append(f"Critical missing requirements: {', '.join(missing_required[:3])}.")
            if matched_required:
                parts.append(f"Current matches: {', '.join(matched_required)}.")

        return " ".join(parts)
