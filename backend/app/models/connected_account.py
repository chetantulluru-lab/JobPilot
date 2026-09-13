import uuid
from datetime import datetime, timezone
from sqlalchemy import Column, String, Boolean, DateTime, ForeignKey, Text
from sqlalchemy.orm import relationship
from app.core.database import Base


class ConnectedAccount(Base):
    __tablename__ = "connected_accounts"

    id = Column(String(36), primary_key=True, default=lambda: str(uuid.uuid4()), index=True)
    user_id = Column(String(36), ForeignKey("users.id", ondelete="CASCADE"), nullable=False, index=True)
    provider = Column(String(50), nullable=False)  # google, github, linkedin
    account_email = Column(String(255), nullable=True)
    account_name = Column(String(255), nullable=True)
    avatar_url = Column(String(500), nullable=True)
    is_connected = Column(Boolean, default=False, nullable=False)
    scopes = Column(String(500), nullable=True)  # e.g., "gmail.readonly"
    last_synced_at = Column(DateTime(timezone=True), nullable=True)
    created_at = Column(DateTime(timezone=True), default=lambda: datetime.now(timezone.utc), nullable=False)

    # Relationships
    user = relationship("User", back_populates="connected_accounts")
    email_events = relationship("EmailEvent", back_populates="connected_account", cascade="all, delete-orphan")


class EmailEvent(Base):
    __tablename__ = "email_events"

    id = Column(String(36), primary_key=True, default=lambda: str(uuid.uuid4()), index=True)
    connected_account_id = Column(String(36), ForeignKey("connected_accounts.id", ondelete="CASCADE"), nullable=True, index=True)
    user_id = Column(String(36), ForeignKey("users.id", ondelete="CASCADE"), nullable=False, index=True)
    sender = Column(String(255), nullable=False)
    company = Column(String(255), nullable=False)
    subject = Column(String(500), nullable=False)
    snippet = Column(Text, nullable=False)
    category = Column(String(50), default="OTHER", nullable=False)
    # Categories: APPLICATION_RECEIVED, ASSESSMENT, INTERVIEW, SHORTLISTED, REJECTION, OFFER, OTHER
    detected_date = Column(String(50), nullable=False)
    is_processed = Column(Boolean, default=False, nullable=False)
    created_at = Column(DateTime(timezone=True), default=lambda: datetime.now(timezone.utc), nullable=False)

    # Relationships
    connected_account = relationship("ConnectedAccount", back_populates="email_events")
