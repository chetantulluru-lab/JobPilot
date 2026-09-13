# 🧠 JobPilot AI Architecture & Zero-Fabrication Policy

---

## 1. Provider-Independent AI Architecture

JobPilot decouples the business logic from specific AI vendors via the `AIProvider` interface located in `backend/app/services/ai/base.py`:

```python
class AIProvider(ABC):
    @abstractmethod
    async def generate_completion(self, request: AIRequest) -> AIResponse:
        pass
    
    @abstractmethod
    async def is_available(self) -> bool:
        pass
```

### Supported & Default Provider
- **Provider**: `OpenRouterProvider` (`app/services/ai/openrouter_provider.py`)
- **Model**: `deepseek/deepseek-r1-0528:free` (Free tier OpenRouter model)
- **Configuration**:
  ```env
  OPENROUTER_API_KEY=your-openrouter-key-here
  OPENROUTER_MODEL=deepseek/deepseek-r1-0528:free
  ```

---

## 2. Chain-of-Thought (CoT) Handling

DeepSeek R1 reasoning models enclose internal chain-of-thought within `<think>...</think>` tags. The `OpenRouterProvider` automatically strips these reasoning tokens using robust regular expressions:

```python
cleaned_text = re.sub(r'<think>[\s\S]*?</think>', '', raw_text).strip()
```

This guarantees that the user receives only polished, professional career advice and never sees raw reasoning traces or system artifacts.

---

## 3. Strict Zero Fabrication Policy

In candidate career applications, hallucinated or exaggerated credentials can disqualify an applicant during verification. JobPilot enforces a multi-tier defense against hallucination:

```
                  Prompt Level
   Candidate Profile Injected as Ground Truth
   System instructions: "NEVER invent unverified skills"
                       │
                       ▼
                 Generation Level
         OpenRouter / DeepSeek R1 Inference
                       │
                       ▼
                Validation Level
          AIValidationService scans output
  Flags unverified skills & replaces false claims
                       │
                       ▼
                 Fallback Level
      Deterministic rule-based response if offline
```

### 3.1 Prompt Grounding
The `PromptBuilder` (`app/services/ai/prompt_builder.py`) strictly serializes the user's verified profile:
- Only confirmed education, verified skills, and recorded work history are included.
- Prompts state: *"You must ONLY reference skills explicitly confirmed in candidate's profile. Missing skills must be treated as areas to learn, NEVER claimed as current proficiencies."*

### 3.2 Programmatic Verification (`AIValidationService`)
Located in `app/services/ai/validation_service.py`:
- `validate_grounding(generated_text, candidate_skills)`: Extracts skill keywords from generated text and compares against verified skills.
- Detects false assertions like "5 years of experience in AWS" when AWS is marked as missing.
- Sanitizes false assertions or logs audit warnings before the output reaches the user.

### 3.3 Offline & Rate-Limit Fallback Guarantee
If OpenRouter returns HTTP 429 (rate limit exceeded), times out (35 seconds), or is offline:
- The system switches immediately to `is_fallback = True`.
- Generates high-quality, grounded guidance formulated directly from the candidate's actual profile and match scores.
- The UI gracefully notifies the candidate that grounded fallback mode is active without crashing or hanging.
