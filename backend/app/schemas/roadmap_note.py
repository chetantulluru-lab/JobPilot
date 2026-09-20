from datetime import datetime
from typing import Optional
from pydantic import BaseModel


class RoadmapNoteRequest(BaseModel):
    note_text: str = ""
    is_bookmarked: Optional[bool] = None


class RoadmapNoteResponse(BaseModel):
    id: str
    roadmap_id: str
    day_id: str
    note_text: str
    is_bookmarked: bool
    updated_at: datetime


class BookmarkedDaySummary(BaseModel):
    day_id: str
    roadmap_id: str
    roadmap_title: str
    day_number: int
    day_title: str
    note_text: str
    is_bookmarked: bool
