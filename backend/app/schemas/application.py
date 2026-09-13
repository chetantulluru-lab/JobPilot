from datetime import datetime
from typing import List, Optional
from pydantic import BaseModel, ConfigDict


class ApplicationEventBase(BaseModel):
    title: str
    description: Optional[str] = None
    event_date: str
    stage: str


class ApplicationEventCreate(ApplicationEventBase):
    pass


class ApplicationEventResponse(ApplicationEventBase):
    model_config = ConfigDict(from_attributes=True)

    id: str
    application_id: str
    created_at: datetime


class ApplicationBase(BaseModel):
    company: str
    role: str
    location: Optional[str] = None
    salary: Optional[str] = None
    work_mode: str = "Remote"
    status: str = "APPLIED"
    applied_date: str
    next_step: Optional[str] = None
    recruiter_name: Optional[str] = None
    recruiter_email: Optional[str] = None
    notes: Optional[str] = None


class ApplicationCreate(ApplicationBase):
    job_id: Optional[str] = None


class ApplicationUpdate(BaseModel):
    company: Optional[str] = None
    role: Optional[str] = None
    location: Optional[str] = None
    salary: Optional[str] = None
    work_mode: Optional[str] = None
    status: Optional[str] = None
    next_step: Optional[str] = None
    recruiter_name: Optional[str] = None
    recruiter_email: Optional[str] = None
    notes: Optional[str] = None


class ApplicationResponse(ApplicationBase):
    model_config = ConfigDict(from_attributes=True)

    id: str
    user_id: str
    job_id: Optional[str] = None
    created_at: datetime
    updated_at: datetime
    events: List[ApplicationEventResponse] = []
