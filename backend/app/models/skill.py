import uuid
from datetime import datetime, timezone
from sqlalchemy import Column, String, Boolean, DateTime, ForeignKey
from sqlalchemy.orm import relationship
from app.core.database import Base


class Skill(Base):
    __tablename__ = "skills"

    id = Column(String(36), primary_key=True, default=lambda: str(uuid.uuid4()), index=True)
    profile_id = Column(String(36), ForeignKey("career_profiles.id", ondelete="CASCADE"), nullable=False, index=True)
    name = Column(String(100), nullable=False, index=True)
    category = Column(String(100), default="Technical", nullable=False)  # Languages, Frameworks, Databases, Tools
    proficiency = Column(String(50), default="Intermediate", nullable=False)  # Beginner, Intermediate, Advanced, Expert
    is_top_skill = Column(Boolean, default=False, nullable=False)
    created_at = Column(DateTime(timezone=True), default=lambda: datetime.now(timezone.utc), nullable=False)

    # Relationships
    profile = relationship("CareerProfile", back_populates="skills")
