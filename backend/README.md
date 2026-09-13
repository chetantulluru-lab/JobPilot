# 🚀 JobPilot Backend API

Welcome to the **JobPilot Backend**! This is the high-performance, asynchronous REST API and NLP engine powering the JobPilot Android application. It is built with **FastAPI**, **PostgreSQL 17**, **SQLAlchemy 2.0**, **Alembic**, and a deterministic **Resume NLP Engine** (`pypdf`, `python-docx`).

---

## 📋 Table of Contents
1. [Architecture Overview](#architecture-overview)
2. [Resume NLP Pipeline (Phase 3A)](#resume-nlp-pipeline-phase-3a)
3. [Quick Start Guide (Windows)](#quick-start-guide-windows)
4. [Interactive API Documentation](#interactive-api-documentation)
5. [Secrets & Environment Variables (.env)](#secrets--environment-variables-env)
6. [Database Setup & Migrations](#database-setup--migrations)
7. [Connecting from the Android Emulator](#connecting-from-the-android-emulator)
8. [Third-Party Integrations Setup](#third-party-integrations-setup)
9. [Running Automated Tests](#running-automated-tests)
10. [Project Directory Structure](#project-directory-structure)

---

## 🏗 Architecture Overview

```
Android App (Kotlin / Jetpack Compose)
       │
       ▼  HTTP / REST (JSON + JWT via Retrofit)
FastAPI Backend (Uvicorn / Port 8000)
       │
   ┌───┴─────────────────────────────────────────┐
   │                                             │
   ▼                                             ▼
Resume NLP Engine                         PostgreSQL 17 Database
- Text Extractor (pypdf / python-docx)    - Users & Career Profiles
- Normalizer & Section Splitter           - Resumes & Parsed Metadata
- Entity & Skill Extractor (Curated Dict) - Jobs & Semantic Matches
- Missing Information Audit Engine        - Application Tracker & Pipeline
- Review & Confirmation State Machine     - Notifications
```

---

## 🧠 Resume NLP Pipeline (Phase 3A)

The JobPilot Resume NLP pipeline is a **local, deterministic, zero-hallucination** extraction engine. It extracts structured career data directly from uploaded PDF and Word (DOCX) files without requiring external LLM API keys.

### Processing Pipeline:
```
Upload Document (PDF / DOCX)
       │
       ▼
File Validation (MIME type, size, extension)
       │
       ▼
Text Extraction (pypdf / python-docx preserving headings & structure)
       │
       ▼
Text Normalization (whitespace cleanup, unified linebreaks, standardized bullets)
       │
       ▼
Section Segmentation (Summary, Education, Skills, Experience, Projects, Certifications)
       │
       ▼
Entity & Skill Extraction (Curated technical dictionary with token boundaries; date ranges)
       │
       ▼
Missing Fields Audit (pinpoints missing dates, portfolio, social links; calculates completion %)
       │
       ▼
User Review & Edit API (GET & PUT /api/v1/resumes/{id}/extracted-data)
       │
       ▼
Confirmation & Merge (POST /api/v1/resumes/{id}/confirm -> updates PostgreSQL CareerProfile)
```

### Key NLP Principles:
- **Zero Fabrication Policy**: If a project or experience has no dates, the system records `null` and marks it in `missing_fields`. It never invents months or years.
- **Accurate Token Boundaries**: Short programming languages (such as `C`, `R`, `Go`, `SQL`) are matched using symbol-aware word boundaries to prevent false positives on common words like "Computer", "Course", or "Research".
- **Single Source of Truth**: Confirmed resume data and manually entered user data feed the exact same `CareerProfile`.

---

## ⚡ Quick Start Guide (Windows)

All backend dependencies (`FastAPI`, `pypdf`, `python-docx`, `reportlab`, `psycopg`, etc.) are installed in the local virtual environment (`backend/.venv`).

### 1. Open PowerShell and Navigate to the Backend Directory
```powershell
cd C:\Users\chetan\OneDrive\Desktop\JobPilot\backend
```

### 2. Activate the Python Virtual Environment
```powershell
.\.venv\Scripts\Activate.ps1
```

### 3. Start the FastAPI Development Server
```powershell
uvicorn app.main:app --reload --host 0.0.0.0 --port 8000
```

---

## 📖 Interactive API Documentation

Once the server is running, open your browser to explore and test all API endpoints interactively:

- **Swagger UI**: [http://127.0.0.1:8000/docs](http://127.0.0.1:8000/docs)
- **ReDoc UI**: [http://127.0.0.1:8000/redoc](http://127.0.0.1:8000/redoc)
- **Health Check**: [http://127.0.0.1:8000/health](http://127.0.0.1:8000/health)

### New Resume NLP Endpoints:
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/v1/resumes/upload` | Upload PDF/DOCX, run NLP pipeline, save raw text and structured data. |
| `GET` | `/api/v1/resumes/{id}/extracted-data` | Retrieve structured extracted resume data and audit report for review. |
| `PUT` | `/api/v1/resumes/{id}/extracted-data` | Update extracted fields before confirmation (e.g. user fills missing phone). |
| `POST` | `/api/v1/resumes/{id}/confirm` | Confirm reviewed resume data and map it directly into PostgreSQL `CareerProfile`. |
| `GET` | `/api/v1/resumes/audit/missing-fields` | Get real-time profile completeness audit and recommendations. |

---

## 🔐 Secrets & Environment Variables (`.env`)

- **`.env.example`** *(Tracked in Git)*: A template containing only placeholder values and documentation.
- **`.env`** *(Untracked / Ignored in Git)*: Local secrets on your machine (PostgreSQL connection, local JWT secret). Listed in `.gitignore` and never committed.

| Variable | Purpose | Local Default |
| :--- | :--- | :--- |
| `ENVIRONMENT` | Runtime environment (`development` or `production`) | `development` |
| `DATABASE_URL` | PostgreSQL connection string | `postgresql+psycopg://postgres:postgres@localhost:5432/jobpilot` |
| `JWT_SECRET_KEY` | Secret cryptographic key for signing tokens | High-entropy string |
| `JWT_ALGORITHM` | JWT signing algorithm | `HS256` |
| `ACCESS_TOKEN_EXPIRE_MINUTES` | Access token lifespan | `1440` (24 hours) |

---

## 🗄 Database Setup & Migrations

The database used is **PostgreSQL 17** running locally on port `5432` with database `jobpilot`.

```powershell
# Apply all existing migrations to PostgreSQL:
alembic upgrade head

# Generate a new migration after editing models:
alembic revision --autogenerate -m "describe changes here"
```

---

## 📱 Connecting from the Android Emulator

Android emulators map the special IP address **`10.0.2.2`** directly to the host computer's `127.0.0.1`.
The Android app connects to:
```text
http://10.0.2.2:8000/api/v1/
```

If the backend is offline, the Android application automatically falls back to `MockDataProvider`.

---

## 🎯 Intelligent Job Matching Engine (Phase 3B)

The JobPilot Matching Engine evaluates candidate alignment against real job postings using a **deterministic, multi-factor, explainable weighted scoring model**. It provides structured, actionable skill-gap roadmaps with approximate learning timelines without making unverified semantic AI/LLM claims.

### Key Architecture Components:
1. **Skill Normalization & Aliasing (`app/services/matching/skill_normalizer.py`)**:
   - Comprehensive canonical mapping: `Postgres` -> `PostgreSQL`, `ReactJS` -> `React`, `Golang` -> `Go`, `ML` -> `Machine Learning`, `AWS` -> `AWS`, etc.
   - Symbol-aware boundary regex guards (`(?<![A-Za-z0-9_#\+])` and `(?![A-Za-z0-9_#\+])`) preventing false positives on short letter tokens (`C`, `R`, `Go`, `AI`, `ML`).
2. **Requirement Extraction (`app/services/matching/requirement_extractor.py`)**:
   - Classifies job skills into **REQUIRED** vs **PREFERRED** categories.
   - Parses minimum experience years and education degree requirements with text extraction fallbacks.
3. **Candidate Competency Corroboration (`app/services/matching/candidate_profile_builder.py`)**:
   - Evaluates confirmed `CareerProfile` skills.
   - Cross-references skills against project descriptions and work experience to detect *corroborated skills* and isolate *weak/unverified skills*.
4. **Relevance Engines (`app/services/matching/relevance_engine.py`)**:
   - **Experience Matcher**: Quantifies practical tech stack overlap and real project implementation.
   - **Education Matcher**: Recognizes degree level alignment and related fields (e.g. AI/ML, Data Science, IT, Software Engineering for Computer Science postings).
5. **Weighted Scoring with Dominance Penalty (`app/services/matching/scoring_engine.py`)**:
   - **5-Factor Formula**:
     - Required Skills Coverage: **55%**
     - Experience & Projects: **20%**
     - Preferred Skills Coverage: **10%**
     - Profile Completeness: **10%**
     - Education Relevance: **5%**
   - **Dominance Penalty**: Caps final score if core required skill coverage is low (<35% capped at 45; <50% capped at 59) so preferred skills cannot artificially inflate a poor core match.
   - **Match Tiers**: `Excellent Match` (90+), `Strong Match` (75-89), `Moderate Match` (60-74), `Low Match` (40-59), `Weak Match` (<40).
   - Generates transparent, human-readable explanations.
6. **Actionable Skill Gap Engine (`app/services/matching/skill_gap_engine.py`)**:
   - Generates curated learning roadmaps for missing competencies (Docker, Kubernetes, FastAPI, PostgreSQL, AWS, Redis, REST APIs, Unit Testing, etc.) with approximate durations, topic checklists, and practical exercises.
7. **Performance & Caching (`app/services/matching/matching_service.py`)**:
   - Stores computed matches in PostgreSQL with `matcher_version == "3.0-rule"`.
   - Supports on-demand re-computation via `?force_refresh=true`.

---

## 🧪 Running Automated Tests

```powershell
# From the backend directory with virtual environment activated:
pytest -v
```

Expected output:
```text
tests/test_applications.py::test_application_lifecycle PASSED
tests/test_auth.py::test_user_registration PASSED
tests/test_isolation.py::test_user_cannot_access_or_delete_other_users_application PASSED
tests/test_jobs_and_matching.py::test_list_and_filter_jobs PASSED
tests/test_jobs_and_matching.py::test_job_matching_and_skill_gaps PASSED
tests/test_matching_engine.py::TestSkillNormalizer::test_canonical_aliases PASSED
tests/test_matching_engine.py::TestSkillNormalizer::test_symbol_boundary_preservation PASSED
tests/test_matching_engine.py::TestSkillNormalizer::test_extract_skills_from_prose_with_boundary_guards PASSED
tests/test_matching_engine.py::TestRequirementExtractor::test_extract_from_job_with_explicit_fields PASSED
tests/test_matching_engine.py::TestRequirementExtractor::test_extract_preferred_from_requirements_text_fallback PASSED
tests/test_matching_engine.py::TestCandidateProfileBuilder::test_profile_corroboration PASSED
tests/test_matching_engine.py::TestRelevanceEngine::test_experience_matcher PASSED
tests/test_matching_engine.py::TestRelevanceEngine::test_education_matcher_related_fields PASSED
tests/test_matching_engine.py::TestScoringEngine::test_score_tiers PASSED
tests/test_matching_engine.py::TestScoringEngine::test_dominance_penalty_caps_score PASSED
tests/test_matching_engine.py::TestScoringEngine::test_high_match_calculation_and_explanation PASSED
tests/test_matching_engine.py::TestSkillGapEngine::test_curated_skill_roadmaps PASSED
tests/test_matching_engine.py::TestSkillGapEngine::test_dynamic_fallback_for_unknown_skill PASSED
tests/test_matching_engine.py::TestMatchingIntegrationAPI::test_match_api_and_caching PASSED
tests/test_matching_engine.py::TestMatchingIntegrationAPI::test_skill_gaps_api_structure PASSED
tests/test_matching_engine.py::TestMatchingIntegrationAPI::test_user_isolation_matching PASSED
tests/test_nlp_pipeline.py::test_pdf_extraction_valid PASSED
...
53 passed, 2 warnings in 15.07s
```

---

## 📁 Project Directory Structure

```
backend/
├── alembic/              # Database migration scripts & history
├── app/
│   ├── api/v1/           # REST API route handlers
│   │   ├── auth.py       # Login, register, profile
│   │   ├── profile.py    # Education, skills, projects CRUD
│   │   ├── jobs.py       # Live jobs, matching, skill gaps
│   │   ├── applications.py # Pipeline tracking & events
│   │   ├── resumes.py    # Resume upload, review & confirmation
│   │   ├── notifications.py # Notification inbox
│   │   ├── integrations.py  # OAuth placeholders
│   │   └── health.py     # System & database health probe
│   ├── core/             # Configuration, database, security
│   ├── models/           # SQLAlchemy 2.0 ORM database models (20 tables)
│   ├── schemas/          # Pydantic V2 data validation schemas
│   ├── services/         # Business logic
│   │   ├── nlp/          # Resume NLP Engine (Phase 3A)
│   │   │   ├── text_extractor.py   # PDF & DOCX text parser
│   │   │   ├── normalizer.py       # Text cleaning & boundary preservation
│   │   │   ├── section_splitter.py # Resume heading segmentation
│   │   │   ├── entity_extractor.py # Contact, skills, dates, education
│   │   │   ├── skill_dictionary.py # Curated technical skill dictionary
│   │   │   ├── audit_engine.py     # Missing fields & completeness audit
│   │   │   └── parser.py           # ResumeParser orchestrator
│   │   ├── matching/     # Intelligent Job Matching Engine (Phase 3B)
│   │   │   ├── skill_normalizer.py          # Canonical alias resolution & boundary guards
│   │   │   ├── requirement_extractor.py     # Required vs Preferred competency classifier
│   │   │   ├── candidate_profile_builder.py # Profile extraction & skill corroboration
│   │   │   ├── relevance_engine.py          # Experience & related-field education matcher
│   │   │   ├── scoring_engine.py            # 5-factor weighted model & dominance penalty
│   │   │   ├── skill_gap_engine.py          # Structured learning roadmaps
│   │   │   ├── semantic_provider.py         # Vector embedding protocol boundary
│   │   │   └── matching_service.py          # Caching & DB orchestrator
│   │   ├── job_matching_service.py # API facade delegating to matching service
│   │   ├── resume_service.py       # Resume service & profile merge
│   │   └── profile_service.py      # Profile strength calculator
│   └── main.py           # FastAPI application entry point
├── tests/                # Automated pytest test suite (53 tests)
├── .env.example          # Public environment variables template
├── .gitignore            # Security ignore rules
└── requirements.txt      # Python dependencies
```
