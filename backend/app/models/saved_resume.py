import uuid
from datetime import datetime, timezone
from sqlalchemy import Column, String, Boolean, DateTime, ForeignKey, Text, JSON
from sqlalchemy.orm import relationship
from app.core.database import Base


class SavedResume(Base):
    __tablename__ = "saved_resumes"

    id = Column(String(36), primary_key=True, default=lambda: str(uuid.uuid4()), index=True)
    user_id = Column(String(36), ForeignKey("users.id", ondelete="CASCADE"), nullable=False, index=True)
    title = Column(String(255), nullable=False)
    template_type = Column(String(50), default="Modern", nullable=False)  # Minimal, Modern, Professional, Executive
    target_job_id = Column(String(36), ForeignKey("jobs.id", ondelete="SET NULL"), nullable=True, index=True)
    
    # Grounded structured sections
    contact_json = Column(JSON, nullable=True)
    summary_text = Column(Text, nullable=True)
    skills_json = Column(JSON, nullable=True)
    experience_json = Column(JSON, nullable=True)
    projects_json = Column(JSON, nullable=True)
    education_json = Column(JSON, nullable=True)
    certifications_json = Column(JSON, nullable=True)
    custom_sections_json = Column(JSON, nullable=True)
    
    is_tailored = Column(Boolean, default=False, nullable=False)
    tailored_role_title = Column(String(255), nullable=True)
    pdf_path = Column(String(500), nullable=True)
    
    created_at = Column(DateTime(timezone=True), default=lambda: datetime.now(timezone.utc), nullable=False)
    updated_at = Column(DateTime(timezone=True), default=lambda: datetime.now(timezone.utc), onupdate=lambda: datetime.now(timezone.utc), nullable=False)

    # Relationships
    user = relationship("User")
    target_job = relationship("Job")
