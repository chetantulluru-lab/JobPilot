"""
Tests for JobPilot Critical Real-Device Bug Fix + Real Data Integration Sprint.
Validates:
- Job Provider dynamic search (Java, Python, FastAPI, etc.)
- Zero-fabrication empty profile match gating (Match Unavailable)
- Registration Email OTP flow (start, hash, verify, clean empty profile)
- OAuth state signing and Android deep link redirection
"""

import pytest
from app.models.career_profile import CareerProfile
from app.models.skill import Skill
from app.models.otp import EmailOtp
from app.models.user import User
from app.core.security import create_oauth_state_token
from app.core.database import SessionLocal


def test_job_provider_search_keywords(client):
    """Verify provider architecture searches across title, company, description, and skills."""
    # 1. Search Java
    res_java = client.get("/api/v1/jobs?query=Java")
    assert res_java.status_code == 200
    java_jobs = res_java.json()
    assert len(java_jobs) >= 1
    assert any("Java" in j["skills_required"] or "Java" in j["title"] for j in java_jobs)
    assert all("source" in j and j["source"] for j in java_jobs)

    # 2. Search Python
    res_python = client.get("/api/v1/jobs?query=Python")
    assert res_python.status_code == 200
    python_jobs = res_python.json()
    assert len(python_jobs) >= 1
    assert any("Python" in j["skills_required"] or "Python" in j["title"] for j in python_jobs)

    # 3. Search Cybersecurity
    res_sec = client.get("/api/v1/jobs?query=Cybersecurity")
    assert res_sec.status_code == 200
    sec_jobs = res_sec.json()
    assert len(sec_jobs) >= 1
    assert any("Cybersecurity" in j["skills_required"] or "Cybersecurity" in j["title"] for j in sec_jobs)


def test_empty_profile_gating_match_score(client):
    """
    Zero-fabrication rule: A newly registered user with no confirmed skills
    must receive 'Match Unavailable' without a fake match percentage.
    """
    import uuid
    unique_email = f"empty_{uuid.uuid4().hex[:8]}@jobpilot.io"
    # 1. Register fresh user
    user_payload = {
        "email": unique_email,
        "password": "Password123!",
        "full_name": "New Candidate"
    }
    reg_res = client.post("/api/v1/auth/register", json=user_payload)
    assert reg_res.status_code == 201
    user_data = reg_res.json()

    # 2. Log in
    login_res = client.post("/api/v1/auth/login", json={"email": user_payload["email"], "password": user_payload["password"]})
    token = login_res.json()["access_token"]
    headers = {"Authorization": f"Bearer {token}"}

    # 3. Ensure profile has zero skills
    profile = client.get("/api/v1/profile", headers=headers).json()
    assert len(profile.get("skills", [])) == 0

    # 4. Fetch jobs
    jobs = client.get("/api/v1/jobs?query=Python").json()
    assert len(jobs) >= 1
    target_job_id = jobs[0]["id"]

    # 5. Request match score — MUST return Match Unavailable
    match_res = client.get(f"/api/v1/jobs/{target_job_id}/match", headers=headers)
    assert match_res.status_code == 200
    match_data = match_res.json()
    assert match_data["match_score"] is None
    assert match_data["match_tier"] == "Match Unavailable"
    assert match_data["is_profile_insufficient"] is True
    assert "Complete your Career Profile or upload your resume" in match_data["explanation"]

    # 6. Now add confirmed skill to profile and verify score calculates
    skill_payload = {"name": "Python", "category": "Technical", "proficiency": "Advanced"}
    client.post("/api/v1/profile/skills", json=skill_payload, headers=headers)

    # Re-request match — score should now compute
    match_res2 = client.get(f"/api/v1/jobs/{target_job_id}/match?force_refresh=true", headers=headers)
    assert match_res2.status_code == 200
    match_data2 = match_res2.json()
    assert match_data2["match_score"] is not None
    assert match_data2["match_score"] > 0
    assert match_data2["is_profile_insufficient"] is False


def test_registration_email_otp_flow(client):
    """Validates complete email OTP registration flow."""
    db = SessionLocal()
    try:
        import uuid
        test_email = f"otp_{uuid.uuid4().hex[:8]}@jobpilot.io"

        # 1. Start registration
        start_payload = {
            "full_name": "OTP Test User",
            "email": test_email,
            "password": "SecurePassword999!"
        }
        start_res = client.post("/api/v1/auth/register/start", json=start_payload)
        assert start_res.status_code == 200
        assert start_res.json()["status"] == "otp_sent"

        # 2. Query OTP record from database
        otp_record = db.query(EmailOtp).filter(EmailOtp.email == test_email, EmailOtp.is_used == False).first()
        assert otp_record is not None
        assert otp_record.attempts == 0

        # 3. Verify with wrong OTP
        verify_bad = client.post("/api/v1/auth/register/verify", json={"email": test_email, "otp": "000000"})
        assert verify_bad.status_code == 400
        assert "Invalid verification code" in verify_bad.json()["detail"]

        # 4. Extract valid code for test verification by generating hash match
        import hashlib
        known_code = "123456"
        otp_record.otp_hash = hashlib.sha256(f"{test_email}:{known_code}".encode("utf-8")).hexdigest()
        db.commit()

        verify_good = client.post("/api/v1/auth/register/verify", json={"email": test_email, "otp": known_code})
        assert verify_good.status_code == 200
        tokens = verify_good.json()
        assert "access_token" in tokens

        # 5. Verify user created in DB and profile is 100% clean/empty
        auth_headers = {"Authorization": f"Bearer {tokens['access_token']}"}
        profile = client.get("/api/v1/profile", headers=auth_headers).json()
        assert len(profile.get("skills", [])) == 0
        assert len(profile.get("projects", [])) == 0
        assert len(profile.get("experience", [])) == 0
    finally:
        db.close()


def test_oauth_connect_and_deep_link_redirect(client, user_a_headers):
    """Validates OAuth authorization URL generation and Android deep link return."""
    db = SessionLocal()
    try:
        # 1. Connect Google — should return URL with signed state
        g_res = client.get("/api/v1/integrations/google/connect", headers=user_a_headers)
        assert g_res.status_code == 200

        # 2. Callback with state — should redirect to jobpilot://oauth/success?provider=google
        user = db.query(User).filter(User.email == "user_a_test@jobpilot.io").first()
        assert user is not None
        state = create_oauth_state_token(user.id, "google")

        callback_res = client.get(f"/api/v1/integrations/google/callback?code=mock_code&state={state}", follow_redirects=False)
        assert callback_res.status_code in [307, 302]
        assert callback_res.headers["location"] == "jobpilot://oauth/success?provider=google"

        # 3. Status should show Google connected
        status_res = client.get("/api/v1/integrations/status", headers=user_a_headers)
        assert status_res.status_code == 200
        assert status_res.json()["google"]["is_connected"] is True
    finally:
        db.close()
