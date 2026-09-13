import uuid
from datetime import datetime, timezone
from sqlalchemy import Column, String, Integer, DateTime, ForeignKey, Text
from sqlalchemy.orm import relationship
from app.core.database import Base


class JobMatch(Base):
    __tablename__ = "job_matches"

    id = Column(String(36), primary_key=True, default=lambda: str(uuid.uuid4()), index=True)
    user_id = Column(String(36), ForeignKey("users.id", ondelete="CASCADE"), nullable=False, index=True)
    job_id = Column(String(36), ForeignKey("jobs.id", ondelete="CASCADE"), nullable=False, index=True)
    match_score = Column(Integer, nullable=False)  # 0 to 100
    match_tier = Column(String(50), nullable=False)  # Excellent Match, Strong Match, Moderate Match, Low Match, Weak Match
    rationale = Column(Text, nullable=False)
    matched_skills = Column(String(500), nullable=False)  # Comma-separated
    missing_skills = Column(String(500), nullable=False)  # Comma-separated
    missing_required_skills = Column(Text, nullable=True) # Comma-separated or JSON
    missing_preferred_skills = Column(Text, nullable=True) # Comma-separated or JSON
    weak_skills = Column(Text, nullable=True)              # Comma-separated or JSON
    scoring_breakdown_json = Column(Text, nullable=True)   # JSON: required, preferred, experience, education, completeness
    matcher_version = Column(String(20), default="3.0-rule", nullable=False)
    created_at = Column(DateTime(timezone=True), default=lambda: datetime.now(timezone.utc), nullable=False)
    updated_at = Column(DateTime(timezone=True), default=lambda: datetime.now(timezone.utc), onupdate=lambda: datetime.now(timezone.utc), nullable=False)

    # Relationships
    user = relationship("User", back_populates="job_matches")
    job = relationship("Job", back_populates="matches")
    skill_gaps = relationship("SkillGap", back_populates="job_match", cascade="all, delete-orphan")


class SkillGap(Base):
    __tablename__ = "skill_gaps"

    id = Column(String(36), primary_key=True, default=lambda: str(uuid.uuid4()), index=True)
    job_match_id = Column(String(36), ForeignKey("job_matches.id", ondelete="CASCADE"), nullable=False, index=True)
    skill_name = Column(String(100), nullable=False)
    importance = Column(String(50), default="High", nullable=False)  # High, Medium, Nice-to-have
    current_proficiency = Column(String(50), default="Not detected in profile", nullable=False)
    why_it_matters = Column(Text, nullable=True)
    recommendation = Column(Text, nullable=False)
    step_number = Column(Integer, default=1, nullable=False)
    estimated_time = Column(String(50), nullable=True)  # e.g., "3 days", "1 week"
    roadmap_topics_json = Column(Text, nullable=True)    # JSON array of study topics
    suggested_practice = Column(Text, nullable=True)     # Recommended hands-on project
    created_at = Column(DateTime(timezone=True), default=lambda: datetime.now(timezone.utc), nullable=False)

    # Relationships
    job_match = relationship("JobMatch", back_populates="skill_gaps")
