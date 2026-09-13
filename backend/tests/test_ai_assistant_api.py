import pytest


def test_assistant_chat_flow_and_isolation(client, user_a_headers, user_b_headers):
    # 1. User A starts a chat
    chat_payload = {
        "message": "Why is my match low for this position?",
        "context_type": "GENERAL"
    }
    res_a = client.post("/api/v1/assistant/chat", json=chat_payload, headers=user_a_headers)
    assert res_a.status_code == 200
    data_a = res_a.json()
    assert "reply" in data_a
    assert "conversation_id" in data_a
    conv_id_a = data_a["conversation_id"]

    # 2. List conversations for User A
    list_res_a = client.get("/api/v1/assistant/conversations", headers=user_a_headers)
    assert list_res_a.status_code == 200
    conv_ids_a = [c["id"] for c in list_res_a.json()]
    assert conv_id_a in conv_ids_a

    # 3. User B cannot see User A's conversation (Multi-tenant isolation)
    list_res_b = client.get("/api/v1/assistant/conversations", headers=user_b_headers)
    assert list_res_b.status_code == 200
    conv_ids_b = [c["id"] for c in list_res_b.json()]
    assert conv_id_a not in conv_ids_b

    # 4. User B cannot access User A's conversation by ID
    get_res_b = client.get(f"/api/v1/assistant/conversations/{conv_id_a}", headers=user_b_headers)
    assert get_res_b.status_code == 404

    # 5. User A can retrieve conversation details with messages
    get_res_a = client.get(f"/api/v1/assistant/conversations/{conv_id_a}", headers=user_a_headers)
    assert get_res_a.status_code == 200
    assert len(get_res_a.json()["messages"]) >= 2  # user message + assistant reply

    # 6. Quick coach endpoint works
    quick_res = client.post(
        "/api/v1/assistant/quick-coach",
        json={"message": "How can I improve my resume?"},
        headers=user_a_headers
    )
    assert quick_res.status_code == 200
    assert "reply" in quick_res.json()
