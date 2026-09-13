from typing import List, Optional
from fastapi import APIRouter, Depends, HTTPException, Query, status
from sqlalchemy.orm import Session
from app.core.dependencies import get_db, get_current_user
from app.models.user import User
from app.models.job import Job
from app.schemas.job import JobCreate, JobResponse, JobMatchResponse, SkillGapResponse
from app.services.job_matching_service import JobMatchingService

router = APIRouter(prefix="/jobs", tags=["Jobs & AI Matching"])


@router.get("", response_model=List[JobResponse])
def list_jobs(
    role: Optional[str] = Query(None, description="Filter by job title or role keyword"),
    location: Optional[str] = Query(None, description="Filter by location"),
    work_mode: Optional[str] = Query(None, description="Remote, Hybrid, or On-site"),
    employment_type: Optional[str] = Query(None, description="Full-time, Internship, Contract"),
    skill: Optional[str] = Query(None, description="Filter by required skill"),
    limit: int = Query(20, ge=1, le=100),
    offset: int = Query(0, ge=0),
    db: Session = Depends(get_db)
):
    """
    Search and filter curated jobs.
    Supports filtering by role, location, work mode, and skills.
    """
    query = db.query(Job).filter(Job.is_active == True)

    if role:
        query = query.filter(Job.title.ilike(f"%{role.strip()}%"))
    if location:
        query = query.filter(Job.location.ilike(f"%{location.strip()}%"))
    if work_mode and work_mode.lower() != "all":
        query = query.filter(Job.work_mode.ilike(work_mode.strip()))
    if employment_type:
        query = query.filter(Job.employment_type.ilike(employment_type.strip()))
    if skill:
        query = query.filter(Job.skills_required.ilike(f"%{skill.strip()}%"))

    return query.order_by(Job.created_at.desc()).offset(offset).limit(limit).all()


@router.get("/{job_id}", response_model=JobResponse)
def get_job_by_id(job_id: str, db: Session = Depends(get_db)):
    """Retrieve full details of a job posting."""
    job = db.query(Job).filter(Job.id == job_id).first()
    if not job:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Job not found.")
    return job


@router.post("", response_model=JobResponse, status_code=status.HTTP_201_CREATED)
def create_job(job_in: JobCreate, db: Session = Depends(get_db)):
    """Ingest a new job into the JobPilot database (Admin / System Ingestion)."""
    job = Job(**job_in.model_dump())
    db.add(job)
    db.commit()
    db.refresh(job)
    return job


@router.get("/{job_id}/match", response_model=JobMatchResponse)
def get_job_match(
    job_id: str,
    force_refresh: bool = Query(False, description="Bypass cache and force recomputation"),
    current_user: User = Depends(get_current_user),
    db: Session = Depends(get_db)
):
    """
    Computes candidate-to-job match score using structured NLP, skill normalization,
    and multi-factor weighted profile analysis.
    """
    job = db.query(Job).filter(Job.id == job_id).first()
    if not job:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Job not found.")
    return JobMatchingService.calculate_match(db, current_user.id, job, force_refresh=force_refresh)


@router.get("/{job_id}/skill-gaps", response_model=SkillGapResponse)
def get_skill_gaps(
    job_id: str,
    current_user: User = Depends(get_current_user),
    db: Session = Depends(get_db)
):
    """
    Diagnoses skill gaps between the candidate's career profile and the target job,
    providing an actionable step-by-step learning roadmap to close the gap.
    """
    job = db.query(Job).filter(Job.id == job_id).first()
    if not job:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Job not found.")
    return JobMatchingService.get_skill_gap_analysis(db, current_user.id, job)
