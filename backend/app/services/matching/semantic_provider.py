"""
Semantic Matching Provider abstraction for JobPilot Matching Engine.
Defines the clean interface for future Sentence Transformers, Vector Embeddings,
and LLM-assisted reasoning without altering API contracts or database models.
"""

from abc import ABC, abstractmethod
from typing import Dict, Any, List


class SemanticMatchingProvider(ABC):
    """
    Interface for semantic similarity providers (e.g. embeddings, vector databases).
    Future implementations can plug in Sentence Transformers or OpenAI embeddings.
    """

    @abstractmethod
    def compute_similarity(self, candidate_text: str, job_text: str) -> float:
        """Computes cosine similarity between candidate profile and job description."""
        pass

    @abstractmethod
    def explain_semantic_fit(self, candidate_summary: str, job_summary: str) -> str:
        """Generates natural language reasoning on semantic nuance alignment."""
        pass


class NoOpSemanticProvider(SemanticMatchingProvider):
    """
    Default provider for Phase 3B.
    Deterministic rule-based and NLP token matching are active;
    no fake embeddings or mock AI calls are executed.
    """

    def compute_similarity(self, candidate_text: str, job_text: str) -> float:
        return 0.0

    def explain_semantic_fit(self, candidate_summary: str, job_summary: str) -> str:
        return ""
