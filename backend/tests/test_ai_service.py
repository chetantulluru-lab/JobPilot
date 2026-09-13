import pytest
from unittest.mock import MagicMock, patch
from app.services.ai.base import AIProvider, AIRequest, AIResponse, AIMessagePayload
from app.services.ai.openrouter_provider import OpenRouterProvider
from app.services.ai.prompt_builder import PromptBuilder
from app.services.ai.validation_service import AIValidationService
from app.services.ai.service import AIService


class TestOpenRouterProvider:
    def test_provider_availability(self):
        prov_without_key = OpenRouterProvider(api_key="")
        assert not prov_without_key.is_available
        assert prov_without_key.provider_name == "openrouter"

        prov_with_key = OpenRouterProvider(api_key="sk-or-testkey-12345")
        assert prov_with_key.is_available

    def test_strip_reasoning_tags(self):
        prov = OpenRouterProvider(api_key="dummy")
        raw = "<think>Analyzing user question...\nThe match is 58% because of missing AWS.</think>Your match is 58% due to missing AWS."
        cleaned = prov._strip_reasoning_tags(raw)
        assert cleaned == "Your match is 58% due to missing AWS."
        assert "<think>" not in cleaned

    @patch("httpx.Client.post")
    def test_successful_completion(self, mock_post):
        mock_resp = MagicMock()
        mock_resp.status_code = 200
        mock_resp.json.return_value = {
            "choices": [{"message": {"content": "<think>thinking</think>Here is your advice."}}],
            "usage": {"total_tokens": 42}
        }
        mock_post.return_value = mock_resp

        prov = OpenRouterProvider(api_key="sk-valid-key")
        req = AIRequest(messages=[AIMessagePayload(role="user", content="Hello")])
        res = prov.generate_chat_completion(req)

        assert res.content == "Here is your advice."
        assert res.tokens_used == 42
        assert not res.is_fallback


class TestPromptBuilderAndSafety:
    def test_prompt_includes_grounded_profile_and_safety_rules(self):
        profile = {
            "full_name": "Jane Doe",
            "skills": [{"name": "Python"}, {"name": "FastAPI"}],
            "projects": [{"title": "Web API", "tech_stack": "Python, FastAPI", "description": "Built backend"}],
            "education": [{"degree": "B.S.", "field_of_study": "Computer Science", "institution": "MIT"}]
        }
        job = {
            "title": "Backend Intern",
            "company": "TechCorp",
            "skills_required": "Python, PostgreSQL, AWS",
            "match_score": 67,
            "matched_skills": ["Python"],
            "missing_skills": ["PostgreSQL", "AWS"]
        }

        messages = PromptBuilder.build_assistant_messages("Why is my match 67%?", [], profile, job)
        system_content = messages[0].content

        assert "ZERO FABRICATION POLICY" in system_content
        assert "Jane Doe" in system_content or "Python" in system_content
        assert "TechCorp" in system_content
        assert "67%" in system_content


class TestValidationService:
    def test_missing_skill_hallucination_correction(self):
        profile = {"skills": [{"name": "Python"}]}
        # If AI hallucinated claiming extensive experience with AWS when AWS is missing:
        hallucinated_text = "I have extensive experience with AWS and cloud infrastructure."
        validated = AIValidationService.validate_content_grounding(
            generated_text=hallucinated_text,
            profile_data=profile,
            missing_skills=["AWS"]
        )
        assert "extensive experience with AWS" not in validated
        assert "AWS" in validated
        assert "learning target" in validated or "foundational" in validated


class TestAIServiceDeterministicFallbacks:
    def test_fallback_when_offline(self):
        # Service with no API key
        service = AIService(provider=OpenRouterProvider(api_key=""))
        assert not service.is_ai_online

        profile = {
            "full_name": "Test User",
            "skills": [{"name": "Python"}, {"name": "SQL"}]
        }
        job = {
            "title": "Data Engineer",
            "company": "DataCorp",
            "match_score": 58,
            "match_tier": "Low Match",
            "matched_skills": ["Python", "SQL"],
            "missing_skills": ["Kafka", "Spark"]
        }

        # Query about match
        res = service.generate_chat_reply("Why is my match 58%?", [], profile, job)
        assert res["is_fallback"]
        assert "58%" in res["content"]
        assert "Kafka" in res["content"] or "Spark" in res["content"]

        # Tailoring fallback
        tailor = service.tailor_resume(profile, job)
        assert tailor["is_fallback"]
        assert len(tailor["tailored_summary"]) > 20

        # Cover letter fallback
        cl = service.generate_cover_letter(profile, job, "Professional")
        assert cl["is_fallback"]
        assert "DataCorp" in cl["content"]
