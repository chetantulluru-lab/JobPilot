def test_application_lifecycle(client, user_a_headers):
    # 1. Create application
    payload = {
        "company": "Nexus AI Labs",
        "role": "Python Developer Intern",
        "location": "Bangalore",
        "salary": "₹30,000 / month",
        "work_mode": "Hybrid",
        "status": "APPLIED",
        "applied_date": "Sep 13, 2026",
        "recruiter_name": "Priya Sharma",
        "recruiter_email": "priya@nexusai.io",
        "notes": "Applied via JobPilot recommendation."
    }
    create_res = client.post("/api/v1/applications", json=payload, headers=user_a_headers)
    assert create_res.status_code == 201
    app_id = create_res.json()["id"]
    assert create_res.json()["status"] == "APPLIED"

    # 2. Add milestone event
    event_payload = {
        "title": "Technical Round Scheduled",
        "description": "45-min live coding interview with lead backend engineer.",
        "event_date": "Sep 18, 2026",
        "stage": "INTERVIEW"
    }
    event_res = client.post(f"/api/v1/applications/{app_id}/events", json=event_payload, headers=user_a_headers)
    assert event_res.status_code == 201
    assert event_res.json()["title"] == "Technical Round Scheduled"

    # 3. Update application status
    update_res = client.put(
        f"/api/v1/applications/{app_id}",
        json={"status": "INTERVIEW", "next_step": "Technical Round on Sep 18"},
        headers=user_a_headers
    )
    assert update_res.status_code == 200
    assert update_res.json()["status"] == "INTERVIEW"

    # 4. Verify application detail has timeline events
    detail_res = client.get(f"/api/v1/applications/{app_id}", headers=user_a_headers)
    assert detail_res.status_code == 200
    events = detail_res.json()["events"]
    assert len(events) >= 2  # Initial + Technical Round (+ auto status change event)

    # 5. Delete application
    del_res = client.delete(f"/api/v1/applications/{app_id}", headers=user_a_headers)
    assert del_res.status_code == 204
