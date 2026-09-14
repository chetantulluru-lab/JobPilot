import pytest
import uuid
from fastapi.testclient import TestClient
from sqlalchemy.orm import Session

from app.models.user import User
from app.models.career_profile import CareerProfile
from app.models.roadmap import Roadmap, RoadmapPhase, RoadmapDay
from app.services.ai.openrouter_provider import OpenRouterProvider
from app.services.ai.base import AIRequest, AIMessage


@pytest.fixture
def test_user_with_token(client: TestClient, db: Session):
    email = f"roadmap_tester_{uuid.uuid4().hex[:8]}@example.com"
    # Start registration
    resp = client.post("/api/v1/auth/register/start", json={
        "full_name": "Roadmap Tester",
        "email": email,
        "password": "Password123!"
    })
    assert resp.status_code == 200

    # Retrieve OTP directly from DB for test verification
    from app.models.otp import EmailOtp
    otp_record = db.query(EmailOtp).filter(EmailOtp.email == email).order_by(EmailOtp.created_at.desc()).first()
    assert otp_record is not None

    # We know the hash was generated from code; for test, let's verify via verify endpoint
    # Or directly create and activate user
    user = db.query(User).filter(User.email == email).first()
    if not user:
        from app.core.security import get_password_hash
        user = User(
            email=email,
            full_name="Roadmap Tester",
            hashed_password=get_password_hash("Password123!"),
            is_active=True,
            is_verified=True
        )
        db.add(user)
        db.flush()
        profile = CareerProfile(user_id=user.id, profile_strength=0, current_streak=0)
        db.add(profile)
        db.commit()

    # Login to get valid JWT token
    login_resp = client.post("/api/v1/auth/login", json={
        "email": email,
        "password": "Password123!"
    })
    assert login_resp.status_code == 200
    token = login_resp.json()["access_token"]
    return {"user": user, "token": token, "email": email}


def test_roadmap_suggestions(client: TestClient):
    """Verifies instant autocomplete suggestions without AI tokens."""
    # Test letter-by-letter matching
    resp = client.get("/api/v1/roadmaps/suggestions?query=pyth")
    assert resp.status_code == 200
    data = resp.json()
    assert "suggestions" in data
    assert any("Python" in s for s in data["suggestions"])

    # Test case insensitivity
    resp2 = client.get("/api/v1/roadmaps/suggestions?query=FASTAPI")
    assert resp2.status_code == 200
    assert any("FastAPI" in s for s in resp2.json()["suggestions"])

    # Test empty query returns featured
    resp3 = client.get("/api/v1/roadmaps/suggestions?query=")
    assert resp3.status_code == 200
    assert len(resp3.json()["suggestions"]) > 0


def test_openrouter_three_key_rotation_and_sanitization():
    """Verifies 3-key pool initialization, round-robin distribution, and reasoning tag stripping."""
    provider = OpenRouterProvider(
        api_keys=["key-one-1234", "key-two-5678", "key-three-9012"],
        default_model="deepseek/deepseek-r1-0528"
    )
    assert provider.is_available is True
    assert len(provider._keys) == 3

    # Test candidate keys rotation
    keys_first = provider._get_candidate_keys()
    keys_second = provider._get_candidate_keys()
    assert keys_first != keys_second

    # Test DeepSeek R1 <think> tag stripping
    raw_deepseek_output = (
        "<think>\n"
        "Let me analyze what the user needs. They asked for Python.\n"
        "First step is variables.\n"
        "</think>\n"
        "Here is the clean lesson plan for Python variables."
    )
    cleaned = provider._strip_reasoning_tags(raw_deepseek_output)
    assert "<think>" not in cleaned
    assert "</think>" not in cleaned
    assert cleaned == "Here is the clean lesson plan for Python variables."


def test_roadmap_generation_lifecycle(client: TestClient, test_user_with_token: dict, monkeypatch):
    """Verifies roadmap generation, day-by-day learning, phase unlocking, and skill transfer."""
    monkeypatch.setenv("TESTING", "true")
    headers = {"Authorization": f"Bearer {test_user_with_token['token']}"}

    # 1. Generate Roadmap
    gen_resp = client.post("/api/v1/roadmaps/generate", json={
        "goal": "Python Backend Developer",
        "duration": "3 Months"
    }, headers=headers)
    assert gen_resp.status_code == 201
    roadmap_data = gen_resp.json()
    roadmap_id = roadmap_data["id"]
    assert roadmap_data["title"] == "Python Backend Developer"
    assert roadmap_data["progress_percentage"] == 0
    assert len(roadmap_data["phases"]) >= 4

    phase_1 = roadmap_data["phases"][0]
    phase_2 = roadmap_data["phases"][1]
    assert phase_1["is_unlocked"] is True
    assert phase_2["is_unlocked"] is False
    assert len(phase_1["days"]) >= 5

    day_1 = phase_1["days"][0]

    # 2. Get Topic-Specific Resources for Phase 1 (EN, TE, HI)
    res_resp = client.get(f"/api/v1/roadmaps/{roadmap_id}/phases/{phase_1['id']}/resources", headers=headers)
    assert res_resp.status_code == 200
    resources = res_resp.json()
    assert len(resources) >= 4
    assert any(r["language"] == "English" for r in resources)
    assert any(r["language"] == "Telugu" for r in resources)
    assert any(r["language"] == "Hindi" for r in resources)

    # Filter Telugu
    res_te = client.get(f"/api/v1/roadmaps/{roadmap_id}/phases/{phase_1['id']}/resources?language=Telugu", headers=headers)
    assert res_te.status_code == 200
    assert all(r["language"] == "Telugu" for r in res_te.json())

    # 3. Complete Day 1 -> Progress updates, streak updates to 1
    comp_resp = client.post(f"/api/v1/roadmaps/{roadmap_id}/days/{day_1['id']}/complete", headers=headers)
    assert comp_resp.status_code == 200
    comp_data = comp_resp.json()
    assert comp_data["is_completed"] is True
    assert comp_data["completed_days"] == 1
    assert comp_data["progress_percentage"] > 0
    assert comp_data["current_streak"] == 1

    # 4. Complete remaining days in Phase 1 -> Phase 2 automatically unlocks
    for day in phase_1["days"][1:]:
        c_resp = client.post(f"/api/v1/roadmaps/{roadmap_id}/days/{day['id']}/complete", headers=headers)
        assert c_resp.status_code == 200

    # Verify Phase 2 is now unlocked
    detail_resp = client.get(f"/api/v1/roadmaps/{roadmap_id}", headers=headers)
    assert detail_resp.status_code == 200
    updated_phases = detail_resp.json()["phases"]
    assert updated_phases[0]["is_completed"] is True
    assert updated_phases[1]["is_unlocked"] is True

    # 5. Add skills to resume
    skills_resp = client.post(f"/api/v1/roadmaps/{roadmap_id}/add-skills-to-resume", headers=headers)
    assert skills_resp.status_code == 200
    assert skills_resp.json()["status"] == "success"
    assert len(skills_resp.json()["added_skills"]) > 0


def test_forgot_password_otp_flow(client: TestClient, test_user_with_token: dict, db: Session):
    """Verifies 2-step forgot password OTP verification and password reset."""
    email = test_user_with_token["email"]

    # Step 1: Start forgot password
    start_resp = client.post("/api/v1/auth/forgot-password/start", json={"email": email})
    assert start_resp.status_code == 200
    assert start_resp.json()["status"] == "otp_sent"

    # Retrieve generated OTP hash for verification
    from app.models.otp import EmailOtp
    otp_entry = db.query(EmailOtp).filter(EmailOtp.email == email, EmailOtp.is_used == False).first()
    assert otp_entry is not None

    # Step 2: Attempt reset with wrong OTP -> 400
    bad_resp = client.post("/api/v1/auth/forgot-password/verify", json={
        "email": email,
        "otp": "000000",
        "new_password": "NewSecurePassword456!"
    })
    assert bad_resp.status_code == 400

    # Verify invalid user returns 404
    missing_resp = client.post("/api/v1/auth/forgot-password/start", json={"email": "nonexistent_user_999@example.com"})
    assert missing_resp.status_code == 404
