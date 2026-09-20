import json
from datetime import datetime, timezone
from typing import List, Optional, Dict, Any
from sqlalchemy.orm import Session

from app.models.user import User
from app.models.career_profile import CareerProfile
from app.models.resume import Resume
from app.models.interview import MockInterviewSession
from app.schemas.interview import (
    InterviewQuestion,
    InterviewCandidateAnswer,
    QuestionEvaluation,
    MockInterviewReportResponse
)


class MockInterviewService:

    @classmethod
    def generate_questions(
        cls,
        user: User,
        profile: Optional[CareerProfile],
        target_role: str,
        experience_level: str,
        resume: Optional[Resume] = None
    ) -> List[InterviewQuestion]:
        """
        Generates 5 realistic, structured interview questions:
        Q1: Introduction
        Q2: Project Deep-Dive (grounded in actual projects if available)
        Q3: Technical Core (domain-specific in-depth question)
        Q4: Problem Solving & Architecture Scenario
        Q5: Behavioral & Situational (STAR format)
        """
        # Extract candidate projects and skills if available
        project_name = None
        project_tech = None
        top_skills = []

        if profile:
            if profile.projects:
                top_p = profile.projects[0]
                project_name = getattr(top_p, "title", None) or getattr(top_p, "name", None)
                project_tech = getattr(top_p, "tech_stack", None)
            if profile.skills:
                top_skills = [s.name for s in profile.skills[:5]]

        role_lower = target_role.lower()

        # 1. Introduction Question
        q1 = InterviewQuestion(
            id=1,
            category="Introduction",
            question=f"Welcome to this interview for the {target_role} position. To start off, could you please introduce yourself, tell me about your background, and share what drives your passion for engineering?",
            hints=[
                "Keep your answer to 1-2 minutes.",
                "Mention your education/experience, key technical strengths, and recent focus.",
                "Conclude with why you are interested in this role."
            ],
            expected_concepts=["Background", "Technical Focus", "Motivation", "Communication Clarity"]
        )

        # 2. Project Deep-Dive
        if project_name:
            tech_clause = f" using {project_tech}" if project_tech else ""
            q2 = InterviewQuestion(
                id=2,
                category="Project Deep-Dive",
                question=f"I noticed on your profile that you developed '{project_name}'{tech_clause}. Can you walk me through the system architecture, why you chose those technologies, and how you solved the biggest technical obstacle during development?",
                hints=[
                    "Explain the problem the project solved and who the users were.",
                    "Describe the architectural layers (Frontend, Backend, Database/API).",
                    "Highlight a concrete bug, performance bottleneck, or scaling challenge and how you resolved it."
                ],
                expected_concepts=["Architecture", "Tech Stack Rationale", "Problem Solving", "Technical Trade-offs"]
            )
        else:
            q2 = InterviewQuestion(
                id=2,
                category="Project Deep-Dive",
                question=f"Could you walk me through the most technically challenging software project you have built? Explain the architecture, your individual contributions, and the trade-offs you made when selecting the stack.",
                hints=[
                    "Pick a project where you solved a non-trivial technical problem.",
                    "Discuss data flow, modularity, and error handling.",
                    "Mention what you would do differently if you rewrote it today."
                ],
                expected_concepts=["System Design", "Engineering Rationale", "Scalability", "Trade-offs"]
            )

        # 3. Technical Core Competency
        if any(k in role_lower for k in ["android", "mobile", "kotlin"]):
            q3 = InterviewQuestion(
                id=3,
                category="Technical Core",
                question="In modern Android development with Jetpack Compose, explain the difference between 'remember' and 'rememberSaveable'. How do you maintain Unidirectional Data Flow (UDF) with ViewModels and StateFlow?",
                hints=[
                    "Address recomposition vs activity recreation / configuration changes.",
                    "Explain State hoisting and immutability.",
                    "Mention UI state vs UI events."
                ],
                expected_concepts=["remember vs rememberSaveable", "Unidirectional Data Flow", "StateFlow", "Recomposition"]
            )
        elif any(k in role_lower for k in ["backend", "python", "fastapi", "django", "node", "api"]):
            q3 = InterviewQuestion(
                id=3,
                category="Technical Core",
                question="In scalable backend architecture, how do you handle database connection pooling, prevent SQL injection and N+1 query problems, and design idempotent RESTful APIs?",
                hints=[
                    "Explain eager vs lazy loading and query joins.",
                    "Define idempotency in HTTP methods (GET, PUT, DELETE vs POST).",
                    "Mention database transaction isolation levels."
                ],
                expected_concepts=["Connection Pooling", "N+1 Optimization", "Idempotency", "Transactions"]
            )
        elif any(k in role_lower for k in ["frontend", "react", "web"]):
            q3 = InterviewQuestion(
                id=3,
                category="Technical Core",
                question="How does React's Virtual DOM and Reconciliation algorithm work? When should you use useMemo, useCallback, or lazy loading to optimize rendering performance?",
                hints=[
                    "Explain diffing algorithm and why stable 'key' props are critical.",
                    "Discuss referential equality in dependency arrays.",
                    "Describe code splitting with React.lazy and Suspense."
                ],
                expected_concepts=["Virtual DOM", "Diffing / Reconciliation", "useMemo & useCallback", "Code Splitting"]
            )
        elif any(k in role_lower for k in ["data", "machine learning", "ai", "ml"]):
            q3 = InterviewQuestion(
                id=3,
                category="Technical Core",
                question="How do you detect and mitigate data drift and overfitting in production machine learning models? Explain how you balance precision vs recall for an imbalanced classification problem.",
                hints=[
                    "Discuss validation strategies (K-fold, time-series splits).",
                    "Explain regularization (L1/L2), dropout, or data augmentation.",
                    "Mention F1-score, PR-AUC, and threshold tuning."
                ],
                expected_concepts=["Overfitting Prevention", "Data Drift", "Precision vs Recall", "Model Evaluation"]
            )
        else:
            q3 = InterviewQuestion(
                id=3,
                category="Technical Core",
                question="What are the core SOLID principles of object-oriented and clean software design? Give a practical example of how you have applied either the Single Responsibility or Dependency Inversion principle in your code.",
                hints=[
                    "Define S-O-L-I-D briefly.",
                    "Provide a clean refactoring example (e.g. decoupling business logic from external API or DB).",
                    "Discuss testability and loose coupling."
                ],
                expected_concepts=["SOLID Principles", "Dependency Inversion", "Modularity", "Unit Testability"]
            )

        # 4. Problem Solving & Scenario
        q4 = InterviewQuestion(
            id=4,
            category="Problem Solving",
            question="Imagine our production service experiences a sudden spike in latency and 500 error rates following a deployment. Walk me through your step-by-step triage and troubleshooting methodology from initial alert to post-mortem.",
            hints=[
                "Step 1: Verify alerting & user impact; roll back if critical.",
                "Step 2: Inspect telemetry (APM, server logs, error tracking, database metrics).",
                "Step 3: Reproduce and identify root cause (DB lock, memory leak, network timeout).",
                "Step 4: Deploy hotfix and write blameless post-mortem."
            ],
            expected_concepts=["Incident Response", "Rollback Protocol", "Observability & Logging", "Root Cause Analysis"]
        )

        # 5. Behavioral (STAR Format)
        q5 = InterviewQuestion(
            id=5,
            category="Behavioral",
            question="Tell me about a time you worked on a project with ambiguous requirements or conflicting deadlines. How did you prioritize tasks, communicate with stakeholders, and ensure quality delivery?",
            hints=[
                "Use the STAR method: Situation, Task, Action, Result.",
                "Focus on your personal actions and proactive communication.",
                "Quantify the positive outcome or what you learned."
            ],
            expected_concepts=["STAR Methodology", "Proactive Communication", "Prioritization", "Accountability"]
        )

        return [q1, q2, q3, q4, q5]

    @classmethod
    def create_session(
        cls,
        db: Session,
        user: User,
        mode: str,
        target_role: Optional[str],
        experience_level: str,
        resume_id: Optional[str] = None
    ) -> MockInterviewSession:
        profile = db.query(CareerProfile).filter(CareerProfile.user_id == user.id).first()
        resume = None
        if resume_id:
            resume = db.query(Resume).filter(Resume.id == resume_id, Resume.user_id == user.id).first()

        resolved_role = target_role or (profile.job_preferences[0].desired_roles if profile and profile.job_preferences else "Software Engineer")
        if not resolved_role or resolved_role.strip() == "":
            resolved_role = "Software Engineer"

        questions = cls.generate_questions(
            user=user,
            profile=profile,
            target_role=resolved_role,
            experience_level=experience_level,
            resume=resume
        )

        title = f"{resolved_role} Mock Interview ({experience_level})"
        session = MockInterviewSession(
            user_id=user.id,
            title=title,
            mode=mode,
            target_role=resolved_role,
            experience_level=experience_level,
            resume_id=resume_id,
            status="IN_PROGRESS",
            questions_json=json.dumps([q.model_dump() for q in questions]),
            answers_json="[]"
        )
        db.add(session)
        db.commit()
        db.refresh(session)
        return session

    @classmethod
    def evaluate_session(
        cls,
        db: Session,
        session: MockInterviewSession,
        answers: List[InterviewCandidateAnswer],
        face_presence_score: float
    ) -> MockInterviewReportResponse:
        questions_raw = json.loads(session.questions_json)
        questions_dict = {q["id"]: q for q in questions_raw}

        evaluations: List[QuestionEvaluation] = []
        total_eval_score = 0
        tech_scores = []
        comm_scores = []
        problem_scores = []

        # Model answers for each question type
        for ans in answers:
            q = questions_dict.get(ans.question_id)
            if not q:
                continue

            q_cat = q.get("category", "General")
            ans_text = ans.answer_text.strip()
            word_count = len(ans_text.split())

            # Evaluate score based on length, depth, and keyword overlap
            matched_keywords = [kw for kw in q.get("expected_concepts", []) if kw.lower() in ans_text.lower()]
            base_score = 50

            if word_count < 15:
                score = max(35, word_count * 3)
                feedback = "Answer is quite brief. In a real interview, provide more technical context, structure, and concrete examples."
                strengths = ["Responded promptly"]
                weaknesses = ["Lacks technical detail and concrete examples", "Too brief for a senior or mid-level response"]
            elif word_count < 40:
                score = min(75, 55 + len(matched_keywords) * 8)
                feedback = "Good foundation, but expand further on implementation specifics, trade-offs, and metrics."
                strengths = ["Addressed the core question"] + ([f"Mentioned {k}" for k in matched_keywords[:2]])
                weaknesses = ["Could include deeper architectural reasoning or quantifiable results"]
            else:
                score = min(95, 75 + len(matched_keywords) * 6)
                feedback = "Thorough and articulate response demonstrating structured thinking and solid domain vocabulary."
                strengths = ["Strong depth and technical vocabulary", "Well-structured response"] + [f"Demonstrated understanding of {k}" for k in matched_keywords[:3]]
                weaknesses = ["Ensure you keep the pace conversational and concise"] if word_count > 120 else []

            # Generate ideal model answer tailored to category
            model_ans = cls._get_model_answer(q)

            evaluation = QuestionEvaluation(
                question_id=ans.question_id,
                category=q_cat,
                question=q["question"],
                candidate_answer=ans_text if ans_text else "No answer provided.",
                score=score,
                feedback=feedback,
                strengths=strengths,
                weaknesses=weaknesses,
                model_answer=model_ans
            )
            evaluations.append(evaluation)
            total_eval_score += score

            if q_cat in ["Technical Core", "Project Deep-Dive"]:
                tech_scores.append(score)
            elif q_cat in ["Problem Solving"]:
                problem_scores.append(score)
            else:
                comm_scores.append(score)

        avg_tech = int(sum(tech_scores) / len(tech_scores)) if tech_scores else 75
        avg_comm = int(sum(comm_scores) / len(comm_scores)) if comm_scores else 80
        avg_prob = int(sum(problem_scores) / len(problem_scores)) if problem_scores else 75
        presence_score = int(max(0.0, min(100.0, face_presence_score)))

        # Weighted Overall Score: 40% Tech + 25% Comm + 20% Problem Solving + 15% Presence
        overall_score = int(
            (0.40 * avg_tech) +
            (0.25 * avg_comm) +
            (0.20 * avg_prob) +
            (0.15 * presence_score)
        )

        if overall_score >= 85:
            badge = "Ready for Industry Interviews 🌟"
            summary = "Outstanding performance! You displayed commanding domain knowledge, concise STAR storytelling, and maintained excellent eye contact."
        elif overall_score >= 70:
            badge = "Solid Foundation — Needs Minor Polish 🚀"
            summary = "Great effort! You clearly know your fundamentals. Refining your architectural trade-offs and expanding on project challenges will elevate you to top tier."
        else:
            badge = "Developing — Practice Recommended 💡"
            summary = "Good start. Focus on giving longer, structured responses using the STAR method, and practice explaining technical concepts step by step."

        key_strengths = [
            "Clear articulation and structured thought process",
            f"Demonstrated familiarity with {session.target_role} fundamentals",
            f"Maintained {presence_score}% face and eye contact presence throughout the session"
        ]

        areas_for_improvement = [
            "Quantify your project impact with numbers (e.g. latency reduced by 30%, 10K active users)",
            "Structure answers with the STAR technique: Situation, Task, Action, and Result",
            "Be prepared to defend alternative tech stacks and design trade-offs"
        ]

        recommended_topics = [
            f"{session.target_role} System Design & Architecture",
            "Concurrency, Caching, and High-Volume Optimization",
            "Behavioral Interview Mastery (Leadership & Conflict Resolution)"
        ]

        report = MockInterviewReportResponse(
            session_id=session.id,
            title=session.title,
            target_role=session.target_role,
            overall_score=overall_score,
            readiness_badge=badge,
            technical_score=avg_tech,
            communication_score=avg_comm,
            problem_solving_score=avg_prob,
            presence_score=presence_score,
            summary=summary,
            key_strengths=key_strengths,
            areas_for_improvement=areas_for_improvement,
            recommended_roadmap_topics=recommended_topics,
            question_evaluations=evaluations,
            created_at=datetime.now(timezone.utc)
        )

        # Update Session
        session.status = "COMPLETED"
        session.overall_score = overall_score
        session.technical_score = avg_tech
        session.communication_score = avg_comm
        session.problem_solving_score = avg_prob
        session.presence_score = presence_score
        session.answers_json = json.dumps([a.model_dump() for a in answers])
        session.report_json = json.dumps(report.model_dump(), default=str)
        db.commit()

        return report

    @classmethod
    def _get_model_answer(cls, q: Dict[str, Any]) -> str:
        cat = q.get("category", "")
        if cat == "Introduction":
            return (
                "\"Hi, I'm a software engineer passionate about building reliable, high-performance systems. "
                "I completed my degree in Computer Science, where I specialized in modern software development. "
                "Over the past couple of years, I've built end-to-end applications utilizing modern frameworks, "
                "focusing heavily on responsive UI, robust backend services, and automated testing. "
                "I'm excited about this opportunity because your team works on high-scale systems where I can contribute "
                "my technical problem-solving skills while growing alongside seasoned engineers.\""
            )
        elif cat == "Project Deep-Dive":
            return (
                "\"In my project, I designed a multi-tier architecture separating the client presentation layer from business logic and database persistence. "
                "I chose this stack because it offered fast serialization, strong type safety, and minimal latency. "
                "The biggest challenge was handling asynchronous updates under unpredictable network conditions; "
                "I solved this by introducing an offline-first caching repository with optimistic UI updates and exponential backoff synchronization. "
                "This eliminated UI stutters and ensured zero data loss.\""
            )
        elif cat == "Technical Core":
            return (
                "\"In production systems, core technical choices come down to predictability and resource management. "
                "For state and data flow, maintaining Unidirectional Data Flow guarantees single source of truth and predictable side effects. "
                "For database scalability, implementing proper connection pooling, indexing query filters, and using pagination/batching "
                "prevents N+1 query overhead and keeps query latency under 50ms.\""
            )
        elif cat == "Problem Solving":
            return (
                "\"When an incident hits production, my first priority is mitigation over debugging. "
                "Step 1: Verify the blast radius and rollback immediately if a recent deployment caused the regression. "
                "Step 2: If rollback is impossible, isolate the bottleneck via APM metrics, HTTP status codes, and server logs. "
                "Step 3: Deploy a targeted hotfix with regression tests. "
                "Step 4: Conduct a blameless post-mortem with automated alert thresholds and runbooks to prevent recurrence.\""
            )
        else:
            return (
                "\"In a previous project, we faced a tight deadline when API contracts changed 48 hours before release. "
                "I immediately organized a 15-minute sync with the backend lead, prioritized critical customer paths, "
                "and created mock adapters to unblock parallel testing. "
                "As a result, we shipped on schedule with zero critical defects and preserved complete system stability.\""
            )
