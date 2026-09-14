"""
Provider-independent AI abstraction for JobPilot.
Defines interfaces and data structures for interacting with Large Language Models.
"""

from abc import ABC, abstractmethod
from dataclasses import dataclass, field
from typing import Dict, List, Optional, Any


@dataclass
class AIMessagePayload:
    role: str  # "system", "user", "assistant"
    content: str


AIMessage = AIMessagePayload


@dataclass
class AIRequest:
    messages: List[AIMessagePayload]
    temperature: float = 0.3
    max_tokens: int = 1500
    model: Optional[str] = None
    extra_headers: Dict[str, str] = field(default_factory=dict)


@dataclass
class AIResponse:
    content: str
    model: str
    provider: str
    is_fallback: bool = False
    tokens_used: Optional[int] = None
    raw_response: Optional[Dict[str, Any]] = None


class AIProvider(ABC):
    """Abstract interface for AI model providers (OpenRouter, OpenAI, Local, etc.)."""

    @property
    @abstractmethod
    def provider_name(self) -> str:
        """Name of the provider (e.g., 'openrouter')."""
        pass

    @property
    @abstractmethod
    def is_available(self) -> bool:
        """Returns True if the provider is configured and available for requests."""
        pass

    @abstractmethod
    def generate_chat_completion(self, request: AIRequest) -> AIResponse:
        """Generates a chat completion synchronously or raises a descriptive ProviderError."""
        pass
