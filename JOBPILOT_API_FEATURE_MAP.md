# JobPilot API Feature Map

This document provides a comprehensive mapping of every backend API endpoint defined in the Android Retrofit Service (`JobPilotApiService.kt`) and consumed by both the Android APK and Windows Desktop Application.

All calls communicate over HTTPS directly with the live Render backend at `https://jobpilot-backend-e97f.onrender.com/api/v1/`.

---

## 1. Authentication Endpoints

| HTTP Method | API Route | Android ViewModel / Screen | Windows Client Handler | Description / Purpose |
| :--- | :--- | :--- | :--- | :--- |
| `GET` | `/health` | `ApiClient` / Health Check | `api.js` (`checkHealth`) | Verify backend service availability |
| `POST` | `/auth/login` | `AuthViewModel` / `LoginScreen` | `api.js` (`login`) | User authentication & JWT token issuance |
| `POST` | `/auth/register` | `AuthViewModel` / `RegisterScreen` | `api.js` (`register`) | Standard user registration |
| `POST` | `/auth/register/start` | `AuthViewModel` / `RegisterScreen` | `api.js` (`registerStart`) | Trigger Brevo transactional 6-digit OTP email |
| `POST` | `/auth/register/verify`| `AuthViewModel` / `RegisterScreen` | `api.js` (`registerVerify`) | Validate 6-digit OTP and complete account creation |
| `POST` | `/auth/forgot-password/start` | `AuthViewModel` / `ForgotPasswordScreen` | `api.js` (`forgotPasswordStart`) | Send password reset OTP email via Brevo |
| `POST` | `/auth/forgot-password/verify`| `AuthViewModel` / `ForgotPasswordScreen` | `api.js` (`forgotPasswordVerify`) | Verify reset OTP and obtain keystone token |
| `POST` | `/auth/reset-password-keystone` | `AuthViewModel` / `ForgotPasswordScreen` | `api.js` (`resetPasswordKeystone`) | Reset user password using verified keystone |
| `GET` | `/auth/me` | `AuthViewModel` / `SplashScreen` | `api.js` (`getMe`) | Restore active session & fetch user profile |
| `POST` | `/auth/refresh` | `TokenManager` / `AuthInterceptor`| `api.js` (`refreshToken`) | Refresh expired JWT access token |

---

## 2. Profile & User Data Endpoints

| HTTP Method | API Route | Android ViewModel / Screen | Windows Client Handler | Description / Purpose |
| :--- | :--- | :--- | :--- | :--- |
| `GET` | `/profile` | `ProfileViewModel` / `ProfileScreen` | `api.js` (`getProfile`) | Fetch full career profile, skills, and completeness |
| `PUT` | `/profile/personal-info` | `ProfileViewModel` / `ProfileScreen` | `api.js` (`updatePersonalInfo`) | Update bio, title, location, phone |
| `POST` | `/profile/photo` | `ProfileViewModel` / `ProfileScreen` | `api.js` (`uploadProfilePhoto`) | Upload avatar image (multipart form) |
| `DELETE` | `/profile/photo` | `ProfileViewModel` / `ProfileScreen` | `api.js` (`deleteProfilePhoto`) | Remove user profile photo |
| `POST` | `/profile/education` | `ProfileViewModel` / `ProfileScreen` | `api.js` (`addEducation`) | Add education entry |
| `PUT` | `/profile/education/{id}`| `ProfileViewModel` / `ProfileScreen` | `api.js` (`updateEducation`) | Update existing education entry |
| `DELETE` | `/profile/education/{id}`| `ProfileViewModel` / `ProfileScreen` | `api.js` (`deleteEducation`) | Delete education record |
| `POST` | `/profile/skills` | `ProfileViewModel` / `ProfileScreen` | `api.js` (`addSkill`) | Add verified skill tag |
| `DELETE` | `/profile/skills/{id}` | `ProfileViewModel` / `ProfileScreen` | `api.js` (`deleteSkill`) | Remove skill tag |
| `POST` | `/profile/projects` | `ProfileViewModel` / `ProfileScreen` | `api.js` (`addProject`) | Add portfolio project |
| `PUT` | `/profile/projects/{id}` | `ProfileViewModel` / `ProfileScreen` | `api.js` (`updateProject`) | Update portfolio project |
| `DELETE` | `/profile/projects/{id}` | `ProfileViewModel` / `ProfileScreen` | `api.js` (`deleteProject`) | Delete portfolio project |
| `POST` | `/profile/experience` | `ProfileViewModel` / `ProfileScreen` | `api.js` (`addExperience`) | Add work experience entry |
| `PUT` | `/profile/experience/{id}`| `ProfileViewModel` / `ProfileScreen` | `api.js` (`updateExperience`) | Update work experience |
| `DELETE` | `/profile/experience/{id}`| `ProfileViewModel` / `ProfileScreen` | `api.js` (`deleteExperience`) | Delete work experience |
| `POST` | `/profile/certifications`| `ProfileViewModel` / `ProfileScreen` | `api.js` (`addCertification`) | Add certification entry |
| `PUT` | `/profile/certifications/{id}` | `ProfileViewModel` / `ProfileScreen` | `api.js` (`updateCertification`) | Update certification |
| `DELETE` | `/profile/certifications/{id}` | `ProfileViewModel` / `ProfileScreen` | `api.js` (`deleteCertification`) | Delete certification |
| `POST` | `/profile/social-profiles` | `ProfileViewModel` / `ProfileScreen` | `api.js` (`addSocialProfile`) | Add LinkedIn / GitHub / Portfolio link |
| `DELETE` | `/profile/social-profiles/{id}` | `ProfileViewModel` / `ProfileScreen` | `api.js` (`deleteSocialProfile`) | Remove social link |
| `POST` | `/profile/preferences` | `ProfileViewModel` / `ProfileScreen` | `api.js` (`setJobPreferences`) | Update target roles, salary, and work mode |

---

## 3. Roadmaps & Day Learning Endpoints

| HTTP Method | API Route | Android ViewModel / Screen | Windows Client Handler | Description / Purpose |
| :--- | :--- | :--- | :--- | :--- |
| `GET` | `/roadmaps` | `RoadmapViewModel` / `RoadmapHubScreen` | `api.js` (`getRoadmaps`) | Fetch all user generated roadmaps |
| `POST` | `/roadmaps/generate` | `RoadmapViewModel` / `RoadmapCreateScreen` | `api.js` (`generateRoadmap`) | AI curriculum generation from prompt/title |
| `POST` | `/roadmaps/generate-from-courses` | `RoadmapViewModel` / `RoadmapCreateScreen` | `api.js` (`generateRoadmapFromCourses`) | Multi-course curriculum synthesizer |
| `GET` | `/roadmaps/catalog` | `RoadmapViewModel` / `RoadmapCreateScreen` | `api.js` (`getCourseCatalog`) | Curated courses catalog |
| `GET` | `/roadmaps/suggestions` | `RoadmapViewModel` / `RoadmapCreateScreen` | `api.js` (`getRoadmapSuggestions`) | Autocomplete roadmap suggestions |
| `POST` | `/roadmaps/assistant/ask` | `RoadmapViewModel` / `RoadmapHubScreen` | `api.js` (`askCurriculumAssistant`) | Interactive roadmap assistant advisor |
| `GET` | `/roadmaps/{id}` | `RoadmapViewModel` / `RoadmapDetailScreen` | `api.js` (`getRoadmapById`) | Detailed roadmap phases and days |
| `DELETE` | `/roadmaps/{id}` | `RoadmapViewModel` / `RoadmapDetailScreen` | `api.js` (`deleteRoadmap`) | Delete user roadmap |
| `POST` | `/roadmaps/days/{day_id}/complete` | `RoadmapViewModel` / `DayLearningScreen` | `api.js` (`completeRoadmapDay`) | Mark day as completed & increment streak |
| `GET` | `/roadmaps/phases/{phase_id}/resources` | `RoadmapViewModel` / `DayLearningScreen` | `api.js` (`getPhaseResources`) | Fetch video lectures & external links |
| `GET` | `/roadmaps/days/{day_id}/quiz` | `RoadmapViewModel` / `DayLearningScreen` | `api.js` (`getDayQuiz`) | Fetch daily multiple choice quiz |
| `POST` | `/roadmaps/days/{day_id}/quiz/submit` | `RoadmapViewModel` / `DayLearningScreen` | `api.js` (`submitDayQuiz`) | Submit quiz answers for scoring |
| `GET` | `/roadmaps/days/{day_id}/note` | `RoadmapViewModel` / `DayLearningScreen` | `api.js` (`getDayNote`) | Fetch user note for learning day |
| `PUT` | `/roadmaps/days/{day_id}/note` | `RoadmapViewModel` / `DayLearningScreen` | `api.js` (`saveDayNote`) | Save/update day learning note |
| `GET` | `/roadmaps/user/bookmarks` | `RoadmapViewModel` / `RoadmapHubScreen` | `api.js` (`getUserBookmarks`) | List bookmarked learning days |
| `POST` | `/roadmaps/{id}/add-skills-to-resume` | `RoadmapViewModel` / `RoadmapDetailScreen` | `api.js` (`addSkillsToResume`) | Sync learned roadmap skills to profile |

---

## 4. Resumes & AI Builder Endpoints

| HTTP Method | API Route | Android ViewModel / Screen | Windows Client Handler | Description / Purpose |
| :--- | :--- | :--- | :--- | :--- |
| `POST` | `/resumes/upload` | `ResumeViewModel` / `ResumeUploadFlowScreen` | `api.js` (`uploadResume`) | Multipart PDF/DOCX resume upload & ATS parse |
| `GET` | `/resumes/audit/missing-fields` | `ResumeViewModel` / `ResumeUploadFlowScreen` | `api.js` (`auditMissingFields`) | Detect unparsed or missing profile sections |
| `GET` | `/resumes/{id}/extracted-data` | `ResumeViewModel` / `ResumeUploadFlowScreen` | `api.js` (`getExtractedResumeData`) | Fetch parsed resume structured data |
| `PUT` | `/resumes/{id}/extracted-data` | `ResumeViewModel` / `ResumeUploadFlowScreen` | `api.js` (`updateExtractedResumeData`) | Edit extracted data before confirmation |
| `POST` | `/resumes/{id}/confirm` | `ResumeViewModel` / `ResumeUploadFlowScreen` | `api.js` (`confirmResume`) | Confirm extraction & populate Career Profile |
| `POST` | `/resumes/{id}/analyze` | `ResumeViewModel` / `ResumeHubScreen` | `api.js` (`analyzeResume`) | Run deep ATS compatibility audit |
| `GET` | `/resumes/{id}/analysis` | `ResumeViewModel` / `ResumeHubScreen` | `api.js` (`getResumeAnalysis`) | Fetch analysis score & suggestions |
| `POST` | `/resumes/builder/generate` | `ResumeViewModel` / `ResumeBuilderScreen` | `api.js` (`generateResume`) | AI resume generation from profile |
| `GET` | `/resumes/builder/saved` | `ResumeViewModel` / `ResumeBuilderScreen` | `api.js` (`getSavedResumes`) | Fetch saved generated resume drafts |
| `GET` | `/resumes/builder/{id}` | `ResumeViewModel` / `ResumeBuilderScreen` | `api.js` (`getSavedResume`) | Get specific saved resume |
| `PUT` | `/resumes/builder/{id}` | `ResumeViewModel` / `ResumeBuilderScreen` | `api.js` (`updateSavedResume`) | Update saved resume sections |
| `POST` | `/resumes/builder/{id}/tailor` | `ResumeViewModel` / `ResumeBuilderScreen` | `api.js` (`tailorSavedResume`) | 1-Click resume tailoring against JD |
| `GET` | `/resumes/builder/{id}/export-pdf` | `ResumeViewModel` / `ResumeBuilderScreen` | `api.js` (`exportResumePdf`) | Render & stream ATS-optimized PDF |

---

## 5. AI Mock Interview Simulator Endpoints

| HTTP Method | API Route | Android ViewModel / Screen | Windows Client Handler | Description / Purpose |
| :--- | :--- | :--- | :--- | :--- |
| `POST` | `/interviews/start` | `InterviewViewModel` / `InterviewSetupScreen` | `api.js` (`startMockInterview`) | Start interview session & generate questions |
| `POST` | `/interviews/{session_id}/submit` | `InterviewViewModel` / `LiveInterviewScreen` | `api.js` (`submitMockInterview`) | Submit answers & trigger AI evaluation |
| `GET` | `/interviews/history` | `InterviewViewModel` / `InterviewSetupScreen` | `api.js` (`getInterviewHistory`) | Fetch past interview attempts & scores |
| `GET` | `/interviews/{session_id}/report` | `InterviewViewModel` / `InterviewReportScreen` | `api.js` (`getInterviewReport`) | Fetch detailed score breakdown & roadmap |

---

## 6. AI Assistant & Career Tools Endpoints

| HTTP Method | API Route | Android ViewModel / Screen | Windows Client Handler | Description / Purpose |
| :--- | :--- | :--- | :--- | :--- |
| `GET` | `/assistant/conversations` | `AssistantViewModel` / `AICareerAssistantScreen` | `api.js` (`getConversations`) | Fetch AI chat conversations |
| `GET` | `/assistant/conversations/{id}` | `AssistantViewModel` / `AICareerAssistantScreen` | `api.js` (`getConversation`) | Fetch full conversation transcript |
| `POST` | `/assistant/chat` | `AssistantViewModel` / `AICareerAssistantScreen` | `api.js` (`chat`) | Send message to profile-grounded career AI |
| `POST` | `/assistant/quick-coach` | `AssistantViewModel` / `AICareerAssistantScreen` | `api.js` (`quickCoach`) | 1-Tap quick coaching prompt |
| `POST` | `/career-tools/cover-letter` | `CareerToolsViewModel` / Secondary | `api.js` (`generateCoverLetter`) | Synthesize targeted cover letter |
| `GET` | `/career-tools/cover-letters` | `CareerToolsViewModel` / Secondary | `api.js` (`getCoverLetters`) | List saved cover letters |
| `POST` | `/career-tools/recruiter-message` | `CareerToolsViewModel` / Secondary | `api.js` (`generateRecruiterMessage`) | Generate personalized recruiter outreach |
| `GET` | `/career-tools/recruiter-messages` | `CareerToolsViewModel` / Secondary | `api.js` (`getRecruiterMessages`) | List saved recruiter messages |
| `POST` | `/career-tools/analyze-jd` | `CareerToolsViewModel` / Secondary | `api.js` (`analyzeJD`) | Deep JD keyword & requirements analysis |
| `POST` | `/outreach/generate` | `CareerToolsViewModel` / Secondary | `api.js` (`generateOutreach`) | Alternate cold outreach generator |

---

## 7. Jobs, Applications, Notifications & Integrations

| HTTP Method | API Route | Android ViewModel / Screen | Windows Client Handler | Description / Purpose |
| :--- | :--- | :--- | :--- | :--- |
| `GET` | `/jobs` | `JobViewModel` / `JobListScreen` | `api.js` (`getJobs`) | Search & list jobs with filters |
| `GET` | `/jobs/{id}` | `JobViewModel` / `JobDetailScreen` | `api.js` (`getJobById`) | Get job listing details |
| `GET` | `/jobs/{id}/match` | `JobViewModel` / `JobDetailScreen` | `api.js` (`getJobMatch`) | Compute match percentage score |
| `GET` | `/jobs/{id}/skill-gaps` | `JobViewModel` / `SkillGapDetailScreen` | `api.js` (`getJobSkillGaps`) | Skill gap breakdown (matched vs missing) |
| `GET` | `/applications` | `ApplicationViewModel` / `ApplicationListScreen` | `api.js` (`getApplications`) | List tracked applications by status |
| `POST` | `/applications` | `ApplicationViewModel` / `ApplicationListScreen` | `api.js` (`createApplication`) | Create new tracked application |
| `GET` | `/applications/{id}` | `ApplicationViewModel` / `ApplicationDetailScreen` | `api.js` (`getApplicationById`) | Application details & notes |
| `PUT` | `/applications/{id}` | `ApplicationViewModel` / `ApplicationDetailScreen` | `api.js` (`updateApplication`) | Update status stage or application notes |
| `DELETE` | `/applications/{id}` | `ApplicationViewModel` / `ApplicationDetailScreen` | `api.js` (`deleteApplication`) | Delete tracked application |
| `GET` | `/notifications` | `NotificationViewModel` / `NotificationCenterScreen` | `api.js` (`getNotifications`) | Real-time notification feed |
| `PUT` | `/notifications/{id}/read` | `NotificationViewModel` / `NotificationCenterScreen` | `api.js` (`markNotificationRead`) | Mark single alert as read |
| `PUT` | `/notifications/mark-all-read` | `NotificationViewModel` / `NotificationCenterScreen` | `api.js` (`markAllNotificationsRead`) | Mark all alerts read |
| `GET` | `/integrations/status` | `ConnectedAccountsViewModel` / `ConnectedAccountsScreen` | `api.js` (`getIntegrationsStatus`) | Integration status overview |
| `GET` | `/integrations/github/repos` | `ConnectedAccountsViewModel` / `ConnectedAccountsScreen` | `api.js` (`getGitHubRepos`) | Fetch user GitHub repositories |
| `POST` | `/integrations/github/import-project` | `ConnectedAccountsViewModel` / `ConnectedAccountsScreen` | `api.js` (`importGitHubRepo`) | Import GitHub repo to profile projects |
| `POST` | `/integrations/disconnect/{provider}` | `ConnectedAccountsViewModel` / `ConnectedAccountsScreen` | `api.js` (`disconnectIntegration`) | Disconnect OAuth account provider |
