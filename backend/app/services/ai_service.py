from abc import ABC, abstractmethod
from typing import Dict, Any, List, Optional
from app.core.config import settings


class NLPService(ABC):
    """
    Abstract NLP service interface for resume parsing and text understanding.
    Future phases can implement this with OpenAI, Anthropic, HuggingFace, or local SpaCy models.
    """
    @abstractmethod
    def extract_resume_entities(self, text: str) -> Dict[str, Any]:
        pass

    @abstractmethod
    def calculate_semantic_similarity(self, candidate_text: str, job_text: str) -> float:
        pass


class MockNLPService(NLPService):
    """
    Default offline NLP implementation.
    Safely operates without incurring costs or failing when OPENAI_API_KEY is not configured.
    """
    def extract_resume_entities(self, text: str) -> Dict[str, Any]:
        return {
            "status": "ready_for_nlp_pipeline",
            "provider": "openai_compatible" if settings.has_openai_key else "offline_rule_engine",
            "detected_skills": ["Python", "SQL", "Git", "REST APIs"],
            "education_detected": True,
            "projects_detected": True
        }

    def calculate_semantic_similarity(self, candidate_text: str, job_text: str) -> float:
        return 0.85


# Service provider singleton
nlp_service = MockNLPService()
