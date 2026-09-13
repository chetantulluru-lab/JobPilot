"""
Resume Parser orchestrator for JobPilot NLP engine.
Implements the ResumeParser interface with RuleBasedResumeParser, separating parsing
logic from API routers and allowing seamless future integration of LLM-assisted parsers.
"""

from abc import ABC, abstractmethod
from typing import Dict, Any, Optional
from app.services.nlp.text_extractor import TextExtractor
from app.services.nlp.normalizer import TextNormalizer
from app.services.nlp.section_splitter import SectionSplitter
from app.services.nlp.entity_extractor import EntityExtractor
from app.services.nlp.audit_engine import AuditEngine


class ResumeParser(ABC):
    """Abstract interface for resume parsing engines."""

    @abstractmethod
    def parse(self, file_path: str, file_type: Optional[str] = None) -> Dict[str, Any]:
        """Parses a resume file and returns normalized, structured, and audited career data."""
        pass


class RuleBasedResumeParser(ResumeParser):
    """
    Deterministic rule-based resume parsing engine.
    Extracts text, segments sections, and performs entity recognition using
    validated regexes and curated skill dictionaries.
    Never hallucinates or fabricates missing information.
    """

    def parse(self, file_path: str, file_type: Optional[str] = None) -> Dict[str, Any]:
        # 1. Text Extraction
        raw_text = TextExtractor.extract_text(file_path, file_type)

        # 2. Text Normalization
        normalized_text = TextNormalizer.normalize(raw_text)

        # 3. Section Segmentation
        sections = SectionSplitter.split_into_sections(normalized_text)

        # 4. Entity Extraction
        header_text = sections.get("HEADER", "")
        contact_info = EntityExtractor.extract_contact_info(header_text, normalized_text)
        summary = sections.get("SUMMARY", "")
        education = EntityExtractor.extract_education(sections.get("EDUCATION", ""))
        skills = EntityExtractor.extract_skills(normalized_text, sections.get("SKILLS", ""))
        experience = EntityExtractor.extract_experience(sections.get("EXPERIENCE", ""))
        projects = EntityExtractor.extract_projects(sections.get("PROJECTS", ""))
        certifications = EntityExtractor.extract_certifications(sections.get("CERTIFICATIONS", ""))

        structured_data = {
            "personal_info": {
                "name": contact_info.get("name"),
                "email": contact_info.get("email"),
                "phone": contact_info.get("phone"),
                "location": contact_info.get("location"),
                "summary": summary if summary else None,
                "social_profiles": contact_info.get("social_profiles", {})
            },
            "education": education,
            "skills": skills,
            "experience": experience,
            "projects": projects,
            "certifications": certifications
        }

        # 5. Missing Information Audit
        audit = AuditEngine.audit(structured_data)

        return {
            "raw_text": raw_text,
            "normalized_text": normalized_text,
            "structured_data": structured_data,
            "audit": audit
        }
