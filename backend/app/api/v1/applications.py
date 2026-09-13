from datetime import datetime, timezone
from typing import List, Optional
from fastapi import APIRouter, Depends, HTTPException, Query, status
from sqlalchemy.orm import Session
from app.core.dependencies import get_db, get_current_user
from app.models.user import User
from app.models.application import Application, ApplicationEvent
from app.schemas.application import (
    ApplicationCreate,
    ApplicationUpdate,
    ApplicationResponse,
    ApplicationEventCreate,
    ApplicationEventResponse
)

router = APIRouter(prefix="/applications", tags=["Application Tracking"])


@router.get("", response_model=List[ApplicationResponse])
def list_my_applications(
    status_filter: Optional[str] = Query(None, description="Filter by status (e.g. APPLIED, INTERVIEW, OFFER)"),
    current_user: User = Depends(get_current_user),
    db: Session = Depends(get_db)
):
    """Retrieve all job applications tracked by the current user."""
    query = db.query(Application).filter(Application.user_id == current_user.id)
    if status_filter and status_filter.upper() != "ALL":
        query = query.filter(Application.status == status_filter.upper())
    return query.order_by(Application.created_at.desc()).all()


@router.post("", response_model=ApplicationResponse, status_code=status.HTTP_201_CREATED)
def create_application(
    app_in: ApplicationCreate,
    current_user: User = Depends(get_current_user),
    db: Session = Depends(get_db)
):
    """Create a new job application and log the initial submission event."""
    app = Application(
        user_id=current_user.id,
        **app_in.model_dump()
    )
    db.add(app)
    db.flush()

    # Create initial milestone event
    initial_event = ApplicationEvent(
        application_id=app.id,
        title="Application Submitted",
        description="Application submitted via JobPilot assistant.",
        event_date=app.applied_date,
        stage=app.status
    )
    db.add(initial_event)
    db.commit()
    db.refresh(app)
    return app


@router.get("/{app_id}", response_model=ApplicationResponse)
def get_application(
    app_id: str,
    current_user: User = Depends(get_current_user),
    db: Session = Depends(get_db)
):
    """Retrieve full application details including milestone timeline events."""
    app = db.query(Application).filter(
        Application.id == app_id,
        Application.user_id == current_user.id
    ).first()
    if not app:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Application not found.")
    return app


@router.put("/{app_id}", response_model=ApplicationResponse)
def update_application(
    app_id: str,
    app_in: ApplicationUpdate,
    current_user: User = Depends(get_current_user),
    db: Session = Depends(get_db)
):
    """Update application details, stage status, recruiter notes, or interview dates."""
    app = db.query(Application).filter(
        Application.id == app_id,
        Application.user_id == current_user.id
    ).first()
    if not app:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Application not found.")

    old_status = app.status
    for field, val in app_in.model_dump(exclude_unset=True).items():
        setattr(app, field, val)

    # If status changed, automatically log a timeline event
    if app_in.status and app_in.status != old_status:
        now_str = datetime.now(timezone.utc).strftime("%b %d, %Y")
        event = ApplicationEvent(
            application_id=app.id,
            title=f"Stage Updated: {app.status}",
            description=f"Status transitioned from {old_status} to {app.status}.",
            event_date=now_str,
            stage=app.status
        )
        db.add(event)

    db.commit()
    db.refresh(app)
    return app


@router.delete("/{app_id}", status_code=status.HTTP_204_NO_CONTENT)
def delete_application(
    app_id: str,
    current_user: User = Depends(get_current_user),
    db: Session = Depends(get_db)
):
    """Delete a tracked application."""
    app = db.query(Application).filter(
        Application.id == app_id,
        Application.user_id == current_user.id
    ).first()
    if not app:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Application not found.")
    db.delete(app)
    db.commit()
    return None


@router.post("/{app_id}/events", response_model=ApplicationEventResponse, status_code=status.HTTP_201_CREATED)
def add_application_event(
    app_id: str,
    event_in: ApplicationEventCreate,
    current_user: User = Depends(get_current_user),
    db: Session = Depends(get_db)
):
    """Log a milestone event (e.g. screening call, technical round, offer discussion)."""
    app = db.query(Application).filter(
        Application.id == app_id,
        Application.user_id == current_user.id
    ).first()
    if not app:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Application not found.")

    event = ApplicationEvent(
        application_id=app.id,
        **event_in.model_dump()
    )
    db.add(event)
    db.commit()
    db.refresh(event)
    return event
