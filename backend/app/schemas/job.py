from datetime import datetime
from typing import List, Optional
from pydantic import BaseModel, ConfigDict


class JobBase(BaseModel):
    title: str
    company: str
    location: str
    work_mode: str = "Remote"
    employment_type: str = "Full-time"
    salary_range: Optional[str] = None
    description: str
    requirements: Optional[str] = None
    skills_required: str
    preferred_skills: Optional[str] = None
    education_requirement: Optional[str] = None
    experience_level: Optional[str] = "Entry-level"
    posted_date: Optional[str] = "Recently"


class JobCreate(JobBase):
    pass


class JobResponse(JobBase):
    model_config = ConfigDict(from_attributes=True)

    id: str
    is_active: bool
    created_at: datetime


class JobMatchResponse(BaseModel):
    model_config = ConfigDict(from_attributes=True)

    job_id: str
    job_title: str
    company: str
    match_score: int
    match_tier: str  # Excellent Match, Strong Match, Moderate Match, Low Match, Weak Match
    matched_skills: List[str]
    missing_skills: List[str]
    explanation: str
    # Phase 3B Enhanced fields
    strong_matches: List[str] = []
    missing_required_skills: List[str] = []
    missing_preferred_skills: List[str] = []
    weak_skills: List[str] = []
    experience_relevance: int = 0
    education_relevance: int = 0
    profile_completeness: int = 0
    skill_gap_count: int = 0


class SkillGapItem(BaseModel):
    model_config = ConfigDict(from_attributes=True)

    step_number: int
    skill_name: str
    importance: str  # High, Medium, Nice-to-have
    current_proficiency: str
    recommendation: str
    estimated_time: Optional[str] = None
    # Phase 3B Enhanced fields
    why_it_matters: Optional[str] = None
    roadmap_topics: List[str] = []
    suggested_practice: Optional[str] = None


class SkillGapResponse(BaseModel):
    model_config = ConfigDict(from_attributes=True)

    job_id: str
    job_title: str
    match_score: int
    gaps: List[SkillGapItem]
