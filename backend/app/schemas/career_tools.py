from datetime import datetime
from typing import List, Optional
from pydantic import BaseModel, ConfigDict


class CoverLetterRequest(BaseModel):
    job_id: Optional[str] = None
    company: str
    role: str
    tone: str = "Professional"  # Professional, Confident, Concise


class CoverLetterResponse(BaseModel):
    model_config = ConfigDict(from_attributes=True)

    id: str
    company: str
    role: str
    tone: str
    content: str
    is_fallback: bool = False
    created_at: datetime


class RecruiterMessageRequest(BaseModel):
    job_id: Optional[str] = None
    company: str
    role: str
    recipient_name: Optional[str] = "Hiring Manager"
    platform: str = "LinkedIn"  # LinkedIn, Email
    context: Optional[str] = None


class RecruiterMessageResponse(BaseModel):
    model_config = ConfigDict(from_attributes=True)

    id: str
    recipient_name: Optional[str] = None
    platform: str
    content: str
    is_fallback: bool = False
    created_at: datetime


class JDAnalysisRequest(BaseModel):
    raw_jd_text: str


class JDAnalysisResponse(BaseModel):
    title: str
    company: str
    location: str
    work_mode: str
    min_experience_years: Optional[int] = None
    education_degree: Optional[str] = None
    required_skills: List[str]
    preferred_skills: List[str]
    responsibilities: List[str]
    salary_range: Optional[str] = None
    # Matching with candidate profile
    match_score: int
    match_tier: str
    matched_skills: List[str]
    missing_skills: List[str]
    explanation: str
