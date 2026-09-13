"""
AI Resume Builder and Tailoring API for JobPilot.
Handles resume creation from CareerProfile, multi-template styling,
job tailoring, and ATS-friendly PDF export.
"""

from typing import List, Dict, Any
from fastapi import APIRouter, Depends, HTTPException, status, Response
from sqlalchemy.orm import Session

from app.core.dependencies import get_db, get_current_user
from app.models.user import User
from app.models.career_profile import CareerProfile
from app.models.job import Job
from app.models.saved_resume import SavedResume
from app.schemas.resume_builder import (
    SavedResumeCreate,
    SavedResumeUpdate,
    SavedResumeResponse,
    ResumeTailorRequest,
    ResumeTailorResponse
)
from app.services.ai.service import ai_service
from app.services.pdf_generator_service import ResumePdfGenerator
from app.services.matching.matching_service import MatchingService

router = APIRouter(prefix="/resumes/builder", tags=["AI Resume Builder & Tailoring"])


def _build_resume_dict(profile: CareerProfile, user: User) -> Dict[str, Any]:
    """Extracts clean dictionary of profile attributes ready for resume storage and rendering."""
    contact = {
        "name": user.full_name,
        "email": user.email,
        "phone": getattr(profile.personal_info, "phone", None) if profile.personal_info else None,
        "location": getattr(profile.personal_info, "location", None) if profile.personal_info else None,
        "linkedin": None,
        "github": None,
        "portfolio": None
    }
    if profile.social_profiles:
        for sp in profile.social_profiles:
            plat = (sp.platform or "").lower()
            if "linkedin" in plat:
                contact["linkedin"] = sp.url
            elif "github" in plat:
                contact["github"] = sp.url
            elif "portfolio" in plat:
                contact["portfolio"] = sp.url

    skills = [{"name": s.name, "category": s.category, "proficiency": s.proficiency} for s in (profile.skills or [])]
    experience = [
        {
            "company": e.company,
            "title": e.title,
            "start_date": e.start_date,
            "end_date": e.end_date,
            "is_current": e.is_current,
            "description": e.description
        } for e in (profile.experience or [])
    ]
    projects = [
        {
            "title": p.title,
            "tech_stack": p.tech_stack,
            "description": p.description,
            "github_url": p.github_url,
            "live_url": p.live_url
        } for p in (profile.projects or [])
    ]
    education = [
        {
            "institution": ed.institution,
            "degree": ed.degree,
            "field_of_study": ed.field_of_study,
            "start_year": ed.start_year,
            "end_year": ed.end_year
        } for ed in (profile.education or [])
    ]
    certifications = [
        {
            "name": c.name,
            "issuer": c.issuer,
            "issue_date": c.issue_date
        } for c in (profile.certifications or [])
    ]

    return {
        "contact": contact,
        "summary": profile.summary or f"Results-driven engineer specialized in {skills[0]['name'] if skills else 'software development'}.",
        "skills": skills,
        "experience": experience,
        "projects": projects,
        "education": education,
        "certifications": certifications
    }


@router.post("/generate", response_model=SavedResumeResponse)
def generate_resume_from_profile(
    req: SavedResumeCreate,
    current_user: User = Depends(get_current_user),
    db: Session = Depends(get_db)
):
    """
    Generates a new structured resume grounded in the user's verified CareerProfile.
    """
    profile = db.query(CareerProfile).filter(CareerProfile.user_id == current_user.id).first()
    if not profile:
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST,
            detail="Career Profile is required before generating a resume. Please complete your profile or upload an existing resume."
        )

    resume_data = _build_resume_dict(profile, current_user)

    saved = SavedResume(
        user_id=current_user.id,
        title=req.title or f"{current_user.full_name} - {req.template_type}",
        template_type=req.template_type,
        target_job_id=req.target_job_id,
        contact_json=resume_data["contact"],
        summary_text=resume_data["summary"],
        skills_json=resume_data["skills"],
        experience_json=resume_data["experience"],
        projects_json=resume_data["projects"],
        education_json=resume_data["education"],
        certifications_json=resume_data["certifications"],
        is_tailored=False
    )
    db.add(saved)
    db.commit()
    db.refresh(saved)
    return saved


@router.get("/saved", response_model=List[SavedResumeResponse])
def list_saved_resumes(
    current_user: User = Depends(get_current_user),
    db: Session = Depends(get_db)
):
    """Lists all saved resumes created by the user."""
    return db.query(SavedResume).filter(
        SavedResume.user_id == current_user.id
    ).order_by(SavedResume.updated_at.desc()).all()


@router.get("/{resume_id}", response_model=SavedResumeResponse)
def get_saved_resume(
    resume_id: str,
    current_user: User = Depends(get_current_user),
    db: Session = Depends(get_db)
):
    """Retrieves a single saved resume."""
    saved = db.query(SavedResume).filter(
        SavedResume.id == resume_id,
        SavedResume.user_id == current_user.id
    ).first()
    if not saved:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Resume not found.")
    return saved


@router.put("/{resume_id}", response_model=SavedResumeResponse)
def update_saved_resume(
    resume_id: str,
    req: SavedResumeUpdate,
    current_user: User = Depends(get_current_user),
    db: Session = Depends(get_db)
):
    """Allows user to edit sections of their saved resume."""
    saved = db.query(SavedResume).filter(
        SavedResume.id == resume_id,
        SavedResume.user_id == current_user.id
    ).first()
    if not saved:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Resume not found.")

    if req.title is not None:
        saved.title = req.title
    if req.template_type is not None:
        saved.template_type = req.template_type
    if req.summary_text is not None:
        saved.summary_text = req.summary_text
    if req.contact_json is not None:
        saved.contact_json = req.contact_json
    if req.skills_json is not None:
        saved.skills_json = req.skills_json
    if req.experience_json is not None:
        saved.experience_json = req.experience_json
    if req.projects_json is not None:
        saved.projects_json = req.projects_json
    if req.education_json is not None:
        saved.education_json = req.education_json
    if req.certifications_json is not None:
        saved.certifications_json = req.certifications_json

    db.commit()
    db.refresh(saved)
    return saved


@router.post("/{resume_id}/duplicate", response_model=SavedResumeResponse)
def duplicate_saved_resume(
    resume_id: str,
    current_user: User = Depends(get_current_user),
    db: Session = Depends(get_db)
):
    """Duplicates a saved resume to allow versioning or different job variations."""
    original = db.query(SavedResume).filter(
        SavedResume.id == resume_id,
        SavedResume.user_id == current_user.id
    ).first()
    if not original:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Resume not found.")

    dup = SavedResume(
        user_id=current_user.id,
        title=f"{original.title} (Copy)",
        template_type=original.template_type,
        target_job_id=original.target_job_id,
        contact_json=original.contact_json,
        summary_text=original.summary_text,
        skills_json=original.skills_json,
        experience_json=original.experience_json,
        projects_json=original.projects_json,
        education_json=original.education_json,
        certifications_json=original.certifications_json,
        is_tailored=original.is_tailored,
        tailored_role_title=original.tailored_role_title
    )
    db.add(dup)
    db.commit()
    db.refresh(dup)
    return dup


@router.delete("/{resume_id}", status_code=status.HTTP_204_NO_CONTENT)
def delete_saved_resume(
    resume_id: str,
    current_user: User = Depends(get_current_user),
    db: Session = Depends(get_db)
):
    """Deletes a saved resume."""
    saved = db.query(SavedResume).filter(
        SavedResume.id == resume_id,
        SavedResume.user_id == current_user.id
    ).first()
    if not saved:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Resume not found.")
    db.delete(saved)
    db.commit()
    return None


@router.post("/{resume_id}/tailor", response_model=ResumeTailorResponse)
def tailor_resume_for_job(
    resume_id: str,
    req: ResumeTailorRequest,
    current_user: User = Depends(get_current_user),
    db: Session = Depends(get_db)
):
    """
    Tailors a resume for a specific job:
    1. Runs deterministic match to find real matched and missing skills.
    2. Rewrites summary to highlight relevant verified strengths.
    3. Re-orders skills placing matching keywords first.
    4. NEVER invents unverified skills or employment.
    """
    saved = db.query(SavedResume).filter(
        SavedResume.id == resume_id,
        SavedResume.user_id == current_user.id
    ).first()
    if not saved:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Resume not found.")

    target_job = None
    if req.target_job_id:
        target_job = db.query(Job).filter(Job.id == req.target_job_id).first()

    job_data: Dict[str, Any] = {}
    if target_job:
        match_res = MatchingService.calculate_match(db, current_user.id, target_job.id)
        job_data = {
            "title": target_job.title,
            "company": target_job.company,
            "skills_required": target_job.skills_required,
            "preferred_skills": target_job.preferred_skills,
            "description": target_job.description,
            "matched_skills": match_res.matched_skills,
            "missing_skills": match_res.missing_skills
        }
    elif req.raw_job_description:
        parsed_jd = ai_service.analyze_job_description(req.raw_job_description)
        job_data = parsed_jd
    else:
        role_name = req.target_job_title or "Target Role"
        job_data = {
            "title": role_name,
            "company": "Target Company",
            "matched_skills": [s["name"] for s in (saved.skills_json or [])[:3]],
            "missing_skills": []
        }

    profile_data = {
        "full_name": current_user.full_name,
        "summary": saved.summary_text,
        "skills": saved.skills_json or [],
        "experience": saved.experience_json or [],
        "projects": saved.projects_json or [],
        "education": saved.education_json or []
    }

    # Execute Tailoring
    tailor_result = ai_service.tailor_resume(profile_data, job_data)

    # Re-order candidate's skills putting matched skills first
    matched_set = set(job_data.get("matched_skills", []))
    if saved.skills_json:
        matched_skills = [s for s in saved.skills_json if (s.get("name") if isinstance(s, dict) else str(s)) in matched_set]
        other_skills = [s for s in saved.skills_json if (s.get("name") if isinstance(s, dict) else str(s)) not in matched_set]
        saved.skills_json = matched_skills + other_skills

    saved.summary_text = tailor_result["tailored_summary"]
    saved.is_tailored = True
    saved.tailored_role_title = job_data.get("title")
    if target_job:
        saved.target_job_id = target_job.id
    saved.title = f"{saved.title} (Tailored for {job_data.get('title')})"

    db.commit()
    db.refresh(saved)

    return ResumeTailorResponse(
        saved_resume_id=saved.id,
        tailored_summary=tailor_result["tailored_summary"],
        matched_keywords_to_emphasize=tailor_result.get("matched_keywords_to_emphasize", []),
        missing_skills_notice=tailor_result.get("missing_skills_notice", []),
        is_fallback=tailor_result.get("is_fallback", False)
    )


@router.post("/{resume_id}/export-pdf")
def export_resume_pdf(
    resume_id: str,
    current_user: User = Depends(get_current_user),
    db: Session = Depends(get_db)
):
    """
    Generates and returns an ATS-friendly PDF document across Minimal, Modern,
    Professional, or Executive templates.
    """
    saved = db.query(SavedResume).filter(
        SavedResume.id == resume_id,
        SavedResume.user_id == current_user.id
    ).first()
    if not saved:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Resume not found.")

    resume_render_data = {
        "title": saved.title,
        "contact": saved.contact_json or {"name": current_user.full_name, "email": current_user.email},
        "summary_text": saved.summary_text,
        "skills": saved.skills_json or [],
        "experience": saved.experience_json or [],
        "projects": saved.projects_json or [],
        "education": saved.education_json or [],
        "certifications": saved.certifications_json or []
    }

    pdf_bytes = ResumePdfGenerator.generate_pdf(
        resume_data=resume_render_data,
        template_type=saved.template_type
    )

    clean_filename = f"{current_user.full_name.replace(' ', '_')}_Resume.pdf"
    return Response(
        content=pdf_bytes,
        media_type="application/pdf",
        headers={"Content-Disposition": f'attachment; filename="{clean_filename}"'}
    )
