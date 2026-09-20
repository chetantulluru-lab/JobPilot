from fastapi import APIRouter, Depends
from sqlalchemy.orm import Session

from app.core.dependencies import get_db, get_current_user
from app.models.user import User
from app.schemas.outreach import OutreachGenerateRequest, OutreachResponse
from app.services.outreach_service import OutreachService

router = APIRouter(prefix="/outreach", tags=["Cold Outreach & Cover Letter Generator"])


@router.post("/generate", response_model=OutreachResponse)
def generate_recruiter_outreach(
    req: OutreachGenerateRequest,
    current_user: User = Depends(get_current_user),
    db: Session = Depends(get_db)
):
    """
    Generates customized recruiter outreach:
    1. LinkedIn Connection Note (<300 chars, concise, professional)
    2. Recruiter Cold Email (Subject + tailored value proposition)
    3. Formal Cover Letter highlighting candidate's verified projects and skills
    """
    return OutreachService.generate_outreach(
        db=db,
        user=current_user,
        job_id=req.job_id,
        role_title=req.role_title,
        company=req.company,
        job_description=req.job_description
    )
