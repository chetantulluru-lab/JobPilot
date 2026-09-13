from fastapi import APIRouter, Depends, status, Response
from sqlalchemy.orm import Session
from sqlalchemy import text
from app.core.dependencies import get_db
from app.core.config import settings

router = APIRouter(tags=["Health & Status"])


@router.get("/health")
def health_check(response: Response, db: Session = Depends(get_db)):
    """
    Health check endpoint.
    Verifies service liveness and tests active PostgreSQL database connectivity.
    """
    db_status = "connected"
    try:
        db.execute(text("SELECT 1"))
    except Exception:
        db_status = "disconnected"
        response.status_code = status.HTTP_503_SERVICE_UNAVAILABLE

    return {
        "status": "ok" if db_status == "connected" else "degraded",
        "database": db_status,
        "environment": settings.ENVIRONMENT,
        "version": settings.PROJECT_VERSION
    }
