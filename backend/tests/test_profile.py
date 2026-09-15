def test_get_and_update_profile(client, user_a_headers):
    # Fetch profile
    res = client.get("/api/v1/profile", headers=user_a_headers)
    assert res.status_code == 200
    data = res.json()
    assert "profile_strength" in data

    # Update headline
    res_update = client.put(
        "/api/v1/profile",
        json={"headline": "Lead Python & AI Engineer", "summary": "Building production backend APIs."},
        headers=user_a_headers
    )
    assert res_update.status_code == 200
    assert res_update.json()["headline"] == "Lead Python & AI Engineer"


def test_education_crud(client, user_a_headers):
    # Add education
    payload = {
        "institution": "Indian Institute of Technology",
        "degree": "B.Tech",
        "field_of_study": "Computer Science & Engineering",
        "start_year": 2022,
        "end_year": 2026,
        "grade_or_cgpa": "8.8"
    }
    res_add = client.post("/api/v1/profile/education", json=payload, headers=user_a_headers)
    assert res_add.status_code == 201
    edu_id = res_add.json()["id"]
    assert res_add.json()["institution"] == "Indian Institute of Technology"

    # Update education
    res_update = client.put(
        f"/api/v1/profile/education/{edu_id}",
        json={"grade_or_cgpa": "9.1"},
        headers=user_a_headers
    )
    assert res_update.status_code == 200
    assert res_update.json()["grade_or_cgpa"] == "9.1"

    # Delete education
    res_del = client.delete(f"/api/v1/profile/education/{edu_id}", headers=user_a_headers)
    assert res_del.status_code == 204


def test_skills_crud(client, user_a_headers):
    # Add skills
    for skill_name in ["Python", "FastAPI", "PostgreSQL", "Docker"]:
        res = client.post(
            "/api/v1/profile/skills",
            json={"name": skill_name, "category": "Backend", "proficiency": "Advanced", "is_top_skill": True},
            headers=user_a_headers
        )
        assert res.status_code == 201

    # Verify skills in profile
    prof = client.get("/api/v1/profile", headers=user_a_headers).json()
    skill_names = [s["name"] for s in prof["skills"]]
    assert "Python" in skill_names
    assert "FastAPI" in skill_names


def test_projects_crud(client, user_a_headers):
    proj_data = {
        "title": "JobPilot AI Career Engine",
        "description": "FastAPI and PostgreSQL backend for intelligent resume parsing and job matching.",
        "tech_stack": "Python, FastAPI, SQLAlchemy, PostgreSQL, Docker",
        "github_url": "https://github.com/chetan/jobpilot",
        "start_date": "Jan 2026",
        "end_date": "Present"
    }
    res = client.post("/api/v1/profile/projects", json=proj_data, headers=user_a_headers)
    assert res.status_code == 201
    proj_id = res.json()["id"]
    assert res.json()["title"] == "JobPilot AI Career Engine"

    # Delete project
    del_res = client.delete(f"/api/v1/profile/projects/{proj_id}", headers=user_a_headers)
    assert del_res.status_code == 204


def test_empty_profile_strength_starts_at_zero(client, db):
    import uuid
    unique_email = f"fresh_{uuid.uuid4().hex[:8]}@example.com"
    # Register a brand new user with no profile data
    reg_res = client.post(
        "/api/v1/auth/register",
        json={"email": unique_email, "password": "SecurePassword123!", "full_name": "Fresh User"}
    )
    assert reg_res.status_code == 201

    login_res = client.post(
        "/api/v1/auth/login",
        json={"email": unique_email, "password": "SecurePassword123!"}
    )
    assert login_res.status_code == 200
    fresh_token = login_res.json()["access_token"]
    headers = {"Authorization": f"Bearer {fresh_token}"}

    # Fetch initial profile
    prof_res = client.get("/api/v1/profile", headers=headers)
    assert prof_res.status_code == 200
    prof_data = prof_res.json()
    assert prof_data["profile_strength"] == 0, f"Expected 0% for empty profile, got {prof_data['profile_strength']}%"


def test_certifications_and_preferences_crud(client, user_a_headers):
    # Add certification
    cert_payload = {
        "name": "AWS Certified Solutions Architect",
        "issuer": "Amazon Web Services",
        "issue_date": "2024",
        "credential_url": "https://aws.amazon.com/verify/123"
    }
    res_add = client.post("/api/v1/profile/certifications", json=cert_payload, headers=user_a_headers)
    assert res_add.status_code == 201
    cert_id = res_add.json()["id"]
    assert res_add.json()["name"] == "AWS Certified Solutions Architect"

    # Update certification
    res_upd = client.put(f"/api/v1/profile/certifications/{cert_id}", json={"name": "AWS Certified Solutions Architect - Associate"}, headers=user_a_headers)
    assert res_upd.status_code == 200
    assert res_upd.json()["name"] == "AWS Certified Solutions Architect - Associate"

    # Delete certification
    res_del = client.delete(f"/api/v1/profile/certifications/{cert_id}", headers=user_a_headers)
    assert res_del.status_code == 204

    # Preferences
    pref_payload = {
        "desired_roles": "Backend Engineer, Cloud Architect",
        "preferred_locations": "Bengaluru, Remote",
        "work_modes": "Remote"
    }
    res_pref = client.post("/api/v1/profile/preferences", json=pref_payload, headers=user_a_headers)
    assert res_pref.status_code == 200
    assert res_pref.json()["desired_roles"] == "Backend Engineer, Cloud Architect"

