import json
from typing import List
from fastapi import APIRouter, Depends, HTTPException, status
from sqlalchemy.orm import Session

from app.core.dependencies import get_db, get_current_user
from app.models.user import User
from app.models.interview import MockInterviewSession
from app.schemas.interview import (
    MockInterviewStartRequest,
    MockInterviewSubmitRequest,
    MockInterviewSessionResponse,
    MockInterviewReportResponse,
    InterviewQuestion
)
from app.services.interview_service import MockInterviewService

router = APIRouter(prefix="/interviews", tags=["AI Mock Interview Simulator"])


@router.post("/start", response_model=MockInterviewSessionResponse, status_code=status.HTTP_201_CREATED)
def start_mock_interview(
    req: MockInterviewStartRequest,
    current_user: User = Depends(get_current_user),
    db: Session = Depends(get_db)
):
    """
    Initializes a new interactive AI Mock Interview session.
    Extracts candidate projects and skills if resume/profile is selected,
    or tailors questions to the provided target role.
    """
    session = MockInterviewService.create_session(
        db=db,
        user=current_user,
        mode=req.mode,
        target_role=req.target_role,
        experience_level=req.experience_level,
        resume_id=req.resume_id
    )

    questions_raw = json.loads(session.questions_json)
    questions = [InterviewQuestion(**q) for q in questions_raw]

    return MockInterviewSessionResponse(
        id=session.id,
        title=session.title,
        mode=session.mode,
        target_role=session.target_role,
        experience_level=session.experience_level,
        status=session.status,
        questions=questions,
        overall_score=session.overall_score,
        created_at=session.created_at
    )


@router.post("/{session_id}/submit", response_model=MockInterviewReportResponse)
def submit_mock_interview(
    session_id: str,
    req: MockInterviewSubmitRequest,
    current_user: User = Depends(get_current_user),
    db: Session = Depends(get_db)
):
    """
    Evaluates candidate interview answers and face presence metrics.
    Generates multi-metric score report with model answers and improvement roadmaps.
    """
    session = db.query(MockInterviewSession).filter(
        MockInterviewSession.id == session_id,
        MockInterviewSession.user_id == current_user.id
    ).first()
    if not session:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Interview session not found.")

    report = MockInterviewService.evaluate_session(
        db=db,
        session=session,
        answers=req.answers,
        face_presence_score=req.face_presence_score or 100.0
    )
    return report


@router.get("/history", response_model=List[MockInterviewSessionResponse])
def get_interview_history(
    current_user: User = Depends(get_current_user),
    db: Session = Depends(get_db)
):
    """Retrieves previous interview sessions and scores."""
    sessions = db.query(MockInterviewSession).filter(
        MockInterviewSession.user_id == current_user.id
    ).order_by(MockInterviewSession.created_at.desc()).all()

    results = []
    for s in sessions:
        q_raw = json.loads(s.questions_json) if s.questions_json else []
        questions = [InterviewQuestion(**q) for q in q_raw]
        results.append(
            MockInterviewSessionResponse(
                id=s.id,
                title=s.title,
                mode=s.mode,
                target_role=s.target_role,
                experience_level=s.experience_level,
                status=s.status,
                questions=questions,
                overall_score=s.overall_score,
                created_at=s.created_at
            )
        )
    return results


@router.get("/{session_id}/report", response_model=MockInterviewReportResponse)
def get_interview_report(
    session_id: str,
    current_user: User = Depends(get_current_user),
    db: Session = Depends(get_db)
):
    """Retrieves full evaluation report for an interview session."""
    session = db.query(MockInterviewSession).filter(
        MockInterviewSession.id == session_id,
        MockInterviewSession.user_id == current_user.id
    ).first()
    if not session:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Interview session not found.")

    if not session.report_json:
        raise HTTPException(status_code=status.HTTP_400_BAD_REQUEST, detail="Interview has not been submitted or evaluated yet.")

    report_dict = json.loads(session.report_json)
    return MockInterviewReportResponse(**report_dict)
