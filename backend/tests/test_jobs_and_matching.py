def test_list_and_filter_jobs(client):
    # List all jobs
    response = client.get("/api/v1/jobs")
    assert response.status_code == 200
    jobs = response.json()
    assert len(jobs) >= 1

    # Filter by work_mode
    res_hybrid = client.get("/api/v1/jobs?work_mode=Hybrid")
    assert res_hybrid.status_code == 200
    for j in res_hybrid.json():
        assert j["work_mode"].lower() == "hybrid"


def test_job_matching_and_skill_gaps(client, user_a_headers):
    # Retrieve first job (Python Developer Intern)
    jobs = client.get("/api/v1/jobs").json()
    assert len(jobs) > 0
    job_id = jobs[0]["id"]

    # Calculate match
    match_res = client.get(f"/api/v1/jobs/{job_id}/match", headers=user_a_headers)
    assert match_res.status_code == 200
    match_data = match_res.json()
    assert "match_score" in match_data
    assert "matched_skills" in match_data
    assert "missing_skills" in match_data
    assert "explanation" in match_data
    assert 0 <= match_data["match_score"] <= 100

    # Calculate skill gaps roadmap
    gap_res = client.get(f"/api/v1/jobs/{job_id}/skill-gaps", headers=user_a_headers)
    assert gap_res.status_code == 200
    gap_data = gap_res.json()
    assert "gaps" in gap_data
    for gap in gap_data["gaps"]:
        assert "skill_name" in gap
        assert "recommendation" in gap
        assert "step_number" in gap
