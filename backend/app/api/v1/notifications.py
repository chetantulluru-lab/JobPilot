from typing import List
from fastapi import APIRouter, Depends, HTTPException, status
from sqlalchemy.orm import Session
from app.core.dependencies import get_db, get_current_user
from app.models.user import User
from app.models.notification import Notification
from app.schemas.notification import NotificationResponse
from app.schemas.auth import MessageResponse

router = APIRouter(prefix="/notifications", tags=["Notification Center"])


@router.get("", response_model=List[NotificationResponse])
def list_notifications(
    current_user: User = Depends(get_current_user),
    db: Session = Depends(get_db)
):
    """Retrieve all notifications for the current user."""
    return db.query(Notification).filter(
        Notification.user_id == current_user.id
    ).order_by(Notification.created_at.desc()).all()


@router.patch("/{notif_id}/read", response_model=NotificationResponse)
def mark_notification_read(
    notif_id: str,
    current_user: User = Depends(get_current_user),
    db: Session = Depends(get_db)
):
    """Mark a specific notification as read."""
    notif = db.query(Notification).filter(
        Notification.id == notif_id,
        Notification.user_id == current_user.id
    ).first()
    if not notif:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Notification not found.")
    notif.is_read = True
    db.commit()
    db.refresh(notif)
    return notif


@router.post("/mark-all-read", response_model=MessageResponse)
def mark_all_read(
    current_user: User = Depends(get_current_user),
    db: Session = Depends(get_db)
):
    """Mark all notifications as read for current user."""
    db.query(Notification).filter(
        Notification.user_id == current_user.id,
        Notification.is_read == False
    ).update({"is_read": True})
    db.commit()
    return MessageResponse(status="success", message="All notifications marked as read.")
