import uuid
from datetime import datetime, timezone
from sqlalchemy import Column, String, Integer, DateTime, ForeignKey
from sqlalchemy.orm import relationship
from app.core.database import Base


class JobPreference(Base):
    __tablename__ = "job_preferences"

    id = Column(String(36), primary_key=True, default=lambda: str(uuid.uuid4()), index=True)
    profile_id = Column(String(36), ForeignKey("career_profiles.id", ondelete="CASCADE"), nullable=False, index=True)
    desired_roles = Column(String(500), nullable=True)     # Comma-separated or JSON string
    preferred_locations = Column(String(500), nullable=True) # e.g., "Bangalore, Remote"
    work_modes = Column(String(200), nullable=True)         # "Remote, Hybrid, On-site"
    min_expected_salary = Column(Integer, nullable=True)
    max_expected_salary = Column(Integer, nullable=True)
    created_at = Column(DateTime(timezone=True), default=lambda: datetime.now(timezone.utc), nullable=False)

    # Relationships
    profile = relationship("CareerProfile", back_populates="job_preferences")
