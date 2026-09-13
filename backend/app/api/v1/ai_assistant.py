"""
AI Career Assistant API Endpoints for JobPilot.
Powers interactive career coaching, match explanation, and profile improvement advice
grounded in verified candidate profile data.
"""

from typing import List, Dict, Any, Optional
from fastapi import APIRouter, Depends, HTTPException, status
from sqlalchemy.orm import Session

from app.core.dependencies import get_db, get_current_user
from app.models.user import User
from app.models.career_profile import CareerProfile
from app.models.job import Job
from app.models.ai_assistant import AIConversation, AIMessage
from app.schemas.ai_assistant import (
    AIConversationDto,
    AIMessageDto,
    ChatRequest,
    ChatResponse
)
from app.services.ai.service import ai_service
from app.services.matching.matching_service import MatchingService

router = APIRouter(prefix="/assistant", tags=["AI Career Assistant"])


def _extract_profile_dict(profile: Optional[CareerProfile], user: User) -> Dict[str, Any]:
    if not profile:
        return {"full_name": user.full_name}
    
    return {
        "full_name": user.full_name,
        "headline": profile.headline,
        "summary": profile.summary,
        "skills": [{"name": s.name, "proficiency": s.proficiency} for s in (profile.skills or [])],
        "experience": [
            {
                "company": e.company,
                "title": e.title,
                "start_date": e.start_date,
                "end_date": e.end_date,
                "is_current": e.is_current,
                "description": e.description
            } for e in (profile.experience or [])
        ],
        "projects": [
            {
                "title": p.title,
                "tech_stack": p.tech_stack,
                "description": p.description
            } for p in (profile.projects or [])
        ],
        "education": [
            {
                "degree": ed.degree,
                "field_of_study": ed.field_of_study,
                "institution": ed.institution
            } for ed in (profile.education or [])
        ]
    }


@router.get("/conversations", response_model=List[AIConversationDto])
def list_conversations(
    current_user: User = Depends(get_current_user),
    db: Session = Depends(get_db)
):
    """Lists all career coaching conversations belonging to the authenticated user."""
    return db.query(AIConversation).filter(
        AIConversation.user_id == current_user.id
    ).order_by(AIConversation.updated_at.desc()).all()


@router.get("/conversations/{conversation_id}", response_model=AIConversationDto)
def get_conversation(
    conversation_id: str,
    current_user: User = Depends(get_current_user),
    db: Session = Depends(get_db)
):
    """Retrieves conversation history with full message thread."""
    conv = db.query(AIConversation).filter(
        AIConversation.id == conversation_id,
        AIConversation.user_id == current_user.id
    ).first()
    if not conv:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Conversation not found.")
    return conv


@router.delete("/conversations/{conversation_id}", status_code=status.HTTP_204_NO_CONTENT)
def delete_conversation(
    conversation_id: str,
    current_user: User = Depends(get_current_user),
    db: Session = Depends(get_db)
):
    """Deletes a conversation and its messages."""
    conv = db.query(AIConversation).filter(
        AIConversation.id == conversation_id,
        AIConversation.user_id == current_user.id
    ).first()
    if not conv:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Conversation not found.")
    db.delete(conv)
    db.commit()
    return None


@router.post("/chat", response_model=ChatResponse)
def chat_with_assistant(
    req: ChatRequest,
    current_user: User = Depends(get_current_user),
    db: Session = Depends(get_db)
):
    """
    Sends a message to the AI Career Coach.
    Uses candidate's verified profile and active job context to provide grounded, honest advice.
    """
    if not req.message or not req.message.strip():
        raise HTTPException(status_code=status.HTTP_400_BAD_REQUEST, detail="Message cannot be empty.")

    # 1. Get or create conversation
    conv = None
    if req.conversation_id:
        conv = db.query(AIConversation).filter(
            AIConversation.id == req.conversation_id,
            AIConversation.user_id == current_user.id
        ).first()

    if not conv:
        title = req.message[:50] + ("..." if len(req.message) > 50 else "")
        conv = AIConversation(
            user_id=current_user.id,
            title=title,
            context_type=req.context_type or "GENERAL",
            context_id=req.job_id
        )
        db.add(conv)
        db.flush()

    # 2. Build history
    history = [
        {"sender": m.sender, "content": m.content}
        for m in (conv.messages or [])
    ]

    # 3. Build grounded profile context
    profile = db.query(CareerProfile).filter(CareerProfile.user_id == current_user.id).first()
    profile_data = _extract_profile_dict(profile, current_user)

    # 4. Build job context if job_id specified
    job_context = None
    if req.job_id or conv.context_id:
        target_job_id = req.job_id or conv.context_id
        job = db.query(Job).filter(Job.id == target_job_id).first()
        if job:
            job_context = {
                "id": job.id,
                "title": job.title,
                "company": job.company,
                "location": job.location,
                "skills_required": job.skills_required,
                "preferred_skills": job.preferred_skills,
                "description": job.description
            }
            # Run deterministic match to supply factual score to prompt
            match_res = MatchingService.calculate_match(db, current_user.id, job.id)
            job_context["match_score"] = match_res.match_score
            job_context["match_tier"] = match_res.match_tier
            job_context["matched_skills"] = match_res.matched_skills
            job_context["missing_skills"] = match_res.missing_skills

    # 5. Generate AI reply
    ai_result = ai_service.generate_chat_reply(
        user_message=req.message,
        history=history,
        profile_data=profile_data,
        job_context=job_context
    )

    # 6. Save user message and assistant reply to DB
    user_msg = AIMessage(
        conversation_id=conv.id,
        sender="user",
        content=req.message
    )
    db.add(user_msg)

    assistant_msg = AIMessage(
        conversation_id=conv.id,
        sender="assistant",
        content=ai_result["content"],
        metadata_json={"model": ai_result["model"], "is_fallback": ai_result["is_fallback"]}
    )
    db.add(assistant_msg)
    db.commit()
    db.refresh(assistant_msg)

    return ChatResponse(
        conversation_id=conv.id,
        message_id=assistant_msg.id,
        reply=ai_result["content"],
        model=ai_result["model"],
        is_fallback=ai_result["is_fallback"],
        tokens_used=ai_result.get("tokens_used")
    )


@router.post("/quick-coach", response_model=ChatResponse)
def quick_coach_question(
    req: ChatRequest,
    current_user: User = Depends(get_current_user),
    db: Session = Depends(get_db)
):
    """One-shot advice endpoint for quick UI prompt chips without starting a persisted conversation."""
    profile = db.query(CareerProfile).filter(CareerProfile.user_id == current_user.id).first()
    profile_data = _extract_profile_dict(profile, current_user)

    job_context = None
    if req.job_id:
        job = db.query(Job).filter(Job.id == req.job_id).first()
        if job:
            job_context = {
                "id": job.id,
                "title": job.title,
                "company": job.company,
                "skills_required": job.skills_required,
                "preferred_skills": job.preferred_skills,
            }
            match_res = MatchingService.calculate_match(db, current_user.id, job.id)
            job_context["match_score"] = match_res.match_score
            job_context["match_tier"] = match_res.match_tier
            job_context["matched_skills"] = match_res.matched_skills
            job_context["missing_skills"] = match_res.missing_skills

    ai_result = ai_service.generate_chat_reply(
        user_message=req.message,
        history=[],
        profile_data=profile_data,
        job_context=job_context
    )

    return ChatResponse(
        conversation_id="quick-coach",
        message_id="transient",
        reply=ai_result["content"],
        model=ai_result["model"],
        is_fallback=ai_result["is_fallback"],
        tokens_used=ai_result.get("tokens_used")
    )
