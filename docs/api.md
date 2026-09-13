# 📡 JobPilot REST API Specification

> **Base URL (Local)**: `http://localhost:8000/api/v1`  
> **Base URL (Android Emulator)**: `http://10.0.2.2:8000/api/v1`  
> **Interactive Docs**: `http://localhost:8000/docs` (Swagger UI) & `http://localhost:8000/redoc`

All endpoints except `/auth/login`, `/auth/register`, and `/health` require the `Authorization: Bearer <access_token>` header.

---

## 1. Authentication Endpoints

| Method | Endpoint | Description | Request Body | Response |
| :--- | :--- | :--- | :--- | :--- |
| `POST` | `/auth/register` | Create a new candidate account | `{email, password, full_name}` | `UserDto` |
| `POST` | `/auth/login` | Authenticate with email & password | `{email, password}` | `TokenResponseDto` |
| `POST` | `/auth/refresh` | Exchange refresh token for new access token | `{refresh_token}` | `TokenResponseDto` |
| `GET` | `/auth/me` | Fetch authenticated candidate details | None | `UserDto` |

---

## 2. Career Profile Endpoints

| Method | Endpoint | Description | Request Body | Response |
| :--- | :--- | :--- | :--- | :--- |
| `GET` | `/profile` | Retrieve the authenticated user's profile | None | `CareerProfileDto` |
| `POST` | `/profile/education` | Add education record | `EducationDto` | `EducationDto` |
| `DELETE` | `/profile/education/{id}` | Delete education record | None | `204 No Content` |
| `POST` | `/profile/skills` | Add verified skill | `SkillDto` | `SkillDto` |
| `DELETE` | `/profile/skills/{id}` | Remove skill | None | `204 No Content` |
| `POST` | `/profile/projects` | Add project entry | `ProjectDto` | `ProjectDto` |
| `DELETE` | `/profile/projects/{id}` | Remove project entry | None | `204 No Content` |

---

## 3. Job Search & Semantic Matching Endpoints

| Method | Endpoint | Description | Request Body | Response |
| :--- | :--- | :--- | :--- | :--- |
| `GET` | `/jobs` | List active job postings (with query filters) | Query params | `List[JobDto]` |
| `GET` | `/jobs/{id}` | Get detailed job posting information | None | `JobDto` |
| `GET` | `/jobs/{id}/match` | Calculate semantic match score & explanation | None | `JobMatchDto` |
| `GET` | `/jobs/{id}/skill-gaps` | Get actionable skill gap roadmap | None | `SkillGapDto` |

---

## 4. Application Tracker Endpoints

| Method | Endpoint | Description | Request Body | Response |
| :--- | :--- | :--- | :--- | :--- |
| `GET` | `/applications` | List tracked applications | Query params | `List[ApplicationDto]` |
| `POST` | `/applications` | Create a new tracked application | `ApplicationDto` | `ApplicationDto` (`201`) |
| `GET` | `/applications/{id}` | Get application by ID with timeline | None | `ApplicationDto` |
| `PUT` | `/applications/{id}` | Update status, interview date, or notes | `ApplicationDto` | `ApplicationDto` |
| `DELETE`| `/applications/{id}` | Delete application | None | `204 No Content` |

---

## 5. Resume NLP & Extraction Endpoints

| Method | Endpoint | Description | Request Body | Response |
| :--- | :--- | :--- | :--- | :--- |
| `POST` | `/resumes/upload` | Upload PDF/DOCX for local deterministic NLP | `multipart/form-data` | `ExtractedResumeResponseDto` |
| `GET` | `/resumes/{id}/extracted-data`| Get structured extracted entities | None | `ExtractedResumeResponseDto` |
| `PUT` | `/resumes/{id}/extracted-data`| User edit before confirmation | `ExtractedResumeDataDto` | `ExtractedResumeResponseDto` |
| `POST` | `/resumes/{id}/confirm` | Confirm & merge into CareerProfile | None | `ConfirmResumeResponseDto` |
| `GET` | `/resumes/audit/missing-fields` | Get overall missing profile fields audit | None | `MissingFieldsAuditDto` |

---

## 6. AI Career Assistant Endpoints

| Method | Endpoint | Description | Request Body | Response |
| :--- | :--- | :--- | :--- | :--- |
| `GET` | `/assistant/conversations` | List user's AI conversations | None | `List[AIConversationDto]` |
| `GET` | `/assistant/conversations/{id}` | Get conversation message history | None | `AIConversationDto` |
| `POST` | `/assistant/chat` | Send message to AI Coach (persisted) | `ChatRequest` | `ChatResponse` |
| `POST` | `/assistant/quick-coach` | Fast transient coaching query | `ChatRequest` | `ChatResponse` |

---

## 7. AI Resume Builder & PDF Export Endpoints

| Method | Endpoint | Description | Request Body | Response |
| :--- | :--- | :--- | :--- | :--- |
| `POST` | `/resumes/builder/generate` | Generate initial resume from CareerProfile | `SavedResumeCreate` | `SavedResumeResponse` |
| `GET` | `/resumes/builder/saved` | List candidate's saved resumes | None | `List[SavedResumeResponse]` |
| `GET` | `/resumes/builder/{id}` | Get saved resume by ID | None | `SavedResumeResponse` |
| `PUT` | `/resumes/builder/{id}` | Update resume content or template | `SavedResumeUpdate` | `SavedResumeResponse` |
| `POST` | `/resumes/builder/{id}/tailor` | Tailor resume for target job | `ResumeTailorRequest` | `ResumeTailorResponse` |
| `GET` | `/resumes/builder/{id}/export-pdf`| Download real ATS-friendly PDF file | None | `application/pdf` stream |

---

## 8. Career Tools Endpoints

| Method | Endpoint | Description | Request Body | Response |
| :--- | :--- | :--- | :--- | :--- |
| `POST` | `/career-tools/cover-letter` | Generate tailored cover letter | `CoverLetterRequest` | `CoverLetterResponse` |
| `GET` | `/career-tools/cover-letters` | List saved cover letters | None | `List[CoverLetterResponse]` |
| `POST` | `/career-tools/recruiter-message` | Draft LinkedIn or email outreach message | `RecruiterMessageRequest` | `RecruiterMessageResponse` |
| `GET` | `/career-tools/recruiter-messages`| List saved recruiter messages | None | `List[RecruiterMessageResponse]` |
| `POST` | `/career-tools/analyze-jd` | Paste raw JD for instant parsing & match | `JDAnalysisRequest` | `JDAnalysisResponse` |

---

## 9. Integrations & Notifications Endpoints

| Method | Endpoint | Description | Request Body | Response |
| :--- | :--- | :--- | :--- | :--- |
| `GET` | `/integrations/status` | Get connection status for all providers | None | `IntegrationsOverviewResponse` |
| `POST` | `/integrations/google/sync` | Sync recruiter emails from Gmail | None | `{synced_count, updated_count}` |
| `GET` | `/integrations/github/repos` | List verified GitHub repositories | None | `List[GitHubRepoDto]` |
| `POST` | `/integrations/github/import-project`| Import repository directly into Profile | `GitHubImportRequest` | `ProjectDto` |
| `POST` | `/integrations/disconnect/{provider}`| Disconnect integration token | None | `204 No Content` |
| `GET` | `/notifications` | List user notifications | None | `List[NotificationResponse]` |
| `PUT` | `/notifications/{id}/read` | Mark notification as read | None | `NotificationResponse` |
| `PUT` | `/notifications/mark-all-read` | Mark all notifications as read | None | `204 No Content` |
