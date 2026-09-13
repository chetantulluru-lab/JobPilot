import uuid
from datetime import datetime, timezone
from sqlalchemy import Column, String, DateTime, ForeignKey
from sqlalchemy.orm import relationship
from app.core.database import Base


class Certification(Base):
    __tablename__ = "certifications"

    id = Column(String(36), primary_key=True, default=lambda: str(uuid.uuid4()), index=True)
    profile_id = Column(String(36), ForeignKey("career_profiles.id", ondelete="CASCADE"), nullable=False, index=True)
    name = Column(String(255), nullable=False)
    issuer = Column(String(255), nullable=False)
    issue_date = Column(String(50), nullable=True)
    expiration_date = Column(String(50), nullable=True)
    credential_url = Column(String(500), nullable=True)
    created_at = Column(DateTime(timezone=True), default=lambda: datetime.now(timezone.utc), nullable=False)

    # Relationships
    profile = relationship("CareerProfile", back_populates="certifications")
