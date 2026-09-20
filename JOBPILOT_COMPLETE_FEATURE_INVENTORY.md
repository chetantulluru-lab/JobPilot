# JobPilot Complete Feature Inventory

This document represents the complete, exhaustive feature inventory of the JobPilot application derived directly from the production Android APK source code (`android/app/src/main/java/com/jobpilot/app/`). Every feature listed here is implemented with 1:1 parity in the Windows Desktop Application and connected directly to the production Render backend.

---

## 1. Authentication & Onboarding
- **User Registration**:
  - Full name, email, and password registration form.
  - Client-side validation and password strength assessment.
  - Integration with Render Auth API (`/api/v1/auth/register`, `/api/v1/auth/register/start`, `/api/v1/auth/register/verify`).
- **Email OTP Verification**:
  - Live transactional 6-digit OTP delivery powered by Brevo email service.
  - 10-minute expiry countdown timer.
  - Dynamic resend OTP with cooldown prevention.
  - Maximum attempt tracking and safety locks.
- **User Login & Session Management**:
  - Email and password authentication (`/api/v1/auth/login`).
  - Secure JWT access and refresh token storage (`TokenManager`).
  - Automatic session restoration on app launch (`/api/v1/auth/me`).
  - Seamless token refresh handling (`/api/v1/auth/refresh`).
- **Forgot Password Flow**:
  - 2-Step password reset workflow (`/api/v1/auth/forgot-password/start`, `/api/v1/auth/forgot-password/verify`, `/api/v1/auth/reset-password-keystone`).
  - OTP verification before password update.
- **Career Profile Onboarding**:
  - Guided step-by-step wizard post-registration (`Screen.CareerProfileOnboarding`).
  - Personal Information collection (Headline, Phone, Location, Bio).
  - Education history input.
  - Direct branch to either Resume Upload or AI Resume Builder.

---

## 2. Top App Bar & Main Dashboard
- **Top App Bar**:
  - Branding badge and application logo.
  - Dynamic user greeting with active username.
  - Daily learning streak counter (`🔥`) with animated badge.
  - Quick access Notification icon with live unread badge count.
  - Interactive pulsing `AIOrb` widget triggering instant AI assistance.
- **5-Tab Navigation**:
  - Persistent bottom/sidebar Material 3 navigation across:
    1. **Home / Dashboard** (`Screen.Dashboard`)
    2. **Roadmap Hub** (`Screen.RoadmapHub`)
    3. **Resume Hub / Builder** (`Screen.ResumeHub`)
    4. **Career Profile** (`Screen.Profile`)
    5. **Settings & Preferences** (`Screen.Settings`)
- **Dashboard Widgets & Hubs**:
  - Active Streak and daily goal progress tracker.
  - Profile Completeness score ring (%) with smart action items.
  - Active Roadmap card with next day's module and direct CTA.
  - AI Mock Interview Spotlight featuring targeted role recommendations.
  - Quick action launcher for Resume Tailoring, Job Search, and Application Kanban.

---

## 3. Roadmaps & Day-by-Day Learning
- **Roadmap Generation**:
  - AI-driven curriculum generation from job titles or skill prompts (`/api/v1/roadmaps/generate`).
  - Multi-course curriculum generator (`/api/v1/roadmaps/generate-from-courses`).
  - Course catalog exploration (`/api/v1/roadmaps/catalog`).
  - Live search suggestions for career paths (`/api/v1/roadmaps/suggestions`).
  - Interactive curriculum assistant (`/api/v1/roadmaps/assistant/ask`).
- **Roadmap Management**:
  - List of active, paused, and completed roadmaps (`/api/v1/roadmaps`).
  - Roadmap detailed view with multi-phase timeline (`/api/v1/roadmaps/{id}`).
  - Skill sync from completed roadmaps to Career Profile (`/api/v1/roadmaps/{id}/add-skills-to-resume`).
  - Roadmap deletion and management.
- **Day-by-Day Learning Screen**:
  - Structured daily modules (`Screen.DayLearning`).
  - Markdown lesson viewer with code highlighting and key takeaways.
  - Embedded in-app video lectures and curated external resources (`/api/v1/roadmaps/phases/{phase_id}/resources`).
  - Daily learning completion marker (`/api/v1/roadmaps/days/{day_id}/complete`).
- **Daily Quizzes & Knowledge Checks**:
  - Automated daily quiz retrieval (`/api/v1/roadmaps/days/{day_id}/quiz`).
  - Interactive multiple-choice questions with instant scoring.
  - Submission and review feedback (`/api/v1/roadmaps/days/{day_id}/quiz/submit`).
- **Notes & Bookmarks**:
  - Rich personal note-taking per learning day (`/api/v1/roadmaps/days/{day_id}/note`).
  - Auto-saving note editor.
  - Global user bookmarks list across all roadmaps (`/api/v1/roadmaps/user/bookmarks`).

---

## 4. Resumes, Parsing & AI Builder
- **Resume Upload & ATS Parsing**:
  - Multi-format file upload (PDF and DOCX).
  - High-precision text and structured data extraction (`/api/v1/resumes/upload`, `/api/v1/resumes/{id}/extracted-data`).
  - Interactive extraction review and manual corrections editor.
  - Missing field audit detection (`/api/v1/resumes/audit/missing-fields`).
  - Confirmation and direct profile sync (`/api/v1/resumes/{id}/confirm`).
- **Deep Resume Analysis**:
  - ATS compatibility scoring (0-100).
  - Category breakdowns: Impact, Formatting, Brevity, and Keywords (`/api/v1/resumes/{id}/analyze`, `/api/v1/resumes/{id}/analysis`).
  - Actionable bullet-point optimization recommendations.
- **AI Resume Builder & Tailoring**:
  - Automated resume creation from profile data (`/api/v1/resumes/builder/generate`).
  - Multiple saved resume versions (`/api/v1/resumes/builder/saved`).
  - Real-time resume editor with section management (Summary, Work Experience, Education, Projects, Skills, Certifications).
  - 1-Click Job Description Tailoring (`/api/v1/resumes/builder/{id}/tailor`).
  - Professional PDF export engine (`/api/v1/resumes/builder/{id}/export-pdf`).

---

## 5. AI Mock Interview Simulator
- **Interview Configuration**:
  - Role-specific interview setup (Software Engineer, Product Manager, Data Scientist, etc.).
  - Target difficulty selection (Junior, Mid, Senior, Lead).
  - Focus areas (Technical, System Design, Behavioral, Situational).
  - Contextual job description grounding.
- **Live AI Interview Room**:
  - Split-screen HUD with AI interviewer avatar and live user webcam feed.
  - Face detection HUD and eye-contact tracking visual indicators.
  - Speech synthesis / Audio prompt playback for interviewer questions.
  - Real-time speech-to-text / text response answering interface.
  - Interactive question progression with live timer and status indicators.
- **AI Evaluation & Detailed Report**:
  - Multi-dimensional scoring (Communication, Technical Accuracy, Problem Solving, Confidence).
  - Question-by-question breakdown with ideal sample answers.
  - Key strengths and improvement areas summary.
  - Direct CTA to generate targeted roadmaps based on identified weaknesses.
  - Historical interview session tracking and analytics (`/api/v1/interviews/history`, `/api/v1/interviews/{session_id}/report`).

---

## 6. AI Career Assistant & Career Tools
- **Conversational Career Coach**:
  - Multi-turn AI chat grounded in the user's specific career profile and skills (`/api/v1/assistant/chat`).
  - Conversation history persistence (`/api/v1/assistant/conversations`).
  - One-tap quick coach prompts (e.g., "Review my career trajectory", "How to negotiate salary").
- **Cold Outreach Generator**:
  - AI-generated personalized LinkedIn and email outreach to recruiters/hiring managers (`/api/v1/career-tools/recruiter-message`, `/api/v1/outreach/generate`).
  - Customizable tone (Professional, Enthusiastic, Direct).
  - Saved message library (`/api/v1/career-tools/recruiter-messages`).
- **Cover Letter Generator**:
  - Targeted cover letter synthesis matching resume against job descriptions (`/api/v1/career-tools/cover-letter`).
  - History of generated cover letters (`/api/v1/career-tools/cover-letters`).
- **Job Description Analysis (JD Analyzer)**:
  - Deep JD parsing for key requirements, nice-to-haves, and hidden keywords (`/api/v1/career-tools/analyze-jd`).

---

## 7. Job Discovery & Skill Gap Analysis
- **Job Search & Filters**:
  - Search by title, keywords, location, or work mode (Remote, Hybrid, Onsite) (`/api/v1/jobs`).
  - Detailed job listing view (`/api/v1/jobs/{id}`).
- **Match Scoring**:
  - Real-time algorithmic profile-to-job match percentage calculation (`/api/v1/jobs/{id}/match`).
- **Skill Gap Detail**:
  - Comprehensive skill matching breakdown (`/api/v1/jobs/{id}/skill-gaps`).
  - Matched skills vs. missing critical skills list.
  - Direct 1-tap button to launch a tailored learning roadmap or mock interview for the job.

---

## 8. Application Tracker (Kanban Pipeline)
- **Kanban Board & List View**:
  - Multi-stage job application pipeline:
    - **Saved**
    - **Applied**
    - **Interviewing**
    - **Offer**
    - **Rejected**
  - Applications list with status filtering (`/api/v1/applications`).
- **Application Detail & History**:
  - Detailed application tracker with company, role, salary, date applied, and notes (`/api/v1/applications/{id}`).
  - Application status updates, stage progression, and deletion.
  - Attached resume and cover letter references.

---

## 9. Career Profile & Smart Completion
- **Comprehensive Profile Management**:
  - Personal Information (Full name, title, contact, bio, location) (`/api/v1/profile/personal-info`).
  - Profile photo upload and management (`/api/v1/profile/photo`).
  - Education history (Degree, institution, dates, GPA) (`/api/v1/profile/education`).
  - Work experience history (Company, role, dates, bullet points) (`/api/v1/profile/experience`).
  - Project showcase with live links and tech tags (`/api/v1/profile/projects`).
  - Verified skills catalog with proficiency tags (`/api/v1/profile/skills`).
  - Certifications list (`/api/v1/profile/certifications`).
  - Social & Portfolio links (LinkedIn, GitHub, Portfolio) (`/api/v1/profile/social-profiles`).
  - Career preferences (Target titles, expected compensation, work mode) (`/api/v1/profile/preferences`).
- **Smart Completion Hub**:
  - Automated scan of profile gaps (`Screen.SmartCompletion`).
  - AI-assisted autofill suggestions based on uploaded resumes and connected accounts.

---

## 10. Notifications & Real-Time Alerts
- **Notification Center**:
  - Real-time notification feed (`/api/v1/notifications`).
  - Categories: Roadmap milestones, Application status updates, Daily streak reminders, AI interview insights.
  - Mark individual notifications as read (`/api/v1/notifications/{id}/read`).
  - 1-Click "Mark All Read" action (`/api/v1/notifications/mark-all-read`).
  - Direct deeplinking from notifications to relevant screens (e.g., Application Detail, Roadmap Day).

---

## 11. Settings, Security & Integrations
- **App Settings**:
  - Theme switching: Warm White Compose Light theme & Slate Dark theme (`Screen.Settings`).
  - Account security and password modification.
  - Secure user logout with session clearing.
- **Third-Party Integrations**:
  - Connected accounts status hub (`/api/v1/integrations/status`).
  - GitHub integration: Connect account, list repositories, and 1-tap import repo as a portfolio project (`/api/v1/integrations/github/connect`, `/api/v1/integrations/github/repos`, `/api/v1/integrations/github/import-project`).
  - Google and LinkedIn integration connect flows.
  - Integration disconnection management (`/api/v1/integrations/disconnect/{provider}`).

---

## 12. Desktop Client Packaging & System Integration
- **Standalone Native Windows Runtime**:
  - Windows executable (`JobPilot.exe`) powered by WebView2 / Chromium engine.
  - High-performance local embedded SPA server on dynamic loopback port.
  - Native window frame, minimum size constraints (`960x600`), and responsive scaling.
- **Official Inno Setup 6 Installer (`JobPilot-Setup.exe`)**:
  - Standard Windows Setup Wizard with license/information screens.
  - Installation to Program Files (`{autopf}\JobPilot`).
  - Start Menu shortcuts (`{autoprograms}\JobPilot.lnk`).
  - Desktop shortcut (`{autodesktop}\JobPilot.lnk`).
  - Windows Add/Remove Programs registered uninstaller (`unins000.exe`).
  - Multi-resolution Windows application icon (`icon.ico`: 16x16 to 256x256).
