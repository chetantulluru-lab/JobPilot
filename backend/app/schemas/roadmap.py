from typing import List, Optional
from datetime import datetime
from pydantic import BaseModel, Field


# --- Autocomplete & Search Schemas ---
class RoadmapSuggestionResponse(BaseModel):
    query: str
    suggestions: List[str]


# --- Request Schemas ---
class RoadmapGenerateRequest(BaseModel):
    goal: str = Field(..., min_length=2, max_length=200, description="Role, technology, or topic (e.g. Python Backend Developer, NLP, FastAPI)")
    duration: str = Field("6 Months", description="Learning duration: '3 Months', '6 Months', '12 Months'")


# --- Detailed Response Schemas ---
class PracticeTask(BaseModel):
    title: str
    description: str
    expected_output: Optional[str] = None


class RoadmapResourceResponse(BaseModel):
    id: str
    day_id: Optional[str] = None
    phase_id: str
    title: str
    url: str
    language: str  # English, Telugu, Hindi
    resource_type: str  # video, article, doc
    source: str  # YouTube, Docs, GeeksforGeeks, FreeCodeCamp


class RoadmapDayResponse(BaseModel):
    id: str
    roadmap_id: str
    phase_id: str
    day_number: int
    topic: str
    learning_objective: Optional[str] = None
    subtopics: List[str] = []
    practice_tasks: List[PracticeTask] = []
    is_completed: bool = False
    completed_at: Optional[datetime] = None


class RoadmapPhaseResponse(BaseModel):
    id: str
    roadmap_id: str
    phase_number: int
    title: str
    description: Optional[str] = None
    is_unlocked: bool = False
    is_completed: bool = False
    project_title: Optional[str] = None
    project_description: Optional[str] = None
    days: List[RoadmapDayResponse] = []
    resources: List[RoadmapResourceResponse] = []


class RoadmapDetailResponse(BaseModel):
    id: str
    user_id: str
    title: str
    goal: str
    duration: str
    total_days: int
    completed_days: int
    progress_percentage: int
    is_completed: bool
    skills_learned: List[str] = []
    phases: List[RoadmapPhaseResponse] = []
    created_at: datetime
    updated_at: datetime


class RoadmapSummaryResponse(BaseModel):
    id: str
    user_id: str
    title: str
    goal: str
    duration: str
    total_days: int
    completed_days: int
    progress_percentage: int
    is_completed: bool
    current_phase_title: Optional[str] = None
    current_day_topic: Optional[str] = None
    created_at: datetime


class DayCompleteResponse(BaseModel):
    is_completed: bool
    progress_percentage: int
    completed_days: int
    total_days: int
    phase_unlocked: bool
    roadmap_completed: bool
    current_streak: int
    skills_learned: List[str] = []


class AddSkillsToResumeResponse(BaseModel):
    status: str
    message: str
    added_skills: List[str]


# --- Strict LLM Validation Schemas (Pydantic) ---
class LLMPracticeTask(BaseModel):
    title: str
    description: str


class LLMDay(BaseModel):
    day_number: int
    topic: str
    learning_objective: str
    subtopics: List[str] = []
    practice_tasks: List[LLMPracticeTask] = []


class LLMPhase(BaseModel):
    phase_number: int
    title: str
    description: str
    project_title: str
    project_description: str
    days: List[LLMDay]


class LLMRoadmapStructure(BaseModel):
    title: str
    goal: str
    duration: str
    skills_learned: List[str]
    phases: List[LLMPhase]


# --- Course Catalog & Curriculum Assistant Schemas ---
class CourseCatalogItem(BaseModel):
    id: str
    title: str
    category: str
    badge: str
    description: str
    skills: List[str]
    total_days: int
    total_phases: int


class CourseCatalogResponse(BaseModel):
    courses: List[CourseCatalogItem]


class RoadmapGenerateFromCoursesRequest(BaseModel):
    course_ids: List[str]
    duration: str = "6 Months"


class CurriculumAssistantRequest(BaseModel):
    topic: str
    question: str
    day_number: Optional[int] = None


class CurriculumAssistantResponse(BaseModel):
    answer: str
    topic: str
