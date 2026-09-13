"""
Comprehensive test suite for JobPilot Phase 3A: Real Resume NLP Engine.
Tests:
- PDF extraction (valid, multi-page, empty, corrupted)
- DOCX extraction (valid, headings, paragraphs, tables, empty, corrupted)
- Text normalization
- Section segmentation
- Entity extraction (contact, skills, dates, education, experience, projects, certifications)
- Zero-fabrication date integrity
- Missing fields audit
- User review and edit APIs
- Confirmation and mapping into PostgreSQL Career Profile
- Strict multi-tenant isolation
"""

import io
import pytest
import docx
from reportlab.lib.pagesizes import letter
from reportlab.pdfgen import canvas

from app.services.nlp.text_extractor import (
    TextExtractor, CorruptedFileError, NoExtractableTextError
)
from app.services.nlp.normalizer import TextNormalizer
from app.services.nlp.section_splitter import SectionSplitter
from app.services.nlp.entity_extractor import EntityExtractor
from app.services.nlp.audit_engine import AuditEngine
from app.services.nlp.parser import RuleBasedResumeParser
from app.models.career_profile import CareerProfile


def generate_test_pdf_bytes(multipage: bool = False, empty: bool = False) -> bytes:
    """Generates a realistic test PDF using ReportLab."""
    buffer = io.BytesIO()
    c = canvas.Canvas(buffer, pagesize=letter)

    if not empty:
        c.drawString(100, 750, "Chetan Sharma")
        c.drawString(100, 735, "chetan.sharma@example.com | +1 555-019-2834 | San Francisco, CA")
        c.drawString(100, 720, "https://github.com/chetansharma | https://linkedin.com/in/chetansharma")
        
        c.drawString(100, 690, "PROFESSIONAL SUMMARY")
        c.drawString(100, 675, "Experienced backend engineer specializing in high-throughput APIs and distributed systems.")
        
        c.drawString(100, 645, "TECHNICAL SKILLS")
        c.drawString(100, 630, "Languages: Python, Java, TypeScript, C, SQL")
        c.drawString(100, 615, "Frameworks: FastAPI, Django, React, Node.js")
        c.drawString(100, 600, "Databases & Cloud: PostgreSQL, Redis, Docker, Kubernetes, AWS")

        c.drawString(100, 570, "EDUCATION")
        c.drawString(100, 555, "University of California, Berkeley")
        c.drawString(100, 540, "Bachelor of Science in Computer Science | 2020 - 2024 | 3.8 GPA")

        c.drawString(100, 510, "EXPERIENCE")
        c.drawString(100, 495, "Senior Backend Engineer at CloudScale Technologies | Jan 2024 - Present")
        c.drawString(100, 480, "- Architected asynchronous microservices using FastAPI and PostgreSQL.")
        c.drawString(100, 465, "- Reduced database query latency by 45% using Redis caching.")

        c.drawString(100, 435, "PROJECTS")
        c.drawString(100, 420, "JobPilot AI | Python, FastAPI, Docker")
        c.drawString(100, 405, "https://github.com/chetansharma/jobpilot")
        c.drawString(100, 390, "- Built production backend with 20 PostgreSQL tables and JWT authentication.")

        c.drawString(100, 360, "CERTIFICATIONS")
        c.drawString(100, 345, "AWS Certified Solutions Architect | AWS | 2024")

        if multipage:
            c.showPage()
            c.drawString(100, 750, "ADDITIONAL ACHIEVEMENTS")
            c.drawString(100, 735, "- 1st Place at National Hackathon 2023.")
            c.drawString(100, 720, "- Published paper on Automated API Testing in 2024.")

    c.save()
    buffer.seek(0)
    return buffer.getvalue()


def generate_test_docx_bytes(empty: bool = False) -> bytes:
    """Generates a realistic test DOCX using python-docx."""
    doc = docx.Document()
    if not empty:
        doc.add_heading("Alex Rivera", level=1)
        doc.add_paragraph("alex.rivera@example.com | (415) 555-9876 | Austin, TX")
        doc.add_paragraph("https://github.com/alexrivera | https://linkedin.com/in/alexrivera")

        doc.add_heading("Skills", level=2)
        doc.add_paragraph("Python, Go, PostgreSQL, Docker, Kubernetes, AWS, Machine Learning")

        doc.add_heading("Education", level=2)
        doc.add_paragraph("University of Texas at Austin")
        doc.add_paragraph("B.Tech in Computer Science | 2019 - 2023 | 8.8 CGPA")

        doc.add_heading("Experience", level=2)
        doc.add_paragraph("Software Engineer at DataTech Solutions | 06/2023 - 08/2025")
        doc.add_paragraph("• Developed real-time streaming data pipelines with Kafka and Python.")

        doc.add_heading("Projects", level=2)
        doc.add_paragraph("Smart Career Cockpit")
        doc.add_paragraph("• Automated job tracking and NLP matching engine.")

    buffer = io.BytesIO()
    doc.save(buffer)
    buffer.seek(0)
    return buffer.getvalue()


# ---------------------------------------------------------------------------
# 1. File Extraction Tests
# ---------------------------------------------------------------------------

def test_pdf_extraction_valid():
    pdf_bytes = generate_test_pdf_bytes(multipage=False)
    text = TextExtractor.extract_from_pdf(io.BytesIO(pdf_bytes))
    assert "Chetan Sharma" in text
    assert "chetan.sharma@example.com" in text
    assert "FastAPI" in text
    assert "CloudScale Technologies" in text


def test_pdf_extraction_multipage():
    pdf_bytes = generate_test_pdf_bytes(multipage=True)
    text = TextExtractor.extract_from_pdf(io.BytesIO(pdf_bytes))
    assert "Chetan Sharma" in text
    # Check that content from page 2 is present
    assert "National Hackathon 2023" in text


def test_pdf_extraction_empty():
    pdf_bytes = generate_test_pdf_bytes(empty=True)
    with pytest.raises(NoExtractableTextError):
        TextExtractor.extract_from_pdf(io.BytesIO(pdf_bytes))


def test_pdf_extraction_corrupted():
    corrupted_bytes = b"%PDF-1.4 completely broken binary junk \x00\xff\xee"
    with pytest.raises(CorruptedFileError):
        TextExtractor.extract_from_pdf(io.BytesIO(corrupted_bytes))


def test_docx_extraction_valid():
    docx_bytes = generate_test_docx_bytes(empty=False)
    text = TextExtractor.extract_from_docx(docx_bytes)
    assert "Alex Rivera" in text
    assert "alex.rivera@example.com" in text
    assert "University of Texas at Austin" in text


def test_docx_extraction_empty():
    docx_bytes = generate_test_docx_bytes(empty=True)
    with pytest.raises(NoExtractableTextError):
        TextExtractor.extract_from_docx(docx_bytes)


def test_docx_extraction_corrupted():
    corrupted_bytes = b"PK\x03\x04 corrupt zip archive not word"
    with pytest.raises(CorruptedFileError):
        TextExtractor.extract_from_docx(corrupted_bytes)


# ---------------------------------------------------------------------------
# 2. Text Normalization & Section Detection Tests
# ---------------------------------------------------------------------------

def test_text_normalizer():
    raw_sample = "Line 1\r\n\r\n\r\n\r\nLine 2\twith\xa0tabs\n* Bullet 1\n▪ Bullet 2\n● Bullet 3"
    normalized = TextNormalizer.normalize(raw_sample)
    assert "\r" not in normalized
    assert "\xa0" not in normalized
    assert "\n\n\n" not in normalized  # collapsed
    assert "• Bullet 1" in normalized
    assert "• Bullet 2" in normalized
    assert "• Bullet 3" in normalized


def test_section_splitter():
    sample_text = (
        "John Doe\njohn@example.com\n\n"
        "PROFESSIONAL SUMMARY\nExperienced developer.\n\n"
        "EDUCATION\nMIT\nB.S. in Computer Science\n\n"
        "TECHNICAL SKILLS\nPython, Docker, AWS\n\n"
        "EXPERIENCE\nSoftware Engineer at Acme\n\n"
        "PROJECTS\nProject Alpha | React\n\n"
        "CERTIFICATIONS\nAWS Certified Developer"
    )
    sections = SectionSplitter.split_into_sections(sample_text)
    assert "John Doe" in sections["HEADER"]
    assert "Experienced developer" in sections["SUMMARY"]
    assert "MIT" in sections["EDUCATION"]
    assert "Python, Docker, AWS" in sections["SKILLS"]
    assert "Acme" in sections["EXPERIENCE"]
    assert "Project Alpha" in sections["PROJECTS"]
    assert "AWS Certified Developer" in sections["CERTIFICATIONS"]


# ---------------------------------------------------------------------------
# 3. Entity & Skill Extraction Tests (Accuracy & Non-Fabrication)
# ---------------------------------------------------------------------------

def test_entity_extraction_contact():
    header = "Sarah Connor\nsarah@techfuture.io | +1 (555) 234-5678 | Seattle, WA"
    full_text = f"{header}\nhttps://github.com/sarahconnor\nhttps://linkedin.com/in/sarahconnor"
    contact = EntityExtractor.extract_contact_info(header, full_text)

    assert contact["name"] == "Sarah Connor"
    assert contact["email"] == "sarah@techfuture.io"
    assert "555" in contact["phone"]
    assert "Seattle" in contact["location"]
    assert contact["social_profiles"]["github"] == "https://github.com/sarahconnor"
    assert contact["social_profiles"]["linkedin"] == "https://linkedin.com/in/sarahconnor"


def test_skill_extraction_accuracy_and_false_positives():
    # Test that 'C' is matched when explicitly listed in programming languages,
    # but NOT when merely part of words like 'Computer', 'Curriculum', 'Core'.
    text_without_c_skill = (
        "Computer Science Graduate with Coursework in Core Algorithms and Communication."
    )
    skills1 = EntityExtractor.extract_skills(text_without_c_skill, "")
    skill_names1 = [s["name"] for s in skills1]
    assert "C" not in skill_names1, "Single letter 'C' should not match inside words like Computer/Core"

    text_with_c_skill = (
        "Languages: C, C++, Python, Java, Go, Rust\n"
        "Frameworks: FastAPI, React\n"
        "Databases: PostgreSQL, MongoDB\n"
        "Tools: Docker, Kubernetes, Git, Linux"
    )
    skills2 = EntityExtractor.extract_skills(text_with_c_skill, "Languages: C, C++, Python")
    skill_names2 = {s["name"] for s in skills2}
    assert "C" in skill_names2
    assert "C++" in skill_names2
    assert "Python" in skill_names2
    assert "Go" in skill_names2
    assert "FastAPI" in skill_names2
    assert "PostgreSQL" in skill_names2
    assert "Docker" in skill_names2


def test_date_extraction_zero_fabrication():
    # Test ongoing experience
    ongoing_text = "Senior Engineer at Acme Corp | Jan 2024 - Present"
    start, end, is_current, precision = EntityExtractor.parse_date_range(ongoing_text)
    assert start == "Jan 2024"
    assert end == "Present"
    assert is_current is True

    # Test year-only date
    year_text = "Software Intern | 2023 - 2024"
    start, end, is_current, precision = EntityExtractor.parse_date_range(year_text)
    assert start == "2023"
    assert end == "2024"
    assert is_current is False
    assert precision == "year"

    # Test missing dates in project: must NOT fabricate
    proj_text = "JobPilot Web App\nBuilt responsive user interface using Tailwind CSS."
    projects = EntityExtractor.extract_projects(proj_text)
    assert len(projects) == 1
    assert projects[0]["start_date"] is None
    assert projects[0]["end_date"] is None, "Dates must not be invented if not present in resume"


def test_audit_engine_missing_fields():
    structured_sample = {
        "personal_info": {
            "name": "Jane Doe",
            "email": "jane@example.com",
            "phone": None,
            "location": "New York, NY",
            "social_profiles": {"github": "https://github.com/janedoe", "linkedin": None, "portfolio": None}
        },
        "education": [{"institution": "NYU", "degree": "B.S.", "end_year": 2024, "grade": None}],
        "skills": [{"name": "Python", "category": "Programming Languages"}, {"name": "SQL", "category": "Databases"}],
        "experience": [],
        "projects": [{"name": "Web Crawler", "start_date": None, "github_url": None}],
        "certifications": []
    }
    audit = AuditEngine.audit(structured_sample)
    assert "personal_info.phone" in audit["missing_fields"]
    assert "social_profiles.linkedin" in audit["missing_fields"]
    assert "social_profiles.portfolio" in audit["missing_fields"]
    assert "projects.0.start_date" in audit["missing_fields"]
    assert "education.0.grade" in audit["missing_fields"]
    assert 0 < audit["completion_percentage"] < 100


# ---------------------------------------------------------------------------
# 4. End-to-End API Tests (Upload, Review, Edit, Confirm, Isolation)
# ---------------------------------------------------------------------------

def test_resume_nlp_upload_and_review_flow(client, user_a_headers):
    # 1. Upload valid PDF
    pdf_content = generate_test_pdf_bytes(multipage=False)
    files = {"file": ("test_resume.pdf", io.BytesIO(pdf_content), "application/pdf")}
    data = {"title": "Full Stack Engineer Resume"}

    upload_res = client.post("/api/v1/resumes/upload", files=files, data=data, headers=user_a_headers)
    assert upload_res.status_code == 201
    res_data = upload_res.json()
    resume_id = res_data["id"]
    assert res_data["extraction_status"] == "EXTRACTED"
    assert res_data["completion_percentage"] > 50

    # 2. Review extracted data via GET /resumes/{id}/extracted-data
    review_res = client.get(f"/api/v1/resumes/{resume_id}/extracted-data", headers=user_a_headers)
    assert review_res.status_code == 200
    review_data = review_res.json()
    assert review_data["resume_id"] == resume_id
    assert review_data["structured_data"]["personal_info"]["name"] == "Chetan Sharma"
    assert review_data["structured_data"]["personal_info"]["email"] == "chetan.sharma@example.com"
    assert len(review_data["structured_data"]["skills"]) > 0
    assert len(review_data["structured_data"]["education"]) > 0
    assert len(review_data["structured_data"]["experience"]) > 0
    assert "audit" in review_data

    # 3. Edit extracted data via PUT /resumes/{id}/extracted-data (e.g. user fills missing phone or edits date)
    updated_payload = review_data["structured_data"]
    updated_payload["personal_info"]["phone"] = "+1 (555) 999-0000"
    if updated_payload["projects"]:
        updated_payload["projects"][0]["start_date"] = "Jan 2024"
        updated_payload["projects"][0]["end_date"] = "Mar 2024"

    put_res = client.put(f"/api/v1/resumes/{resume_id}/extracted-data", json=updated_payload, headers=user_a_headers)
    assert put_res.status_code == 200
    put_data = put_res.json()
    assert put_data["extraction_status"] == "REVIEWED"
    assert put_data["structured_data"]["personal_info"]["phone"] == "+1 (555) 999-0000"

    # 4. Confirm extracted data into PostgreSQL Career Profile
    confirm_res = client.post(f"/api/v1/resumes/{resume_id}/confirm", headers=user_a_headers)
    assert confirm_res.status_code == 200
    confirm_data = confirm_res.json()
    assert confirm_data["status"] == "success"
    assert confirm_data["profile_strength"] > 50

    # 5. Verify Career Profile in DB now has the confirmed resume information
    profile_res = client.get("/api/v1/profile", headers=user_a_headers)
    assert profile_res.status_code == 200
    p_data = profile_res.json()
    assert len(p_data["skills"]) > 0
    assert len(p_data["education"]) > 0


def test_resume_nlp_multi_tenant_isolation(client, user_a_headers, user_b_headers):
    # Upload resume as User A
    pdf_content = generate_test_pdf_bytes()
    files = {"file": ("user_a_cv.pdf", io.BytesIO(pdf_content), "application/pdf")}
    upload_res = client.post("/api/v1/resumes/upload", files=files, headers=user_a_headers)
    assert upload_res.status_code == 201
    resume_id = upload_res.json()["id"]

    # User B attempts to access User A's extracted data -> must be 404
    forbidden_get = client.get(f"/api/v1/resumes/{resume_id}/extracted-data", headers=user_b_headers)
    assert forbidden_get.status_code == 404

    # User B attempts to edit User A's extracted data -> must be 404
    forbidden_put = client.put(
        f"/api/v1/resumes/{resume_id}/extracted-data",
        json={"personal_info": {"name": "Hacker"}},
        headers=user_b_headers
    )
    assert forbidden_put.status_code == 404

    # User B attempts to confirm User A's resume -> must be 404
    forbidden_confirm = client.post(f"/api/v1/resumes/{resume_id}/confirm", headers=user_b_headers)
    assert forbidden_confirm.status_code == 404


def test_resume_upload_corrupted_pdf_returns_400(client, user_a_headers):
    corrupted_bytes = b"%PDF-1.4 broken binary bytes \x00\x01\x02"
    files = {"file": ("corrupt.pdf", io.BytesIO(corrupted_bytes), "application/pdf")}
    response = client.post("/api/v1/resumes/upload", files=files, headers=user_a_headers)
    assert response.status_code == 400
    assert "corrupted" in response.json()["detail"].lower() or "unreadable" in response.json()["detail"].lower()


def test_resume_upload_empty_document_returns_400(client, user_a_headers):
    empty_bytes = b""
    files = {"file": ("empty.pdf", io.BytesIO(empty_bytes), "application/pdf")}
    response = client.post("/api/v1/resumes/upload", files=files, headers=user_a_headers)
    assert response.status_code == 400
    assert "empty" in response.json()["detail"].lower()
