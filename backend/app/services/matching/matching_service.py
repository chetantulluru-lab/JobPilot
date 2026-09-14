"""
Matching Service Orchestrator for JobPilot Phase 3B.
Coordinates requirement extraction, profile evaluation, weighted scoring,
explainability generation, and PostgreSQL persistence with cache awareness.
"""

import json
from datetime import datetime, timezone
from typing import List, Optional
from sqlalchemy.orm import Session

from app.models.job import Job
from app.models.career_profile import CareerProfile
from app.models.job_match import JobMatch, SkillGap
from app.schemas.job import JobMatchResponse, SkillGapResponse, SkillGapItem
from app.services.matching.requirement_extractor import JobRequirementExtractor
from app.services.matching.candidate_profile_builder import CandidateProfileBuilder
from app.services.matching.relevance_engine import ExperienceMatcher, EducationMatcher
from app.services.matching.scoring_engine import ScoringEngine
from app.services.matching.skill_gap_engine import SkillGapEngine

CURRENT_MATCHER_VERSION = "3.0-rule"


class MatchingService:
    @classmethod
    def calculate_match(
        cls,
        db: Session,
        user_id: str,
        job: Job,
        force_refresh: bool = False
    ) -> JobMatchResponse:
        """
        Executes the intelligent multi-factor matching pipeline and caches results in PostgreSQL.
        """
        if isinstance(job, str):
            job_obj = db.query(Job).filter(Job.id == job).first()
            if not job_obj:
                raise ValueError(f"Job with ID '{job}' not found.")
            job = job_obj

        # 1. Check existing cached match
        existing_match = db.query(JobMatch).filter(
            JobMatch.user_id == user_id,
            JobMatch.job_id == job.id
        ).first()

        # Check if cache is still valid
        if existing_match and not force_refresh and existing_match.matcher_version == CURRENT_MATCHER_VERSION:
            return cls._build_response_from_model(job, existing_match)

        # 2. Extract classified job requirements
        reqs = JobRequirementExtractor.extract_requirements(
            description=job.description,
            requirements_text=job.requirements,
            skills_required_str=job.skills_required,
            preferred_skills_str=job.preferred_skills,
            education_req_str=job.education_requirement,
            role_title=job.title
        )

        # 3. Build candidate profile competency data
        profile = db.query(CareerProfile).filter(CareerProfile.user_id == user_id).first()
        cand_data = CandidateProfileBuilder.build_from_career_profile(profile)

        # Zero-fabrication rule: If user has no confirmed competencies, stop before scoring
        has_competencies = bool(cand_data.canonical_skills or cand_data.project_techs or cand_data.experience_techs)
        if not has_competencies:
            return JobMatchResponse(
                job_id=job.id,
                job_title=job.title,
                company=job.company,
                match_score=None,
                match_tier="Match Unavailable",
                matched_skills=[],
                missing_skills=reqs.required_skills,
                explanation="Complete your Career Profile or upload your resume to unlock AI job matching.",
                is_profile_insufficient=True,
                strong_matches=[],
                missing_required_skills=reqs.required_skills,
                missing_preferred_skills=reqs.preferred_skills,
                weak_skills=[],
                experience_relevance=0,
                education_relevance=0,
                profile_completeness=cand_data.profile_completeness,
                skill_gap_count=len(reqs.required_skills)
            )

        # 4. Evaluate Experience and Education Relevance
        exp_relevance = ExperienceMatcher.calculate_experience_relevance(
            job_required_skills=reqs.required_skills,
            job_preferred_skills=reqs.preferred_skills,
            candidate_project_techs=cand_data.project_techs,
            candidate_experience_techs=cand_data.experience_techs,
            corroborated_skills=cand_data.corroborated_skills
        )

        edu_relevance = EducationMatcher.calculate_education_relevance(
            job_degree_req=reqs.min_education_degree,
            candidate_degrees=cand_data.education_degrees
        )

        # 5. Compute Weighted Score and Rationale
        score_breakdown = ScoringEngine.calculate_score(
            job_required_skills=reqs.required_skills,
            job_preferred_skills=reqs.preferred_skills,
            candidate_canonical_skills=cand_data.canonical_skills,
            corroborated_skills=cand_data.corroborated_skills,
            weak_skills=cand_data.weak_skills,
            experience_relevance=exp_relevance,
            education_relevance=edu_relevance,
            profile_completeness=cand_data.profile_completeness,
            role_title=job.title
        )

        # 6. Generate Skill Gap Roadmap
        roadmap_items = SkillGapEngine.generate_roadmap(
            missing_required=score_breakdown.missing_required,
            missing_preferred=score_breakdown.missing_preferred,
            role_title=job.title
        )

        # 7. Persist or Update JobMatch in Database
        scoring_breakdown_dict = {
            "required_coverage": score_breakdown.required_skill_coverage,
            "preferred_coverage": score_breakdown.preferred_skill_coverage,
            "experience_relevance": exp_relevance,
            "education_relevance": edu_relevance,
            "profile_completeness": cand_data.profile_completeness
        }

        all_matched = score_breakdown.matched_required + score_breakdown.matched_preferred
        all_missing = score_breakdown.missing_required + score_breakdown.missing_preferred

        if not existing_match:
            match_record = JobMatch(
                user_id=user_id,
                job_id=job.id,
                match_score=score_breakdown.match_score,
                match_tier=score_breakdown.match_tier,
                rationale=score_breakdown.explanation,
                matched_skills=", ".join(all_matched),
                missing_skills=", ".join(all_missing),
                missing_required_skills=", ".join(score_breakdown.missing_required),
                missing_preferred_skills=", ".join(score_breakdown.missing_preferred),
                weak_skills=", ".join(score_breakdown.weak_skills),
                scoring_breakdown_json=json.dumps(scoring_breakdown_dict),
                matcher_version=CURRENT_MATCHER_VERSION
            )
            db.add(match_record)
            db.flush()
        else:
            match_record = existing_match
            match_record.match_score = score_breakdown.match_score
            match_record.match_tier = score_breakdown.match_tier
            match_record.rationale = score_breakdown.explanation
            match_record.matched_skills = ", ".join(all_matched)
            match_record.missing_skills = ", ".join(all_missing)
            match_record.missing_required_skills = ", ".join(score_breakdown.missing_required)
            match_record.missing_preferred_skills = ", ".join(score_breakdown.missing_preferred)
            match_record.weak_skills = ", ".join(score_breakdown.weak_skills)
            match_record.scoring_breakdown_json = json.dumps(scoring_breakdown_dict)
            match_record.matcher_version = CURRENT_MATCHER_VERSION
            match_record.updated_at = datetime.now(timezone.utc)

            # Clear old skill gaps for refresh
            db.query(SkillGap).filter(SkillGap.job_match_id == match_record.id).delete()

        # 8. Persist SkillGap records
        for item in roadmap_items:
            db.add(
                SkillGap(
                    job_match_id=match_record.id,
                    skill_name=item["skill_name"],
                    importance=item["importance"],
                    current_proficiency=item["current_proficiency"],
                    why_it_matters=item.get("why_it_matters"),
                    recommendation=item["recommendation"],
                    step_number=item["step_number"],
                    estimated_time=item["estimated_time"],
                    roadmap_topics_json=json.dumps(item.get("roadmap_topics", [])),
                    suggested_practice=item.get("suggested_practice")
                )
            )

        db.commit()
        db.refresh(match_record)

        return JobMatchResponse(
            job_id=job.id,
            job_title=job.title,
            company=job.company,
            match_score=score_breakdown.match_score,
            match_tier=score_breakdown.match_tier,
            matched_skills=all_matched,
            missing_skills=all_missing,
            explanation=score_breakdown.explanation,
            strong_matches=score_breakdown.strong_matches,
            missing_required_skills=score_breakdown.missing_required,
            missing_preferred_skills=score_breakdown.missing_preferred,
            weak_skills=score_breakdown.weak_skills,
            experience_relevance=exp_relevance,
            education_relevance=edu_relevance,
            profile_completeness=cand_data.profile_completeness,
            skill_gap_count=len(roadmap_items)
        )

    @classmethod
    def get_skill_gap_analysis(
        cls,
        db: Session,
        user_id: str,
        job: Job
    ) -> SkillGapResponse:
        """
        Retrieves the personalized learning roadmap for closing skill gaps on this job.
        """
        if isinstance(job, str):
            job_obj = db.query(Job).filter(Job.id == job).first()
            if not job_obj:
                raise ValueError(f"Job with ID '{job}' not found.")
            job = job_obj

        # Ensure match and gaps are computed
        match_res = cls.calculate_match(db, user_id, job)

        match_record = db.query(JobMatch).filter(
            JobMatch.user_id == user_id,
            JobMatch.job_id == job.id
        ).first()

        gap_items: List[SkillGapItem] = []
        if match_record:
            db_gaps = db.query(SkillGap).filter(
                SkillGap.job_match_id == match_record.id
            ).order_by(SkillGap.step_number.asc()).all()

            for g in db_gaps:
                topics = json.loads(g.roadmap_topics_json) if g.roadmap_topics_json else []
                gap_items.append(
                    SkillGapItem(
                        step_number=g.step_number,
                        skill_name=g.skill_name,
                        importance=g.importance,
                        current_proficiency=g.current_proficiency,
                        recommendation=g.recommendation,
                        estimated_time=g.estimated_time,
                        why_it_matters=g.why_it_matters,
                        roadmap_topics=topics,
                        suggested_practice=g.suggested_practice
                    )
                )

        return SkillGapResponse(
            job_id=job.id,
            job_title=job.title,
            match_score=match_res.match_score,
            gaps=gap_items
        )

    @classmethod
    def _build_response_from_model(cls, job: Job, match: JobMatch) -> JobMatchResponse:
        all_matched = [s.strip() for s in match.matched_skills.split(",") if s.strip()]
        all_missing = [s.strip() for s in match.missing_skills.split(",") if s.strip()]
        missing_req = [s.strip() for s in (match.missing_required_skills or "").split(",") if s.strip()]
        missing_pref = [s.strip() for s in (match.missing_preferred_skills or "").split(",") if s.strip()]
        weak = [s.strip() for s in (match.weak_skills or "").split(",") if s.strip()]

        breakdown = {}
        if match.scoring_breakdown_json:
            try:
                breakdown = json.loads(match.scoring_breakdown_json)
            except Exception:
                pass

        return JobMatchResponse(
            job_id=job.id,
            job_title=job.title,
            company=job.company,
            match_score=match.match_score,
            match_tier=match.match_tier,
            matched_skills=all_matched,
            missing_skills=all_missing,
            explanation=match.rationale,
            strong_matches=all_matched[:3],
            missing_required_skills=missing_req,
            missing_preferred_skills=missing_pref,
            weak_skills=weak,
            experience_relevance=breakdown.get("experience_relevance", 75),
            education_relevance=breakdown.get("education_relevance", 85),
            profile_completeness=breakdown.get("profile_completeness", 80),
            skill_gap_count=len(all_missing)
        )
