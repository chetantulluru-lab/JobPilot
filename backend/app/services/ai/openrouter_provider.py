"""
OpenRouter AI Provider for JobPilot.
Supports a resilient 3-key pool (OPENROUTER_API_KEY_1, OPENROUTER_API_KEY_2, OPENROUTER_API_KEY_3)
with controlled round-robin load distribution, failure cooldown, bounded rate-limit fallback,
and reasoning tag stripping for DeepSeek R1 (deepseek/deepseek-r1-0528).
"""

import re
import time
import logging
import threading
from typing import Optional, Dict, Any, List
import httpx

from app.core.config import settings
from app.services.ai.base import AIProvider, AIRequest, AIResponse

logger = logging.getLogger("jobpilot.ai.openrouter")


class OpenRouterProvider(AIProvider):
    OPENROUTER_URL = "https://openrouter.ai/api/v1/chat/completions"

    def __init__(
        self,
        api_key: Optional[str] = None,
        api_keys: Optional[List[str]] = None,
        default_model: Optional[str] = None
    ):
        # Collect all configured non-empty keys
        keys = []
        if api_key is not None:
            if isinstance(api_key, str) and api_key.strip():
                keys.append(api_key.strip())
        if api_keys is not None:
            for k in api_keys:
                if k and k.strip():
                    keys.append(k.strip())

        if api_key is None and api_keys is None:
            gathered = [
                settings.OPENROUTER_API_KEY_1,
                settings.OPENROUTER_API_KEY_2,
                settings.OPENROUTER_API_KEY_3,
                settings.OPENROUTER_API_KEY,  # Legacy fallback
            ]
            keys = [k.strip() for k in gathered if k and k.strip()]

        self._keys = keys
        self._default_model = default_model or settings.OPENROUTER_MODEL or "deepseek/deepseek-r1-0528"
        self._key_index = 0
        self._lock = threading.Lock()
        # Cooldown map: key -> timestamp until which it is marked failed/rate-limited
        self._cooldowns: Dict[str, float] = {}

    @property
    def provider_name(self) -> str:
        return "openrouter"

    @property
    def is_available(self) -> bool:
        return len(self._keys) > 0

    def _get_candidate_keys(self) -> List[str]:
        """Returns configured keys ordered starting from next round-robin index, prioritizing non-cooldown keys."""
        if not self._keys:
            return []

        now = time.time()
        with self._lock:
            start_idx = self._key_index
            self._key_index = (self._key_index + 1) % len(self._keys)

        # Re-order keys starting at round-robin pointer
        ordered = [self._keys[(start_idx + i) % len(self._keys)] for i in range(len(self._keys))]

        # Partition into available vs cooling down
        healthy = [k for k in ordered if self._cooldowns.get(k, 0) <= now]
        cooling = [k for k in ordered if self._cooldowns.get(k, 0) > now]

        return healthy + cooling

    def _mark_key_cooldown(self, key: str, duration_seconds: float = 60.0):
        """Marks a key in temporary cooldown to prevent rapid hammering."""
        self._cooldowns[key] = time.time() + duration_seconds
        logger.warning(f"Marked OpenRouter key ending in ...{key[-4:] if len(key) >= 4 else '***'} in cooldown for {duration_seconds}s")

    def _strip_reasoning_tags(self, text: str) -> str:
        """
        DeepSeek R1 models often return internal chain-of-thought enclosed
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
        Attempts keys in the candidate pool with bounded retry (max 1 try per configured key).
        Strictly prevents infinite retry loops.
        """
        candidate_keys = self._get_candidate_keys()
        if not candidate_keys:
            raise ValueError("No OpenRouter API key configured in environment.")

        model = request.model or self._default_model
        payload: Dict[str, Any] = {
            "model": model,
            "messages": [{"role": m.role, "content": m.content} for m in request.messages],
            "temperature": request.temperature,
            "max_tokens": request.max_tokens,
        }

        last_error: Optional[Exception] = None

        # Bounded attempt: at most len(candidate_keys) tries
        for attempt_idx, api_key in enumerate(candidate_keys):
            headers = {
                "Authorization": f"Bearer {api_key}",
                "HTTP-Referer": "https://jobpilot.io",
                "X-Title": "JobPilot AI Career Assistant",
                "Content-Type": "application/json"
            }
            if request.extra_headers:
                headers.update(request.extra_headers)

            try:
                with httpx.Client(timeout=90.0) as client:
                    response = client.post(
                        self.OPENROUTER_URL,
                        json=payload,
                        headers=headers
                    )

                if response.status_code == 429:
                    logger.warning(f"OpenRouter key hit rate limit (HTTP 429) on attempt {attempt_idx + 1}.")
                    self._mark_key_cooldown(api_key, duration_seconds=60.0)
                    last_error = RuntimeError("AI service rate limited (HTTP 429).")
                    continue  # Try next configured key

                if response.status_code == 401:
                    logger.error(f"OpenRouter authentication failed (HTTP 401) on attempt {attempt_idx + 1}.")
                    self._mark_key_cooldown(api_key, duration_seconds=300.0)
                    last_error = ValueError("Invalid OpenRouter credentials.")
                    continue  # Try next configured key

                if response.status_code != 200:
                    logger.error(f"OpenRouter returned HTTP {response.status_code}: {response.text[:200]}")
                    self._mark_key_cooldown(api_key, duration_seconds=30.0)
                    last_error = RuntimeError(f"OpenRouter error: HTTP {response.status_code}")
                    continue

                data = response.json()
                choices = data.get("choices", [])
                if not choices:
                    raise RuntimeError("OpenRouter returned empty choices array.")

                raw_content = choices[0].get("message", {}).get("content", "")
                clean_content = self._strip_reasoning_tags(raw_content)

                if not clean_content:
                    logger.warning("OpenRouter response was empty after stripping reasoning tags.")
                    raise RuntimeError("Empty response from AI model (token limit reached during reasoning).")

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

            except (httpx.TimeoutException, httpx.RequestError) as e:
                logger.warning(f"Network error with OpenRouter on attempt {attempt_idx + 1}: {e}")
                self._mark_key_cooldown(api_key, duration_seconds=30.0)
                last_error = ConnectionError(f"Network connection error to AI provider: {str(e)}")
                continue

        # If all keys failed, raise clear error without loop
        if last_error:
            raise last_error
        raise RuntimeError("Roadmap couldn't be generated. Please try again.")
