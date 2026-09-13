from datetime import datetime
from typing import List, Optional, Dict, Any
from pydantic import BaseModel, ConfigDict


class AIMessageDto(BaseModel):
    model_config = ConfigDict(from_attributes=True)

    id: str
    sender: str
    content: str
    metadata_json: Optional[Dict[str, Any]] = None
    created_at: datetime


class AIConversationDto(BaseModel):
    model_config = ConfigDict(from_attributes=True)

    id: str
    title: str
    context_type: str
    context_id: Optional[str] = None
    created_at: datetime
    updated_at: datetime
    messages: List[AIMessageDto] = []


class ChatRequest(BaseModel):
    message: str
    conversation_id: Optional[str] = None
    job_id: Optional[str] = None
    context_type: Optional[str] = "GENERAL"


class ChatResponse(BaseModel):
    conversation_id: str
    message_id: str
    reply: str
    model: str
    is_fallback: bool
    tokens_used: Optional[int] = None
