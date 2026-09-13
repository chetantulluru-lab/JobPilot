import pytest


def test_integrations_status_overview(client, user_a_headers):
    res = client.get("/api/v1/integrations/status", headers=user_a_headers)
    assert res.status_code == 200
    data = res.json()
    assert "google" in data
    assert "github" in data
    assert "linkedin" in data
    assert "is_configured" in data["google"]
    assert "is_connected" in data["google"]


def test_gmail_sync_application_lifecycle_and_notifications(client, user_a_headers):
    # 1. Create target application for TechCorp
    app_payload = {
        "company": "TechCorp",
        "role": "Python Backend Engineer",
        "applied_date": "Sep 01, 2026",
        "status": "APPLIED"
    }
    create_app_res = client.post("/api/v1/applications", json=app_payload, headers=user_a_headers)
    assert create_app_res.status_code == 201
    app_id = create_app_res.json()["id"]

    # 2. Trigger Gmail Sync
    sync_res = client.post("/api/v1/integrations/google/sync", headers=user_a_headers)
    assert sync_res.status_code == 200

    # 3. Verify application status updated from APPLIED to INTERVIEW
    updated_app = client.get(f"/api/v1/applications/{app_id}", headers=user_a_headers).json()
    assert updated_app["status"] == "INTERVIEW"
    assert len(updated_app["events"]) >= 1

    # 4. Verify notification generated
    notifs = client.get("/api/v1/notifications", headers=user_a_headers).json()
    assert any("TechCorp" in n["title"] or "TechCorp" in n["message"] for n in notifs)


def test_github_repos_and_project_import(client, user_a_headers):
    # 1. List repos
    repos_res = client.get("/api/v1/integrations/github/repos", headers=user_a_headers)
    assert repos_res.status_code == 200
    repos = repos_res.json()
    assert len(repos) >= 1
    sample_repo = repos[0]

    # 2. Import project from GitHub
    import_payload = {
        "repo_name": sample_repo["name"],
        "title": "JobPilot Backend Architecture",
        "description": sample_repo["description"],
        "tech_stack": f"{sample_repo['language']}, FastAPI, PostgreSQL",
        "github_url": sample_repo["html_url"]
    }
    import_res = client.post("/api/v1/integrations/github/import-project", json=import_payload, headers=user_a_headers)
    assert import_res.status_code == 200

    # 3. Verify project exists in user profile
    profile = client.get("/api/v1/profile", headers=user_a_headers).json()
    projects = profile.get("projects", [])
    assert any(p["title"] == "JobPilot Backend Architecture" for p in projects)
