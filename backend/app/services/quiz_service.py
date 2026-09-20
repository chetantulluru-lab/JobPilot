from typing import List, Dict, Any
from sqlalchemy.orm import Session
from datetime import datetime, timezone, date

from app.models.roadmap import RoadmapDay, LearningActivity
from app.models.user import User
from app.models.career_profile import CareerProfile
from app.schemas.quiz import (
    QuizQuestion,
    QuizAnswerSubmission,
    QuizQuestionResult,
    QuizSubmitResponse
)


class QuizService:

    @classmethod
    def get_quiz_for_day(cls, day: RoadmapDay) -> List[QuizQuestion]:
        title_lower = (day.topic or day.day_number.__str__()).lower()

        # Generate topic-appropriate questions based on topic keywords
        if any(k in title_lower for k in ["python", "syntax", "variable", "data type"]):
            q1 = QuizQuestion(
                id=1,
                question="Which of the following data structures in Python is mutable?",
                options=["Tuple", "String", "List", "Frozenset"],
                correct_option_index=2,
                explanation="In Python, Lists are mutable, meaning their elements can be changed in-place without creating a new object in memory."
            )
            q2 = QuizQuestion(
                id=2,
                question="What is the time complexity of searching for a key in a standard Python dictionary on average?",
                options=["O(n)", "O(log n)", "O(1)", "O(n log n)"],
                correct_option_index=2,
                explanation="Python dictionaries are implemented using hash tables, offering O(1) average-time complexity for key lookups."
            )
            q3 = QuizQuestion(
                id=3,
                question="What will the expression `bool([])` evaluate to in Python?",
                options=["True", "False", "None", "Error"],
                correct_option_index=1,
                explanation="Empty collections (lists, tuples, dicts, sets, strings) evaluate to False in a boolean context in Python."
            )
        elif any(k in title_lower for k in ["database", "sql", "postgresql", "query"]):
            q1 = QuizQuestion(
                id=1,
                question="What is the primary difference between a clustered and non-clustered index?",
                options=[
                    "A clustered index physically orders the data rows in the table; only one can exist per table.",
                    "A non-clustered index physically orders data rows, while clustered does not.",
                    "Both physically reorder data rows on disk.",
                    "Clustered indexes can only be created on foreign keys."
                ],
                correct_option_index=0,
                explanation="A clustered index determines the physical order of data in the table. Because data can only be sorted in one way physically, there can be only one clustered index per table."
            )
            q2 = QuizQuestion(
                id=2,
                question="Which SQL clause is executed FIRST in the logical query processing order?",
                options=["SELECT", "WHERE", "FROM", "GROUP BY"],
                correct_option_index=2,
                explanation="SQL logically processes queries starting with the FROM clause (and JOINs), followed by WHERE, GROUP BY, HAVING, SELECT, and ORDER BY."
            )
            q3 = QuizQuestion(
                id=3,
                question="Which transaction isolation level prevents dirty reads, non-repeatable reads, and phantom reads?",
                options=["Read Uncommitted", "Read Committed", "Repeatable Read", "Serializable"],
                correct_option_index=3,
                explanation="Serializable is the strictest isolation level, completely preventing dirty reads, non-repeatable reads, and phantom reads."
            )
        elif any(k in title_lower for k in ["react", "frontend", "javascript", "dom"]):
            q1 = QuizQuestion(
                id=1,
                question="Why is it important to provide a stable, unique 'key' prop when rendering dynamic lists in React?",
                options=[
                    "Keys style the list items automatically.",
                    "Keys help React identify which items have changed, been added, or removed during Reconciliation.",
                    "Keys convert elements into Redux actions.",
                    "Keys prevent CSS collisions."
                ],
                correct_option_index=1,
                explanation="React uses keys during the Reconciliation (diffing) phase to match elements in the previous tree to elements in the subsequent tree efficiently."
            )
            q2 = QuizQuestion(
                id=2,
                question="What is the primary purpose of the `useCallback` hook in React?",
                options=[
                    "To memoize computed calculation values.",
                    "To memoize a callback function instance between renders to prevent unnecessary child re-renders.",
                    "To trigger side effects after DOM mutations.",
                    "To initialize component state asynchronously."
                ],
                correct_option_index=1,
                explanation="useCallback caches a function definition between renders, returning the same function instance unless dependencies change."
            )
            q3 = QuizQuestion(
                id=3,
                question="What does the browser Event Loop do when handling asynchronous JavaScript operations?",
                options=[
                    "It executes asynchronous callbacks in a background thread instantly.",
                    "It checks if the Call Stack is empty and moves tasks from the Callback/Microtask Queue onto the Call Stack.",
                    "It converts JavaScript code into multi-threaded C++ bytecode.",
                    "It terminates long-running DOM events."
                ],
                correct_option_index=1,
                explanation="The Event Loop constantly monitors the Call Stack and the Task/Microtask Queue, pushing callbacks to the Call Stack only when it becomes empty."
            )
        elif any(k in title_lower for k in ["android", "kotlin", "compose", "mobile"]):
            q1 = QuizQuestion(
                id=1,
                question="In Jetpack Compose, what is the key difference between `remember` and `rememberSaveable`?",
                options=[
                    "`remember` survives process death and configuration changes; `rememberSaveable` does not.",
                    "`rememberSaveable` automatically saves value state across configuration changes (e.g. screen rotation) and process death.",
                    "Both only survive during a single recomposition.",
                    "`rememberSaveable` cannot be used with primitive data types."
                ],
                correct_option_index=1,
                explanation="While `remember` retains state across recompositions, `rememberSaveable` saves it in the saved instance state bundle, surviving screen rotations and OS process recreation."
            )
            q2 = QuizQuestion(
                id=2,
                question="What is the recommended design pattern for managing state in Android Compose architecture?",
                options=[
                    "Bidirectional Event Binding",
                    "Unidirectional Data Flow (UDF) with State hoisting",
                    "Global static variables",
                    "Direct Activity mutations"
                ],
                correct_option_index=1,
                explanation="Unidirectional Data Flow (state flows down from ViewModel, events flow up from UI) ensures a single source of truth and predictable UI state."
            )
            q3 = QuizQuestion(
                id=3,
                question="Which Kotlin Coroutine dispatcher is recommended for disk I/O and network operations?",
                options=["Dispatchers.Main", "Dispatchers.Default", "Dispatchers.IO", "Dispatchers.Unconfined"],
                correct_option_index=2,
                explanation="Dispatchers.IO uses a shared pool of on-demand background threads designed specifically for blocking I/O (files, networking, databases)."
            )
        else:
            q1 = QuizQuestion(
                id=1,
                question=f"When studying {day.topic}, what is the fundamental first step in decomposing a complex engineering problem?",
                options=[
                    "Writing code immediately without designing data models.",
                    "Clarifying requirements, identifying inputs/outputs, and isolating edge cases.",
                    "Optimizing memory allocation before understanding the functional specification.",
                    "Copying templates from third-party repositories."
                ],
                correct_option_index=1,
                explanation="Effective engineering begins with understanding the problem boundary, inputs, outputs, and edge cases before writing implementation code."
            )
            q2 = QuizQuestion(
                id=2,
                question="In software architecture, what does the principle of 'High Cohesion, Low Coupling' promote?",
                options=[
                    "Modules should have tightly bound dependencies and disparate responsibilities.",
                    "Modules should have related, focused responsibilities internally, while minimizing direct dependencies on external modules.",
                    "Code should be written in a single monolith file to maximize speed.",
                    "APIs should be completely stateful across all microservices."
                ],
                correct_option_index=1,
                explanation="High cohesion ensures a component does one focused job well; low coupling minimizes inter-dependencies so changes in one component don't break others."
            )
            q3 = QuizQuestion(
                id=3,
                question="Why is continuous automated testing crucial when iterating on feature roadmaps?",
                options=[
                    "It satisfies arbitrary compliance rules without technical benefits.",
                    "It catches regressions early, enables confident refactoring, and documents expected software behavior.",
                    "It replaces the need for code reviews entirely.",
                    "It reduces database connection overhead."
                ],
                correct_option_index=1,
                explanation="Automated test suites provide a safety net that catches bugs early, allows rapid refactoring, and ensures feature reliability."
            )

        return [q1, q2, q3]

    @classmethod
    def evaluate_quiz(
        cls,
        db: Session,
        day: RoadmapDay,
        user: User,
        submissions: List[QuizAnswerSubmission]
    ) -> QuizSubmitResponse:
        questions = cls.get_quiz_for_day(day)
        questions_dict = {q.id: q for q in questions}

        results: List[QuizQuestionResult] = []
        correct_count = 0

        for sub in submissions:
            q = questions_dict.get(sub.question_id)
            if not q:
                continue
            is_correct = (sub.selected_option_index == q.correct_option_index)
            if is_correct:
                correct_count += 1

            results.append(
                QuizQuestionResult(
                    question_id=q.id,
                    is_correct=is_correct,
                    correct_option_index=q.correct_option_index or 0,
                    selected_option_index=sub.selected_option_index,
                    explanation=q.explanation
                )
            )

        total_q = len(questions)
        pct = int((correct_count / total_q) * 100) if total_q > 0 else 0
        passed = pct >= 70

        # If passed, mark day completed and increment streak
        day_completed = False
        if passed and not day.is_completed:
            day.is_completed = True
            day_completed = True

            # Check and update user learning streak
            today = date.today().isoformat()
            profile = db.query(CareerProfile).filter(CareerProfile.user_id == user.id).first()
            if profile:
                if profile.last_activity_date != today:
                    profile.current_streak += 1
                    if profile.current_streak > profile.longest_streak:
                        profile.longest_streak = profile.current_streak
                    profile.last_activity_date = today

            db.commit()

        feedback = (
            f"Awesome job! You scored {pct}% ({correct_count}/{total_q} correct). Day marked complete! Learning streak updated 🔥"
            if passed
            else f"You scored {pct}%. Review the explanations below and try again to unlock day completion and maintain your streak!"
        )

        profile = db.query(CareerProfile).filter(CareerProfile.user_id == user.id).first()
        streak = profile.current_streak if profile else 1

        return QuizSubmitResponse(
            day_id=day.id,
            total_questions=total_q,
            correct_answers=correct_count,
            score_percentage=pct,
            passed=passed,
            day_completed=day_completed or day.is_completed,
            current_streak=streak,
            feedback=feedback,
            question_results=results
        )
