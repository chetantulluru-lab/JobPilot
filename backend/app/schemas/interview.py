from datetime import datetime
from typing import List, Optional, Dict, Any
from pydantic import BaseModel, Field


class MockInterviewStartRequest(BaseModel):
    mode: str = Field("ROLE_BASED", description="RESUME_BASED or ROLE_BASED")
    target_role: Optional[str] = Field(None, description="Target job title (e.g. Android Engineer, Full Stack Developer)")
    experience_level: str = Field("Entry-Level", description="Entry-Level, Mid-Level, or Senior")
    resume_id: Optional[str] = Field(None, description="Optional resume ID to ground questions on actual projects/skills")


class InterviewQuestion(BaseModel):
    id: int
    category: str  # Introduction, Project Deep-Dive, Technical Core, Problem Solving, Behavioral
    question: str
    hints: List[str] = []
    expected_concepts: List[str] = []


class InterviewCandidateAnswer(BaseModel):
    question_id: int
    answer_text: str


class MockInterviewSubmitRequest(BaseModel):
    answers: List[InterviewCandidateAnswer]
    face_presence_score: Optional[float] = Field(100.0, ge=0.0, le=100.0, description="Percentage of time candidate face was detected")


class QuestionEvaluation(BaseModel):
    question_id: int
    category: str
    question: str
    candidate_answer: str
    score: int  # 0 - 100
    feedback: str
    strengths: List[str] = []
    weaknesses: List[str] = []
    model_answer: str


class MockInterviewReportResponse(BaseModel):
    session_id: str
    title: str
    target_role: str
    overall_score: int  # 0 - 100
    readiness_badge: str  # e.g. "Ready for Industry Interviews", "Solid Foundation - Needs Polish", "Developing"
    technical_score: int
    communication_score: int
    problem_solving_score: int
    presence_score: int
    summary: str
    key_strengths: List[str] = []
    areas_for_improvement: List[str] = []
    recommended_roadmap_topics: List[str] = []
    question_evaluations: List[QuestionEvaluation] = []
    created_at: datetime


class MockInterviewSessionResponse(BaseModel):
    id: str
    title: str
    mode: str
    target_role: str
    experience_level: str
    status: str
    questions: List[InterviewQuestion]
    overall_score: Optional[int] = None
    created_at: datetime
