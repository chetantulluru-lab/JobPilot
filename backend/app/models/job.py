import uuid
from datetime import datetime, timezone
from sqlalchemy import Column, String, Boolean, DateTime, Text
from sqlalchemy.orm import relationship
from app.core.database import Base


class Job(Base):
    __tablename__ = "jobs"

    id = Column(String(36), primary_key=True, default=lambda: str(uuid.uuid4()), index=True)
    title = Column(String(255), nullable=False, index=True)
    company = Column(String(255), nullable=False, index=True)
    location = Column(String(255), nullable=False)
    work_mode = Column(String(50), nullable=False, default="Remote", index=True)  # Remote, Hybrid, On-site
    employment_type = Column(String(50), nullable=False, default="Full-time")      # Full-time, Internship, Contract
    salary_range = Column(String(100), nullable=True)
    description = Column(Text, nullable=False)
    requirements = Column(Text, nullable=True)
    skills_required = Column(String(500), nullable=False)  # Comma-separated: "Python, SQL, Git, FastAPI, Docker"
    preferred_skills = Column(String(500), nullable=True)  # Comma-separated: "AWS, Kubernetes, Redis"
    education_requirement = Column(String(255), nullable=True)  # e.g., "Bachelor's degree in Computer Science"
    experience_level = Column(String(50), default="Entry-level")
    posted_date = Column(String(50), nullable=True)
    source = Column(String(100), default="Company Careers", nullable=True)
    source_url = Column(String(500), nullable=True)
    application_url = Column(String(500), nullable=True)
    external_id = Column(String(255), nullable=True, index=True)
    is_active = Column(Boolean, default=True, nullable=False)
    created_at = Column(DateTime(timezone=True), default=lambda: datetime.now(timezone.utc), nullable=False)

    # Relationships
    matches = relationship("JobMatch", back_populates="job", cascade="all, delete-orphan")
    applications = relationship("Application", back_populates="job")
