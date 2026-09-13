import pytest
from app.services.matching.skill_normalizer import SkillNormalizer
from app.services.matching.requirement_extractor import JobRequirementExtractor
from app.services.matching.candidate_profile_builder import CandidateProfileBuilder
from app.services.matching.relevance_engine import ExperienceMatcher, EducationMatcher
from app.services.matching.scoring_engine import ScoringEngine
from app.services.matching.skill_gap_engine import SkillGapEngine, CURATED_ROADMAPS
from app.models import Job, CareerProfile, Skill, Experience, Project, Education


class TestSkillNormalizer:
    def test_canonical_aliases(self):
        assert SkillNormalizer.normalize_skill("postgres") == "PostgreSQL"
        assert SkillNormalizer.normalize_skill("ReactJS") == "React"
        assert SkillNormalizer.normalize_skill("golang") == "Go"
        assert SkillNormalizer.normalize_skill("ML") == "Machine Learning"
        assert SkillNormalizer.normalize_skill("AWS") == "AWS"
        assert SkillNormalizer.normalize_skill("amazon web services") == "AWS"
        assert SkillNormalizer.normalize_skill("k8s") == "Kubernetes"
        assert SkillNormalizer.normalize_skill("TS") == "TypeScript"
        assert SkillNormalizer.normalize_skill("JS") == "JavaScript"

    def test_symbol_boundary_preservation(self):
        assert SkillNormalizer.normalize_skill("C++") == "C++"
        assert SkillNormalizer.normalize_skill("C#") == "C#"
        assert SkillNormalizer.normalize_skill(".NET") == ".NET"

    def test_extract_skills_from_prose_with_boundary_guards(self):
        text = """
        Looking for a backend developer proficient in programming languages: Python, React, and PostgreSQL.
        The candidate should have good communication and ongoing interest in development.
        Experience with C++ is required.
        """
        extracted = SkillNormalizer.extract_skills_from_prose(text)
        
        # Recognized real skills
        assert "Python" in extracted
        assert "React" in extracted
        assert "PostgreSQL" in extracted
        assert "C++" in extracted

        # Boundary guard checks: "ongoing" or "good" must NOT match "Go"
        # "communication" must NOT match "C"
        # "interest" must NOT match "R"
        assert "C" not in extracted
        assert "R" not in extracted
        assert "Go" not in extracted


class TestRequirementExtractor:
    def test_extract_from_job_with_explicit_fields(self):
        reqs = JobRequirementExtractor.extract_requirements(
            description="We are seeking an engineer to build scalable APIs.",
            requirements_text="3+ years of experience with Python and relational databases.",
            skills_required_str="Python, FastAPI, PostgreSQL",
            preferred_skills_str="Docker, Kubernetes, AWS",
            education_req_str="Bachelor's in Computer Science",
            role_title="Senior Backend Engineer"
        )

        assert "Python" in reqs.required_skills
        assert "FastAPI" in reqs.required_skills
        assert "PostgreSQL" in reqs.required_skills

        assert "Docker" in reqs.preferred_skills
        assert "Kubernetes" in reqs.preferred_skills
        assert "AWS" in reqs.preferred_skills

        assert reqs.min_experience_years == 3
        assert reqs.min_education_degree == "Bachelor's in Computer Science"

    def test_extract_preferred_from_requirements_text_fallback(self):
        reqs = JobRequirementExtractor.extract_requirements(
            description="Awesome startup environment.",
            requirements_text="""
            Requirements:
            - React, TypeScript, Node.js
            
            Nice to Have:
            - GraphQL, Redis, Docker
            """,
            skills_required_str="React, TypeScript, Node.js",
            preferred_skills_str=None
        )

        # Redis or Docker should be captured in preferred
        assert "Docker" in reqs.preferred_skills or "Redis" in reqs.preferred_skills or "GraphQL" in reqs.preferred_skills


class TestCandidateProfileBuilder:
    def test_profile_corroboration(self):
        profile = CareerProfile(
            headline="Full Stack Engineer",
            summary="Passionate engineer building Python and React applications.",
            skills=[
                Skill(name="Python", proficiency="Advanced"),
                Skill(name="FastAPI", proficiency="Intermediate"),
                Skill(name="Docker", proficiency="Beginner"),
                Skill(name="Kubernetes", proficiency="Beginner"),
            ],
            projects=[
                Project(
                    title="API Gateway",
                    description="Built high-performance API in Python and FastAPI",
                    tech_stack="Python, FastAPI, Redis",
                )
            ],
            experience=[
                Experience(
                    company="Tech Corp",
                    title="Junior Developer",
                    description="Developed backend microservices using Python and PostgreSQL",
                )
            ],
            education=[
                Education(
                    degree="Bachelor of Science",
                    field_of_study="Computer Science",
                    institution="MIT",
                )
            ],
        )

        cand = CandidateProfileBuilder.build_from_career_profile(profile)

        assert "Python" in cand.canonical_skills
        assert "FastAPI" in cand.canonical_skills
        assert "Docker" in cand.canonical_skills
        assert "PostgreSQL" in cand.experience_techs
        assert "Redis" in cand.project_techs

        # Python and FastAPI are corroborated by projects & experience
        assert "Python" in cand.corroborated_skills
        assert "FastAPI" in cand.corroborated_skills

        # Docker and Kubernetes were marked Beginner
        assert "Docker" in cand.weak_skills
        assert "Kubernetes" in cand.weak_skills


class TestRelevanceEngine:
    def test_experience_matcher(self):
        score = ExperienceMatcher.calculate_experience_relevance(
            job_required_skills=["Python", "FastAPI", "PostgreSQL"],
            job_preferred_skills=["Docker", "AWS"],
            candidate_project_techs={"Python", "FastAPI", "Redis"},
            candidate_experience_techs={"Python", "PostgreSQL"},
            corroborated_skills={"Python", "FastAPI"}
        )
        assert 75 <= score <= 100

    def test_education_matcher_related_fields(self):
        # AI/ML degree matched with CS job requirement
        score = EducationMatcher.calculate_education_relevance(
            job_degree_req="Bachelor in Computer Science",
            candidate_degrees=[{"degree": "Master of Science", "field": "Artificial Intelligence"}]
        )
        assert score >= 85

        # Unrelated degree
        unrelated_score = EducationMatcher.calculate_education_relevance(
            job_degree_req="Bachelor in Computer Science",
            candidate_degrees=[{"degree": "Bachelor of Arts", "field": "Music Performance"}]
        )
        assert unrelated_score <= 50


class TestScoringEngine:
    def test_score_tiers(self):
        assert ScoringEngine.determine_match_tier(95) == "Excellent Match"
        assert ScoringEngine.determine_match_tier(80) == "Strong Match"
        assert ScoringEngine.determine_match_tier(68) == "Moderate Match"
        assert ScoringEngine.determine_match_tier(52) == "Low Match"
        assert ScoringEngine.determine_match_tier(25) == "Weak Match"

    def test_dominance_penalty_caps_score(self):
        """If candidate matches 0/4 required skills but 3/3 preferred skills, match must not be inflated."""
        breakdown = ScoringEngine.calculate_score(
            job_required_skills=["Python", "FastAPI", "PostgreSQL", "Redis"],
            job_preferred_skills=["Docker", "AWS", "Git"],
            candidate_canonical_skills={"Docker", "AWS", "Git"},
            corroborated_skills=set(),
            weak_skills=set(),
            experience_relevance=50,
            education_relevance=80,
            profile_completeness=90,
            role_title="Backend Engineer"
        )
        # Required coverage is 0%, dominance penalty should cap score below 45
        assert breakdown.match_score <= 45
        assert breakdown.match_tier in ("Low Match", "Weak Match")

    def test_high_match_calculation_and_explanation(self):
        breakdown = ScoringEngine.calculate_score(
            job_required_skills=["Python", "FastAPI", "PostgreSQL"],
            job_preferred_skills=["Docker", "Kubernetes"],
            candidate_canonical_skills={"Python", "FastAPI", "PostgreSQL", "Docker"},
            corroborated_skills={"Python", "FastAPI"},
            weak_skills=set(),
            experience_relevance=90,
            education_relevance=95,
            profile_completeness=95,
            role_title="Backend Engineer"
        )
        assert breakdown.match_score >= 80
        assert "Python" in breakdown.matched_required
        assert "FastAPI" in breakdown.matched_required
        assert "Docker" in breakdown.matched_preferred
        assert "Strong Match" in breakdown.match_tier or "Excellent Match" in breakdown.match_tier
        assert len(breakdown.explanation) > 30


class TestSkillGapEngine:
    def test_curated_skill_roadmaps(self):
        roadmap = SkillGapEngine.generate_roadmap(
            missing_required=["Docker", "Kubernetes"],
            missing_preferred=["AWS"],
            role_title="Cloud Engineer"
        )
        assert len(roadmap) == 3
        docker_step = roadmap[0]
        assert docker_step["skill_name"] == "Docker"
        assert docker_step["importance"] == "High"
        assert docker_step["why_it_matters"] is not None
        assert len(docker_step["roadmap_topics"]) >= 3
        assert docker_step["suggested_practice"] is not None

        aws_step = roadmap[2]
        assert aws_step["skill_name"] == "AWS"
        assert aws_step["importance"] == "Medium"

    def test_dynamic_fallback_for_unknown_skill(self):
        roadmap = SkillGapEngine.generate_roadmap(
            missing_required=["SomeNewTechXYZ"],
            missing_preferred=[],
            role_title="Specialist"
        )
        assert len(roadmap) == 1
        item = roadmap[0]
        assert item["skill_name"] == "SomeNewTechXYZ"
        assert len(item["roadmap_topics"]) >= 3
        assert "SomeNewTechXYZ" in item["suggested_practice"]


class TestMatchingIntegrationAPI:
    def test_match_api_and_caching(self, client, user_a_headers):
        jobs = client.get("/api/v1/jobs").json()
        assert len(jobs) > 0
        job_id = jobs[0]["id"]

        # Initial calculation
        res = client.get(f"/api/v1/jobs/{job_id}/match", headers=user_a_headers)
        assert res.status_code == 200
        data = res.json()
        assert "match_score" in data
        assert "match_tier" in data
        assert "matched_skills" in data
        assert "missing_required_skills" in data
        assert "experience_relevance" in data
        assert "profile_completeness" in data
        assert 0 <= data["match_score"] <= 100

        # Cached response
        cached_res = client.get(f"/api/v1/jobs/{job_id}/match", headers=user_a_headers)
        assert cached_res.status_code == 200
        cached_data = cached_res.json()
        assert cached_data["match_score"] == data["match_score"]

        # Force refresh
        refreshed_res = client.get(f"/api/v1/jobs/{job_id}/match?force_refresh=true", headers=user_a_headers)
        assert refreshed_res.status_code == 200

    def test_skill_gaps_api_structure(self, client, user_a_headers):
        jobs = client.get("/api/v1/jobs").json()
        job_id = jobs[0]["id"]

        res = client.get(f"/api/v1/jobs/{job_id}/skill-gaps", headers=user_a_headers)
        assert res.status_code == 200
        data = res.json()
        assert "gaps" in data
        assert "match_score" in data
        for gap in data["gaps"]:
            assert "step_number" in gap
            assert "skill_name" in gap
            assert "importance" in gap
            assert "recommendation" in gap
            assert "why_it_matters" in gap
            assert "roadmap_topics" in gap
            assert isinstance(gap["roadmap_topics"], list)

    def test_user_isolation_matching(self, client, user_a_headers, user_b_headers):
        jobs = client.get("/api/v1/jobs").json()
        job_id = jobs[0]["id"]

        # Request match for User A
        res_a = client.get(f"/api/v1/jobs/{job_id}/match", headers=user_a_headers)
        assert res_a.status_code == 200

        # Request match for User B
        res_b = client.get(f"/api/v1/jobs/{job_id}/match", headers=user_b_headers)
        assert res_b.status_code == 200

        # Both succeed independently without leaking data
        assert res_a.json()["job_id"] == job_id
        assert res_b.json()["job_id"] == job_id
