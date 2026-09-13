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
