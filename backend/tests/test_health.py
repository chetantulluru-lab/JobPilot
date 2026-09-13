def test_health_check(client):
    response = client.get("/health")
    assert response.status_code == 200
    data = response.json()
    assert data["status"] == "ok"
    assert data["database"] == "connected"


def test_root_endpoint(client):
    response = client.get("/")
    assert response.status_code == 200
    data = response.json()
    assert "JobPilot" in data["app"]
    assert data["documentation"] == "/docs"


def test_swagger_docs(client):
    response = client.get("/docs")
    assert response.status_code == 200
