import uuid
from datetime import datetime, timezone
from sqlalchemy import Column, String, DateTime, ForeignKey, Text
from sqlalchemy.orm import relationship
from app.core.database import Base


class CoverLetter(Base):
    __tablename__ = "cover_letters"

    id = Column(String(36), primary_key=True, default=lambda: str(uuid.uuid4()), index=True)
    user_id = Column(String(36), ForeignKey("users.id", ondelete="CASCADE"), nullable=False, index=True)
    job_id = Column(String(36), ForeignKey("jobs.id", ondelete="SET NULL"), nullable=True, index=True)
    company = Column(String(255), nullable=False)
    role = Column(String(255), nullable=False)
    tone = Column(String(50), default="Professional", nullable=False)  # Professional, Confident, Concise
    content = Column(Text, nullable=False)
    created_at = Column(DateTime(timezone=True), default=lambda: datetime.now(timezone.utc), nullable=False)
    updated_at = Column(DateTime(timezone=True), default=lambda: datetime.now(timezone.utc), onupdate=lambda: datetime.now(timezone.utc), nullable=False)

    # Relationships
    user = relationship("User")
    job = relationship("Job")


class RecruiterMessage(Base):
    __tablename__ = "recruiter_messages"

    id = Column(String(36), primary_key=True, default=lambda: str(uuid.uuid4()), index=True)
    user_id = Column(String(36), ForeignKey("users.id", ondelete="CASCADE"), nullable=False, index=True)
    job_id = Column(String(36), ForeignKey("jobs.id", ondelete="SET NULL"), nullable=True, index=True)
    recipient_name = Column(String(255), nullable=True)
    platform = Column(String(50), default="LinkedIn", nullable=False)  # LinkedIn, Email
    context = Column(String(500), nullable=True)
    content = Column(Text, nullable=False)
    created_at = Column(DateTime(timezone=True), default=lambda: datetime.now(timezone.utc), nullable=False)

    # Relationships
    user = relationship("User")
    job = relationship("Job")
