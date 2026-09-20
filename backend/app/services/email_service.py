"""
Resend Email Service Abstraction for JobPilot.
Handles transactional email delivery (Registration OTP, Forgot-Password OTP)
via the Resend API with bounded retries, honest error reporting, and safe placeholder mode.
"""

import logging
import httpx
from fastapi import HTTPException, status
from app.core.config import settings

logger = logging.getLogger("jobpilot.email")


class ResendEmailService:
    """
    Clean transactional email service powered by Resend.
    Configurable via RESEND_API_KEY and RESEND_FROM_EMAIL.
    """

    @classmethod
    def send_transactional_email(cls, to: str, subject: str, html_body: str) -> bool:
        """
        Sends an HTML email via Resend API with 1 bounded retry.
        If Resend is not configured (placeholder mode in local dev/tests),
        safely logs and returns True. If configured but fails, raises honest HTTPException.
        """
        api_key = (settings.RESEND_API_KEY or "").strip()
        from_email = (settings.RESEND_FROM_EMAIL or "onboarding@resend.dev").strip()
        recipient = to.strip().lower()

        # 1. Check for Free SMTP (e.g. Gmail / Brevo / College Mail)
        smtp_user = (settings.SMTP_USER or "").strip()
        smtp_pass = (settings.SMTP_PASSWORD or "").strip()
        if smtp_user and smtp_pass:
            try:
                import smtplib
                from email.mime.multipart import MIMEMultipart
                from email.mime.text import MIMEText

                msg = MIMEMultipart("alternative")
                msg["Subject"] = subject
                msg["From"] = smtp_user
                msg["To"] = recipient
                msg.attach(MIMEText(html_body, "html"))

                with smtplib.SMTP(settings.SMTP_HOST, settings.SMTP_PORT, timeout=10.0) as server:
                    server.starttls()
                    server.login(smtp_user, smtp_pass)
                    server.sendmail(smtp_user, [recipient], msg.as_string())
                logger.info(f"Successfully dispatched OTP email to {recipient} via SMTP ({settings.SMTP_HOST})")
                return True
            except Exception as e:
                logger.warning(f"SMTP dispatch failed: {e}")
                raise HTTPException(
                    status_code=status.HTTP_502_BAD_GATEWAY,
                    detail=f"Unable to send verification email via SMTP: {str(e)}"
                )

        # 2. Check for Resend API Key
        api_key = (settings.RESEND_API_KEY or "").strip()
        from_email = (settings.RESEND_FROM_EMAIL or "onboarding@resend.dev").strip()

        if not api_key:
            logger.info(f"[Email Placeholder] Email to {recipient} simulated (Neither SMTP nor RESEND_API_KEY configured)")
            return True

        headers = {
            "Authorization": f"Bearer {api_key}",
            "Content-Type": "application/json"
        }
        payload = {
            "from": from_email,
            "to": [recipient],
            "subject": subject,
            "html": html_body
        }

        max_attempts = 2
        for attempt in range(1, max_attempts + 1):
            try:
                with httpx.Client(timeout=10.0) as client:
                    resp = client.post("https://api.resend.com/emails", json=payload, headers=headers)
                    if resp.status_code in (200, 201):
                        logger.info(f"Successfully dispatched email to {recipient} via Resend")
                        return True
                    else:
                        logger.warning(f"Resend API error (attempt {attempt}): {resp.status_code} - {resp.text}")
                        if attempt == max_attempts:
                            raise HTTPException(
                                status_code=status.HTTP_502_BAD_GATEWAY,
                                detail="Unable to send verification email. Please try again."
                            )
            except HTTPException:
                raise
            except Exception as e:
                logger.warning(f"Resend connection exception (attempt {attempt}): {e}")
                if attempt == max_attempts:
                    raise HTTPException(
                        status_code=status.HTTP_502_BAD_GATEWAY,
                        detail="Unable to send verification email. Please try again."
                    )
        return False

    @classmethod
    def send_registration_otp(cls, email: str, code: str) -> bool:
        """Dispatches the 6-digit registration verification email."""
        subject = "JobPilot — Email Verification Code"
        html_body = f"""
        <div style="font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif; max-width: 520px; margin: 0 auto; padding: 28px; background: #FAFAFA; border: 1px solid #E2E8F0; border-radius: 12px;">
            <div style="display: flex; align-items: center; margin-bottom: 20px;">
                <h2 style="color: #FF5500; font-size: 24px; margin: 0; font-weight: 800;">JobPilot</h2>
            </div>
            <h3 style="color: #0F172A; font-size: 18px; margin-top: 0;">Verify Your Email Address</h3>
            <p style="color: #475569; font-size: 14px; line-height: 22px;">
                Welcome to JobPilot! Please use the following 6-digit verification code to activate your account and start your AI-guided career roadmap:
            </p>
            <div style="background: #FFFFFF; border: 2px dashed #CBD5E1; border-radius: 8px; padding: 18px; text-align: center; margin: 24px 0;">
                <span style="font-size: 34px; font-weight: 800; letter-spacing: 6px; color: #0F172A;">{code}</span>
            </div>
            <p style="color: #64748B; font-size: 13px; line-height: 20px;">
                <strong>Expiration:</strong> This verification code will expire in <strong>10 minutes</strong>.
            </p>
            <div style="margin-top: 24px; padding-top: 16px; border-top: 1px solid #E2E8F0;">
                <p style="color: #94A3B8; font-size: 12px; line-height: 18px; margin: 0;">
                    <strong>Security Notice:</strong> Never share this code with anyone. JobPilot staff will never ask for your password or verification code. If you did not request this, you can safely ignore this email.
                </p>
            </div>
        </div>
        """
        return cls.send_transactional_email(to=email, subject=subject, html_body=html_body)

    @classmethod
    def send_password_reset_otp(cls, email: str, code: str) -> bool:
        """Dispatches the 6-digit password reset verification email."""
        subject = "JobPilot — Password Reset Verification Code"
        html_body = f"""
        <div style="font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif; max-width: 520px; margin: 0 auto; padding: 28px; background: #FAFAFA; border: 1px solid #E2E8F0; border-radius: 12px;">
            <div style="display: flex; align-items: center; margin-bottom: 20px;">
                <h2 style="color: #FF5500; font-size: 24px; margin: 0; font-weight: 800;">JobPilot</h2>
            </div>
            <h3 style="color: #0F172A; font-size: 18px; margin-top: 0;">Reset Your Password</h3>
            <p style="color: #475569; font-size: 14px; line-height: 22px;">
                We received a request to reset the password for your JobPilot account. Use the 6-digit code below to set your new password:
            </p>
            <div style="background: #FFFFFF; border: 2px dashed #CBD5E1; border-radius: 8px; padding: 18px; text-align: center; margin: 24px 0;">
                <span style="font-size: 34px; font-weight: 800; letter-spacing: 6px; color: #0F172A;">{code}</span>
            </div>
            <p style="color: #64748B; font-size: 13px; line-height: 20px;">
                <strong>Expiration:</strong> This code will expire in <strong>10 minutes</strong>.
            </p>
            <div style="margin-top: 24px; padding-top: 16px; border-top: 1px solid #E2E8F0;">
                <p style="color: #94A3B8; font-size: 12px; line-height: 18px; margin: 0;">
                    <strong>Security Notice:</strong> If you did not request a password reset, please secure your email account immediately. JobPilot will never ask for your credentials.
                </p>
            </div>
        </div>
        """
        return cls.send_transactional_email(to=email, subject=subject, html_body=html_body)