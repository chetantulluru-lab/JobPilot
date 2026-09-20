import json
import urllib.request
import urllib.parse
import urllib.error
import sys
import time

BASE_URL = "https://jobpilot-backend-e97f.onrender.com/api/v1"
TEST_RESULTS = []

def record(test_name, success, details):
    status_str = "PASS" if success else "FAIL"
    print(f"[{status_str}] {test_name}: {details}")
    TEST_RESULTS.append({
        "name": test_name,
        "success": success,
        "details": details
    })

def make_request(method, path, data=None, token=None):
    url = f"{BASE_URL}{path}"
    headers = {
        "Content-Type": "application/json",
        "User-Agent": "JobPilot-Windows-E2E-Tester/1.0"
    }
    if token:
        headers["Authorization"] = f"Bearer {token}"
    
    encoded_data = json.dumps(data).encode("utf-8") if data is not None else None
    req = urllib.request.Request(url, data=encoded_data, headers=headers, method=method)
    
    try:
        with urllib.request.urlopen(req, timeout=25) as resp:
            body = resp.read().decode("utf-8")
            try:
                parsed = json.loads(body)
            except Exception:
                parsed = body
            return resp.status, parsed
    except urllib.error.HTTPError as e:
        body = e.read().decode("utf-8")
        try:
            parsed = json.loads(body)
        except Exception:
            parsed = body
        return e.code, parsed
    except Exception as e:
        return 0, str(e)

print("="*60)
print("JOBPILOT LIVE PRODUCTION API E2E VALIDATION SUITE")
print(f"Target: {BASE_URL}")
print("="*60)

# 1. Health
status, data = make_request("GET", "/health")
record("GET /health", status == 200 and data.get("status") == "ok", f"Status {status}, DB: {data.get('database')}")

# 2. Register Start (Brevo OTP trigger)
test_email = f"test_e2e_{int(time.time())}@jobpilot.live"
reg_start_payload = {
    "full_name": "E2E Parity Tester",
    "email": test_email,
    "password": "Password123!@#"
}
status, data = make_request("POST", "/auth/register/start", reg_start_payload)
record("POST /auth/register/start (Brevo OTP Trigger)", status in [200, 201], f"Status {status}, data: {data}")

# 3. Register Verify with Invalid OTP (Expect rejection 400)
verify_payload = {
    "email": test_email,
    "otp": "000000"
}
status, data = make_request("POST", "/auth/register/verify", verify_payload)
record("POST /auth/register/verify (Invalid OTP Rejection)", status in [400, 401, 422], f"Status {status}, correctly rejected: {data.get('detail', data)}")

# 4. Course Catalog
status, data = make_request("GET", "/roadmaps/catalog")
record("GET /roadmaps/catalog", status == 200, f"Status {status}, courses count: {len(data.get('courses', [])) if isinstance(data, dict) else len(data) if isinstance(data, list) else 'ok'}")

# 5. Roadmap Suggestions
status, data = make_request("GET", "/roadmaps/suggestions?query=react")
record("GET /roadmaps/suggestions", status == 200, f"Status {status}, suggestions: {data}")

# 6. Jobs List
status, data = make_request("GET", "/jobs")
record("GET /jobs", status == 200, f"Status {status}, jobs found: {len(data) if isinstance(data, list) else 'ok'}")

# 7. Resumes Audit Missing Fields (unauthenticated check)
status, data = make_request("GET", "/resumes/audit/missing-fields")
record("GET /resumes/audit/missing-fields (Auth Guard)", status in [401, 403], f"Status {status}, auth protected as expected")

# 8. Integrations Status (unauthenticated check)
status, data = make_request("GET", "/integrations/status")
record("GET /integrations/status (Auth Guard)", status in [401, 403], f"Status {status}, auth protected as expected")

# Print Summary
passed = sum(1 for r in TEST_RESULTS if r["success"])
total = len(TEST_RESULTS)
print("\n" + "="*60)
print(f"TEST RESULTS: {passed}/{total} PASSED")
print("="*60)
if passed < total:
    sys.exit(1)
