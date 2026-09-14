from datetime import datetime, date
from typing import List, Optional
from pydantic import BaseModel, Field, ConfigDict


# --- Personal Info ---
class PersonalInfoBase(BaseModel):
    full_name: str
    email: str
    phone: Optional[str] = None
    location: Optional[str] = None
    current_role: Optional[str] = None
    bio: Optional[str] = None
    avatar_url: Optional[str] = None
    age: Optional[int] = None
    college: Optional[str] = None
    degree: Optional[str] = None
    branch: Optional[str] = None


class PersonalInfoCreate(PersonalInfoBase):
    pass


class PersonalInfoUpdate(BaseModel):
    full_name: Optional[str] = None
    email: Optional[str] = None
    phone: Optional[str] = None
    location: Optional[str] = None
    current_role: Optional[str] = None
    bio: Optional[str] = None
    avatar_url: Optional[str] = None
    age: Optional[int] = None
    college: Optional[str] = None
    degree: Optional[str] = None
    branch: Optional[str] = None



class PersonalInfoResponse(PersonalInfoBase):
    model_config = ConfigDict(from_attributes=True)

    id: str
    profile_id: str


# --- Education ---
class EducationBase(BaseModel):
    institution: str
    degree: str
    field_of_study: str
    start_year: Optional[int] = None
    end_year: Optional[int] = None
    grade_or_cgpa: Optional[str] = None


class EducationCreate(EducationBase):
    pass


class EducationUpdate(BaseModel):
    institution: Optional[str] = None
    degree: Optional[str] = None
    field_of_study: Optional[str] = None
    start_year: Optional[int] = None
    end_year: Optional[int] = None
    grade_or_cgpa: Optional[str] = None


class EducationResponse(EducationBase):
    model_config = ConfigDict(from_attributes=True)

    id: str
    profile_id: str


# --- Skill ---
class SkillBase(BaseModel):
    name: str
    category: str = "Technical"
    proficiency: str = "Intermediate"
    is_top_skill: bool = False


class SkillCreate(SkillBase):
    pass


class SkillResponse(SkillBase):
    model_config = ConfigDict(from_attributes=True)

    id: str
    profile_id: str


# --- Experience ---
class ExperienceBase(BaseModel):
    company: str
    title: str
    location: Optional[str] = None
    start_date: Optional[str] = None
    end_date: Optional[str] = None
    is_current: bool = False
    description: Optional[str] = None


class ExperienceCreate(ExperienceBase):
    pass


class ExperienceUpdate(BaseModel):
    company: Optional[str] = None
    title: Optional[str] = None
    location: Optional[str] = None
    start_date: Optional[str] = None
    end_date: Optional[str] = None
    is_current: Optional[bool] = None
    description: Optional[str] = None


class ExperienceResponse(ExperienceBase):
    model_config = ConfigDict(from_attributes=True)

    id: str
    profile_id: str


# --- Project ---
class ProjectBase(BaseModel):
    title: str
    description: Optional[str] = None
    tech_stack: Optional[str] = None
    github_url: Optional[str] = None
    live_url: Optional[str] = None
    start_date: Optional[str] = None
    end_date: Optional[str] = None


class ProjectCreate(ProjectBase):
    pass


class ProjectUpdate(BaseModel):
    title: Optional[str] = None
    description: Optional[str] = None
    tech_stack: Optional[str] = None
    github_url: Optional[str] = None
    live_url: Optional[str] = None
    start_date: Optional[str] = None
    end_date: Optional[str] = None


class ProjectResponse(ProjectBase):
    model_config = ConfigDict(from_attributes=True)

    id: str
    profile_id: str


# --- Certification ---
class CertificationBase(BaseModel):
    name: str
    issuer: str
    issue_date: Optional[str] = None
    expiration_date: Optional[str] = None
    credential_url: Optional[str] = None


class CertificationCreate(CertificationBase):
    pass


class CertificationResponse(CertificationBase):
    model_config = ConfigDict(from_attributes=True)

    id: str
    profile_id: str


# --- Social Profile ---
class SocialProfileBase(BaseModel):
    platform: str
    url: str
    username: Optional[str] = None


class SocialProfileCreate(SocialProfileBase):
    pass


class SocialProfileResponse(SocialProfileBase):
    model_config = ConfigDict(from_attributes=True)

    id: str
    profile_id: str


# --- Job Preference ---
class JobPreferenceBase(BaseModel):
    desired_roles: Optional[str] = None
    preferred_locations: Optional[str] = None
    work_modes: Optional[str] = "Remote"
    min_expected_salary: Optional[int] = None
    max_expected_salary: Optional[int] = None


class JobPreferenceCreate(JobPreferenceBase):
    pass


class JobPreferenceResponse(JobPreferenceBase):
    model_config = ConfigDict(from_attributes=True)

    id: str
    profile_id: str


# --- Full Career Profile Response ---
class CareerProfileResponse(BaseModel):
    model_config = ConfigDict(from_attributes=True)

    id: str
    user_id: str
    headline: Optional[str] = None
    summary: Optional[str] = None
    profile_strength: int = 0
    current_streak: int = 0
    longest_streak: int = 0
    last_activity_date: Optional[date] = None
    personal_info: Optional[PersonalInfoResponse] = None
    education: List[EducationResponse] = []
    skills: List[SkillResponse] = []
    experience: List[ExperienceResponse] = []
    projects: List[ProjectResponse] = []
    certifications: List[CertificationResponse] = []
    social_profiles: List[SocialProfileResponse] = []
    job_preferences: List[JobPreferenceResponse] = []
    created_at: datetime
    updated_at: datetime


class CareerProfileUpdate(BaseModel):
    headline: Optional[str] = None
    summary: Optional[str] = None
