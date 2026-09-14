import uuid
from datetime import datetime, timezone, date
from sqlalchemy import Column, String, Integer, Boolean, DateTime, Date, ForeignKey, Text
from sqlalchemy.orm import relationship
from app.core.database import Base


class Roadmap(Base):
    __tablename__ = "roadmaps"

    id = Column(String(36), primary_key=True, default=lambda: str(uuid.uuid4()), index=True)
    user_id = Column(String(36), ForeignKey("users.id", ondelete="CASCADE"), nullable=False, index=True)
    title = Column(String(255), nullable=False)
    goal = Column(String(255), nullable=False)
    duration = Column(String(50), nullable=False)  # "3 Months", "6 Months", "12 Months"
    total_days = Column(Integer, default=0, nullable=False)
    completed_days = Column(Integer, default=0, nullable=False)
    progress_percentage = Column(Integer, default=0, nullable=False)
    is_completed = Column(Boolean, default=False, nullable=False)
    skills_learned_json = Column(Text, default="[]", nullable=False)
    created_at = Column(DateTime(timezone=True), default=lambda: datetime.now(timezone.utc), nullable=False)
    updated_at = Column(DateTime(timezone=True), default=lambda: datetime.now(timezone.utc), onupdate=lambda: datetime.now(timezone.utc), nullable=False)

    # Relationships
    phases = relationship("RoadmapPhase", back_populates="roadmap", cascade="all, delete-orphan", order_by="RoadmapPhase.phase_number")


class RoadmapPhase(Base):
    __tablename__ = "roadmap_phases"

    id = Column(String(36), primary_key=True, default=lambda: str(uuid.uuid4()), index=True)
    roadmap_id = Column(String(36), ForeignKey("roadmaps.id", ondelete="CASCADE"), nullable=False, index=True)
    phase_number = Column(Integer, nullable=False)
    title = Column(String(255), nullable=False)
    description = Column(Text, nullable=True)
    is_unlocked = Column(Boolean, default=False, nullable=False)
    is_completed = Column(Boolean, default=False, nullable=False)
    project_title = Column(String(255), nullable=True)
    project_description = Column(Text, nullable=True)
    created_at = Column(DateTime(timezone=True), default=lambda: datetime.now(timezone.utc), nullable=False)

    # Relationships
    roadmap = relationship("Roadmap", back_populates="phases")
    days = relationship("RoadmapDay", back_populates="phase", cascade="all, delete-orphan", order_by="RoadmapDay.day_number")
    resources = relationship("RoadmapResource", back_populates="phase", cascade="all, delete-orphan")


class RoadmapDay(Base):
    __tablename__ = "roadmap_days"

    id = Column(String(36), primary_key=True, default=lambda: str(uuid.uuid4()), index=True)
    roadmap_id = Column(String(36), ForeignKey("roadmaps.id", ondelete="CASCADE"), nullable=False, index=True)
    phase_id = Column(String(36), ForeignKey("roadmap_phases.id", ondelete="CASCADE"), nullable=False, index=True)
    day_number = Column(Integer, nullable=False)
    topic = Column(String(255), nullable=False)
    learning_objective = Column(Text, nullable=True)
    subtopics_json = Column(Text, default="[]", nullable=False)
    practice_tasks_json = Column(Text, default="[]", nullable=False)
    is_completed = Column(Boolean, default=False, nullable=False)
    completed_at = Column(DateTime(timezone=True), nullable=True)

    # Relationships
    phase = relationship("RoadmapPhase", back_populates="days")
    resources = relationship("RoadmapResource", back_populates="day", cascade="all, delete-orphan")


class RoadmapResource(Base):
    __tablename__ = "roadmap_resources"

    id = Column(String(36), primary_key=True, default=lambda: str(uuid.uuid4()), index=True)
    day_id = Column(String(36), ForeignKey("roadmap_days.id", ondelete="CASCADE"), nullable=True, index=True)
    phase_id = Column(String(36), ForeignKey("roadmap_phases.id", ondelete="CASCADE"), nullable=False, index=True)
    title = Column(String(255), nullable=False)
    url = Column(String(1000), nullable=False)
    language = Column(String(50), default="English", nullable=False)  # "English", "Telugu", "Hindi"
    resource_type = Column(String(50), default="video", nullable=False)  # "video", "article", "doc"
    source = Column(String(100), default="YouTube", nullable=False)

    # Relationships
    phase = relationship("RoadmapPhase", back_populates="resources")
    day = relationship("RoadmapDay", back_populates="resources")


class LearningActivity(Base):
    __tablename__ = "learning_activities"

    id = Column(String(36), primary_key=True, default=lambda: str(uuid.uuid4()), index=True)
    user_id = Column(String(36), ForeignKey("users.id", ondelete="CASCADE"), nullable=False, index=True)
    activity_type = Column(String(50), default="day_completed", nullable=False)
    reference_id = Column(String(36), nullable=True)
    activity_date = Column(Date, default=lambda: datetime.now(timezone.utc).date(), nullable=False)
    created_at = Column(DateTime(timezone=True), default=lambda: datetime.now(timezone.utc), nullable=False)
