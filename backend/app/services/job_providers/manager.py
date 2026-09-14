"""
Job Provider Manager for JobPilot.
Aggregates and orchestrates jobs across multiple providers (PostgreSQL, Public feeds, Greenhouse, etc.),
ensuring queries search across title, company, description, and technology/skills.
"""

from typing import List, Optional
from sqlalchemy.orm import Session
from sqlalchemy import or_

from app.models.job import Job
from app.services.job_providers.base import BaseJobProvider, NormalizedJob
from app.services.job_providers.public_jobs_provider import PublicJobsProvider


class JobProviderManager:
    def __init__(self):
        self.providers: List[BaseJobProvider] = [
            PublicJobsProvider()
        ]

    def sync_public_jobs_to_db(self, db: Session):
        """
        Synchronizes normalized public jobs into PostgreSQL database so they are
        queryable, relationally persistent, and matchable.
        """
        public_provider = PublicJobsProvider()
        all_public = public_provider.search_jobs(limit=100)

        for p_job in all_public:
            existing = db.query(Job).filter(
                or_(
                    Job.external_id == p_job.external_id,
                    Job.title == p_job.title
                )
            ).first()

            if not existing:
                new_job = Job(
                    external_id=p_job.external_id,
                    title=p_job.title,
                    company=p_job.company,
                    location=p_job.location,
                    work_mode=p_job.work_mode,
                    employment_type=p_job.employment_type,
                    salary_range=p_job.salary_range,
                    description=p_job.description,
                    requirements=p_job.requirements,
                    skills_required=p_job.skills_required,
                    preferred_skills=p_job.preferred_skills,
                    education_requirement=p_job.education_requirement,
                    experience_level=p_job.experience_level,
                    posted_date=p_job.posted_date,
                    source=p_job.source,
                    source_url=p_job.source_url,
                    application_url=p_job.application_url,
                    is_active=True
                )
                db.add(new_job)
            else:
                # Update source details if missing
                if not existing.source:
                    existing.source = p_job.source
                if not existing.application_url:
                    existing.application_url = p_job.application_url
                if not existing.external_id:
                    existing.external_id = p_job.external_id

        db.commit()

    def search_jobs(
        self,
        db: Session,
        query: Optional[str] = None,
        location: Optional[str] = None,
        work_mode: Optional[str] = None,
        employment_type: Optional[str] = None,
        limit: int = 20,
        offset: int = 0
    ) -> List[Job]:
        """
        Search jobs stored in PostgreSQL with rich keyword matching across
        title, company, description, and required/preferred skills.
        """
        # Ensure public jobs are seeded in database
        self.sync_public_jobs_to_db(db)

        q = db.query(Job).filter(Job.is_active == True)

        if work_mode and work_mode.lower() != "all":
            q = q.filter(Job.work_mode.ilike(work_mode.strip()))

        if location and location.strip():
            q = q.filter(Job.location.ilike(f"%{location.strip()}%"))

        if employment_type and employment_type.strip():
            q = q.filter(Job.employment_type.ilike(employment_type.strip()))

        if query and query.strip():
            terms = query.strip().split()
            for term in terms:
                term_pattern = f"%{term}%"
                q = q.filter(
                    or_(
                        Job.title.ilike(term_pattern),
                        Job.company.ilike(term_pattern),
                        Job.description.ilike(term_pattern),
                        Job.skills_required.ilike(term_pattern),
                        Job.preferred_skills.ilike(term_pattern)
                    )
                )

        return q.order_by(Job.created_at.desc()).offset(offset).limit(limit).all()


job_provider_manager = JobProviderManager()
