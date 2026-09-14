import uuid
from datetime import datetime, timezone
from sqlalchemy import Column, String, Integer, Boolean, DateTime, ForeignKey, Text
from sqlalchemy.orm import relationship
from app.core.database import Base


class Resume(Base):
    __tablename__ = "resumes"

    id = Column(String(36), primary_key=True, default=lambda: str(uuid.uuid4()), index=True)
    user_id = Column(String(36), ForeignKey("users.id", ondelete="CASCADE"), nullable=False, index=True)
    title = Column(String(255), nullable=False)
    file_path = Column(String(500), nullable=True)
    file_name = Column(String(255), nullable=False)
    file_type = Column(String(100), nullable=False)  # application/pdf, application/vnd.openxmlformats-officedocument.wordprocessingml.document
    file_size_bytes = Column(Integer, nullable=False)
    raw_text = Column(Text, nullable=True)             # Original extracted text for debugging/auditing
    extraction_status = Column(String(50), default="EXTRACTED", nullable=False)  # PENDING, EXTRACTED, CONFIRMED, FAILED
    parsed_data_json = Column(Text, nullable=True)     # Extracted sections, skills, experience
    missing_fields_json = Column(Text, nullable=True)  # Detected missing items
    audit_json = Column(Text, nullable=True)           # Detailed audit (missing, uncertain, score)
    analysis_json = Column(Text, nullable=True)        # Cached AI ATS-style analysis
    is_active = Column(Boolean, default=True, nullable=False)
    created_at = Column(DateTime(timezone=True), default=lambda: datetime.now(timezone.utc), nullable=False)
    updated_at = Column(DateTime(timezone=True), default=lambda: datetime.now(timezone.utc), onupdate=lambda: datetime.now(timezone.utc), nullable=False)

    # Relationships
    user = relationship("User", back_populates="resumes")


class ResumeTemplate(Base):
    __tablename__ = "resume_templates"

    id = Column(String(36), primary_key=True, default=lambda: str(uuid.uuid4()), index=True)
    template_type = Column(String(50), unique=True, nullable=False)  # Minimal, Modern, Professional, Executive
    name = Column(String(100), nullable=False)
    description = Column(String(500), nullable=False)
    is_ats_optimized = Column(Boolean, default=True, nullable=False)
    created_at = Column(DateTime(timezone=True), default=lambda: datetime.now(timezone.utc), nullable=False)
