import uuid
from datetime import datetime, timezone
from sqlalchemy import Column, String, Integer, DateTime, ForeignKey
from sqlalchemy.orm import relationship
from app.core.database import Base


class Education(Base):
    __tablename__ = "educations"

    id = Column(String(36), primary_key=True, default=lambda: str(uuid.uuid4()), index=True)
    profile_id = Column(String(36), ForeignKey("career_profiles.id", ondelete="CASCADE"), nullable=False, index=True)
    institution = Column(String(255), nullable=False)
    degree = Column(String(255), nullable=False)
    field_of_study = Column(String(255), nullable=False)
    start_year = Column(Integer, nullable=True)
    end_year = Column(Integer, nullable=True)
    grade_or_cgpa = Column(String(50), nullable=True)
    created_at = Column(DateTime(timezone=True), default=lambda: datetime.now(timezone.utc), nullable=False)

    # Relationships
    profile = relationship("CareerProfile", back_populates="education")
