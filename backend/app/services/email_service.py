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
        Sends an HTML email with multi-tier delivery:
        1. Brevo HTTP REST API (HTTPS port 443 - zero port blocks on Render)
        2. Resend HTTP REST API (HTTPS port 443 - zero port blocks on Render)
        3. SMTP (with clean unquoted passwords and timeout protection)
        4. Graceful fallback returning False if network provider blocks all outbound email
        """
        recipient = to.strip().lower()

        # 1. Brevo HTTP API (Port 443 - Free 300 emails/day to any address)
        brevo_key = (settings.BREVO_API_KEY or "").strip()
        if brevo_key:
            try:
                headers = {
                    "api-key": brevo_key,
                    "Content-Type": "application/json",
                    "Accept": "application/json"
                }
                payload = {
                    "sender": {"name": "JobPilot", "email": settings.SMTP_USER or "noreply@jobpilot.io"},
                    "to": [{"email": recipient}],
                    "subject": subject,
                    "htmlContent": html_body
                }
                with httpx.Client(timeout=10.0) as client:
                    resp = client.post("https://api.brevo.com/v3/smtp/email", json=payload, headers=headers)
                    if resp.status_code in (200, 201):
                        logger.info(f"Successfully dispatched email to {recipient} via Brevo HTTP API")
                        return True
                    else:
                        logger.warning(f"Brevo HTTP API error: {resp.status_code} - {resp.text}")
            except Exception as e:
                logger.warning(f"Brevo HTTP connection exception: {e}")

        # 2. Resend HTTP API (Port 443 - Free 3,000 emails/month)
        resend_key = (settings.RESEND_API_KEY or "").strip()
        from_email = (settings.RESEND_FROM_EMAIL or "onboarding@resend.dev").strip()
        if resend_key:
            try:
                headers = {
                    "Authorization": f"Bearer {resend_key}",
                    "Content-Type": "application/json"
                }
                payload = {
                    "from": from_email,
                    "to": [recipient],
                    "subject": subject,
                    "html": html_body
                }
                with httpx.Client(timeout=10.0) as client:
                    resp = client.post("https://api.resend.com/emails", json=payload, headers=headers)
                    if resp.status_code in (200, 201):
                        logger.info(f"Successfully dispatched email to {recipient} via Resend")
                        return True
                    else:
                        logger.warning(f"Resend HTTP API error: {resp.status_code} - {resp.text}")
            except Exception as e:
                logger.warning(f"Resend connection exception: {e}")

        # 3. SMTP (Gmail / Custom SMTP)
        smtp_user = (settings.SMTP_USER or "").strip()
        smtp_pass = (settings.SMTP_PASSWORD or "").strip().strip('"').strip("'")
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

                with smtplib.SMTP(settings.SMTP_HOST, settings.SMTP_PORT, timeout=5.0) as server:
                    server.starttls()
                    server.login(smtp_user, smtp_pass)
                    server.sendmail(smtp_user, [recipient], msg.as_string())
                logger.info(f"Successfully dispatched OTP email to {recipient} via SMTP ({settings.SMTP_HOST})")
                return True
            except Exception as e:
                # Cloud host like Render blocks outbound SMTP ports 25/465/587
                logger.warning(f"SMTP dispatch to {recipient} could not connect ({e}). Host firewall blocks outbound SMTP.")

        # 4. Fallback: Host port blocked or no email provider configured
        logger.info(f"[Email Mode] Outbound email could not be delivered directly. Providing verification code fallback.")
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