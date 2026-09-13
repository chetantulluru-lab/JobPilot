import uuid
from datetime import datetime, timezone
from sqlalchemy import Column, String, DateTime, ForeignKey, Text, JSON
from sqlalchemy.orm import relationship
from app.core.database import Base


class AIConversation(Base):
    __tablename__ = "ai_conversations"

    id = Column(String(36), primary_key=True, default=lambda: str(uuid.uuid4()), index=True)
    user_id = Column(String(36), ForeignKey("users.id", ondelete="CASCADE"), nullable=False, index=True)
    title = Column(String(255), default="Career Coaching Session", nullable=False)
    context_type = Column(String(50), default="GENERAL", nullable=False)  # GENERAL, JOB_MATCH, RESUME_REVIEW, SKILL_GAP
    context_id = Column(String(36), nullable=True)  # Associated job_id or resume_id if applicable
    created_at = Column(DateTime(timezone=True), default=lambda: datetime.now(timezone.utc), nullable=False)
    updated_at = Column(DateTime(timezone=True), default=lambda: datetime.now(timezone.utc), onupdate=lambda: datetime.now(timezone.utc), nullable=False)

    # Relationships
    user = relationship("User")
    messages = relationship("AIMessage", back_populates="conversation", cascade="all, delete-orphan", order_by="AIMessage.created_at")


class AIMessage(Base):
    __tablename__ = "ai_messages"

    id = Column(String(36), primary_key=True, default=lambda: str(uuid.uuid4()), index=True)
    conversation_id = Column(String(36), ForeignKey("ai_conversations.id", ondelete="CASCADE"), nullable=False, index=True)
    sender = Column(String(20), nullable=False)  # "user" or "assistant"
    content = Column(Text, nullable=False)
    metadata_json = Column(JSON, nullable=True)  # Store grounded citations or source profile keys
    created_at = Column(DateTime(timezone=True), default=lambda: datetime.now(timezone.utc), nullable=False)

    # Relationships
    conversation = relationship("AIConversation", back_populates="messages")
