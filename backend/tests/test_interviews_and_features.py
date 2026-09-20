def test_mock_interview_flow(client, user_a_headers):
    # 1. Start interview
    start_payload = {
        "mode": "ROLE_BASED",
        "target_role": "Android Engineer",
        "experience_level": "Entry-Level"
    }
    res_start = client.post("/api/v1/interviews/start", json=start_payload, headers=user_a_headers)
    assert res_start.status_code == 201
    data = res_start.json()
    assert "id" in data
    session_id = data["id"]
    assert len(data["questions"]) == 5
    assert data["questions"][0]["category"] == "Introduction"

    # 2. Submit interview answers
    submit_payload = {
        "answers": [
            {
                "question_id": 1,
                "answer_text": "Hi, I am an Android developer with a B.Tech in Computer Science. I have built multiple applications using Jetpack Compose, Kotlin Coroutines, and Clean Architecture. I am passionate about building intuitive, fluid user experiences."
            },
            {
                "question_id": 2,
                "answer_text": "In my JobPilot app, I structured the architecture into data, domain, and presentation layers. I solved state synchronization by implementing an offline-first caching repository with StateFlow and Room."
            },
            {
                "question_id": 3,
                "answer_text": "Remember retains state across recompositions within the composition lifecycle, whereas rememberSaveable persists state across configuration changes and process death using SavedStateHandle."
            },
            {
                "question_id": 4,
                "answer_text": "When a production latency spike happens, I first check APM metrics and rollback if a new release caused it. Then I inspect logs, connection pool saturation, and error rates to isolate the bottleneck."
            },
            {
                "question_id": 5,
                "answer_text": "When facing a tight deadline with changing requirements, I synchronized with our team lead, cut non-essential features, and focused on core deliverables while preserving automated tests."
            }
        ],
        "face_presence_score": 95.0
    }
    res_submit = client.post(f"/api/v1/interviews/{session_id}/submit", json=submit_payload, headers=user_a_headers)
    assert res_submit.status_code == 200
    report = res_submit.json()
    assert report["overall_score"] > 60
    assert report["presence_score"] == 95
    assert "readiness_badge" in report
    assert len(report["question_evaluations"]) == 5
    assert "model_answer" in report["question_evaluations"][0]

    # 3. Retrieve report
    res_report = client.get(f"/api/v1/interviews/{session_id}/report", headers=user_a_headers)
    assert res_report.status_code == 200
    assert res_report.json()["session_id"] == session_id

    # 4. Check history
    res_hist = client.get("/api/v1/interviews/history", headers=user_a_headers)
    assert res_hist.status_code == 200
    assert any(s["id"] == session_id for s in res_hist.json())


def test_outreach_generation(client, user_a_headers):
    payload = {
        "role_title": "Full Stack Engineer",
        "company": "Stripe",
        "job_description": "We are seeking a Full Stack Engineer proficient in Python and React to build financial infrastructure."
    }
    res = client.post("/api/v1/outreach/generate", json=payload, headers=user_a_headers)
    assert res.status_code == 200
    data = res.json()
    assert "linkedin_note" in data
    assert len(data["linkedin_note"]) <= 300
    assert "cold_email_subject" in data
    assert "cold_email_body" in data
    assert "cover_letter" in data
    assert "Stripe" in data["cover_letter"]


def test_roadmap_quizzes_and_notes(client, user_a_headers):
    # 1. Create a roadmap first
    res_cat = client.get("/api/v1/roadmaps/catalog")
    assert res_cat.status_code == 200
    course_id = res_cat.json()["courses"][0]["id"]

    res_gen = client.post(
        "/api/v1/roadmaps/generate-from-courses",
        json={"course_ids": [course_id], "duration": "10 Days"},
        headers=user_a_headers
    )
    assert res_gen.status_code == 201
    roadmap = res_gen.json()
    day_id = roadmap["phases"][0]["days"][0]["id"]

    # 2. Get quiz for day
    res_quiz = client.get(f"/api/v1/roadmaps/days/{day_id}/quiz", headers=user_a_headers)
    assert res_quiz.status_code == 200
    quiz_data = res_quiz.json()
    assert len(quiz_data["questions"]) == 3

    # 3. Submit quiz answers
    submit_payload = {
        "submissions": [
            {"question_id": 1, "selected_option_index": 1},
            {"question_id": 2, "selected_option_index": 1},
            {"question_id": 3, "selected_option_index": 1}
        ]
    }
    res_sub = client.post(f"/api/v1/roadmaps/days/{day_id}/quiz/submit", json=submit_payload, headers=user_a_headers)
    assert res_sub.status_code == 200
    sub_res = res_sub.json()
    assert "score_percentage" in sub_res
    assert "current_streak" in sub_res

    # 4. Save and fetch note & bookmark
    note_payload = {
        "note_text": "Key takeaway: Always verify time complexity and test edge cases.",
        "is_bookmarked": True
    }
    res_save_note = client.put(f"/api/v1/roadmaps/days/{day_id}/note", json=note_payload, headers=user_a_headers)
    assert res_save_note.status_code == 200
    assert res_save_note.json()["note_text"] == note_payload["note_text"]
    assert res_save_note.json()["is_bookmarked"] is True

    # 5. Get saved note
    res_get_note = client.get(f"/api/v1/roadmaps/days/{day_id}/note", headers=user_a_headers)
    assert res_get_note.status_code == 200
    assert res_get_note.json()["note_text"] == note_payload["note_text"]

    # 6. List bookmarks
    res_bmarks = client.get("/api/v1/roadmaps/user/bookmarks", headers=user_a_headers)
    assert res_bmarks.status_code == 200
    assert any(b["day_id"] == day_id for b in res_bmarks.json())
