import uuid
from datetime import datetime, timezone
from sqlalchemy import Column, String, Integer, DateTime, ForeignKey, Text
from sqlalchemy.orm import relationship
from app.core.database import Base


class MockInterviewSession(Base):
    __tablename__ = "mock_interview_sessions"

    id = Column(String(36), primary_key=True, default=lambda: str(uuid.uuid4()), index=True)
    user_id = Column(String(36), ForeignKey("users.id", ondelete="CASCADE"), nullable=False, index=True)
    title = Column(String(255), nullable=False)
    mode = Column(String(50), default="ROLE_BASED", nullable=False)  # RESUME_BASED or ROLE_BASED
    target_role = Column(String(255), nullable=False)
    experience_level = Column(String(50), default="Entry-Level", nullable=False)
    resume_id = Column(String(36), ForeignKey("resumes.id", ondelete="SET NULL"), nullable=True, index=True)
    status = Column(String(50), default="IN_PROGRESS", nullable=False)  # IN_PROGRESS, COMPLETED, ABANDONED

    questions_json = Column(Text, nullable=False, default="[]")
    answers_json = Column(Text, nullable=False, default="[]")

    overall_score = Column(Integer, nullable=True)
    technical_score = Column(Integer, nullable=True)
    communication_score = Column(Integer, nullable=True)
    problem_solving_score = Column(Integer, nullable=True)
    presence_score = Column(Integer, nullable=True)
    report_json = Column(Text, nullable=True)

    created_at = Column(DateTime(timezone=True), default=lambda: datetime.now(timezone.utc), nullable=False)
    updated_at = Column(DateTime(timezone=True), default=lambda: datetime.now(timezone.utc), onupdate=lambda: datetime.now(timezone.utc), nullable=False)

    # Relationships
    user = relationship("User")
    resume = relationship("Resume")
