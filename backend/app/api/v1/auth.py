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

from app.schemas.otp import (
    RegisterStartRequest, RegisterStartResponse, RegisterVerifyRequest,
    ForgotPasswordStartRequest, ForgotPasswordStartResponse,
    ForgotPasswordVerifyRequest, ForgotPasswordVerifyResponse
)
from app.services.otp_service import OtpService

router = APIRouter(prefix="/auth", tags=["Authentication"])


@router.post("/register/start", response_model=RegisterStartResponse, status_code=status.HTTP_200_OK)
def register_start(req: RegisterStartRequest, db: Session = Depends(get_db)):
    """
    Step 1 of Registration: Initiates email OTP verification.
    Generates a secure 6-digit OTP, stores hashed record with 10-min expiration,
    and dispatches verification email.
    """
    return OtpService.start_registration(db, req)


@router.post("/register/verify", response_model=TokenResponse, status_code=status.HTTP_200_OK)
def register_verify(req: RegisterVerifyRequest, db: Session = Depends(get_db)):
    """
    Step 2 of Registration: Verifies 6-digit OTP, activates account,
    initializes a clean empty Career Profile, and returns JWT auth tokens.
    """
    return OtpService.verify_and_register(db, req)


@router.post("/forgot-password/start", response_model=ForgotPasswordStartResponse, status_code=status.HTTP_200_OK)
def forgot_password_start(req: ForgotPasswordStartRequest, db: Session = Depends(get_db)):
    """
    Step 1 of Password Reset: Validates email, creates 6-digit hashed OTP,
    and dispatches code via Resend.
    """
    return OtpService.start_password_reset(db, req)


@router.post("/forgot-password/verify", response_model=ForgotPasswordVerifyResponse, status_code=status.HTTP_200_OK)
def forgot_password_verify(req: ForgotPasswordVerifyRequest, db: Session = Depends(get_db)):
    """
    Step 2 of Password Reset: Verifies 6-digit OTP and safely resets password.
    """
    return OtpService.verify_and_reset_password(db, req)


@router.post("/register", response_model=UserResponse, status_code=status.HTTP_201_CREATED)
def register(user_in: UserRegister, db: Session = Depends(get_db)):
    """Direct registration endpoint (backward-compatible)."""
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
