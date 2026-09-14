"""
Base Job Provider interface and normalized job model for JobPilot.
"""

from abc import ABC, abstractmethod
from typing import List, Optional
from pydantic import BaseModel


class NormalizedJob(BaseModel):
    external_id: str
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
    source: str = "Company Careers"
    source_url: Optional[str] = None
    application_url: Optional[str] = None


class BaseJobProvider(ABC):
    name: str = "BaseProvider"

    @abstractmethod
    def search_jobs(
        self,
        query: Optional[str] = None,
        location: Optional[str] = None,
        work_mode: Optional[str] = None,
        limit: int = 20
    ) -> List[NormalizedJob]:
        """Search jobs from this provider."""
        pass
