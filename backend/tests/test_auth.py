import uuid


def test_user_registration(client):
    unique_email = f"test_{uuid.uuid4().hex[:6]}@jobpilot.io"
    payload = {
        "email": unique_email,
        "password": "SecurePassword123",
        "full_name": "Test Engineer"
    }
    response = client.post("/api/v1/auth/register", json=payload)
    assert response.status_code == 201
    data = response.json()
    assert data["email"] == unique_email.lower()
    assert data["full_name"] == "Test Engineer"
    assert "hashed_password" not in data  # Never leak password hashes!


def test_user_duplicate_registration(client):
    unique_email = f"dup_{uuid.uuid4().hex[:6]}@jobpilot.io"
    payload = {
        "email": unique_email,
        "password": "SecurePassword123",
        "full_name": "Test Engineer"
    }
    res1 = client.post("/api/v1/auth/register", json=payload)
    assert res1.status_code == 201

    res2 = client.post("/api/v1/auth/register", json=payload)
    assert res2.status_code == 409
    assert "already exists" in res2.json()["detail"]


def test_user_login_success(client):
    unique_email = f"login_{uuid.uuid4().hex[:6]}@jobpilot.io"
    client.post(
        "/api/v1/auth/register",
        json={"email": unique_email, "password": "MyPassword123", "full_name": "Login Test"}
    )
    response = client.post(
        "/api/v1/auth/login",
        json={"email": unique_email, "password": "MyPassword123"}
    )
    assert response.status_code == 200
    data = response.json()
    assert "access_token" in data
    assert "refresh_token" in data
    assert data["token_type"] == "bearer"


def test_user_login_invalid_password(client):
    unique_email = f"wrong_{uuid.uuid4().hex[:6]}@jobpilot.io"
    client.post(
        "/api/v1/auth/register",
        json={"email": unique_email, "password": "MyPassword123", "full_name": "Login Test"}
    )
    response = client.post(
        "/api/v1/auth/login",
        json={"email": unique_email, "password": "WrongPassword"}
    )
    assert response.status_code == 401


def test_protected_me_endpoint(client, user_a_headers):
    # Without token -> 401
    res_unauth = client.get("/api/v1/auth/me")
    assert res_unauth.status_code == 401

    # With valid bearer token -> 200
    res_auth = client.get("/api/v1/auth/me", headers=user_a_headers)
    assert res_auth.status_code == 200
    data = res_auth.json()
    assert data["email"] == "user_a_test@jobpilot.io"


def test_swagger_oauth2_token_endpoint(client):
    unique_email = f"swagger_{uuid.uuid4().hex[:6]}@jobpilot.io"
    client.post(
        "/api/v1/auth/register",
        json={"email": unique_email, "password": "SwaggerPass123", "full_name": "Swagger User"}
    )
    # Standard OAuth2 form post (username & password form-encoded)
    res = client.post(
        "/api/v1/auth/token",
        data={"username": unique_email, "password": "SwaggerPass123"}
    )
    assert res.status_code == 200
    data = res.json()
    assert "access_token" in data
    assert data["token_type"] == "bearer"


def test_keystone_registration_and_reset(client):
    unique_email = f"keystone_{uuid.uuid4().hex[:6]}@jobpilot.io"
    # 1. Register with custom keystone
    reg_payload = {
        "email": unique_email,
        "password": "OldPassword123",
        "full_name": "Keystone Tester",
        "keystone": "mysecretkeystone"
    }
    reg_res = client.post("/api/v1/auth/register", json=reg_payload)
    assert reg_res.status_code == 201

    # 2. Reset password with wrong keystone -> should fail (400)
    fail_res = client.post(
        "/api/v1/auth/reset-password-keystone",
        json={"email": unique_email, "keystone": "wrongkey", "new_password": "NewPassword123"}
    )
    assert fail_res.status_code == 400

    # 3. Reset password with correct keystone -> should succeed (200)
    ok_res = client.post(
        "/api/v1/auth/reset-password-keystone",
        json={"email": unique_email, "keystone": "mysecretkeystone", "new_password": "NewPassword123"}
    )
    assert ok_res.status_code == 200
    assert ok_res.json()["status"] == "success"

    # 4. Login with new password -> should succeed (200)
    login_res = client.post(
        "/api/v1/auth/login",
        json={"email": unique_email, "password": "NewPassword123"}
    )
    assert login_res.status_code == 200
    assert "access_token" in login_res.json()

