import io


def test_resume_upload_and_validation(client, user_a_headers):
    # Test valid PDF upload
    pdf_bytes = b"%PDF-1.4 Mock PDF file content for JobPilot resume test"
    files = {"file": ("my_resume.pdf", io.BytesIO(pdf_bytes), "application/pdf")}
    data = {"title": "Software Engineer 2026"}

    response = client.post("/api/v1/resumes/upload", files=files, data=data, headers=user_a_headers)
    assert response.status_code == 201
    res_data = response.json()
    assert res_data["title"] == "Software Engineer 2026"
    assert res_data["file_name"] == "my_resume.pdf"
    assert "missing_fields" in res_data
    resume_id = res_data["id"]

    # Retrieve uploaded resume
    get_res = client.get(f"/api/v1/resumes/{resume_id}", headers=user_a_headers)
    assert get_res.status_code == 200

    # Delete resume
    del_res = client.delete(f"/api/v1/resumes/{resume_id}", headers=user_a_headers)
    assert del_res.status_code == 204


def test_resume_invalid_extension(client, user_a_headers):
    # Test rejecting executable / invalid file
    bad_bytes = b"echo 'bad script'"
    files = {"file": ("malicious.exe", io.BytesIO(bad_bytes), "application/octet-stream")}
    response = client.post("/api/v1/resumes/upload", files=files, headers=user_a_headers)
    assert response.status_code == 400
    assert "Unsupported file format" in response.json()["detail"]


def test_profile_missing_fields_audit(client, user_a_headers):
    response = client.get("/api/v1/resumes/audit/missing-fields", headers=user_a_headers)
    assert response.status_code == 200
    data = response.json()
    assert "missing_fields" in data
    assert "total_missing" in data
