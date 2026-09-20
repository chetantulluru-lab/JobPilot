from typing import List, Optional
from pydantic import Field
from pydantic_settings import BaseSettings, SettingsConfigDict


class Settings(BaseSettings):
    """
    Central Configuration for JobPilot Backend.
    Uses Pydantic Settings to load and validate variables from .env file or OS environment.
    """
    model_config = SettingsConfigDict(
        env_file=".env",
        env_file_encoding="utf-8",
        case_sensitive=True,
        extra="ignore"
    )

    # 1. Project & Environment
    PROJECT_NAME: str = "JobPilot API"
    PROJECT_VERSION: str = "0.2.0"
    ENVIRONMENT: str = "development"
    DEBUG: bool = True
    API_V1_STR: str = "/api/v1"
    BACKEND_CORS_ORIGINS: List[str] = [
        "http://localhost:5173",
        "http://127.0.0.1:5173",
        "http://10.0.2.2:8000",
        "http://localhost:8000"
    ]

    # 2. Database (Required)
    DATABASE_URL: str = Field(
        ...,
        description="PostgreSQL connection string. Format: postgresql+psycopg://user:pass@host:port/dbname"
    )

    # 3. Security & Authentication (Required)
    JWT_SECRET_KEY: str = Field(
        ...,
        description="Cryptographic secret key used to sign JWT access and refresh tokens."
    )
    JWT_ALGORITHM: str = "HS256"
    ACCESS_TOKEN_EXPIRE_MINUTES: int = 1440  # 24 hours
    REFRESH_TOKEN_EXPIRE_DAYS: int = 7

    # 4. Resume Storage
    RESUME_UPLOAD_DIR: str = "storage/resumes"
    MAX_RESUME_FILE_SIZE_MB: int = 10

    BACKEND_PUBLIC_URL: str = "https://jobpilot-backend-e97f.onrender.com"
    GOOGLE_CLIENT_ID: Optional[str] = None
    GOOGLE_CLIENT_SECRET: Optional[str] = None
    GOOGLE_REDIRECT_URI: str = "http://localhost:8000/api/v1/integrations/google/callback"

    GITHUB_CLIENT_ID: Optional[str] = None
    GITHUB_CLIENT_SECRET: Optional[str] = None
    GITHUB_REDIRECT_URI: str = "http://localhost:8000/api/v1/integrations/github/callback"

    LINKEDIN_CLIENT_ID: Optional[str] = None
    LINKEDIN_CLIENT_SECRET: Optional[str] = None
    LINKEDIN_REDIRECT_URI: str = "http://localhost:8000/api/v1/integrations/linkedin/callback"

    OPENAI_API_KEY: Optional[str] = None

    # OpenRouter Integration (3 Keys for Resilience & Load Distribution)
    OPENROUTER_API_KEY: Optional[str] = None
    OPENROUTER_API_KEY_1: Optional[str] = None
    OPENROUTER_API_KEY_2: Optional[str] = None
    OPENROUTER_API_KEY_3: Optional[str] = None
    OPENROUTER_MODEL: str = "deepseek/deepseek-r1-0528"

    # Resend Email OTP (Registration & Forgot Password)
    RESEND_API_KEY: Optional[str] = None
    RESEND_FROM_EMAIL: str = "onboarding@resend.dev"

    # Brevo (Sendinblue) Free HTTP REST API (300 emails/day to ANY address over HTTPS Port 443)
    BREVO_API_KEY: Optional[str] = None

    # Free SMTP / Gmail Email OTP Alternative (Zero Cost)
    SMTP_HOST: str = "smtp.gmail.com"
    SMTP_PORT: int = 587
    SMTP_USER: Optional[str] = None
    SMTP_PASSWORD: Optional[str] = None

    # Production Deployment Base URL
    API_BASE_URL: str = "https://jobpilot-backend-e97f.onrender.com"

    FIREBASE_PROJECT_ID: Optional[str] = None
    FIREBASE_PRIVATE_KEY: Optional[str] = None
    FIREBASE_CLIENT_EMAIL: Optional[str] = None

    @property
    def is_production(self) -> bool:
        return self.ENVIRONMENT.lower() == "production"

    @property
    def has_google_oauth(self) -> bool:
        return bool(self.GOOGLE_CLIENT_ID and self.GOOGLE_CLIENT_SECRET)

    @property
    def effective_google_redirect_uri(self) -> str:
        if self.is_production:
            return f"{self.BACKEND_PUBLIC_URL}/api/v1/integrations/google/callback"
        return self.GOOGLE_REDIRECT_URI

    @property
    def has_github_oauth(self) -> bool:
        return bool(self.GITHUB_CLIENT_ID and self.GITHUB_CLIENT_SECRET)

    @property
    def effective_github_redirect_uri(self) -> str:
        if self.is_production:
            return f"{self.BACKEND_PUBLIC_URL}/api/v1/integrations/github/callback"
        return self.GITHUB_REDIRECT_URI

    @property
    def has_linkedin_oauth(self) -> bool:
        return bool(self.LINKEDIN_CLIENT_ID and self.LINKEDIN_CLIENT_SECRET)

    @property
    def effective_linkedin_redirect_uri(self) -> str:
        if self.is_production:
            return f"{self.BACKEND_PUBLIC_URL}/api/v1/integrations/linkedin/callback"
        return self.LINKEDIN_REDIRECT_URI

    @property
    def has_openai_key(self) -> bool:
        return bool(self.OPENAI_API_KEY and self.OPENAI_API_KEY.strip())

    @property
    def has_openrouter_key(self) -> bool:
        return bool(self.OPENROUTER_API_KEY and self.OPENROUTER_API_KEY.strip())


settings = Settings()
