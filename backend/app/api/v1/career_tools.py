"""
Career Tools API Endpoints for JobPilot.
Provides cover letter generation, recruiter outreach messaging,
and raw job description analysis with instant match scoring.
"""

from typing import List, Dict, Any
from fastapi import APIRouter, Depends, HTTPException, status
from sqlalchemy.orm import Session

from app.core.dependencies import get_db, get_current_user
from app.models.user import User
from app.models.career_profile import CareerProfile
from app.models.job import Job
from app.models.career_tools import CoverLetter, RecruiterMessage
from app.schemas.career_tools import (
    CoverLetterRequest,
    CoverLetterResponse,
    RecruiterMessageRequest,
    RecruiterMessageResponse,
    JDAnalysisRequest,
    JDAnalysisResponse
)
from app.services.ai.service import ai_service
from app.services.matching.candidate_profile_builder import CandidateProfileBuilder
from app.services.matching.relevance_engine import ExperienceMatcher, EducationMatcher
from app.services.matching.scoring_engine import ScoringEngine
from app.services.matching.skill_normalizer import SkillNormalizer

router = APIRouter(prefix="/career-tools", tags=["Career Tools & JD Intelligence"])


def _get_profile_dict(db: Session, user: User) -> Dict[str, Any]:
    profile = db.query(CareerProfile).filter(CareerProfile.user_id == user.id).first()
    if not profile:
        return {"full_name": user.full_name}

    return {
        "full_name": user.full_name,
        "headline": profile.headline,
        "summary": profile.summary,
        "skills": [{"name": s.name, "proficiency": s.proficiency} for s in (profile.skills or [])],
        "experience": [
            {
                "company": e.company,
                "title": e.title,
                "start_date": e.start_date,
                "end_date": e.end_date,
                "is_current": e.is_current,
                "description": e.description
            } for e in (profile.experience or [])
        ],
        "projects": [
            {
                "title": p.title,
                "tech_stack": p.tech_stack,
                "description": p.description
            } for p in (profile.projects or [])
        ],
        "education": [
            {
                "degree": ed.degree,
                "field_of_study": ed.field_of_study,
                "institution": ed.institution
            } for ed in (profile.education or [])
        ]
    }


@router.post("/cover-letter", response_model=CoverLetterResponse)
def generate_cover_letter(
    req: CoverLetterRequest,
    current_user: User = Depends(get_current_user),
    db: Session = Depends(get_db)
):
    """Generates a truthful, job-tailored cover letter and stores it."""
    profile_data = _get_profile_dict(db, current_user)
    job_data = {
        "company": req.company,
        "title": req.role,
        "skills_required": ""
    }

    if req.job_id:
        job = db.query(Job).filter(Job.id == req.job_id).first()
        if job:
            job_data["company"] = job.company
            job_data["title"] = job.title
            job_data["skills_required"] = job.skills_required
            job_data["preferred_skills"] = job.preferred_skills
            job_data["description"] = job.description

    result = ai_service.generate_cover_letter(profile_data, job_data, tone=req.tone)

    cl = CoverLetter(
        user_id=current_user.id,
        job_id=req.job_id,
        company=job_data["company"],
        role=job_data["title"],
        tone=req.tone,
        content=result["content"]
    )
    db.add(cl)
    db.commit()
    db.refresh(cl)

    return CoverLetterResponse(
        id=cl.id,
        company=cl.company,
        role=cl.role,
        tone=cl.tone,
        content=cl.content,
        is_fallback=result["is_fallback"],
        created_at=cl.created_at
    )


@router.get("/cover-letters", response_model=List[CoverLetterResponse])
def list_cover_letters(
    current_user: User = Depends(get_current_user),
    db: Session = Depends(get_db)
):
    """Lists saved cover letters for the authenticated user."""
    return db.query(CoverLetter).filter(
        CoverLetter.user_id == current_user.id
    ).order_by(CoverLetter.created_at.desc()).all()


@router.delete("/cover-letters/{letter_id}", status_code=status.HTTP_204_NO_CONTENT)
def delete_cover_letter(
    letter_id: str,
    current_user: User = Depends(get_current_user),
    db: Session = Depends(get_db)
):
    """Deletes a saved cover letter."""
    cl = db.query(CoverLetter).filter(
        CoverLetter.id == letter_id,
        CoverLetter.user_id == current_user.id
    ).first()
    if not cl:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Cover letter not found.")
    db.delete(cl)
    db.commit()
    return None


@router.post("/recruiter-message", response_model=RecruiterMessageResponse)
def generate_recruiter_message(
    req: RecruiterMessageRequest,
    current_user: User = Depends(get_current_user),
    db: Session = Depends(get_db)
):
    """Generates an outreach message for LinkedIn or email."""
    profile_data = _get_profile_dict(db, current_user)
    job_data = {
        "company": req.company,
        "title": req.role
    }
    if req.job_id:
        job = db.query(Job).filter(Job.id == req.job_id).first()
        if job:
            job_data["company"] = job.company
            job_data["title"] = job.title

    result = ai_service.generate_recruiter_message(
        profile_data=profile_data,
        target_job=job_data,
        recipient_name=req.recipient_name,
        platform=req.platform,
        user_context=req.context
    )

    rm = RecruiterMessage(
        user_id=current_user.id,
        job_id=req.job_id,
        recipient_name=req.recipient_name,
        platform=req.platform,
        context=req.context,
        content=result["content"]
    )
    db.add(rm)
    db.commit()
    db.refresh(rm)

    return RecruiterMessageResponse(
        id=rm.id,
        recipient_name=rm.recipient_name,
        platform=rm.platform,
        content=rm.content,
        is_fallback=result["is_fallback"],
        created_at=rm.created_at
    )


@router.get("/recruiter-messages", response_model=List[RecruiterMessageResponse])
def list_recruiter_messages(
    current_user: User = Depends(get_current_user),
    db: Session = Depends(get_db)
):
    """Lists saved recruiter messages for the authenticated user."""
    return db.query(RecruiterMessage).filter(
        RecruiterMessage.user_id == current_user.id
    ).order_by(RecruiterMessage.created_at.desc()).all()


@router.post("/analyze-jd", response_model=JDAnalysisResponse)
def analyze_job_description_and_match(
    req: JDAnalysisRequest,
    current_user: User = Depends(get_current_user),
    db: Session = Depends(get_db)
):
    """
    Parses unstructured job description text, extracts required/preferred competencies,
    and runs Phase 3B deterministic scoring against candidate's profile.
    """
    if not req.raw_jd_text or not req.raw_jd_text.strip():
        raise HTTPException(status_code=status.HTTP_400_BAD_REQUEST, detail="Job description text is required.")

    # 1. Parse JD attributes
    parsed = ai_service.analyze_job_description(req.raw_jd_text)

    # 2. Extract Candidate Profile
    profile = db.query(CareerProfile).filter(CareerProfile.user_id == current_user.id).first()
    cand = CandidateProfileBuilder.build_from_career_profile(profile)

    req_skills = parsed.get("required_skills", [])
    pref_skills = parsed.get("preferred_skills", [])

    # 3. Calculate Relevance
    exp_rel = ExperienceMatcher.calculate_experience_relevance(
        job_required_skills=req_skills,
        job_preferred_skills=pref_skills,
        candidate_project_techs=cand.project_techs,
        candidate_experience_techs=cand.experience_techs,
        corroborated_skills=cand.corroborated_skills
    )

    edu_rel = EducationMatcher.calculate_education_relevance(
        job_degree_req=parsed.get("education_degree"),
        candidate_degrees=cand.education_degrees
    )

    # 4. Multi-factor Weighted Scoring
    breakdown = ScoringEngine.calculate_score(
        job_required_skills=req_skills,
        job_preferred_skills=pref_skills,
        candidate_canonical_skills=cand.canonical_skills,
        corroborated_skills=cand.corroborated_skills,
        weak_skills=cand.weak_skills,
        experience_relevance=exp_rel,
        education_relevance=edu_rel,
        profile_completeness=cand.profile_completeness,
        role_title=parsed.get("title", "this position")
    )

    return JDAnalysisResponse(
        title=parsed.get("title", "Analyzed Position"),
        company=parsed.get("company", "Company"),
        location=parsed.get("location", "Remote"),
        work_mode=parsed.get("work_mode", "Remote"),
        min_experience_years=parsed.get("min_experience_years"),
        education_degree=parsed.get("education_degree"),
        required_skills=req_skills,
        preferred_skills=pref_skills,
        responsibilities=parsed.get("responsibilities", []),
        salary_range=parsed.get("salary_range"),
        match_score=breakdown.match_score,
        match_tier=breakdown.match_tier,
        matched_skills=breakdown.matched_required + breakdown.matched_preferred,
        missing_skills=breakdown.missing_required,
        explanation=breakdown.explanation
    )
