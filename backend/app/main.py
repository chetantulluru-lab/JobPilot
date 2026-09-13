from fastapi import FastAPI, Request, status
from fastapi.middleware.cors import CORSMiddleware
from fastapi.responses import JSONResponse
from app.core.config import settings
from app.api.v1 import api_router
from app.api.v1.health import router as health_router

# Initialize FastAPI application
app = FastAPI(
    title=settings.PROJECT_NAME,
    version=settings.PROJECT_VERSION,
    description="Official REST API for JobPilot - Your career. Piloted by AI.",
    docs_url="/docs",
    redoc_url="/redoc",
    openapi_url=f"{settings.API_V1_STR}/openapi.json"
)

# Configure Cross-Origin Resource Sharing (CORS)
app.add_middleware(
    CORSMiddleware,
    allow_origins=settings.BACKEND_CORS_ORIGINS,
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# Include top-level health check (available at /health directly)
app.include_router(health_router)

# Include versioned API routers (/api/v1/...)
app.include_router(api_router, prefix=settings.API_V1_STR)


@app.get("/", tags=["Root"])
def root():
    """Root endpoint welcoming visitors and directing to interactive Swagger documentation."""
    return {
        "app": settings.PROJECT_NAME,
        "tagline": "Your career. Piloted by AI.",
        "version": settings.PROJECT_VERSION,
        "environment": settings.ENVIRONMENT,
        "documentation": "/docs",
        "health": "/health"
    }


@app.exception_handler(Exception)
async def global_exception_handler(request: Request, exc: Exception):
    """
    Global catch-all exception handler.
    Guarantees consistent error formatting and prevents leaking internal stack traces or secrets to clients.
    """
    if settings.DEBUG:
        error_detail = str(exc)
    else:
        error_detail = "An internal server error occurred. Please try again later."

    return JSONResponse(
        status_code=status.HTTP_500_INTERNAL_SERVER_ERROR,
        content={
            "status": "error",
            "message": error_detail,
            "path": request.url.path
        }
    )
