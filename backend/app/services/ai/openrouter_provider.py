"""
OpenRouter AI Provider for JobPilot.
Routes requests to free and open models (specifically DeepSeek R1 0528 Free)
via OpenRouter API without exposing secrets to frontend or clients.
"""

import re
import logging
from typing import Optional, Dict, Any
import httpx

from app.core.config import settings
from app.services.ai.base import AIProvider, AIRequest, AIResponse

logger = logging.getLogger("jobpilot.ai.openrouter")


class OpenRouterProvider(AIProvider):
    OPENROUTER_URL = "https://openrouter.ai/api/v1/chat/completions"

    def __init__(self, api_key: Optional[str] = None, default_model: Optional[str] = None):
        self._api_key = api_key or settings.OPENROUTER_API_KEY
        self._default_model = default_model or settings.OPENROUTER_MODEL or "deepseek/deepseek-r1-0528:free"

    @property
    def provider_name(self) -> str:
        return "openrouter"

    @property
    def is_available(self) -> bool:
        return bool(self._api_key and self._api_key.strip())

    def _strip_reasoning_tags(self, text: str) -> str:
        """
        DeepSeek R1 free models often return internal chain-of-thought enclosed
        in <think>...</think> tags. Strips these tags to ensure clean user-facing output.
        """
        if not text:
            return ""
        # Remove <think>...</think> blocks including newlines
        cleaned = re.sub(r"<think>[\s\S]*?</think>", "", text)
        return cleaned.strip()

    def generate_chat_completion(self, request: AIRequest) -> AIResponse:
        """
        Executes a completion request against OpenRouter API.
        """
        if not self.is_available:
            raise ValueError("OpenRouter API key is not configured in backend environment.")

        model = request.model or self._default_model
        payload: Dict[str, Any] = {
            "model": model,
            "messages": [{"role": m.role, "content": m.content} for m in request.messages],
            "temperature": request.temperature,
            "max_tokens": request.max_tokens,
        }

        headers = {
            "Authorization": f"Bearer {self._api_key.strip()}",
            "HTTP-Referer": "https://jobpilot.io",
            "X-Title": "JobPilot AI Career Assistant",
            "Content-Type": "application/json"
        }
        if request.extra_headers:
            headers.update(request.extra_headers)

        try:
            with httpx.Client(timeout=35.0) as client:
                response = client.post(
                    self.OPENROUTER_URL,
                    json=payload,
                    headers=headers
                )

            if response.status_code == 429:
                logger.warning("OpenRouter free tier rate limit exceeded (HTTP 429).")
                raise RuntimeError("AI service is currently experiencing high demand. Please try again in a few moments.")

            if response.status_code == 401:
                logger.error("OpenRouter authentication failed (HTTP 401). Invalid API key.")
                raise ValueError("Invalid OpenRouter credentials.")

            if response.status_code != 200:
                logger.error(f"OpenRouter returned HTTP {response.status_code}: {response.text[:200]}")
                raise RuntimeError(f"OpenRouter error: HTTP {response.status_code}")

            data = response.json()
            choices = data.get("choices", [])
            if not choices:
                raise RuntimeError("OpenRouter returned empty choices array.")

            raw_content = choices[0].get("message", {}).get("content", "")
            clean_content = self._strip_reasoning_tags(raw_content)

            usage = data.get("usage", {})
            tokens_used = usage.get("total_tokens")

            return AIResponse(
                content=clean_content,
                model=model,
                provider="openrouter",
                is_fallback=False,
                tokens_used=tokens_used,
                raw_response=data
            )

        except httpx.TimeoutException as e:
            logger.warning(f"OpenRouter request timed out after 35s: {e}")
            raise TimeoutError("AI request timed out. Please try again.")
        except httpx.RequestError as e:
            logger.error(f"Network error connecting to OpenRouter: {e}")
            raise ConnectionError(f"Network connection error to AI provider: {str(e)}")
