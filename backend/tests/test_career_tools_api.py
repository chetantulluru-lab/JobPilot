import pytest


def test_cover_letter_and_recruiter_messages(client, user_a_headers, user_b_headers):
    # 1. Generate Cover Letter
    cl_payload = {
        "company": "NextGen AI",
        "role": "Python Backend Engineer",
        "tone": "Confident"
    }
    res_cl = client.post("/api/v1/career-tools/cover-letter", json=cl_payload, headers=user_a_headers)
    assert res_cl.status_code == 200
    cl_data = res_cl.json()
    assert "content" in cl_data
    assert "NextGen AI" in cl_data["content"]
    assert cl_data["tone"] == "Confident"
    cl_id = cl_data["id"]

    # 2. List cover letters for User A
    list_cl = client.get("/api/v1/career-tools/cover-letters", headers=user_a_headers)
    assert list_cl.status_code == 200
    assert any(item["id"] == cl_id for item in list_cl.json())

    # 3. User B cannot see User A's cover letter
    list_cl_b = client.get("/api/v1/career-tools/cover-letters", headers=user_b_headers)
    assert list_cl_b.status_code == 200
    assert all(item["id"] != cl_id for item in list_cl_b.json())

    # 4. Generate Recruiter Message
    rm_payload = {
        "company": "NextGen AI",
        "role": "Python Backend Engineer",
        "recipient_name": "Sarah Miller",
        "platform": "LinkedIn",
        "context": "Applied via official careers portal yesterday."
    }
    res_rm = client.post("/api/v1/career-tools/recruiter-message", json=rm_payload, headers=user_a_headers)
    assert res_rm.status_code == 200
    rm_data = res_rm.json()
    assert "content" in rm_data
    assert "Sarah Miller" in rm_data["content"] or "Sarah" in rm_data["content"]


def test_analyze_job_description_endpoint(client, user_a_headers):
    raw_jd = """
    Software Engineer - Backend
    TechCorp | San Francisco, CA (Remote)
    
    About the Role:
    We are looking for a Software Engineer to join our core backend team.
    
    Requirements:
    - 3+ years of experience with Python and PostgreSQL
    - Proficiency with FastAPI or Django
    
    Nice to Have:
    - Docker, Kubernetes, AWS
    - Bachelor's in Computer Science
    """
    res = client.post("/api/v1/career-tools/analyze-jd", json={"raw_jd_text": raw_jd}, headers=user_a_headers)
    assert res.status_code == 200
    data = res.json()
    assert "required_skills" in data
    assert "match_score" in data
    assert "match_tier" in data
    assert "explanation" in data
    assert 0 <= data["match_score"] <= 100
    # Checks that skills were extracted and normalized
    req_skills = data["required_skills"]
    assert "Python" in req_skills
    assert "PostgreSQL" in req_skills
