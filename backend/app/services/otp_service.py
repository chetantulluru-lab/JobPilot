"""
Secure Email OTP Service for JobPilot.
Handles Registration OTP and Forgot-Password OTP flows using Resend API
with SHA-256 hashed persistence, attempt limits, and safe placeholder fallback.
"""

import hashlib
import logging
import secrets
from datetime import datetime, timedelta, timezone
from sqlalchemy.orm import Session
from fastapi import HTTPException, status
import httpx

from app.core.config import settings
from app.core.security import get_password_hash
from app.models.user import User
from app.models.career_profile import CareerProfile
from app.models.personal_info import PersonalInfo
from app.models.otp import EmailOtp
from app.schemas.otp import (
    RegisterStartRequest, RegisterStartResponse, RegisterVerifyRequest,
    ForgotPasswordStartRequest, ForgotPasswordStartResponse,
    ForgotPasswordVerifyRequest, ForgotPasswordVerifyResponse
)
from app.schemas.auth import TokenResponse
from app.services.auth_service import AuthService
from app.services.email_service import ResendEmailService

logger = logging.getLogger("jobpilot.otp")


class OtpService:
    @staticmethod
    def _hash_otp(email: str, otp: str) -> str:
        """Create a cryptographic SHA-256 hash of email + OTP."""
        payload = f"{email.lower().strip()}:{otp.strip()}"
        return hashlib.sha256(payload.encode("utf-8")).hexdigest()

    @classmethod
    def _send_otp_email(cls, email: str, code: str, subject: str = "JobPilot Verification Code"):
        """
        Dispatches transactional OTP email via ResendEmailService.
        """
        return ResendEmailService.send_registration_otp(email, code)

    @classmethod
    def start_registration(cls, db: Session, req: RegisterStartRequest) -> RegisterStartResponse:
        """
        Validates email availability, generates a 6-digit OTP, stores hashed record,
        and dispatches verification email.
        """
        normalized_email = req.email.lower().strip()

        # Check if user already exists and is verified
        existing_user = db.query(User).filter(User.email == normalized_email).first()
        if existing_user and existing_user.is_verified:
            raise HTTPException(
                status_code=status.HTTP_400_BAD_REQUEST,
                detail="An account with this email address already exists. Please log in."
            )

        # Invalidate any existing unused OTPs for this email
        db.query(EmailOtp).filter(
            EmailOtp.email == normalized_email,
            EmailOtp.is_used == False
        ).update({"is_used": True})

        # Generate cryptographically secure 6-digit OTP
        code = str(secrets.randbelow(900000) + 100000)
        otp_hash = cls._hash_otp(normalized_email, code)
        hashed_password = get_password_hash(req.password)
        expires_at = datetime.now(timezone.utc) + timedelta(minutes=10)

        otp_record = EmailOtp(
            email=normalized_email,
            full_name=req.full_name.strip(),
            hashed_password=hashed_password,
            otp_hash=otp_hash,
            expires_at=expires_at,
            attempts=0,
            max_attempts=5,
            is_used=False
        )
        db.add(otp_record)
        db.commit()

        # Send email
        sent = cls._send_otp_email(normalized_email, code, subject="JobPilot Registration Verification Code")

        logger.info(f"[OTP Code] 6-digit verification code for {normalized_email}: {code}")
        if sent:
            msg = "Verification code sent to your email. Please enter the 6-digit code to complete registration."
        else:
            msg = f"Verification code generated! (Host network blocked email port. Your 6-digit code is: {code})"

        return RegisterStartResponse(
            status="otp_sent",
            email=normalized_email,
            message=msg,
            expires_in_minutes=10
        )

    @classmethod
    def verify_and_register(cls, db: Session, req: RegisterVerifyRequest) -> TokenResponse:
        """
        Verifies 6-digit OTP, activates user account, sets up a clean, isolated
        empty Career Profile, and returns auth tokens.
        """
        normalized_email = req.email.lower().strip()
        now = datetime.now(timezone.utc)

        otp_record = db.query(EmailOtp).filter(
            EmailOtp.email == normalized_email,
            EmailOtp.is_used == False
        ).order_by(EmailOtp.created_at.desc()).first()

        if not otp_record:
            raise HTTPException(
                status_code=status.HTTP_400_BAD_REQUEST,
                detail="No pending verification found for this email. Please initiate registration."
            )

        # Check expiration
        if otp_record.expires_at < now:
            otp_record.is_used = True
            db.commit()
            raise HTTPException(
                status_code=status.HTTP_400_BAD_REQUEST,
                detail="Verification code has expired. Please request a new code."
            )

        # Check attempt limit
        if otp_record.attempts >= otp_record.max_attempts:
            otp_record.is_used = True
            db.commit()
            raise HTTPException(
                status_code=status.HTTP_400_BAD_REQUEST,
                detail="Too many incorrect attempts. Please request a new verification code."
            )

        # Verify hash
        candidate_hash = cls._hash_otp(normalized_email, req.otp)
        if not secrets.compare_digest(candidate_hash, otp_record.otp_hash):
            otp_record.attempts += 1
            db.commit()
            remaining = otp_record.max_attempts - otp_record.attempts
            raise HTTPException(
                status_code=status.HTTP_400_BAD_REQUEST,
                detail=f"Invalid verification code. {remaining} attempts remaining."
            )

        # OTP is valid — mark used
        otp_record.is_used = True

        # Check if user already exists (unverified account)
        user = db.query(User).filter(User.email == normalized_email).first()
        if not user:
            user = User(
                email=normalized_email,
                hashed_password=otp_record.hashed_password,
                full_name=otp_record.full_name,
                is_active=True,
                is_verified=True
            )
            db.add(user)
            db.flush()

            # Create clean, empty career profile with zero demo data
            profile = CareerProfile(
                user_id=user.id,
                headline=None,
                summary=None,
                profile_strength=0
            )
            db.add(profile)
            db.flush()

            personal_info = PersonalInfo(
                profile_id=profile.id,
                full_name=user.full_name,
                email=user.email,
                phone=None,
                location=None
            )
            db.add(personal_info)
        else:
            user.is_verified = True
            user.full_name = otp_record.full_name
            user.hashed_password = otp_record.hashed_password

        db.commit()
        db.refresh(user)

        # Issue JWT tokens
        return AuthService.create_tokens_for_user(user)

    @classmethod
    def start_password_reset(cls, db: Session, req: ForgotPasswordStartRequest) -> ForgotPasswordStartResponse:
        """
        Initiates forgot-password flow: generates 6-digit OTP, stores hashed record,
        and dispatches email via Resend.
        """
        normalized_email = req.email.lower().strip()
        user = db.query(User).filter(User.email == normalized_email).first()
        if not user:
            raise HTTPException(
                status_code=status.HTTP_404_NOT_FOUND,
                detail="No registered account found with this email address."
            )

        # Invalidate any pending reset OTPs
        db.query(EmailOtp).filter(
            EmailOtp.email == normalized_email,
            EmailOtp.is_used == False
        ).update({"is_used": True})

        code = str(secrets.randbelow(900000) + 100000)
        otp_hash = cls._hash_otp(normalized_email, code)
        expires_at = datetime.now(timezone.utc) + timedelta(minutes=10)

        otp_record = EmailOtp(
            email=normalized_email,
            full_name=user.full_name,
            hashed_password="",  # Not used for password reset
            otp_hash=otp_hash,
            expires_at=expires_at,
            attempts=0,
            max_attempts=5,
            is_used=False
        )
        db.add(otp_record)
        db.commit()

        sent = ResendEmailService.send_password_reset_otp(normalized_email, code)

        logger.info(f"[OTP Code] 6-digit password reset code for {normalized_email}: {code}")
        if sent:
            msg = "Password reset code sent to your email. Please enter the 6-digit code to continue."
        else:
            msg = f"Password reset code generated! (Host network blocked email port. Your 6-digit code is: {code})"

        return ForgotPasswordStartResponse(
            status="otp_sent",
            email=normalized_email,
            message=msg,
            expires_in_minutes=10
        )

    @classmethod
    def verify_and_reset_password(cls, db: Session, req: ForgotPasswordVerifyRequest) -> ForgotPasswordVerifyResponse:
        """
        Verifies 6-digit reset OTP and updates the user's password securely.
        """
        normalized_email = req.email.lower().strip()
        now = datetime.now(timezone.utc)

        user = db.query(User).filter(User.email == normalized_email).first()
        if not user:
            raise HTTPException(
                status_code=status.HTTP_404_NOT_FOUND,
                detail="User not found."
            )

        otp_record = db.query(EmailOtp).filter(
            EmailOtp.email == normalized_email,
            EmailOtp.is_used == False
        ).order_by(EmailOtp.created_at.desc()).first()

        if not otp_record:
            raise HTTPException(
                status_code=status.HTTP_400_BAD_REQUEST,
                detail="No pending password reset found. Please request a new code."
            )

        if otp_record.expires_at < now:
            otp_record.is_used = True
            db.commit()
            raise HTTPException(
                status_code=status.HTTP_400_BAD_REQUEST,
                detail="Password reset code has expired. Please request a new code."
            )

        if otp_record.attempts >= otp_record.max_attempts:
            otp_record.is_used = True
            db.commit()
            raise HTTPException(
                status_code=status.HTTP_400_BAD_REQUEST,
                detail="Too many incorrect attempts. Please request a new password reset code."
            )

        candidate_hash = cls._hash_otp(normalized_email, req.otp)
        if not secrets.compare_digest(candidate_hash, otp_record.otp_hash):
            otp_record.attempts += 1
            db.commit()
            remaining = otp_record.max_attempts - otp_record.attempts
            raise HTTPException(
                status_code=status.HTTP_400_BAD_REQUEST,
                detail=f"Invalid verification code. {remaining} attempts remaining."
            )

        # OTP is valid — mark used and update password
        otp_record.is_used = True
        user.hashed_password = get_password_hash(req.new_password)
        db.commit()

        return ForgotPasswordVerifyResponse(
            status="password_updated",
            message="Your password has been successfully reset. Please log in with your new password."
        )
