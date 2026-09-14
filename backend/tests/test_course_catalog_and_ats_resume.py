import pytest
from fastapi.testclient import TestClient
from app.main import app

client = TestClient(app)


@pytest.fixture
def auth_headers(user_a_headers):
    return user_a_headers



def test_course_catalog():
    response = client.get("/api/v1/roadmaps/catalog")
    assert response.status_code == 200
    data = response.json()
    assert "courses" in data
    assert len(data["courses"]) >= 28

    # Verify DSA, Mobile Dev, AI & Language tracks are all present
    ids = [c["id"] for c in data["courses"]]
    assert "dsa-cse" in ids
    assert "python-dev" in ids
    assert "java-dev" in ids
    assert "os-cse" in ids
    assert "android-kotlin" in ids
    assert "ios-swift" in ids
    assert "flutter-dart" in ids
    assert "react-native" in ids
    assert "deep-learning-pytorch" in ids
    assert "generative-ai-llms" in ids
    assert "data-analytics" in ids
    assert "nlp-transformers" in ids
    assert "rust-lang" in ids
    assert "typescript-lang" in ids
    assert "csharp-lang" in ids
    assert "php-lang" in ids


def test_generate_from_multiple_courses(auth_headers):
    payload = {
        "course_ids": ["dsa-cse", "python-dev"],
        "duration": "6 Months"
    }
    response = client.post("/api/v1/roadmaps/generate-from-courses", json=payload, headers=auth_headers)
    assert response.status_code == 201
    data = response.json()
    assert "Master Track" in data["title"] or "Roadmap" in data["title"]
    assert len(data["phases"]) >= 2
    assert data["total_days"] >= 15
    assert len(data["skills_learned"]) > 0

    phase_id = data["phases"][0]["id"]

    # Test direct phase resources in English
    res_en = client.get(f"/api/v1/roadmaps/phases/{phase_id}/resources?language=English", headers=auth_headers)
    assert res_en.status_code == 200
    assert len(res_en.json()) > 0

    # Test direct phase resources in Telugu
    res_te = client.get(f"/api/v1/roadmaps/phases/{phase_id}/resources?language=Telugu", headers=auth_headers)
    assert res_te.status_code == 200
    te_data = res_te.json()
    assert len(te_data) > 0
    assert any(r["language"].lower() == "telugu" for r in te_data)

    # Test direct phase resources in Hindi
    res_hi = client.get(f"/api/v1/roadmaps/phases/{phase_id}/resources?language=Hindi", headers=auth_headers)
    assert res_hi.status_code == 200
    hi_data = res_hi.json()
    assert len(hi_data) > 0
    assert any(r["language"].lower() == "hindi" for r in hi_data)


def test_curriculum_assistant(auth_headers):
    payload = {
        "topic": "Dynamic Programming",
        "question": "What is the difference between memoization and tabulation?",
        "day_number": 10
    }
    response = client.post("/api/v1/roadmaps/assistant/ask", json=payload, headers=auth_headers)
    assert response.status_code == 200
    data = response.json()
    assert "answer" in data
    assert len(data["answer"]) > 20
    assert data["topic"] == "Dynamic Programming"


def test_create_student_ats_resume(auth_headers):
    payload = {
        "full_name": "Alex Mercer",
        "email": "alex.mercer@test.com",
        "phone": "+1 555-0199",
        "college": "State Technical Institute",
        "branch": "B.Tech in Computer Science & Engineering",
        "cgpa": "9.1 / 10",
        "grad_year": "2026",
        "github": "https://github.com/alexmercer",
        "linkedin": "https://linkedin.com/in/alexmercer",
        "skills": ["Java", "Python", "FastAPI", "React", "PostgreSQL", "Docker"],
        "projects": [
            {
                "title": "Cloud Distributed Cache",
                "tech_stack": "Python, Redis, Docker",
                "description": "Engineered a low-latency caching layer reducing database queries by 65%.",
                "github_url": "https://github.com/alexmercer/cache"
            }
        ],
        "experience": "Software Engineering Intern at Tech Corp.",
        "achievements": "LeetCode Top 5% • Global Hackathon Winner"
    }
    response = client.post("/api/v1/resumes/builder/create-ats-resume", json=payload, headers=auth_headers)
    assert response.status_code == 200
    data = response.json()
    assert data["title"] == "Alex Mercer - ATS Resume"
    assert data["contact_json"]["github"] == "https://github.com/alexmercer"
    assert len(data["education_json"]) > 0
    assert data["education_json"][0]["cgpa"] == "9.1 / 10"
