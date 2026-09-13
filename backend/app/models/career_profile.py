import uuid
from datetime import datetime, timezone
from sqlalchemy import Column, String, Integer, DateTime, ForeignKey
from sqlalchemy.orm import relationship
from app.core.database import Base


class CareerProfile(Base):
    __tablename__ = "career_profiles"

    id = Column(String(36), primary_key=True, default=lambda: str(uuid.uuid4()), index=True)
    user_id = Column(String(36), ForeignKey("users.id", ondelete="CASCADE"), unique=True, nullable=False, index=True)
    headline = Column(String(255), nullable=True)
    summary = Column(String(2000), nullable=True)
    profile_strength = Column(Integer, default=50, nullable=False)  # 0 to 100%
    created_at = Column(DateTime(timezone=True), default=lambda: datetime.now(timezone.utc), nullable=False)
    updated_at = Column(DateTime(timezone=True), default=lambda: datetime.now(timezone.utc), onupdate=lambda: datetime.now(timezone.utc), nullable=False)

    # Relationships
    user = relationship("User", back_populates="profile")
    personal_info = relationship("PersonalInfo", back_populates="profile", uselist=False, cascade="all, delete-orphan")
    education = relationship("Education", back_populates="profile", cascade="all, delete-orphan")
    skills = relationship("Skill", back_populates="profile", cascade="all, delete-orphan")
    experience = relationship("Experience", back_populates="profile", cascade="all, delete-orphan")
    projects = relationship("Project", back_populates="profile", cascade="all, delete-orphan")
    certifications = relationship("Certification", back_populates="profile", cascade="all, delete-orphan")
    social_profiles = relationship("SocialProfile", back_populates="profile", cascade="all, delete-orphan")
    job_preferences = relationship("JobPreference", back_populates="profile", cascade="all, delete-orphan")
