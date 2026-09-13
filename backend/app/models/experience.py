import uuid
from datetime import datetime, timezone
from sqlalchemy import Column, String, Boolean, DateTime, ForeignKey, Text
from sqlalchemy.orm import relationship
from app.core.database import Base


class Experience(Base):
    __tablename__ = "experiences"

    id = Column(String(36), primary_key=True, default=lambda: str(uuid.uuid4()), index=True)
    profile_id = Column(String(36), ForeignKey("career_profiles.id", ondelete="CASCADE"), nullable=False, index=True)
    company = Column(String(255), nullable=False)
    title = Column(String(255), nullable=False)
    location = Column(String(255), nullable=True)
    start_date = Column(String(50), nullable=True)  # e.g., "Jun 2025" or "2025-06"
    end_date = Column(String(50), nullable=True)    # e.g., "Aug 2025" or "Present"
    is_current = Column(Boolean, default=False, nullable=False)
    description = Column(Text, nullable=True)
    created_at = Column(DateTime(timezone=True), default=lambda: datetime.now(timezone.utc), nullable=False)

    # Relationships
    profile = relationship("CareerProfile", back_populates="experience")
