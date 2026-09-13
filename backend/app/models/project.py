import uuid
from datetime import datetime, timezone
from sqlalchemy import Column, String, DateTime, ForeignKey, Text
from sqlalchemy.orm import relationship
from app.core.database import Base


class Project(Base):
    __tablename__ = "projects"

    id = Column(String(36), primary_key=True, default=lambda: str(uuid.uuid4()), index=True)
    profile_id = Column(String(36), ForeignKey("career_profiles.id", ondelete="CASCADE"), nullable=False, index=True)
    title = Column(String(255), nullable=False)
    description = Column(Text, nullable=True)
    tech_stack = Column(String(500), nullable=True)  # e.g., "Python, FastAPI, Docker, PostgreSQL"
    github_url = Column(String(500), nullable=True)
    live_url = Column(String(500), nullable=True)
    start_date = Column(String(50), nullable=True)
    end_date = Column(String(50), nullable=True)
    created_at = Column(DateTime(timezone=True), default=lambda: datetime.now(timezone.utc), nullable=False)

    # Relationships
    profile = relationship("CareerProfile", back_populates="projects")
