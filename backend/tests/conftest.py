import os
import sys
import pytest

# Add parent directory (backend) to python path
sys.path.insert(0, os.path.abspath(os.path.join(os.path.dirname(__file__), "..")))

from fastapi.testclient import TestClient
from app.main import app
from app.core.database import SessionLocal
from app.models.user import User


@pytest.fixture(scope="session")
def client():
    """Provides a FastAPI TestClient instance."""
    with TestClient(app) as c:
        yield c


@pytest.fixture
def user_a_headers(client):
    """Registers and authenticates User A, returning Authorization headers."""
    email = "user_a_test@jobpilot.io"
    password = "password123"
    # Register if not exists
    client.post(
        "/api/v1/auth/register",
        json={"email": email, "password": password, "full_name": "User Alpha"}
    )
    # Login
    res = client.post(
        "/api/v1/auth/login",
        json={"email": email, "password": password}
    )
    token = res.json()["access_token"]
    return {"Authorization": f"Bearer {token}"}


@pytest.fixture
def user_b_headers(client):
    """Registers and authenticates User B, returning Authorization headers."""
    email = "user_b_test@jobpilot.io"
    password = "password123"
    client.post(
        "/api/v1/auth/register",
        json={"email": email, "password": password, "full_name": "User Beta"}
    )
    res = client.post(
        "/api/v1/auth/login",
        json={"email": email, "password": password}
    )
    token = res.json()["access_token"]
    return {"Authorization": f"Bearer {token}"}


@pytest.fixture(autouse=True)
def mock_openrouter_http(monkeypatch):
    """Mocks external OpenRouter HTTP completions during test runs for speed, stability, and zero external quota usage."""
    import httpx
    from unittest.mock import MagicMock

    orig_post = httpx.Client.post

    def fake_post(self, url, *args, **kwargs):
        if "openrouter.ai" in str(url):
            payload = kwargs.get("json", {})
            messages = payload.get("messages", [])
            all_content = " ".join([m.get("content", "") for m in messages])

            resp = MagicMock()
            resp.status_code = 200

            if "recruiter" in all_content.lower() or "sarah" in all_content.lower():
                body = "Hi Sarah Miller,\n\nI am eager to connect regarding the role at NextGen AI."
            elif "cover letter" in all_content.lower() or "nextgen" in all_content.lower():
                body = "Dear NextGen AI Hiring Team,\n\nI am excited to apply for the Python Backend Engineer role."
            elif "extract the following fields" in all_content.lower() or "unstructured job description" in all_content.lower():
                body = '{\n  "required_skills": ["Python", "PostgreSQL"],\n  "preferred_skills": ["Docker", "AWS"],\n  "experience_years": 3,\n  "role_title": "Software Engineer - Backend"\n}'
            else:
                body = "I am your JobPilot AI career coach. How can I help you today?"

            resp.json.return_value = {
                "choices": [{"message": {"content": f"<think>Grounded reasoning</think>{body}"}}],
                "usage": {"total_tokens": 42}
            }
            return resp
        return orig_post(self, url, *args, **kwargs)

    monkeypatch.setattr(httpx.Client, "post", fake_post)
