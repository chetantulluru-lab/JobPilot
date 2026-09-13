"""
Job Matching Service facade.
Delegates to the modular MatchingService engine (Phase 3B), preserving backward compatibility.
"""

from sqlalchemy.orm import Session
from app.models.job import Job
from app.schemas.job import JobMatchResponse, SkillGapResponse
from app.services.matching.matching_service import MatchingService


class JobMatchingService:
    @classmethod
    def calculate_match(cls, db: Session, user_id: str, job: Job, force_refresh: bool = False) -> JobMatchResponse:
        return MatchingService.calculate_match(db=db, user_id=user_id, job=job, force_refresh=force_refresh)

    @classmethod
    def get_skill_gap_analysis(cls, db: Session, user_id: str, job: Job) -> SkillGapResponse:
        return MatchingService.get_skill_gap_analysis(db=db, user_id=user_id, job=job)
