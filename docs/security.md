# 🛡️ JobPilot Security Architecture & Hardening Standards

> **Security Mandate**: Protect candidate personal identifying information (PII), enforce zero-fabrication AI safety, prevent unauthorized cross-tenant data access, and eliminate credential exposure risks.

---

## 1. Secrets Management & Environment Isolation

1. **Zero Hardcoded Secrets**:
   - API keys, OAuth secrets, database passwords, and JWT signing keys are loaded strictly from environment variables (`.env`).
   - `.env` is permanently included in `.gitignore` and is never committed to version control.
   - `.env.example` provides documentation templates containing only safe, self-explanatory placeholders.
2. **Android APK Hardening**:
   - The Android client contains **zero** API secrets, tokens, or private keys.
   - All external provider communication (OpenRouter, Google APIs, GitHub, PostgreSQL) is proxied securely through the backend.
   - Tokens stored locally on the device (`TokenManager`) are user-scoped JWT access and refresh tokens.

---

## 2. Multi-Tenant Data Isolation

- **Row-Level Tenancy**: Every database table with user data (`career_profiles`, `resumes`, `saved_resumes`, `applications`, `ai_conversations`, `ai_messages`, `cover_letters`, `recruiter_messages`, `notifications`, `connected_accounts`) has a foreign key to `users.id`.
- **Query Enclosure**: All repository and API queries filter by `user_id == current_user.id`.
- **Cross-Tenant Attack Resistance**: Accessing another user's ID (e.g. `GET /api/v1/resumes/builder/{foreign_id}`) produces an immediate `404 Not Found`, preventing object enumeration and privacy leakage.

---

## 3. OAuth 2.0 & Third-Party Security

- **No Passwords**: JobPilot never requests, accepts, or stores user passwords for third-party services (Gmail, GitHub, LinkedIn).
- **Official Token Exchange**: Uses standard Authorization Code flows with PKCE / state validation.
- **Minimal Scopes**: Gmail scopes request `gmail.readonly` solely for subject and snippet matching against known application company names.
- **Token Revocation**: Disconnecting an account via `POST /api/v1/integrations/disconnect/{provider}` immediately deletes stored access/refresh tokens.

---

## 4. Zero-Fabrication AI & Prompt Safety

1. **Grounding Constraints**: System prompts instruct the LLM strictly to cite only candidate-verified profile attributes.
2. **Output Sanitization**: DeepSeek R1 `<think>` reasoning traces are scrubbed via regex before output leaves the server.
3. **Deterministic Local Fallbacks**: If external AI APIs fail or rate-limit (HTTP 429), rule-based algorithms generate grounded advice without exposing stack traces or API errors.

---

## 5. Network & Transport Security

- **Android Cleartext Traffic Policy**: `android/app/src/main/res/xml/network_security_config.xml` strictly forbids cleartext HTTP, with exemptions permitted only for local developer test loops (`10.0.2.2` and `localhost`).
- **Production TLS**: Enforced HTTPS termination across all public endpoints with HSTS.
- **JWT Lifetimes**: Access tokens expire in 60 minutes; refresh tokens are rotatable and revocable.
