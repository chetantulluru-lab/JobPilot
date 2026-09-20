from typing import List, Optional
from datetime import datetime, timezone
from fastapi import APIRouter, Depends, Query, HTTPException, status
from sqlalchemy.orm import Session

from app.core.dependencies import get_db, get_current_user
from app.models.user import User
from app.models.roadmap import Roadmap, RoadmapPhase, RoadmapDay
from app.models.roadmap_note import RoadmapNote
from app.schemas.roadmap import (
    RoadmapGenerateRequest, RoadmapSuggestionResponse,
    RoadmapDetailResponse, RoadmapSummaryResponse,
    RoadmapResourceResponse, DayCompleteResponse,
    AddSkillsToResumeResponse, CourseCatalogResponse,
    RoadmapGenerateFromCoursesRequest,
    CurriculumAssistantRequest, CurriculumAssistantResponse
)
from app.schemas.quiz import DailyQuizResponse, QuizSubmitRequest, QuizSubmitResponse
from app.schemas.roadmap_note import RoadmapNoteRequest, RoadmapNoteResponse, BookmarkedDaySummary
from app.services.roadmap_service import RoadmapService
from app.services.quiz_service import QuizService

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


@router.get("/phases/{phase_id}/resources", response_model=List[RoadmapResourceResponse])
def get_phase_resources_direct(
    phase_id: str,
    language: Optional[str] = Query(None, description="Filter by language: 'English', 'Telugu', 'Hindi', or all"),
    current_user: User = Depends(get_current_user),
    db: Session = Depends(get_db)
):
    """
    Direct phase resource fetching matching mobile client signatures.
    """
    return RoadmapService.get_or_create_phase_resources(db, current_user.id, None, phase_id, language)


@router.post("/days/{day_id}/complete", response_model=DayCompleteResponse)
def complete_day_direct(
    day_id: str,
    current_user: User = Depends(get_current_user),
    db: Session = Depends(get_db)
):
    """
    Direct day completion matching mobile client signatures.
    """
    return RoadmapService.complete_day(db, current_user.id, None, day_id)


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


# --- Quiz Endpoints ---

@router.get("/days/{day_id}/quiz", response_model=DailyQuizResponse)
def get_day_quiz(
    day_id: str,
    current_user: User = Depends(get_current_user),
    db: Session = Depends(get_db)
):
    """Retrieves 3 interactive multiple-choice quiz questions for the day's topic."""
    day = db.query(RoadmapDay).filter(RoadmapDay.id == day_id).first()
    if not day:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Roadmap day not found.")
    questions = QuizService.get_quiz_for_day(day)
    return DailyQuizResponse(
        day_id=day.id,
        day_title=day.topic,
        questions=questions
    )


@router.post("/days/{day_id}/quiz/submit", response_model=QuizSubmitResponse)
def submit_day_quiz(
    day_id: str,
    req: QuizSubmitRequest,
    current_user: User = Depends(get_current_user),
    db: Session = Depends(get_db)
):
    """
    Evaluates submitted quiz answers. If score >= 70%, marks day completed,
    increments the user's learning streak, and provides explanations.
    """
    day = db.query(RoadmapDay).filter(RoadmapDay.id == day_id).first()
    if not day:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Roadmap day not found.")
    return QuizService.evaluate_quiz(db, day, current_user, req.submissions)


# --- Notes & Bookmarks Endpoints ---

@router.get("/days/{day_id}/note", response_model=RoadmapNoteResponse)
def get_day_note(
    day_id: str,
    current_user: User = Depends(get_current_user),
    db: Session = Depends(get_db)
):
    """Retrieves personal notes and bookmark state for a specific roadmap day."""
    day = db.query(RoadmapDay).filter(RoadmapDay.id == day_id).first()
    if not day:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Roadmap day not found.")

    note = db.query(RoadmapNote).filter(
        RoadmapNote.day_id == day_id,
        RoadmapNote.user_id == current_user.id
    ).first()

    if not note:
        phase = db.query(RoadmapPhase).filter(RoadmapPhase.id == day.phase_id).first()
        return RoadmapNoteResponse(
            id=f"temp-{day_id}",
            roadmap_id=phase.roadmap_id if phase else "",
            day_id=day_id,
            note_text="",
            is_bookmarked=False,
            updated_at=datetime.now(timezone.utc)
        )

    return RoadmapNoteResponse(
        id=note.id,
        roadmap_id=note.roadmap_id,
        day_id=note.day_id,
        note_text=note.note_text,
        is_bookmarked=note.is_bookmarked,
        updated_at=note.updated_at
    )


@router.put("/days/{day_id}/note", response_model=RoadmapNoteResponse)
def save_day_note(
    day_id: str,
    req: RoadmapNoteRequest,
    current_user: User = Depends(get_current_user),
    db: Session = Depends(get_db)
):
    """Saves or updates personal notes and bookmark status for a roadmap day."""
    day = db.query(RoadmapDay).filter(RoadmapDay.id == day_id).first()
    if not day:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Roadmap day not found.")

    phase = db.query(RoadmapPhase).filter(RoadmapPhase.id == day.phase_id).first()
    roadmap_id = phase.roadmap_id if phase else ""

    note = db.query(RoadmapNote).filter(
        RoadmapNote.day_id == day_id,
        RoadmapNote.user_id == current_user.id
    ).first()

    if not note:
        note = RoadmapNote(
            user_id=current_user.id,
            roadmap_id=roadmap_id,
            day_id=day_id,
            note_text=req.note_text,
            is_bookmarked=req.is_bookmarked if req.is_bookmarked is not None else False
        )
        db.add(note)
    else:
        note.note_text = req.note_text
        if req.is_bookmarked is not None:
            note.is_bookmarked = req.is_bookmarked

    db.commit()
    db.refresh(note)

    return RoadmapNoteResponse(
        id=note.id,
        roadmap_id=note.roadmap_id,
        day_id=note.day_id,
        note_text=note.note_text,
        is_bookmarked=note.is_bookmarked,
        updated_at=note.updated_at
    )


@router.get("/user/bookmarks", response_model=List[BookmarkedDaySummary])
def list_user_bookmarks(
    current_user: User = Depends(get_current_user),
    db: Session = Depends(get_db)
):
    """Lists all bookmarked days and saved notes for the user across all roadmaps."""
    notes = db.query(RoadmapNote).filter(
        RoadmapNote.user_id == current_user.id,
        (RoadmapNote.is_bookmarked == True) | (RoadmapNote.note_text != "")
    ).all()

    summaries = []
    for n in notes:
        day = db.query(RoadmapDay).filter(RoadmapDay.id == n.day_id).first()
        roadmap = db.query(Roadmap).filter(Roadmap.id == n.roadmap_id).first()
        if day and roadmap:
            summaries.append(
                BookmarkedDaySummary(
                    day_id=day.id,
                    roadmap_id=roadmap.id,
                    roadmap_title=roadmap.title,
                    day_number=day.day_number,
                    day_title=day.topic,
                    note_text=n.note_text,
                    is_bookmarked=n.is_bookmarked
                )
            )
    return summaries

