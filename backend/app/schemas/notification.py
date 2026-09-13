from datetime import datetime
from typing import Optional
from pydantic import BaseModel, ConfigDict


class NotificationResponse(BaseModel):
    model_config = ConfigDict(from_attributes=True)

    id: str
    user_id: str
    title: str
    message: str
    notification_type: str
    is_read: bool
    deep_link: Optional[str] = None
    created_at: datetime


class NotificationUpdateRead(BaseModel):
    is_read: bool = True
