from fastapi import APIRouter, Depends, HTTPException, status
from fastapi.security import OAuth2PasswordRequestForm
from sqlalchemy.orm import Session
from app.core.dependencies import get_db, get_current_user
from app.models.user import User
from app.schemas.auth import (
    UserRegister,
    UserLogin,
    TokenResponse,
    TokenRefresh,
    UserResponse,
    ForgotPasswordRequest,
    MessageResponse
)
from app.services.auth_service import AuthService

router = APIRouter(prefix="/auth", tags=["Authentication"])


@router.post("/register", response_model=UserResponse, status_code=status.HTTP_201_CREATED)
def register(user_in: UserRegister, db: Session = Depends(get_db)):
    """Register a new user and create their base career profile."""
    return AuthService.register_user(db, user_in)


@router.post("/login", response_model=TokenResponse)
def login(login_data: UserLogin, db: Session = Depends(get_db)):
    """Authenticate user with email and password, returning JWT access and refresh tokens."""
    user = AuthService.authenticate_user(db, login_data.email, login_data.password)
    if not user:
        raise HTTPException(
            status_code=status.HTTP_401_UNAUTHORIZED,
            detail="Incorrect email or password.",
            headers={"WWW-Authenticate": "Bearer"},
        )
    return AuthService.create_tokens_for_user(user)


@router.post("/token", response_model=TokenResponse)
def login_for_access_token(
    form_data: OAuth2PasswordRequestForm = Depends(),
    db: Session = Depends(get_db)
):
    """
    OAuth2 standard form login for interactive Swagger UI docs authorization.
    Accepts form-data with username (email) and password.
    """
    user = AuthService.authenticate_user(db, form_data.username, form_data.password)
    if not user:
        raise HTTPException(
            status_code=status.HTTP_401_UNAUTHORIZED,
            detail="Incorrect email or password.",
            headers={"WWW-Authenticate": "Bearer"},
        )
    return AuthService.create_tokens_for_user(user)


@router.get("/me", response_model=UserResponse)
def get_me(current_user: User = Depends(get_current_user)):
    """Retrieve details for the currently authenticated user."""
    return current_user


@router.post("/refresh", response_model=TokenResponse)
def refresh_token(refresh_in: TokenRefresh, db: Session = Depends(get_db)):
    """Generate a new access token using a valid refresh token."""
    new_token, expires_in = AuthService.refresh_access_token(db, refresh_in.refresh_token)
    return TokenResponse(
        access_token=new_token,
        refresh_token=refresh_in.refresh_token,
        token_type="bearer",
        expires_in=expires_in
    )


@router.post("/forgot-password", response_model=MessageResponse)
def forgot_password(req: ForgotPasswordRequest, db: Session = Depends(get_db)):
    """Initiate password recovery. Generates a secure reset procedure simulation."""
    # We do not leak whether user exists
    return MessageResponse(
        status="success",
        message="If this email is registered, password reset instructions have been dispatched."
    )
