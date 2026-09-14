from datetime import datetime
from typing import List, Optional, Dict, Any
from pydantic import BaseModel, ConfigDict


class ExtractedSocialProfiles(BaseModel):
    model_config = ConfigDict(from_attributes=True)
    github: Optional[str] = None
    linkedin: Optional[str] = None
    portfolio: Optional[str] = None


class ExtractedPersonalInfo(BaseModel):
    model_config = ConfigDict(from_attributes=True)
    name: Optional[str] = None
    email: Optional[str] = None
    phone: Optional[str] = None
    location: Optional[str] = None
    summary: Optional[str] = None
    social_profiles: ExtractedSocialProfiles = ExtractedSocialProfiles()


class ExtractedEducationItem(BaseModel):
    model_config = ConfigDict(from_attributes=True)
    institution: Optional[str] = None
    degree: Optional[str] = None
    field: Optional[str] = None
    start_year: Optional[int] = None
    end_year: Optional[int] = None
    grade: Optional[str] = None


class ExtractedSkillItem(BaseModel):
    model_config = ConfigDict(from_attributes=True)
    name: str
    category: str = "Technical"


class ExtractedExperienceItem(BaseModel):
    model_config = ConfigDict(from_attributes=True)
    company: str
    role: str
    start_date: Optional[str] = None
    end_date: Optional[str] = None
    is_current: bool = False
    description: Optional[str] = None


class ExtractedProjectItem(BaseModel):
    model_config = ConfigDict(from_attributes=True)
    name: str
    description: Optional[str] = None
    technologies: List[str] = []
    start_date: Optional[str] = None
    end_date: Optional[str] = None
    github_url: Optional[str] = None
    live_url: Optional[str] = None


class ExtractedCertificationItem(BaseModel):
    model_config = ConfigDict(from_attributes=True)
    name: str
    issuer: str
    date: Optional[str] = None


class ExtractedResumeData(BaseModel):
    model_config = ConfigDict(from_attributes=True)
    personal_info: ExtractedPersonalInfo = ExtractedPersonalInfo()
    education: List[ExtractedEducationItem] = []
    skills: List[ExtractedSkillItem] = []
    experience: List[ExtractedExperienceItem] = []
    projects: List[ExtractedProjectItem] = []
    certifications: List[ExtractedCertificationItem] = []


class ResumeAuditReport(BaseModel):
    model_config = ConfigDict(from_attributes=True)
    missing_fields: List[str] = []
    uncertain_fields: List[str] = []
    completion_percentage: int = 50
    recommendation: str = ""


class ExtractedResumeResponse(BaseModel):
    model_config = ConfigDict(from_attributes=True)
    resume_id: str
    title: str
    file_name: str
    extraction_status: str
    structured_data: ExtractedResumeData
    audit: ResumeAuditReport


class ResumeResponse(BaseModel):
    model_config = ConfigDict(from_attributes=True)
    id: str
    user_id: str
    title: str
    file_name: str
    file_type: str
    file_size_bytes: int
    missing_fields: List[str] = []
    extraction_status: str = "EXTRACTED"
    completion_percentage: Optional[int] = None
    is_active: bool
    created_at: datetime


class MissingFieldsAuditResponse(BaseModel):
    model_config = ConfigDict(from_attributes=True)
    total_missing: int
    missing_fields: List[str]
    recommendation: str
    completion_percentage: Optional[int] = 80


class ConfirmResumeResponse(BaseModel):
    model_config = ConfigDict(from_attributes=True)
    status: str
    message: str
    profile_id: str
    profile_strength: int
    confirmed_items_count: Dict[str, int]


class ResumeAnalysisResponse(BaseModel):
    resume_id: str
    ats_score: int
    label: str = "AI-Powered ATS-Style Analysis"
    summary: str
    strengths: List[str] = []
    weaknesses: List[str] = []
    missing_skills: List[str] = []
    content_improvements: List[str] = []
    formatting_notes: List[str] = []
    disclaimer: str = "Informational guidance based on industry standards. JobPilot makes no employment or interview guarantees."

