"""
Curated Technical Skill Dictionary for JobPilot NLP Engine.
Separated from extraction logic for maintainability and easy future expansion.
"""

from typing import Dict, List, Set

# Curated technical skills categorized into logical domains
TECHNICAL_SKILLS: Dict[str, List[str]] = {
    "Programming Languages": [
        "Python", "Java", "JavaScript", "TypeScript", "C", "C++", "C#", "Go", "Golang",
        "Rust", "Ruby", "PHP", "Swift", "Kotlin", "Scala", "R", "Dart", "SQL", "HTML", "CSS",
        "Bash", "Shell", "PowerShell", "Perl", "Haskell", "Elixir", "Clojure"
    ],
    "Frameworks & Libraries": [
        "FastAPI", "Django", "Flask", "React", "React Native", "Next.js", "Vue", "Vue.js",
        "Angular", "Node.js", "Express", "Express.js", "Spring", "Spring Boot", "ASP.NET",
        ".NET Core", "Laravel", "Ruby on Rails", "Svelte", "Tailwind CSS", "Bootstrap",
        "Jetpack Compose", "Flutter", "GraphQL", "gRPC", "Redux", "Kafka", "RabbitMQ", "Celery"
    ],
    "Databases": [
        "PostgreSQL", "Postgres", "MySQL", "SQLite", "MongoDB", "Redis", "Cassandra",
        "DynamoDB", "Elasticsearch", "Neo4j", "Oracle", "Microsoft SQL Server", "MariaDB",
        "Firebase", "Supabase", "Snowflake", "BigQuery"
    ],
    "Cloud & DevOps": [
        "AWS", "Amazon Web Services", "Azure", "Microsoft Azure", "GCP", "Google Cloud",
        "Docker", "Kubernetes", "Linux", "Ubuntu", "Debian", "CentOS", "RedHat",
        "Git", "GitHub", "GitLab", "Bitbucket", "CI/CD", "Jenkins", "GitHub Actions",
        "Terraform", "Ansible", "Nginx", "Apache", "Prometheus", "Grafana"
    ],
    "AI & Data Science": [
        "Machine Learning", "Deep Learning", "NLP", "Natural Language Processing",
        "Generative AI", "LLM", "Large Language Models", "RAG", "Prompt Engineering",
        "TensorFlow", "PyTorch", "Keras", "scikit-learn", "Pandas", "NumPy", "SciPy",
        "OpenCV", "Hugging Face", "LangChain", "LlamaIndex", "Computer Vision",
        "Matplotlib", "Seaborn", "Tableau", "Power BI"
    ],
    "Cybersecurity": [
        "Cybersecurity", "Network Security", "Information Security", "Wireshark",
        "Kali Linux", "Penetration Testing", "Ethical Hacking", "OWASP", "Burp Suite",
        "Metasploit", "Nmap", "Cryptography", "SOC", "SIEM", "Vulnerability Assessment"
    ],
    "Methodologies & Tools": [
        "Agile", "Scrum", "REST API", "Microservices", "System Design", "Jira",
        "Postman", "Figma", "VS Code", "IntelliJ IDEA", "Android Studio"
    ]
}

# Single-letter or very short skill names that require strict boundary and context validation
SHORT_OR_SENSITIVE_SKILLS: Set[str] = {"C", "R", "Go", "Git", "SQL"}

# Normalize skill names mapping variations to canonical display names
SKILL_ALIASES: Dict[str, str] = {
    "golang": "Go",
    "postgres": "PostgreSQL",
    "postgresql": "PostgreSQL",
    "vue.js": "Vue",
    "react.js": "React",
    "reactjs": "React",
    "node": "Node.js",
    "nodejs": "Node.js",
    "expressjs": "Express",
    "spring-boot": "Spring Boot",
    "k8s": "Kubernetes",
    "amazon web services": "AWS",
    "microsoft azure": "Azure",
    "google cloud platform": "GCP",
    "google cloud": "GCP",
    "natural language processing": "NLP",
    "large language models": "LLM",
    "sklearn": "scikit-learn",
    "github actions": "GitHub Actions",
    "restful api": "REST API",
    "rest apis": "REST API",
}


def get_all_skills_flat() -> List[str]:
    """Returns a flattened list of all unique skills in the dictionary."""
    skills = []
    for skill_list in TECHNICAL_SKILLS.values():
        skills.extend(skill_list)
    return list(dict.fromkeys(skills))


def get_skill_category(skill_name: str) -> str:
    """Returns the primary category for a given skill name."""
    canon = SKILL_ALIASES.get(skill_name.lower(), skill_name)
    for category, skills in TECHNICAL_SKILLS.items():
        if any(s.lower() == canon.lower() for s in skills):
            return category
    return "Technical"
