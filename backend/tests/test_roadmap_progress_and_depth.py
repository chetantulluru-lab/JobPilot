import pytest
from fastapi.testclient import TestClient
from app.main import app

client = TestClient(app)


def test_catalog_realistic_depth():
    response = client.get("/api/v1/roadmaps/catalog")
    assert response.status_code == 200
    data = response.json()
    courses = data["courses"]
    assert len(courses) >= 28

    for c in courses:
        assert c["total_days"] >= 25, f"Course {c['id']} has only {c['total_days']} days (expected >= 25)"
        assert c["total_phases"] >= 2, f"Course {c['id']} has only {c['total_phases']} phases (expected >= 2)"


def test_roadmap_realistic_days_and_duration_scaling(user_a_headers):
    payload_6m = {
        "course_ids": ["python-dev"],
        "duration": "6 Months"
    }
    res_6m = client.post("/api/v1/roadmaps/generate-from-courses", json=payload_6m, headers=user_a_headers)
    assert res_6m.status_code == 201
    data_6m = res_6m.json()
    assert data_6m["total_days"] >= 60, f"Expected >= 60 days for 6 Months, got {data_6m['total_days']}"
    assert len(data_6m["phases"]) >= 3

    payload_multi = {
        "course_ids": ["dsa-cse", "python-dev"],
        "duration": "6 Months"
    }
    res_multi = client.post("/api/v1/roadmaps/generate-from-courses", json=payload_multi, headers=user_a_headers)
    assert res_multi.status_code == 201
    data_multi = res_multi.json()
    assert data_multi["total_days"] >= 70, f"Expected >= 70 days for DSA + Python, got {data_multi['total_days']}"


def test_day_completion_progress_advancement_and_no_premature_completion(user_a_headers):
    payload = {
        "course_ids": ["dsa-cse"],
        "duration": "6 Months"
    }
    res = client.post("/api/v1/roadmaps/generate-from-courses", json=payload, headers=user_a_headers)
    assert res.status_code == 201
    roadmap = res.json()
    total_days = roadmap["total_days"]
    assert total_days >= 60

    first_phase = roadmap["phases"][0]
    day_1 = first_phase["days"][0]
    day_1_id = day_1["id"]

    complete_res = client.post(f"/api/v1/roadmaps/days/{day_1_id}/complete", headers=user_a_headers)
    assert complete_res.status_code == 200
    comp_data = complete_res.json()

    assert comp_data["completed_days"] == 1, f"Expected 1 completed day, got {comp_data['completed_days']}"
    assert comp_data["total_days"] == total_days
    assert comp_data["progress_percentage"] > 0
    assert comp_data["roadmap_completed"] is False

    detail_res = client.get(f"/api/v1/roadmaps/{roadmap['id']}", headers=user_a_headers)
    assert detail_res.status_code == 200
    detail = detail_res.json()
    assert detail["completed_days"] == 1
    assert detail["is_completed"] is False
    assert detail["progress_percentage"] > 0
