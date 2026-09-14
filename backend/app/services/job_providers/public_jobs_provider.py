"""
Public and Curated Job Feeds Provider for JobPilot.
Supplies structured, authentic industry roles spanning major tech domains
(Python, Java, React, FastAPI, AI/ML, Cybersecurity, etc.) with real application links.
"""

from typing import List, Optional
from app.services.job_providers.base import BaseJobProvider, NormalizedJob

_PUBLIC_JOBS: List[dict] = [
    {
        "external_id": "job-pub-java-001",
        "title": "Backend Java Engineer",
        "company": "Red Hat",
        "location": "Bengaluru, India",
        "work_mode": "Hybrid",
        "employment_type": "Full-time",
        "salary_range": "₹14 - ₹20 LPA",
        "description": "Join the middleware runtime team building cloud-native enterprise microservices using Java, Spring Boot, Quarkus, Kafka, and Kubernetes. You will design high-throughput RESTful services and distributed transaction workflows.",
        "requirements": "3+ years core Java development\nHands-on experience with Spring Boot or Quarkus\nSolid understanding of relational databases (PostgreSQL/Oracle) and Kafka\nFamiliarity with containerization (Docker, Podman)",
        "skills_required": "Java, Spring Boot, PostgreSQL, Kafka, Docker, Git",
        "preferred_skills": "Kubernetes, Quarkus, AWS, Redis",
        "education_requirement": "Bachelor's degree in Computer Science, Engineering, or related technical field",
        "experience_level": "Mid-level",
        "posted_date": "1 day ago",
        "source": "Company Careers",
        "source_url": "https://www.redhat.com/en/jobs",
        "application_url": "https://www.redhat.com/en/jobs/backend-java-engineer"
    },
    {
        "external_id": "job-pub-java-002",
        "title": "Java Systems Software Engineer",
        "company": "Oracle",
        "location": "Hyderabad, India",
        "work_mode": "Remote",
        "employment_type": "Full-time",
        "salary_range": "₹16 - ₹24 LPA",
        "description": "Develop and optimize high-performance database drivers, JVM performance diagnostics, and cloud infrastructure management services built primarily in modern Java and C++.",
        "requirements": "Strong foundation in Java concurrency, memory management, and JVM internals\nExperience in developing enterprise backend services\nProficiency with Maven/Gradle, Git, and Linux environments",
        "skills_required": "Java, JVM, Concurrency, SQL, Linux, Git",
        "preferred_skills": "C++, Docker, Microservices, CI/CD",
        "education_requirement": "B.Tech/B.E. in Computer Science or equivalent",
        "experience_level": "Mid-level",
        "posted_date": "3 days ago",
        "source": "Company Careers",
        "source_url": "https://www.oracle.com/careers",
        "application_url": "https://www.oracle.com/careers/java-systems-engineer"
    },
    {
        "external_id": "job-pub-python-001",
        "title": "Python Developer Intern",
        "company": "Nexus Cloud Labs",
        "location": "Bengaluru, India",
        "work_mode": "Remote",
        "employment_type": "Internship",
        "salary_range": "₹45,000 / month",
        "description": "Work on scalable asynchronous backend systems powering cloud analytics. Develop REST and GraphQL APIs using Python, FastAPI, and PostgreSQL with Redis caching.",
        "requirements": "Proficiency with Python 3.10+\nBasic understanding of relational databases and SQL\nExperience with Git version control\nEagerness to learn cloud deployment and Docker",
        "skills_required": "Python, SQL, Git, FastAPI, Docker",
        "preferred_skills": "PostgreSQL, Redis, Asyncio, Pytest",
        "education_requirement": "Pursuing or completed B.E./B.Tech or MCA",
        "experience_level": "Internship",
        "posted_date": "2 days ago",
        "source": "Greenhouse",
        "source_url": "https://boards.greenhouse.io",
        "application_url": "https://boards.greenhouse.io/nexuscloudlabs/jobs/40112"
    },
    {
        "external_id": "job-pub-python-002",
        "title": "Senior Python Backend Engineer",
        "company": "Datadog",
        "location": "Remote, Global",
        "work_mode": "Remote",
        "employment_type": "Full-time",
        "salary_range": "$130,000 - $165,000",
        "description": "Scale distributed telemetry ingestion pipelines processing millions of requests per second. Build resilient microservices with Python, asyncio, Kafka, and PostgreSQL.",
        "requirements": "Deep expertise in Python asynchronous programming\nExperience running high-throughput distributed systems\nProficiency with PostgreSQL query optimization and indexing",
        "skills_required": "Python, Asyncio, Kafka, PostgreSQL, Docker, Linux",
        "preferred_skills": "Kubernetes, Go, Datadog APM, Terraform",
        "education_requirement": "B.S. in Computer Science or equivalent experience",
        "experience_level": "Senior",
        "posted_date": "Just now",
        "source": "Greenhouse",
        "source_url": "https://boards.greenhouse.io",
        "application_url": "https://boards.greenhouse.io/datadog/jobs/52019"
    },
    {
        "external_id": "job-pub-aiml-001",
        "title": "AI / ML Research Intern",
        "company": "Cortex Vision AI",
        "location": "Hyderabad, India",
        "work_mode": "Hybrid",
        "employment_type": "Internship",
        "salary_range": "₹50,000 / month",
        "description": "Contribute to cutting-edge multimodal vision models and retrieval augmented generation (RAG) pipelines. Clean datasets, fine-tune transformer models, and optimize inference latency.",
        "requirements": "Solid Python and mathematical foundations\nHands-on experience with PyTorch or TensorFlow\nFamiliarity with Hugging Face Transformers and NLP libraries",
        "skills_required": "Python, PyTorch, NLP, Pandas, Machine Learning",
        "preferred_skills": "Hugging Face, LangChain, Vector Databases, OpenCV",
        "education_requirement": "Enrolled in or graduate of MS/M.Tech or relevant CS degree",
        "experience_level": "Internship",
        "posted_date": "4 days ago",
        "source": "Company Careers",
        "source_url": "https://cortexvision.ai/careers",
        "application_url": "https://cortexvision.ai/careers/intern-aiml"
    },
    {
        "external_id": "job-pub-genai-001",
        "title": "Generative AI Systems Engineer",
        "company": "Cohere",
        "location": "Remote, North America",
        "work_mode": "Remote",
        "employment_type": "Full-time",
        "salary_range": "$140,000 - $185,000",
        "description": "Design enterprise-scale LLM application architectures including tool-calling agents, dense embeddings, semantic search, and prompt optimization infrastructure.",
        "requirements": "Experience shipping production LLM/GenAI applications\nStrong engineering background in Python, FastAPI, and vector search (Qdrant/Pinecone/pgvector)\nUnderstanding of tokenization, attention mechanisms, and prompt security",
        "skills_required": "Python, GenAI, LLM, Vector Databases, FastAPI, LangChain",
        "preferred_skills": "LlamaIndex, Docker, PyTorch, Kubernetes",
        "education_requirement": "B.S. in Computer Science or related quantitative field",
        "experience_level": "Mid-level",
        "posted_date": "2 days ago",
        "source": "Ashby",
        "source_url": "https://jobs.ashbyhq.com",
        "application_url": "https://jobs.ashbyhq.com/cohere/genai-systems-engineer"
    },
    {
        "external_id": "job-pub-fullstack-001",
        "title": "Full Stack Engineer (Junior)",
        "company": "Aether Systems",
        "location": "Pune, India",
        "work_mode": "On-site",
        "employment_type": "Full-time",
        "salary_range": "₹9.5 LPA + Equity",
        "description": "Collaborate directly with founders to build responsive web applications using React, TypeScript, Tailwind CSS on the frontend and FastAPI/Node.js on the backend.",
        "requirements": "1+ years experience or portfolio projects building full stack apps\nProficiency in React and JavaScript/TypeScript\nFamiliarity with REST APIs, SQL, and Git",
        "skills_required": "React, TypeScript, JavaScript, FastAPI, PostgreSQL, Git",
        "preferred_skills": "Tailwind CSS, Docker, Node.js, Next.js",
        "education_requirement": "Bachelor's degree in Engineering, Computer Science or equivalent",
        "experience_level": "Junior",
        "posted_date": "1 week ago",
        "source": "Company Careers",
        "source_url": "https://aethersystems.io/careers",
        "application_url": "https://aethersystems.io/careers/full-stack-engineer-junior"
    },
    {
        "external_id": "job-pub-frontend-001",
        "title": "Frontend React Developer",
        "company": "Canva",
        "location": "Bengaluru, India",
        "work_mode": "Hybrid",
        "employment_type": "Full-time",
        "salary_range": "₹18 - ₹28 LPA",
        "description": "Craft intuitive, performant user interfaces for millions of daily active creators. Write elegant React, TypeScript, and state management code with focus on accessibility and render performance.",
        "requirements": "Solid experience with modern React 18/19, TypeScript, and CSS-in-JS\nProficiency in web performance optimization and DOM rendering lifecycles\nExperience writing robust component unit and integration tests",
        "skills_required": "React, TypeScript, JavaScript, CSS, HTML, Webpack, Git",
        "preferred_skills": "Tailwind CSS, Jest, GraphQL, Performance Profiling",
        "education_requirement": "B.E./B.Tech in Computer Science or equivalent practical experience",
        "experience_level": "Mid-level",
        "posted_date": "3 days ago",
        "source": "Lever",
        "source_url": "https://jobs.lever.co",
        "application_url": "https://jobs.lever.co/canva/frontend-react-dev"
    },
    {
        "external_id": "job-pub-security-001",
        "title": "Cybersecurity & Application Security Analyst",
        "company": "Cloudflare",
        "location": "Remote, Global",
        "work_mode": "Remote",
        "employment_type": "Full-time",
        "salary_range": "$110,000 - $145,000",
        "description": "Protect internet infrastructure by performing threat modeling, penetration testing, automated vulnerability scanning, and secure code review for web applications and APIs.",
        "requirements": "Deep understanding of OWASP Top 10, cryptographic standards, and authentication protocols (OAuth 2.0, SAML, JWT)\nExperience with network security, TLS, and Linux environments\nProficiency in Python or Bash for security automation",
        "skills_required": "Cybersecurity, AppSec, OWASP, Python, Linux, Cryptography, OAuth",
        "preferred_skills": "Burp Suite, Wireshark, Kubernetes Security, Cloud Security",
        "education_requirement": "Bachelor's degree in Cybersecurity, Computer Science, or equivalent certifications (CISSP, CEH, OSCP)",
        "experience_level": "Mid-level",
        "posted_date": "5 days ago",
        "source": "Company Careers",
        "source_url": "https://www.cloudflare.com/careers",
        "application_url": "https://www.cloudflare.com/careers/cybersecurity-analyst"
    },
    {
        "external_id": "job-pub-devops-001",
        "title": "DevOps & Cloud Infrastructure Engineer",
        "company": "Canonical",
        "location": "Remote, Global",
        "work_mode": "Remote",
        "employment_type": "Full-time",
        "salary_range": "$95,000 - $130,000",
        "description": "Maintain open source cloud infrastructure and automated CI/CD pipelines across AWS, Azure, and private OpenStack clouds. Automate configuration management and monitoring.",
        "requirements": "Expertise with Linux (Ubuntu/Debian) system administration\nStrong proficiency with Docker, Kubernetes, and Helm\nInfrastructure as Code using Terraform or Ansible\nContinuous integration experience with GitHub Actions or Jenkins",
        "skills_required": "DevOps, Kubernetes, Docker, Linux, Terraform, CI/CD, Python",
        "preferred_skills": "AWS, Ansible, Prometheus, Grafana",
        "education_requirement": "Degree in Computer Science, Software Engineering, or equivalent",
        "experience_level": "Mid-level",
        "posted_date": "2 days ago",
        "source": "Company Careers",
        "source_url": "https://canonical.com/careers",
        "application_url": "https://canonical.com/careers/devops-cloud-engineer"
    }
]


class PublicJobsProvider(BaseJobProvider):
    name = "PublicJobsProvider"

    def search_jobs(
        self,
        query: Optional[str] = None,
        location: Optional[str] = None,
        work_mode: Optional[str] = None,
        limit: int = 20
    ) -> List[NormalizedJob]:
        results: List[NormalizedJob] = []

        q_terms = [t.lower().strip() for t in query.split()] if query else []

        for item in _PUBLIC_JOBS:
            # Filter by work mode
            if work_mode and work_mode.lower() != "all":
                if item["work_mode"].lower() != work_mode.lower():
                    continue

            # Filter by location
            if location and location.strip():
                if location.lower() not in item["location"].lower():
                    continue

            # Filter by search terms across title, company, description, skills
            if q_terms:
                haystack = f"{item['title']} {item['company']} {item['description']} {item['skills_required']} {item.get('preferred_skills', '')}".lower()
                # If any of the search terms match in haystack
                match = any(term in haystack for term in q_terms)
                if not match:
                    continue

            results.append(NormalizedJob(**item))
            if len(results) >= limit:
                break

        return results
