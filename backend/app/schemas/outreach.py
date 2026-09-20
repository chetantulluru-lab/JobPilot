from typing import Optional
from pydantic import BaseModel, Field


class OutreachGenerateRequest(BaseModel):
    job_id: Optional[str] = Field(None, description="Optional job ID to pull real company & role requirements")
    role_title: Optional[str] = Field(None, description="Job title if custom")
    company: Optional[str] = Field(None, description="Company name if custom")
    job_description: Optional[str] = Field(None, description="Optional job description text")


class OutreachResponse(BaseModel):
    job_title: str
    company: str
    linkedin_note: str
    cold_email_subject: str
    cold_email_body: str
    cover_letter: str
