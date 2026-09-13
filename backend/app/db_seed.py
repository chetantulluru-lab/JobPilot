"""
JobPilot Database Seed Script
Populates initial curated engineering jobs, resume templates, and default mock accounts.
"""
from app.core.database import SessionLocal
from app.models.job import Job
from app.models.resume import ResumeTemplate


def seed_database():
    db = SessionLocal()
    try:
        # 1. Seed Resume Templates
        templates = [
            ResumeTemplate(
                template_type="Minimal",
                name="Clean Minimalist",
                description="Clean typography, generous whitespace, ideal for software engineering and technical roles.",
                is_ats_optimized=True
            ),
            ResumeTemplate(
                template_type="Modern",
                name="Modern Tech",
                description="Contemporary layout with tech skill accent tags and prominent GitHub/project sections.",
                is_ats_optimized=True
            ),
            ResumeTemplate(
                template_type="Professional",
                name="Standard Professional",
                description="Structured formal layout tailored for enterprise companies and traditional corporate roles.",
                is_ats_optimized=True
            ),
            ResumeTemplate(
                template_type="Executive",
                name="Executive Impact",
                description="High-density impact-oriented format highlighting leadership metrics and project milestones.",
                is_ats_optimized=True
            )
        ]
        for t in templates:
            existing = db.query(ResumeTemplate).filter(ResumeTemplate.template_type == t.template_type).first()
            if not existing:
                db.add(t)

        # 2. Seed Curated Jobs
        jobs = [
            Job(
                title="Python Developer Intern",
                company="Nexus AI Labs",
                location="Bangalore (Hybrid)",
                work_mode="Hybrid",
                employment_type="Internship",
                salary_range="₹25,000 - ₹35,000 / month",
                description="Join our core machine learning infrastructure team to build scalable data pipelines, REST APIs, and microservices in Python and FastAPI.",
                requirements="Strong understanding of Python 3, Object-Oriented Programming, Git version control, and basic relational databases (PostgreSQL/MySQL). Docker knowledge is a plus.",
                skills_required="Python, SQL, Git, FastAPI, Docker",
                experience_level="Internship",
                posted_date="2 days ago"
            ),
            Job(
                title="Junior DevOps Engineer",
                company="CloudScale Systems",
                location="Remote",
                work_mode="Remote",
                employment_type="Full-time",
                salary_range="₹6,00,000 - ₹8,50,000 / year",
                description="Design and maintain automated CI/CD pipelines, container orchestration environments, and cloud infrastructure monitoring.",
                requirements="Hands-on experience with containerization (Docker, Kubernetes), Linux system administration, AWS/GCP, and Infrastructure as Code.",
                skills_required="Docker, Kubernetes, AWS, Linux, CI/CD, Python",
                experience_level="Entry-level",
                posted_date="1 day ago"
            ),
            Job(
                title="Full Stack Engineer (Python & React)",
                company="Orbit Technologies",
                location="Hyderabad (On-site)",
                work_mode="On-site",
                employment_type="Full-time",
                salary_range="₹7,00,000 - ₹10,00,000 / year",
                description="Develop end-to-end web applications combining robust Python backends with responsive React / TypeScript user interfaces.",
                requirements="Proficiency with Python, React, RESTful API design, database modeling, and state management.",
                skills_required="Python, React, TypeScript, PostgreSQL, Git",
                experience_level="Entry-level",
                posted_date="3 days ago"
            ),
            Job(
                title="AI / ML Research Intern",
                company="DeepVision Dynamics",
                location="Remote",
                work_mode="Remote",
                employment_type="Internship",
                salary_range="₹30,000 - ₹45,000 / month",
                description="Assist our research engineers in evaluating Transformer architectures, fine-tuning large language models, and benchmarking NLP pipelines.",
                requirements="Solid mathematical foundation in linear algebra, experience with PyTorch or TensorFlow, Python data science libraries (NumPy, Pandas).",
                skills_required="Python, PyTorch, Machine Learning, NLP, Git",
                experience_level="Internship",
                posted_date="4 days ago"
            )
        ]
        for j in jobs:
            existing = db.query(Job).filter(Job.title == j.title, Job.company == j.company).first()
            if not existing:
                db.add(j)

        db.commit()
        print("Database seeded successfully with initial templates and curated jobs.")
    except Exception as e:
        db.rollback()
        print(f"Error seeding database: {e}")
    finally:
        db.close()


if __name__ == "__main__":
    seed_database()
