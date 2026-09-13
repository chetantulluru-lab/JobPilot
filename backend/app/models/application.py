import uuid
from datetime import datetime, timezone
from sqlalchemy import Column, String, DateTime, ForeignKey, Text
from sqlalchemy.orm import relationship
from app.core.database import Base


class Application(Base):
    __tablename__ = "applications"

    id = Column(String(36), primary_key=True, default=lambda: str(uuid.uuid4()), index=True)
    user_id = Column(String(36), ForeignKey("users.id", ondelete="CASCADE"), nullable=False, index=True)
    job_id = Column(String(36), ForeignKey("jobs.id", ondelete="SET NULL"), nullable=True, index=True)
    company = Column(String(255), nullable=False, index=True)
    role = Column(String(255), nullable=False)
    location = Column(String(255), nullable=True)
    salary = Column(String(100), nullable=True)
    work_mode = Column(String(50), default="Remote", nullable=False)
    status = Column(String(50), default="APPLIED", nullable=False, index=True)
    # Statuses: APPLIED, RECEIVED, ASSESSMENT, INTERVIEW, SHORTLISTED, REJECTED, OFFER, OTHER
    applied_date = Column(String(50), nullable=False)
    next_step = Column(String(255), nullable=True)
    recruiter_name = Column(String(255), nullable=True)
    recruiter_email = Column(String(255), nullable=True)
    notes = Column(Text, nullable=True)
    created_at = Column(DateTime(timezone=True), default=lambda: datetime.now(timezone.utc), nullable=False)
    updated_at = Column(DateTime(timezone=True), default=lambda: datetime.now(timezone.utc), onupdate=lambda: datetime.now(timezone.utc), nullable=False)

    # Relationships
    user = relationship("User", back_populates="applications")
    job = relationship("Job", back_populates="applications")
    events = relationship("ApplicationEvent", back_populates="application", cascade="all, delete-orphan", order_by="ApplicationEvent.created_at")


class ApplicationEvent(Base):
    __tablename__ = "application_events"

    id = Column(String(36), primary_key=True, default=lambda: str(uuid.uuid4()), index=True)
    application_id = Column(String(36), ForeignKey("applications.id", ondelete="CASCADE"), nullable=False, index=True)
    title = Column(String(255), nullable=False)
    description = Column(Text, nullable=True)
    event_date = Column(String(50), nullable=False)
    stage = Column(String(50), nullable=False)
    created_at = Column(DateTime(timezone=True), default=lambda: datetime.now(timezone.utc), nullable=False)

    # Relationships
    application = relationship("Application", back_populates="events")
