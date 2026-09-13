from abc import ABC, abstractmethod
from app.schemas.integration import EmailClassifyResponse


class EmailClassifier(ABC):
    """
    Abstract Service Interface for Recruiter Email Classification.
    Allows easy swapping between Rule-Based keyword engine, Local BERT, or OpenAI/Gemini NLP.
    """
    @abstractmethod
    def classify(self, sender: str, subject: str, body: str) -> EmailClassifyResponse:
        pass


class RuleBasedEmailClassifier(EmailClassifier):
    """
    Production-safe deterministic email classifier using heuristic keywords.
    Does not pretend to be an active neural network or require paid AI keys.
    """
    def classify(self, sender: str, subject: str, body: str) -> EmailClassifyResponse:
        content = f"{subject} {body}".lower()

        if any(term in content for term in ["pleased to offer", "offer letter", "formal offer", "job offer"]):
            return EmailClassifyResponse(
                category="OFFER",
                confidence=0.96,
                explanation="Detected explicit job offer phrasing.",
                suggested_action="Review offer details and update JobPilot application status to 'Offer Received'."
            )

        if any(term in content for term in ["interview", "invitation to interview", "round 1", "round 2", "technical interview", "zoom link", "google meet"]):
            return EmailClassifyResponse(
                category="INTERVIEW",
                confidence=0.92,
                explanation="Detected interview schedule or meeting invitation details.",
                suggested_action="Add interview to your calendar and review required job competencies."
            )

        if any(term in content for term in ["assessment", "hackerrank", "codility", "online test", "coding challenge", "take-home"]):
            return EmailClassifyResponse(
                category="ASSESSMENT",
                confidence=0.88,
                explanation="Detected coding assessment or test challenge link.",
                suggested_action="Schedule uninterrupted time to complete the coding assessment before the deadline."
            )

        if any(term in content for term in ["unfortunately", "not moving forward", "other candidates", "regret to inform", "pursue other applicants"]):
            return EmailClassifyResponse(
                category="REJECTION",
                confidence=0.90,
                explanation="Detected standard non-selection notification phrasing.",
                suggested_action="Log application status as 'Closed' and explore other strong matching opportunities."
            )

        if any(term in content for term in ["shortlisted", "next stage", "moving you to the next round"]):
            return EmailClassifyResponse(
                category="SHORTLISTED",
                confidence=0.85,
                explanation="Detected progress to recruiter screening / shortlisting stage.",
                suggested_action="Update application stage to 'Shortlisted'."
            )

        if any(term in content for term in ["thank you for applying", "application received", "we have received your application"]):
            return EmailClassifyResponse(
                category="APPLICATION_RECEIVED",
                confidence=0.94,
                explanation="Detected automated confirmation that your job application was received.",
                suggested_action="Verify company tracking in JobPilot Application tracker."
            )

        return EmailClassifyResponse(
            category="OTHER",
            confidence=0.60,
            explanation="Standard communication without clear application stage markers.",
            suggested_action="Manual review recommended."
        )


# Default singleton instance
email_classifier = RuleBasedEmailClassifier()
