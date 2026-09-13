import pytest


def test_resume_builder_crud_tailoring_and_pdf_export(client, user_a_headers, user_b_headers):
    # 1. Setup profile for User A
    client.put(
        "/api/v1/profile",
        json={"headline": "Full Stack Engineer", "summary": "Experienced Python and React developer."},
        headers=user_a_headers
    )
    client.post(
        "/api/v1/profile/skills",
        json={"name": "Python", "category": "Languages", "proficiency": "Expert"},
        headers=user_a_headers
    )
    client.post(
        "/api/v1/profile/projects",
        json={"title": "Cloud Manager", "description": "Built backend services in Python", "tech_stack": "Python, Docker"},
        headers=user_a_headers
    )

    # 2. Generate Resume from profile with Modern template
    gen_res = client.post(
        "/api/v1/resumes/builder/generate",
        json={"title": "Software Engineer Resume", "template_type": "Modern"},
        headers=user_a_headers
    )
    assert gen_res.status_code == 200
    resume_data = gen_res.json()
    resume_id = resume_data["id"]
    assert resume_data["template_type"] == "Modern"
    assert "Python" in str(resume_data["skills_json"])

    # 3. List saved resumes
    list_res = client.get("/api/v1/resumes/builder/saved", headers=user_a_headers)
    assert list_res.status_code == 200
    assert len(list_res.json()) >= 1

    # 4. Update section in saved resume
    update_res = client.put(
        f"/api/v1/resumes/builder/{resume_id}",
        json={"summary_text": "Updated custom summary for leadership role."},
        headers=user_a_headers
    )
    assert update_res.status_code == 200
    assert update_res.json()["summary_text"] == "Updated custom summary for leadership role."

    # 5. Tailor resume for job
    jobs = client.get("/api/v1/jobs").json()
    target_job_id = jobs[0]["id"] if jobs else None

    tailor_res = client.post(
        f"/api/v1/resumes/builder/{resume_id}/tailor",
        json={"target_job_id": target_job_id},
        headers=user_a_headers
    )
    assert tailor_res.status_code == 200
    assert "tailored_summary" in tailor_res.json()

    # 6. Export as clean, valid PDF document
    pdf_res = client.post(f"/api/v1/resumes/builder/{resume_id}/export-pdf", headers=user_a_headers)
    assert pdf_res.status_code == 200
    assert pdf_res.headers["content-type"] == "application/pdf"
    # PDF magic byte signature check
    assert pdf_res.content.startswith(b"%PDF-")

    # 7. Duplicate resume
    dup_res = client.post(f"/api/v1/resumes/builder/{resume_id}/duplicate", headers=user_a_headers)
    assert dup_res.status_code == 200
    dup_id = dup_res.json()["id"]
    assert dup_id != resume_id
    assert "(Copy)" in dup_res.json()["title"]

    # 8. User B cannot access User A's saved resume (User isolation)
    get_b = client.get(f"/api/v1/resumes/builder/{resume_id}", headers=user_b_headers)
    assert get_b.status_code == 404

    # 9. Delete duplicate
    del_res = client.delete(f"/api/v1/resumes/builder/{dup_id}", headers=user_a_headers)
    assert del_res.status_code == 204
