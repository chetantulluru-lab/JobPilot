from datetime import datetime
from typing import List, Optional, Dict, Any
from pydantic import BaseModel, ConfigDict


class SavedResumeCreate(BaseModel):
    title: str = "Software Engineer Resume"
    template_type: str = "Modern"
    target_job_id: Optional[str] = None


class SavedResumeUpdate(BaseModel):
    title: Optional[str] = None
    template_type: Optional[str] = None
    summary_text: Optional[str] = None
    contact_json: Optional[Dict[str, Any]] = None
    skills_json: Optional[List[Any]] = None
    experience_json: Optional[List[Any]] = None
    projects_json: Optional[List[Any]] = None
    education_json: Optional[List[Any]] = None
    certifications_json: Optional[List[Any]] = None


class SavedResumeResponse(BaseModel):
    model_config = ConfigDict(from_attributes=True)

    id: str
    user_id: str
    title: str
    template_type: str
    target_job_id: Optional[str] = None
    contact_json: Optional[Dict[str, Any]] = None
    summary_text: Optional[str] = None
    skills_json: Optional[List[Any]] = None
    experience_json: Optional[List[Any]] = None
    projects_json: Optional[List[Any]] = None
    education_json: Optional[List[Any]] = None
    certifications_json: Optional[List[Any]] = None
    is_tailored: bool
    tailored_role_title: Optional[str] = None
    created_at: datetime
    updated_at: datetime


class ResumeTailorRequest(BaseModel):
    target_job_id: Optional[str] = None
    target_job_title: Optional[str] = None
    raw_job_description: Optional[str] = None


class ResumeTailorResponse(BaseModel):
    saved_resume_id: str
    tailored_summary: str
    matched_keywords_to_emphasize: List[str]
    missing_skills_notice: List[str]
    is_fallback: bool


class StudentResumeCreateRequest(BaseModel):
    full_name: str
    email: str
    phone: Optional[str] = None
    college: Optional[str] = None
    branch: Optional[str] = None
    cgpa: Optional[str] = None
    grad_year: Optional[str] = None
    github: Optional[str] = None
    linkedin: Optional[str] = None
    portfolio: Optional[str] = None
    skills: List[str] = []
    projects: List[Dict[str, Any]] = []
    experience: Optional[str] = None
    achievements: Optional[str] = None
    template_type: str = "Modern"
