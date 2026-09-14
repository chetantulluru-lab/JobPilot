from typing import List, Optional
from fastapi import APIRouter, Depends, Query, status
from sqlalchemy.orm import Session

from app.core.dependencies import get_db, get_current_user
from app.models.user import User
from app.schemas.roadmap import (
    RoadmapGenerateRequest, RoadmapSuggestionResponse,
    RoadmapDetailResponse, RoadmapSummaryResponse,
    RoadmapResourceResponse, DayCompleteResponse,
    AddSkillsToResumeResponse, CourseCatalogResponse,
    RoadmapGenerateFromCoursesRequest,
    CurriculumAssistantRequest, CurriculumAssistantResponse
)
from app.services.roadmap_service import RoadmapService

router = APIRouter(prefix="/roadmaps", tags=["Roadmaps & Learning"])


@router.get("/catalog", response_model=CourseCatalogResponse)
def get_course_catalog():
    """
    Returns the comprehensive catalog of 100+ Computer Science courses,
    core subjects, and developer tracks.
    """
    courses = RoadmapService.get_catalog_courses()
    return CourseCatalogResponse(courses=courses)


@router.post("/generate-from-courses", response_model=RoadmapDetailResponse, status_code=status.HTTP_201_CREATED)
def generate_roadmap_from_courses(
    req: RoadmapGenerateFromCoursesRequest,
    current_user: User = Depends(get_current_user),
    db: Session = Depends(get_db)
):
    """
    Assembles an instant, structured career roadmap from selected course modules.
    Supports single or multi-subject master tracks (e.g. DSA + Java).
    """
    return RoadmapService.generate_from_courses(db, current_user.id, req.course_ids, req.duration)


@router.post("/assistant/ask", response_model=CurriculumAssistantResponse)
def ask_curriculum_assistant(
    req: CurriculumAssistantRequest,
    current_user: User = Depends(get_current_user)
):
    """
    Interactive AI Curriculum Assistant for clarifying doubts and receiving code explanations on daily topics.
    """
    res = RoadmapService.ask_curriculum_assistant(req.topic, req.question, req.day_number)
    return CurriculumAssistantResponse(answer=res["answer"], topic=res["topic"])


@router.get("/suggestions", response_model=RoadmapSuggestionResponse)
def get_roadmap_suggestions(
    query: str = Query("", description="Letter-by-letter input string")
):
    """
    Instant autocomplete search suggestions as user types letter-by-letter.
    Incurs 0 AI token cost.
    """
    return RoadmapService.get_suggestions(query)


@router.post("/generate", response_model=RoadmapDetailResponse, status_code=status.HTTP_201_CREATED)
def generate_roadmap(
    req: RoadmapGenerateRequest,
    current_user: User = Depends(get_current_user),
    db: Session = Depends(get_db)
):
    """
    Generates a structured career roadmap using OpenRouter DeepSeek R1 once,
    validates full JSON schema with Pydantic, and saves permanently to PostgreSQL.
    """
    return RoadmapService.generate_roadmap(db, current_user.id, req)


@router.get("", response_model=List[RoadmapSummaryResponse])
def list_my_roadmaps(
    current_user: User = Depends(get_current_user),
    db: Session = Depends(get_db)
):
    """
    Lists all active and completed roadmaps created by the authenticated user.
    """
    return RoadmapService.get_user_roadmaps(db, current_user.id)


@router.get("/{roadmap_id}", response_model=RoadmapDetailResponse)
def get_roadmap_detail(
    roadmap_id: str,
    current_user: User = Depends(get_current_user),
    db: Session = Depends(get_db)
):
    """
    Retrieves full roadmap curriculum, phases, day-by-day lessons, and phase projects.
    """
    return RoadmapService.get_roadmap_detail(db, current_user.id, roadmap_id)


@router.get("/{roadmap_id}/phases/{phase_id}/resources", response_model=List[RoadmapResourceResponse])
def get_phase_resources(
    roadmap_id: str,
    phase_id: str,
    language: Optional[str] = Query(None, description="Filter by language: 'English', 'Telugu', 'Hindi', or all"),
    current_user: User = Depends(get_current_user),
    db: Session = Depends(get_db)
):
    """
    Retrieves topic-specific video and learning resources for the requested phase.
    Supports English, Telugu, and Hindi filtering.
    Caches in PostgreSQL to avoid redundant operations.
    """
    return RoadmapService.get_or_create_phase_resources(db, current_user.id, roadmap_id, phase_id, language)


@router.post("/{roadmap_id}/days/{day_id}/complete", response_model=DayCompleteResponse)
def complete_day(
    roadmap_id: str,
    day_id: str,
    current_user: User = Depends(get_current_user),
    db: Session = Depends(get_db)
):
    """
    Marks a lesson complete. Recalculates progress percentage, evaluates phase unlocking,
    updates consecutive activity streak, and returns completion metadata.
    """
    return RoadmapService.complete_day(db, current_user.id, roadmap_id, day_id)


@router.post("/{roadmap_id}/add-skills-to-resume", response_model=AddSkillsToResumeResponse)
def add_skills_to_resume(
    roadmap_id: str,
    current_user: User = Depends(get_current_user),
    db: Session = Depends(get_db)
):
    """
    Adds newly mastered skills from a completed roadmap to the user's CareerProfile.
    """
    return RoadmapService.add_skills_to_resume(db, current_user.id, roadmap_id)


@router.delete("/{roadmap_id}", status_code=status.HTTP_204_NO_CONTENT)
def delete_roadmap(
    roadmap_id: str,
    current_user: User = Depends(get_current_user),
    db: Session = Depends(get_db)
):
    """Deletes a roadmap and its associated phases, days, and cached resources."""
    RoadmapService.delete_roadmap(db, current_user.id, roadmap_id)
    return None
