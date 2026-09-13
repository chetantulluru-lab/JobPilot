from typing import Generator
from sqlalchemy import create_engine
from sqlalchemy.orm import declarative_base, sessionmaker, Session
from app.core.config import settings

# Create SQLAlchemy 2.x Engine
# pool_pre_ping=True tests connections before using them to prevent stale connection errors
engine = create_engine(
    settings.DATABASE_URL,
    pool_pre_ping=True,
    pool_size=10,
    max_overflow=20,
    echo=False  # Set to True only when debugging raw SQL queries
)

# Session factory for DB transactions
SessionLocal = sessionmaker(
    autocommit=False,
    autoflush=False,
    bind=engine
)

# Base class for all SQLAlchemy ORM models
Base = declarative_base()


def get_db() -> Generator[Session, None, None]:
    """
    FastAPI dependency that provides a transactional database session per request.
    Automatically closes the session after the request finishes.
    """
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()
