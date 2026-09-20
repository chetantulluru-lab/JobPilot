from fastapi import APIRouter
from app.api.v1.auth import router as auth_router
from app.api.v1.profile import router as profile_router
from app.api.v1.resumes import router as resumes_router
from app.api.v1.roadmaps import router as roadmaps_router
from app.api.v1.ai_assistant import router as ai_assistant_router
from app.api.v1.resume_builder import router as resume_builder_router
from app.api.v1.career_tools import router as career_tools_router
from app.api.v1.health import router as health_router

# Legacy routers preserved for backward compatibility
from app.api.v1.jobs import router as jobs_router
from app.api.v1.applications import router as applications_router
from app.api.v1.notifications import router as notifications_router
from app.api.v1.integrations import router as integrations_router
from app.api.v1.interviews import router as interviews_router
from app.api.v1.outreach import router as outreach_router

api_router = APIRouter()

api_router.include_router(health_router)
api_router.include_router(auth_router)
api_router.include_router(profile_router)
api_router.include_router(roadmaps_router)
api_router.include_router(resumes_router)
api_router.include_router(resume_builder_router)
api_router.include_router(career_tools_router)
api_router.include_router(ai_assistant_router)
api_router.include_router(interviews_router)
api_router.include_router(outreach_router)

# Preserved legacy endpoints
api_router.include_router(jobs_router)
api_router.include_router(applications_router)
api_router.include_router(notifications_router)
api_router.include_router(integrations_router)
