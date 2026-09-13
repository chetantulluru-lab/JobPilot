import os
import json
from typing import List, Optional
from fastapi import APIRouter, Depends, File, Form, HTTPException, UploadFile, status
from sqlalchemy.orm import Session
from app.core.dependencies import get_db, get_current_user
from app.models.user import User
from app.models.resume import Resume
from app.schemas.resume import (
    ResumeResponse, MissingFieldsAuditResponse, ExtractedResumeData,
    ExtractedResumeResponse, ConfirmResumeResponse
)
from app.services.resume_service import ResumeService

router = APIRouter(prefix="/resumes", tags=["Resume & NLP Hub"])


@router.post("/upload", response_model=ResumeResponse, status_code=status.HTTP_201_CREATED)
async def upload_resume(
    file: UploadFile = File(...),
    title: Optional[str] = Form(None),
    current_user: User = Depends(get_current_user),
    db: Session = Depends(get_db)
):
    """
    Upload a resume file (PDF or DOCX).
    Executes the Resume NLP extraction pipeline:
    File Validation -> Text Extraction -> Normalization -> Section Detection ->
    Entity Extraction -> Missing Fields Audit.
    """
    resume = await ResumeService.save_and_create_resume(
        db=db,
        user_id=current_user.id,
        file=file,
        title=title or file.filename
    )
    missing = json.loads(resume.missing_fields_json) if resume.missing_fields_json else []
    audit_data = json.loads(resume.audit_json) if resume.audit_json else {}
    completion = audit_data.get("completion_percentage", 75)

    return ResumeResponse(
        id=resume.id,
        user_id=resume.user_id,
        title=resume.title,
        file_name=resume.file_name,
        file_type=resume.file_type,
        file_size_bytes=resume.file_size_bytes,
        missing_fields=missing,
        extraction_status=resume.extraction_status,
        completion_percentage=completion,
        is_active=resume.is_active,
        created_at=resume.created_at
    )


@router.get("", response_model=List[ResumeResponse])
def list_my_resumes(
    current_user: User = Depends(get_current_user),
    db: Session = Depends(get_db)
):
    """List all resumes uploaded by the current user."""
    resumes = db.query(Resume).filter(
        Resume.user_id == current_user.id,
        Resume.is_active == True
    ).order_by(Resume.created_at.desc()).all()

    response = []
    for r in resumes:
        missing = json.loads(r.missing_fields_json) if r.missing_fields_json else []
        audit_data = json.loads(r.audit_json) if r.audit_json else {}
        completion = audit_data.get("completion_percentage", 75)

        response.append(
            ResumeResponse(
                id=r.id,
                user_id=r.user_id,
                title=r.title,
                file_name=r.file_name,
                file_type=r.file_type,
                file_size_bytes=r.file_size_bytes,
                missing_fields=missing,
                extraction_status=r.extraction_status,
                completion_percentage=completion,
                is_active=r.is_active,
                created_at=r.created_at
            )
        )
    return response


@router.get("/audit/missing-fields", response_model=MissingFieldsAuditResponse)
def audit_missing_fields(
    current_user: User = Depends(get_current_user),
    db: Session = Depends(get_db)
):
    """
    Audits the current profile against industry requirements.
    Detects missing social links, portfolio, and dates without inventing fake user data.
    """
    return ResumeService.get_audit_report(db, current_user.id)


@router.get("/{resume_id}", response_model=ResumeResponse)
def get_resume(
    resume_id: str,
    current_user: User = Depends(get_current_user),
    db: Session = Depends(get_db)
):
    """Retrieve metadata for a specific resume owned by the current user."""
    resume = db.query(Resume).filter(
        Resume.id == resume_id,
        Resume.user_id == current_user.id
    ).first()
    if not resume:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Resume not found.")

    missing = json.loads(resume.missing_fields_json) if resume.missing_fields_json else []
    audit_data = json.loads(resume.audit_json) if resume.audit_json else {}
    completion = audit_data.get("completion_percentage", 75)

    return ResumeResponse(
        id=resume.id,
        user_id=resume.user_id,
        title=resume.title,
        file_name=resume.file_name,
        file_type=resume.file_type,
        file_size_bytes=resume.file_size_bytes,
        missing_fields=missing,
        extraction_status=resume.extraction_status,
        completion_percentage=completion,
        is_active=resume.is_active,
        created_at=resume.created_at
    )


@router.get("/{resume_id}/extracted-data", response_model=ExtractedResumeResponse)
def get_extracted_resume_data(
    resume_id: str,
    current_user: User = Depends(get_current_user),
    db: Session = Depends(get_db)
):
    """
    Retrieves the structured extracted resume data and audit report for user review.
    Enforces strict user isolation.
    """
    return ResumeService.get_extracted_data(db, current_user.id, resume_id)


@router.put("/{resume_id}/extracted-data", response_model=ExtractedResumeResponse)
def update_extracted_resume_data(
    resume_id: str,
    updated_data: ExtractedResumeData,
    current_user: User = Depends(get_current_user),
    db: Session = Depends(get_db)
):
    """
    Allows the user to review, edit, and correct extracted information before confirming
    it into their trusted Career Profile.
    """
    return ResumeService.update_extracted_data(db, current_user.id, resume_id, updated_data)


@router.post("/{resume_id}/confirm", response_model=ConfirmResumeResponse)
def confirm_resume(
    resume_id: str,
    current_user: User = Depends(get_current_user),
    db: Session = Depends(get_db)
):
    """
    Confirms reviewed resume data and maps it directly into the user's PostgreSQL CareerProfile.
    Safely merges skills, education, experience, projects, and social profiles.
    """
    return ResumeService.confirm_and_apply_to_profile(db, current_user.id, resume_id)


@router.delete("/{resume_id}", status_code=status.HTTP_204_NO_CONTENT)
def delete_resume(
    resume_id: str,
    current_user: User = Depends(get_current_user),
    db: Session = Depends(get_db)
):
    """Delete a resume file and its database record."""
    resume = db.query(Resume).filter(
        Resume.id == resume_id,
        Resume.user_id == current_user.id
    ).first()
    if not resume:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Resume not found.")

    if resume.file_path and os.path.exists(resume.file_path):
        try:
            os.remove(resume.file_path)
        except OSError:
            pass

    db.delete(resume)
    db.commit()
    return None
