from datetime import datetime, timezone
from typing import Optional, Tuple
from fastapi import HTTPException, status
from sqlalchemy.orm import Session
from app.core.config import settings
from app.core.security import (
    get_password_hash,
    verify_password,
    create_access_token,
    create_refresh_token,
    decode_token
)
from app.models.user import User
from app.models.career_profile import CareerProfile
from app.models.personal_info import PersonalInfo
from app.schemas.auth import UserRegister, TokenResponse


class AuthService:
    @staticmethod
    def register_user(db: Session, user_in: UserRegister) -> User:
        """
        Registers a new user, hashes their password, and creates their initial CareerProfile.
        """
        # Check if email is already registered
        existing_user = db.query(User).filter(User.email == user_in.email.lower()).first()
        if existing_user:
            raise HTTPException(
                status_code=status.HTTP_409_CONFLICT,
                detail="A user with this email address already exists."
            )

        # Hash password and create User record
        user = User(
            email=user_in.email.lower(),
            hashed_password=get_password_hash(user_in.password),
            full_name=user_in.full_name,
            is_active=True,
            is_verified=False
        )
        db.add(user)
        db.flush()

        # Create associated empty CareerProfile
        profile = CareerProfile(
            user_id=user.id,
            headline=f"Aspiring Professional | {user_in.full_name}",
            summary="Career profile managed by JobPilot AI.",
            profile_strength=40
        )
        db.add(profile)
        db.flush()

        # Create initial PersonalInfo record
        personal_info = PersonalInfo(
            profile_id=profile.id,
            full_name=user_in.full_name,
            email=user_in.email.lower()
        )
        db.add(personal_info)

        db.commit()
        db.refresh(user)
        return user

    @staticmethod
    def authenticate_user(db: Session, email: str, password: str) -> Optional[User]:
        """Validates credentials and returns the User if valid."""
        user = db.query(User).filter(User.email == email.lower()).first()
        if not user:
            return None
        if not verify_password(password, user.hashed_password):
            return None
        return user

    @staticmethod
    def create_tokens_for_user(user: User) -> TokenResponse:
        """Generates access and refresh tokens for the authenticated user."""
        access_token = create_access_token(subject=user.id)
        refresh_token = create_refresh_token(subject=user.id)
        return TokenResponse(
            access_token=access_token,
            refresh_token=refresh_token,
            token_type="bearer",
            expires_in=settings.ACCESS_TOKEN_EXPIRE_MINUTES * 60
        )

    @staticmethod
    def refresh_access_token(db: Session, refresh_token: str) -> Tuple[str, int]:
        """Validates a refresh token and generates a new access token."""
        payload = decode_token(refresh_token)
        if not payload or payload.get("type") != "refresh":
            raise HTTPException(
                status_code=status.HTTP_401_UNAUTHORIZED,
                detail="Invalid or expired refresh token."
            )
        user_id = payload.get("sub")
        user = db.query(User).filter(User.id == user_id, User.is_active == True).first()
        if not user:
            raise HTTPException(
                status_code=status.HTTP_401_UNAUTHORIZED,
                detail="User not found or inactive."
            )
        new_access_token = create_access_token(subject=user.id)
        return new_access_token, settings.ACCESS_TOKEN_EXPIRE_MINUTES * 60
