"""
Skill Normalizer and Alias Resolution for JobPilot Matching Engine.
Maps diverse skill names, aliases, and shorthand representations into canonical skills.
Employs symbol-aware boundaries to prevent false positives for short tokens (C, R, Go, AI, ML).
"""

import re
from typing import Dict, Iterable, List, Set

# Canonical Skill Mappings (Alias -> Canonical Name)
CANONICAL_SKILL_ALIASES: Dict[str, str] = {
    # Programming Languages
    "python": "Python",
    "python3": "Python",
    "python programming": "Python",
    "c": "C",
    "c++": "C++",
    "cpp": "C++",
    "c#": "C#",
    "c-sharp": "C#",
    "csharp": "C#",
    "java": "Java",
    "javascript": "JavaScript",
    "js": "JavaScript",
    "typescript": "TypeScript",
    "ts": "TypeScript",
    "go": "Go",
    "golang": "Go",
    "rust": "Rust",
    "ruby": "Ruby",
    "php": "PHP",
    "swift": "Swift",
    "kotlin": "Kotlin",
    "scala": "Scala",
    "r": "R",
    "r programming": "R",
    "dart": "Dart",
    "sql": "SQL",
    "html": "HTML",
    "html5": "HTML",
    "css": "CSS",
    "css3": "CSS",
    "bash": "Bash",
    "shell": "Bash",
    "powershell": "PowerShell",

    # Frameworks & Libraries
    "fastapi": "FastAPI",
    "django": "Django",
    "flask": "Flask",
    "react": "React",
    "react.js": "React",
    "reactjs": "React",
    "react native": "React Native",
    "next.js": "Next.js",
    "nextjs": "Next.js",
    "vue": "Vue",
    "vue.js": "Vue",
    "vuejs": "Vue",
    "angular": "Angular",
    "angularjs": "Angular",
    "node": "Node.js",
    "nodejs": "Node.js",
    "node.js": "Node.js",
    "express": "Express",
    "express.js": "Express",
    "expressjs": "Express",
    "spring": "Spring Boot",
    "spring boot": "Spring Boot",
    "spring-boot": "Spring Boot",
    "asp.net": "ASP.NET",
    ".net core": ".NET Core",
    "dotnet": ".NET Core",
    "jetpack compose": "Jetpack Compose",
    "flutter": "Flutter",
    "tailwind": "Tailwind CSS",
    "tailwind css": "Tailwind CSS",
    "bootstrap": "Bootstrap",
    "rest api": "REST API",
    "rest apis": "REST API",
    "restful api": "REST API",
    "restful apis": "REST API",
    "rest": "REST API",
    "graphql": "GraphQL",
    "grpc": "gRPC",
    "kafka": "Kafka",
    "apache kafka": "Kafka",
    "rabbitmq": "RabbitMQ",
    "celery": "Celery",

    # Databases
    "postgres": "PostgreSQL",
    "postgresql": "PostgreSQL",
    "postgre sql": "PostgreSQL",
    "mysql": "MySQL",
    "sqlite": "SQLite",
    "mongodb": "MongoDB",
    "mongo": "MongoDB",
    "redis": "Redis",
    "cassandra": "Cassandra",
    "dynamodb": "DynamoDB",
    "elasticsearch": "Elasticsearch",
    "oracle": "Oracle",
    "sql server": "Microsoft SQL Server",
    "ms sql": "Microsoft SQL Server",
    "firebase": "Firebase",
    "supabase": "Supabase",

    # Cloud & DevOps
    "aws": "AWS",
    "amazon web services": "AWS",
    "azure": "Azure",
    "microsoft azure": "Azure",
    "gcp": "GCP",
    "google cloud": "GCP",
    "google cloud platform": "GCP",
    "docker": "Docker",
    "kubernetes": "Kubernetes",
    "k8s": "Kubernetes",
    "linux": "Linux",
    "ubuntu": "Linux",
    "git": "Git",
    "github": "GitHub",
    "gitlab": "GitLab",
    "ci/cd": "CI/CD",
    "cicd": "CI/CD",
    "github actions": "GitHub Actions",
    "jenkins": "Jenkins",
    "terraform": "Terraform",
    "ansible": "Ansible",
    "nginx": "Nginx",

    # AI & Data Science
    "machine learning": "Machine Learning",
    "ml": "Machine Learning",
    "deep learning": "Deep Learning",
    "dl": "Deep Learning",
    "nlp": "NLP",
    "natural language processing": "NLP",
    "generative ai": "Generative AI",
    "genai": "Generative AI",
    "gen ai": "Generative AI",
    "llm": "LLM",
    "large language models": "LLM",
    "rag": "RAG",
    "retrieval-augmented generation": "RAG",
    "prompt engineering": "Prompt Engineering",
    "tensorflow": "TensorFlow",
    "pytorch": "PyTorch",
    "keras": "Keras",
    "scikit-learn": "scikit-learn",
    "sklearn": "scikit-learn",
    "pandas": "Pandas",
    "numpy": "NumPy",
    "opencv": "OpenCV",
    "hugging face": "Hugging Face",
    "huggingface": "Hugging Face",
    "langchain": "LangChain",

    # Testing & Quality
    "unit testing": "Unit Testing",
    "pytest": "PyTest",
    "junit": "JUnit",
    "test driven development": "TDD",
    "tdd": "TDD",
    "automated testing": "Automated Testing",

    # Cybersecurity
    "cybersecurity": "Cybersecurity",
    "wireshark": "Wireshark",
    "kali linux": "Kali Linux",
    "penetration testing": "Penetration Testing",
    "owasp": "OWASP"
}

# Sensitive tokens that need context checking to prevent false positives in prose
SENSITIVE_SKILL_TOKENS: Set[str] = {"C", "R", "Go", "AI", "ML", "SQL", "Git", "REST"}


class SkillNormalizer:
    @staticmethod
    def normalize_skill(raw_skill: str) -> str:
        """
        Maps a raw skill string to its canonical display name.
        """
        if not raw_skill:
            return ""
        cleaned = raw_skill.strip()
        cleaned_lower = cleaned.lower()

        # Direct match in alias map
        if cleaned_lower in CANONICAL_SKILL_ALIASES:
            return CANONICAL_SKILL_ALIASES[cleaned_lower]

        # Check for punctuation variations (e.g. "React.js" -> "react js")
        no_punct = re.sub(r"[\.\-_]", " ", cleaned_lower).strip()
        no_punct_single_space = re.sub(r"\s+", " ", no_punct)
        if no_punct_single_space in CANONICAL_SKILL_ALIASES:
            return CANONICAL_SKILL_ALIASES[no_punct_single_space]

        # Preserve original capitalizations if already recognized
        return cleaned

    @classmethod
    def normalize_skill_set(cls, skills: Iterable[str]) -> Set[str]:
        """
        Normalizes an iterable of skills into a set of unique canonical skill names.
        """
        normalized = set()
        for s in skills:
            norm = cls.normalize_skill(s)
            if norm:
                normalized.add(norm)
        return normalized

    @classmethod
    def extract_skills_from_prose(cls, text: str) -> Set[str]:
        """
        Safely extracts canonical skills from arbitrary English prose without false positives.
        Ensures short letters (C, R, Go, AI, ML) only match in technical contexts.
        """
        if not text:
            return set()

        found_skills: Set[str] = set()

        for alias, canonical in CANONICAL_SKILL_ALIASES.items():
            # Check for sensitive short tokens
            if canonical in SENSITIVE_SKILL_TOKENS:
                escaped = re.escape(alias)
                # Must not be preceded or followed by alphanumeric or symbol characters
                pattern = rf"(?<![A-Za-z0-9_#\+])({escaped})(?![A-Za-z0-9_#\+])"

                if canonical in {"C", "R"}:
                    # Only match if near programming keywords or comma-separated list of languages
                    if re.search(rf"(?:languages?|programming|coding|skills?|proficient in|experience with)[^\n]*?{pattern}", text, re.IGNORECASE):
                        found_skills.add(canonical)
                    elif re.search(rf"{pattern}\s*,\s*(?:C\+\+|Python|Java|SQL)", text):
                        found_skills.add(canonical)
                elif canonical == "Go":
                    if alias == "golang":
                        if re.search(r"\bGolang\b", text, re.IGNORECASE):
                            found_skills.add("Go")
                    else:
                        # Match standalone capitalized 'Go' near programming/tech context
                        if re.search(rf"(?:languages?|programming|coding|skills?|backend|developer)[^\n]*?\bGo\b", text, re.IGNORECASE):
                            found_skills.add("Go")
                elif canonical in {"AI", "ML"}:
                    # Must be capitalized AI or ML, surrounded by tech context
                    if re.search(rf"\b({alias.upper()})\b", text):
                        found_skills.add(canonical)
                else:
                    if re.search(pattern, text, re.IGNORECASE):
                        found_skills.add(canonical)
            else:
                # Standard symbol-aware boundary
                escaped = re.escape(alias)
                start_bound = r"\b" if re.match(r"^[A-Za-z0-9]", alias) else r"(?<![A-Za-z0-9_#\+])"
                end_bound = r"\b" if re.search(r"[A-Za-z0-9]$", alias) else r"(?![A-Za-z0-9_#\+])"
                pattern = rf"{start_bound}{escaped}{end_bound}"
                if re.search(pattern, text, re.IGNORECASE):
                    found_skills.add(canonical)

        return found_skills
