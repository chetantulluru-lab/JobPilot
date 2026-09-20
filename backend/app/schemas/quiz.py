from typing import List, Optional, Dict, Any
from pydantic import BaseModel


class QuizQuestion(BaseModel):
    id: int
    question: str
    options: List[str]
    explanation: str
    correct_option_index: Optional[int] = None


class DailyQuizResponse(BaseModel):
    day_id: str
    day_title: str
    questions: List[QuizQuestion]


class QuizAnswerSubmission(BaseModel):
    question_id: int
    selected_option_index: int


class QuizSubmitRequest(BaseModel):
    submissions: List[QuizAnswerSubmission]


class QuizQuestionResult(BaseModel):
    question_id: int
    is_correct: bool
    correct_option_index: int
    selected_option_index: int
    explanation: str


class QuizSubmitResponse(BaseModel):
    day_id: str
    total_questions: int
    correct_answers: int
    score_percentage: int
    passed: bool
    day_completed: bool
    current_streak: int
    feedback: str
    question_results: List[QuizQuestionResult]
