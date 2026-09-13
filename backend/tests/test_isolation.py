def test_user_cannot_access_or_delete_other_users_application(client, user_a_headers, user_b_headers):
    # User A creates an application
    app_payload = {
        "company": "Top Secret AI Corp",
        "role": "Chief Architect",
        "applied_date": "Sep 13, 2026",
        "status": "INTERVIEW",
        "notes": "User A private interview details"
    }
    create_res = client.post("/api/v1/applications", json=app_payload, headers=user_a_headers)
    assert create_res.status_code == 201
    user_a_app_id = create_res.json()["id"]

    # User B attempts to view User A's application -> 404 (Not Found / Access Denied)
    view_res = client.get(f"/api/v1/applications/{user_a_app_id}", headers=user_b_headers)
    assert view_res.status_code == 404

    # User B attempts to delete User A's application -> 404
    del_res = client.delete(f"/api/v1/applications/{user_a_app_id}", headers=user_b_headers)
    assert del_res.status_code == 404

    # Verify User A's application is intact
    check_res = client.get(f"/api/v1/applications/{user_a_app_id}", headers=user_a_headers)
    assert check_res.status_code == 200
    assert check_res.json()["company"] == "Top Secret AI Corp"


def test_user_cannot_delete_other_users_education(client, user_a_headers, user_b_headers):
    # User A adds education
    edu_res = client.post(
        "/api/v1/profile/education",
        json={"institution": "Stanford University", "degree": "MS", "field_of_study": "AI"},
        headers=user_a_headers
    )
    assert edu_res.status_code == 201
    user_a_edu_id = edu_res.json()["id"]

    # User B attempts to delete User A's education -> 404
    attack_res = client.delete(f"/api/v1/profile/education/{user_a_edu_id}", headers=user_b_headers)
    assert attack_res.status_code == 404
