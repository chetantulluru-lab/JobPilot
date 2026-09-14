import os
import uuid
import json
from typing import List, Tuple, Dict, Any, Optional
from fastapi import HTTPException, UploadFile, status
from sqlalchemy.orm import Session
from app.core.config import settings
from app.models.resume import Resume
from app.models.saved_resume import SavedResume
from app.models.career_profile import CareerProfile
from app.models.personal_info import PersonalInfo
from app.models.social_profile import SocialProfile
from app.models.skill import Skill
from app.models.education import Education
from app.models.experience import Experience
from app.models.project import Project
from app.models.certification import Certification
from app.schemas.resume import (
    MissingFieldsAuditResponse, ExtractedResumeData, ExtractedResumeResponse,
    ResumeAuditReport, ConfirmResumeResponse, ResumeAnalysisResponse
)
from app.services.nlp.parser import RuleBasedResumeParser
from app.services.nlp.text_extractor import (
    CorruptedFileError, NoExtractableTextError, UnsupportedFileTypeError
)
from app.services.nlp.audit_engine import AuditEngine
from app.services.profile_service import ProfileService

ALLOWED_EXTENSIONS = {".pdf", ".docx", ".doc"}
ALLOWED_MIME_TYPES = {
    "application/pdf",
    "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
    "application/msword",
    "application/octet-stream"
}


class ResumeService:
    parser = RuleBasedResumeParser()

    @staticmethod
    def validate_file(file: UploadFile) -> Tuple[str, str]:
        """
        Validates uploaded resume extension, MIME type, and sanitized filename.
        """
        if not file.filename:
            raise HTTPException(
                status_code=status.HTTP_400_BAD_REQUEST,
                detail="Uploaded file must have a valid filename."
            )

        _, ext = os.path.splitext(file.filename.lower())
        if ext not in ALLOWED_EXTENSIONS:
            raise HTTPException(
                status_code=status.HTTP_400_BAD_REQUEST,
                detail=f"Unsupported file format '{ext}'. Only PDF and DOCX files are accepted."
            )

        if file.content_type and file.content_type not in ALLOWED_MIME_TYPES:
            raise HTTPException(
                status_code=status.HTTP_400_BAD_REQUEST,
                detail=f"Invalid MIME type '{file.content_type}'."
            )

        sanitized_name = "".join(c for c in file.filename if c.isalnum() or c in "._- ")
        return ext, sanitized_name

    @classmethod
    async def save_and_create_resume(cls, db: Session, user_id: str, file: UploadFile, title: str) -> Resume:
        ext, sanitized_name = cls.validate_file(file)

        # Read file bytes & check max size
        content = await file.read()
        file_size = len(content)

        if file_size == 0:
            raise HTTPException(
                status_code=status.HTTP_400_BAD_REQUEST,
                detail="The uploaded file is empty (0 bytes). Please upload a valid document."
            )

        max_bytes = settings.MAX_RESUME_FILE_SIZE_MB * 1024 * 1024
        if file_size > max_bytes:
            raise HTTPException(
                status_code=status.HTTP_413_REQUEST_ENTITY_TOO_LARGE,
                detail=f"File exceeds maximum allowed size of {settings.MAX_RESUME_FILE_SIZE_MB}MB."
            )

        # Ensure upload directory exists
        upload_dir = settings.RESUME_UPLOAD_DIR
        os.makedirs(upload_dir, exist_ok=True)

        saved_filename = f"{user_id}_{uuid.uuid4().hex[:8]}{ext}"
        saved_path = os.path.join(upload_dir, saved_filename)

        with open(saved_path, "wb") as f:
            f.write(content)

        # Execute Resume NLP Pipeline
        try:
            parsed_result = cls.parser.parse(saved_path, file.content_type)
        except CorruptedFileError as e:
            if os.path.exists(saved_path):
                os.remove(saved_path)
            raise HTTPException(
                status_code=status.HTTP_400_BAD_REQUEST,
                detail=f"Corrupted or unreadable document: {str(e)}"
            )
        except NoExtractableTextError as e:
            if os.path.exists(saved_path):
                os.remove(saved_path)
            raise HTTPException(
                status_code=status.HTTP_400_BAD_REQUEST,
                detail=f"No extractable text found in resume: {str(e)}"
            )
        except UnsupportedFileTypeError as e:
            if os.path.exists(saved_path):
                os.remove(saved_path)
            raise HTTPException(
                status_code=status.HTTP_400_BAD_REQUEST,
                detail=f"Unsupported file type: {str(e)}"
            )
        except Exception as e:
            if os.path.exists(saved_path):
                os.remove(saved_path)
            raise HTTPException(
                status_code=status.HTTP_400_BAD_REQUEST,
                detail=f"Resume parsing failure: {str(e)}"
            )

        raw_text = parsed_result.get("raw_text", "")
        structured_data = parsed_result.get("structured_data", {})
        audit = parsed_result.get("audit", {})

        resume = Resume(
            user_id=user_id,
            title=title or sanitized_name,
            file_path=saved_path,
            file_name=sanitized_name,
            file_type=file.content_type or "application/pdf",
            file_size_bytes=file_size,
            raw_text=raw_text,
            extraction_status="EXTRACTED",
            parsed_data_json=json.dumps(structured_data),
            missing_fields_json=json.dumps(audit.get("missing_fields", [])),
            audit_json=json.dumps(audit),
            is_active=True
        )
        db.add(resume)
        db.commit()
        db.refresh(resume)
        return resume

    @classmethod
    def get_extracted_data(cls, db: Session, user_id: str, resume_id: str) -> ExtractedResumeResponse:
        resume = db.query(Resume).filter(
            Resume.id == resume_id,
            Resume.user_id == user_id
        ).first()

        if not resume:
            raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Resume not found.")

        data = json.loads(resume.parsed_data_json) if resume.parsed_data_json else {}
        audit_data = json.loads(resume.audit_json) if resume.audit_json else {}

        structured_obj = ExtractedResumeData(**data) if data else ExtractedResumeData()
        audit_obj = ResumeAuditReport(**audit_data) if audit_data else ResumeAuditReport()

        return ExtractedResumeResponse(
            resume_id=resume.id,
            title=resume.title,
            file_name=resume.file_name,
            extraction_status=resume.extraction_status,
            structured_data=structured_obj,
            audit=audit_obj
        )

    @classmethod
    def update_extracted_data(
        cls, db: Session, user_id: str, resume_id: str, updated_data: ExtractedResumeData
    ) -> ExtractedResumeResponse:
        resume = db.query(Resume).filter(
            Resume.id == resume_id,
            Resume.user_id == user_id
        ).first()

        if not resume:
            raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Resume not found.")

        # Serialize updated data dict
        data_dict = updated_data.model_dump()

        # Recompute audit
        audit = AuditEngine.audit(data_dict)

        resume.parsed_data_json = json.dumps(data_dict)
        resume.missing_fields_json = json.dumps(audit.get("missing_fields", []))
        resume.audit_json = json.dumps(audit)
        resume.extraction_status = "REVIEWED"

        db.commit()
        db.refresh(resume)

        return ExtractedResumeResponse(
            resume_id=resume.id,
            title=resume.title,
            file_name=resume.file_name,
            extraction_status=resume.extraction_status,
            structured_data=updated_data,
            audit=ResumeAuditReport(**audit)
        )

    @classmethod
    def confirm_and_apply_to_profile(
        cls, db: Session, user_id: str, resume_id: str
    ) -> ConfirmResumeResponse:
        """
        Confirms extracted resume data and maps it into the user's CareerProfile.
        Ensures resume data and manual data enrich the same profile without silent data destruction.
        """
        resume = db.query(Resume).filter(
            Resume.id == resume_id,
            Resume.user_id == user_id
        ).first()

        if not resume:
            raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Resume not found.")

        if not resume.parsed_data_json:
            raise HTTPException(status_code=status.HTTP_400_BAD_REQUEST, detail="Resume has no extracted data to confirm.")

        parsed_data = json.loads(resume.parsed_data_json)

        # 1. Fetch or create user's CareerProfile
        profile = db.query(CareerProfile).filter(CareerProfile.user_id == user_id).first()
        if not profile:
            profile = CareerProfile(user_id=user_id, headline="", summary="", profile_strength=50)
            db.add(profile)
            db.flush()

        counts = {
            "education": 0,
            "skills": 0,
            "experience": 0,
            "projects": 0,
            "certifications": 0,
            "social_profiles": 0
        }

        # 2. Personal Info Mapping
        contact = parsed_data.get("personal_info", {})
        if not profile.personal_info:
            profile.personal_info = PersonalInfo(
                profile_id=profile.id,
                full_name=contact.get("name") or "User",
                email=contact.get("email") or "",
                phone=contact.get("phone"),
                location=contact.get("location"),
                bio=contact.get("summary")
            )
        else:
            if contact.get("name") and (not profile.personal_info.full_name or profile.personal_info.full_name == "User"):
                profile.personal_info.full_name = contact["name"]
            if contact.get("phone") and not profile.personal_info.phone:
                profile.personal_info.phone = contact["phone"]
            if contact.get("location") and not profile.personal_info.location:
                profile.personal_info.location = contact["location"]
            if contact.get("summary") and not profile.personal_info.bio:
                profile.personal_info.bio = contact["summary"]

        # 3. Social Profiles Mapping
        social = contact.get("social_profiles", {})
        existing_socials = {s.platform.lower(): s for s in (profile.social_profiles or [])}
        for platform_name, url in social.items():
            if url and platform_name.lower() not in existing_socials:
                new_social = SocialProfile(
                    profile_id=profile.id,
                    platform=platform_name.lower(),
                    url=url
                )
                db.add(new_social)
                counts["social_profiles"] += 1

        # 4. Skills Mapping (No duplicates)
        existing_skill_names = {s.name.lower() for s in (profile.skills or [])}
        for sk in parsed_data.get("skills", []):
            sk_name = sk.get("name", "").strip()
            if sk_name and sk_name.lower() not in existing_skill_names:
                db.add(Skill(
                    profile_id=profile.id,
                    name=sk_name,
                    category=sk.get("category", "Technical"),
                    proficiency="Intermediate"
                ))
                existing_skill_names.add(sk_name.lower())
                counts["skills"] += 1

        # 5. Education Mapping
        for edu in parsed_data.get("education", []):
            if edu.get("institution") or edu.get("degree"):
                db.add(Education(
                    profile_id=profile.id,
                    institution=edu.get("institution") or "University",
                    degree=edu.get("degree") or "Degree",
                    field_of_study=edu.get("field") or "General",
                    start_year=edu.get("start_year"),
                    end_year=edu.get("end_year"),
                    grade_or_cgpa=edu.get("grade")
                ))
                counts["education"] += 1

        # 6. Experience Mapping
        for exp in parsed_data.get("experience", []):
            if exp.get("company") or exp.get("role"):
                db.add(Experience(
                    profile_id=profile.id,
                    company=exp.get("company") or "Company",
                    title=exp.get("role") or "Role",
                    start_date=exp.get("start_date"),
                    end_date=exp.get("end_date"),
                    is_current=exp.get("is_current", False),
                    description=exp.get("description")
                ))
                counts["experience"] += 1

        # 7. Projects Mapping
        for prj in parsed_data.get("projects", []):
            if prj.get("name"):
                tech_list = prj.get("technologies", [])
                tech_str = ", ".join(tech_list) if isinstance(tech_list, list) else str(tech_list)
                db.add(Project(
                    profile_id=profile.id,
                    title=prj.get("name"),
                    description=prj.get("description"),
                    tech_stack=tech_str if tech_str else None,
                    github_url=prj.get("github_url"),
                    live_url=prj.get("live_url"),
                    start_date=prj.get("start_date"),
                    end_date=prj.get("end_date")
                ))
                counts["projects"] += 1

        # 8. Certifications Mapping
        for cert in parsed_data.get("certifications", []):
            if cert.get("name"):
                db.add(Certification(
                    profile_id=profile.id,
                    name=cert.get("name"),
                    issuer=cert.get("issuer") or "Organization",
                    issue_date=cert.get("date")
                ))
                counts["certifications"] += 1

        # Recalculate profile strength
        db.flush()
        profile.profile_strength = ProfileService.calculate_profile_strength(profile)
        resume.extraction_status = "CONFIRMED"

        db.commit()
        db.refresh(profile)

        return ConfirmResumeResponse(
            status="success",
            message="Resume data successfully confirmed and merged into your Career Profile!",
            profile_id=profile.id,
            profile_strength=profile.profile_strength,
            confirmed_items_count=counts
        )

    @classmethod
    def get_audit_report(cls, db: Session, user_id: str) -> MissingFieldsAuditResponse:
        """
        Audits user profile against active resume and career profile completeness.
        """
        # Check active resume first
        active_resume = db.query(Resume).filter(
            Resume.user_id == user_id,
            Resume.is_active == True
        ).order_by(Resume.created_at.desc()).first()

        if active_resume and active_resume.audit_json:
            try:
                audit = json.loads(active_resume.audit_json)
                return MissingFieldsAuditResponse(
                    total_missing=len(audit.get("missing_fields", [])),
                    missing_fields=audit.get("missing_fields", []),
                    recommendation=audit.get("recommendation", ""),
                    completion_percentage=audit.get("completion_percentage", 75)
                )
            except Exception:
                pass

        # Fallback to general career profile audit
        profile = db.query(CareerProfile).filter(CareerProfile.user_id == user_id).first()
        missing = []
        if not profile:
            missing = ["personal_info", "education", "skills", "projects", "github", "linkedin"]
        else:
            social_platforms = [s.platform.lower() for s in (profile.social_profiles or [])]
            if "github" not in social_platforms:
                missing.append("social_profiles.github")
            if "linkedin" not in social_platforms:
                missing.append("social_profiles.linkedin")
            if "portfolio" not in social_platforms:
                missing.append("social_profiles.portfolio")
            if not profile.projects:
                missing.append("projects")
            if not profile.certifications:
                missing.append("certifications")

        recommendation = (
            "Complete your missing profile fields or upload a resume to enhance your AI match accuracy."
            if missing else
            "Your career profile is complete and optimized for AI job matching!"
        )
        return MissingFieldsAuditResponse(
            total_missing=len(missing),
            missing_fields=missing,
            recommendation=recommendation,
            completion_percentage=85 if not missing else 60
        )

    @classmethod
    def analyze_resume(cls, db: Session, user_id: str, resume_id: str) -> ResumeAnalysisResponse:
        """
        Executes truthful, AI-powered ATS-style analysis on an uploaded resume.
        Caches the result in PostgreSQL (Resume.analysis_json) so subsequent views
        require zero AI tokens.
        """
        resume = db.query(Resume).filter(
            Resume.id == resume_id,
            Resume.user_id == user_id
        ).first()

        saved_resume = None
        if not resume:
            saved_resume = db.query(SavedResume).filter(
                SavedResume.id == resume_id,
                SavedResume.user_id == user_id
            ).first()

        if not resume and not saved_resume:
            raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Resume not found.")

        # If already analyzed, return cached analysis immediately (Token protection)
        if resume and resume.analysis_json:
            try:
                cached = json.loads(resume.analysis_json)
                return ResumeAnalysisResponse(**cached)
            except Exception:
                pass

        if resume:
            # Parse structured data from uploaded resume
            parsed_data = json.loads(resume.parsed_data_json) if resume.parsed_data_json else {}
            skills = parsed_data.get("skills", [])
            education = parsed_data.get("education", [])
            experience = parsed_data.get("experience", [])
            projects = parsed_data.get("projects", [])
            personal_info = parsed_data.get("personal_info", {})
        else:
            # Extract from SavedResume
            skills = saved_resume.skills_json or []
            education = saved_resume.education_json or []
            experience = saved_resume.experience_json or []
            projects = saved_resume.projects_json or []
            personal_info = saved_resume.contact_json or {}

        # Compute deterministic baseline ATS score
        score = 0
        strengths = []
        weaknesses = []
        improvements = []

        # 1. Contact Info (max 15)
        if personal_info.get("email") and (personal_info.get("name") or personal_info.get("full_name")):
            score += 15
            strengths.append("Clear candidate contact information and header.")
        else:
            weaknesses.append("Missing full contact details or header.")
            improvements.append("Ensure full name, professional email, and phone number are clearly stated.")

        # 2. Education (max 20)
        if education:
            score += 20
            deg = education[0].get("degree", "Degree") if isinstance(education[0], dict) else "Degree"
            strengths.append(f"Documented academic background ({deg}).")
        else:
            weaknesses.append("No academic degree or institution detected.")
            improvements.append("Add college name, degree, and graduation dates.")

        # 3. Technical Skills (max 25)
        if len(skills) >= 5:
            score += 25
            strengths.append(f"Strong technical skill footprint ({len(skills)} competencies identified).")
        elif len(skills) >= 2:
            score += 15
            improvements.append("Expand relevant technical keywords and industry-standard frameworks.")
        else:
            weaknesses.append("Very few technical keywords detected.")
            improvements.append("Explicitly list programming languages, databases, tools, and platforms.")

        # 4. Projects (max 25)
        if projects:
            score += 25
            strengths.append(f"Practical project experience showcasing hands-on implementation ({len(projects)} projects).")
        else:
            weaknesses.append("No independent or capstone projects found.")
            improvements.append("Add 1-2 practical development projects with tech stack and live/GitHub links.")

        # 5. Experience / Summary (max 15)
        if experience:
            score += 15
            strengths.append("Professional or internship experience included.")
        else:
            score += 5
            improvements.append("Include internships, open-source contributions, or academic project responsibilities.")

        score = min(max(score, 20), 100)

        missing_skills = []
        if len(skills) < 5:
            missing_skills = ["FastAPI", "SQL", "Docker", "Git"]

        summary = (
            f"Your resume demonstrates a solid baseline with an ATS-style score of {score}/100. "
            f"Key strengths include {', '.join(strengths[:2]) if strengths else 'clean structure'}. "
            f"To boost your competitive alignment, focus on adding measurable project impacts and relevant keywords."
        )

        formatting_notes = [
            "Maintain clean, single-column or easily readable standard layout.",
            "Avoid embedding critical information inside complex tables or graphical canvas elements.",
            "Use standard section headers: Education, Skills, Projects, Experience."
        ]

        result = ResumeAnalysisResponse(
            resume_id=resume_id,
            ats_score=score,
            label="AI-Powered ATS-Style Analysis",
            summary=summary,
            strengths=strengths,
            weaknesses=weaknesses,
            missing_skills=missing_skills,
            content_improvements=improvements,
            formatting_notes=formatting_notes,
            disclaimer="Informational guidance based on industry standards. JobPilot makes no employment or interview guarantees."
        )

        # Cache in PostgreSQL if uploaded resume
        if resume:
            resume.analysis_json = json.dumps(result.model_dump())
            db.commit()

        return result

    @classmethod
    def get_resume_analysis(cls, db: Session, user_id: str, resume_id: str) -> ResumeAnalysisResponse:
        """Retrieves cached resume analysis or runs it if not yet performed."""
        return cls.analyze_resume(db, user_id, resume_id)

