"""
JobPilot Matching Engine Package (Phase 3B).
"""

from app.services.matching.skill_normalizer import SkillNormalizer
from app.services.matching.requirement_extractor import JobRequirementExtractor, JobRequirements
from app.services.matching.candidate_profile_builder import CandidateProfileBuilder, CandidateProfileData
from app.services.matching.relevance_engine import ExperienceMatcher, EducationMatcher
from app.services.matching.scoring_engine import ScoringEngine, MatchScoreBreakdown
from app.services.matching.skill_gap_engine import SkillGapEngine
from app.services.matching.semantic_provider import SemanticMatchingProvider, NoOpSemanticProvider
from app.services.matching.matching_service import MatchingService

__all__ = [
    "SkillNormalizer",
    "JobRequirementExtractor",
    "JobRequirements",
    "CandidateProfileBuilder",
    "CandidateProfileData",
    "ExperienceMatcher",
    "EducationMatcher",
    "ScoringEngine",
    "MatchScoreBreakdown",
    "SkillGapEngine",
    "SemanticMatchingProvider",
    "NoOpSemanticProvider",
    "MatchingService",
]
