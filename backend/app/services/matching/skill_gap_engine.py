"""
Skill Gap Engine for JobPilot Matching Engine.
Produces structured, actionable learning roadmaps for missing competencies without fabricating precision.
All estimates are explicitly designated as approximate.
"""

from typing import Dict, List, Any

# Curated Technical Learning Roadmaps
CURATED_ROADMAPS: Dict[str, Dict[str, Any]] = {
    "Docker": {
        "why_it_matters": "Docker is essential for reproducible microservices, deployment isolation, and local development parity.",
        "estimated_time": "5-7 days (approx.)",
        "roadmap_topics": [
            "1. Containerization vs Virtualization concepts",
            "2. Docker architecture, daemon, and image layers",
            "3. Writing clean multi-stage Dockerfiles",
            "4. Multi-container orchestration with Docker Compose",
            "5. Volume mounts, networking, and production security"
        ],
        "suggested_practice": "Containerize a FastAPI REST API backed by a PostgreSQL database and Redis caching container using Docker Compose."
    },
    "Kubernetes": {
        "why_it_matters": "Kubernetes is the industry standard for production container orchestration, auto-scaling, and service mesh management.",
        "estimated_time": "2-3 weeks (approx.)",
        "roadmap_topics": [
            "1. Kubernetes cluster architecture (Control plane, Kubelet, etcd)",
            "2. Pods, ReplicaSets, and Deployments",
            "3. Services (ClusterIP, NodePort, LoadBalancer) and Ingress",
            "4. ConfigMaps, Secrets, and Persistent Volumes",
            "5. Local practice with Minikube or Kind"
        ],
        "suggested_practice": "Deploy your containerized backend API into a local Minikube cluster with rolling updates and health checks."
    },
    "FastAPI": {
        "why_it_matters": "FastAPI is a modern, high-performance web framework for building asynchronous Python APIs.",
        "estimated_time": "4-6 days (approx.)",
        "roadmap_topics": [
            "1. Asynchronous Python (async/await) and ASGI architecture",
            "2. Request validation and schemas using Pydantic V2",
            "3. Dependency Injection system and session management",
            "4. Database integration with SQLAlchemy 2.0 and Alembic",
            "5. JWT authentication and automated OpenAPI documentation"
        ],
        "suggested_practice": "Build a modular REST API with JWT authentication, CRUD endpoints, and automated Pytest test coverage."
    },
    "PostgreSQL": {
        "why_it_matters": "PostgreSQL is the leading open-source relational database, offering ACID compliance, JSONB support, and indexing.",
        "estimated_time": "5-7 days (approx.)",
        "roadmap_topics": [
            "1. Relational schema design, constraints, and foreign keys",
            "2. Query optimization, EXPLAIN ANALYZE, and indexing strategies",
            "3. Transactions, ACID properties, and isolation levels",
            "4. Working with JSON/JSONB and full-text search",
            "5. Database migrations using Alembic"
        ],
        "suggested_practice": "Design an e-commerce or job application schema with indexes, foreign keys, and perform query optimization."
    },
    "AWS": {
        "why_it_matters": "AWS is the dominant cloud provider for hosting enterprise infrastructure, compute, and serverless pipelines.",
        "estimated_time": "2-3 weeks (approx.)",
        "roadmap_topics": [
            "1. Core AWS compute: EC2, ECS, and AWS Lambda",
            "2. Storage & databases: S3 and RDS PostgreSQL",
            "3. Networking & security: VPC, Subnets, and IAM roles",
            "4. CloudWatch logging and metrics monitoring",
            "5. Infrastructure deployment using Terraform or AWS CDK"
        ],
        "suggested_practice": "Deploy a containerized web service to AWS ECS or App Runner backed by an RDS PostgreSQL instance."
    },
    "Redis": {
        "why_it_matters": "Redis is an in-memory key-value data store used for sub-millisecond caching, rate-limiting, and pub/sub queues.",
        "estimated_time": "3-4 days (approx.)",
        "roadmap_topics": [
            "1. In-memory caching concepts and cache eviction strategies (LRU)",
            "2. Redis data structures: Strings, Hashes, Lists, Sets, Sorted Sets",
            "3. Cache-aside and write-through caching patterns",
            "4. Session storage and rate limiting implementation",
            "5. Asynchronous background jobs with Celery or RQ"
        ],
        "suggested_practice": "Implement a cache-aside caching layer on top of a slow database query endpoint with automatic cache invalidation."
    },
    "REST API": {
        "why_it_matters": "REST APIs define standard architectural constraints for client-server communication across web and mobile apps.",
        "estimated_time": "3-5 days (approx.)",
        "roadmap_topics": [
            "1. HTTP methods (GET, POST, PUT, DELETE, PATCH) and idempotent design",
            "2. Proper HTTP status code usage (200, 201, 400, 401, 403, 404, 500)",
            "3. Pagination, filtering, and query parameter structuring",
            "4. Error handling and standard error response schemas",
            "5. API documentation with Swagger/OpenAPI"
        ],
        "suggested_practice": "Design and document a complete CRUD resource API with pagination and error responses."
    },
    "Unit Testing": {
        "why_it_matters": "Automated testing ensures code correctness, guards against regressions, and accelerates deployment velocity.",
        "estimated_time": "4-5 days (approx.)",
        "roadmap_topics": [
            "1. Unit testing principles, test isolation, and AAA pattern",
            "2. Writing clean tests with Pytest and fixtures",
            "3. Mocking database sessions and external HTTP calls",
            "4. Integration testing with TestClient",
            "5. Measuring code coverage with pytest-cov"
        ],
        "suggested_practice": "Write a suite of unit and integration tests achieving 85%+ coverage for an existing API router."
    }
}


class SkillGapEngine:
    @classmethod
    def generate_roadmap(
        cls,
        missing_required: List[str],
        missing_preferred: List[str],
        role_title: str = "this position"
    ) -> List[Dict[str, Any]]:
        """
        Generates structured roadmap items for each missing required and preferred skill.
        Required skills are placed first with High importance.
        """
        roadmaps: List[Dict[str, Any]] = []
        step_number = 1

        # Process Required skills first
        for skill in missing_required:
            entry = cls._get_or_create_roadmap(skill, importance="High", role_title=role_title)
            entry["step_number"] = step_number
            roadmaps.append(entry)
            step_number += 1

        # Process Preferred skills next
        for skill in missing_preferred:
            entry = cls._get_or_create_roadmap(skill, importance="Medium", role_title=role_title)
            entry["step_number"] = step_number
            roadmaps.append(entry)
            step_number += 1

        return roadmaps

    @classmethod
    def _get_or_create_roadmap(cls, skill_name: str, importance: str, role_title: str) -> Dict[str, Any]:
        curated = CURATED_ROADMAPS.get(skill_name)
        if curated:
            return {
                "skill_name": skill_name,
                "importance": importance,
                "current_proficiency": "Not detected in Career Profile",
                "why_it_matters": curated["why_it_matters"],
                "recommendation": f"Focus on mastering {skill_name}. {curated['suggested_practice']}",
                "estimated_time": curated["estimated_time"],
                "roadmap_topics": curated["roadmap_topics"],
                "suggested_practice": curated["suggested_practice"]
            }

        # Fallback for unlisted technical skills
        return {
            "skill_name": skill_name,
            "importance": importance,
            "current_proficiency": "Not detected in Career Profile",
            "why_it_matters": f"{skill_name} is requested for {role_title}.",
            "recommendation": f"Review standard documentation for {skill_name}, understand core architectural principles, and implement a sample project.",
            "estimated_time": "1-2 weeks (approx.)",
            "roadmap_topics": [
                f"1. {skill_name} fundamentals and installation",
                f"2. Core features and best practices for {skill_name}",
                f"3. Integrating {skill_name} into application stack",
                f"4. Debugging and production considerations"
            ],
            "suggested_practice": f"Build a practical proof-of-concept project integrating {skill_name} and publish it to GitHub."
        }
