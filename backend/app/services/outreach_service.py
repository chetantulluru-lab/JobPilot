from typing import Optional
from sqlalchemy.orm import Session

from app.models.user import User
from app.models.career_profile import CareerProfile
from app.models.job import Job
from app.schemas.outreach import OutreachResponse


class OutreachService:

    @classmethod
    def generate_outreach(
        cls,
        db: Session,
        user: User,
        job_id: Optional[str] = None,
        role_title: Optional[str] = None,
        company: Optional[str] = None,
        job_description: Optional[str] = None
    ) -> OutreachResponse:
        profile = db.query(CareerProfile).filter(CareerProfile.user_id == user.id).first()

        resolved_title = role_title
        resolved_company = company

        if job_id:
            job = db.query(Job).filter(Job.id == job_id).first()
            if job:
                resolved_title = job.title
                resolved_company = job.company

        resolved_title = resolved_title or "Software Engineer"
        resolved_company = resolved_company or "your engineering team"

        candidate_name = user.full_name or "Candidate"
        top_skills = [s.name for s in (profile.skills if profile else [])[:3]]
        skills_str = ", ".join(top_skills) if top_skills else "modern software engineering"

        top_project = None
        if profile and profile.projects:
            top_project = profile.projects[0].title

        project_mention = f" I recently built {top_project} using {skills_str}." if top_project else f" My core technical stack includes {skills_str}."

        # 1. LinkedIn Connection Note (<300 characters strictly)
        linkedin_note = (
            f"Hi! I'm {candidate_name}, excited by {resolved_company}'s work in tech. "
            f"With experience in {skills_str},{project_mention[:60]} "
            f"I'd love to connect regarding open {resolved_title} opportunities!"
        ).strip()
        if len(linkedin_note) > 298:
            linkedin_note = linkedin_note[:295] + "..."

        # 2. Cold Email
        cold_email_subject = f"Application for {resolved_title} — {candidate_name} ({skills_str})"
        cold_email_body = f"""Dear Hiring Team at {resolved_company},

I hope this message finds you well.

I am reaching out to express my strong interest in the {resolved_title} position at {resolved_company}. Having followed your engineering updates, I am deeply impressed by your team's focus on building reliable, scalable systems.

As an engineer specializing in {skills_str},{' most notably developing ' + top_project + ',' if top_project else ''} I have hands-on experience delivering modular code, responsive interfaces, and optimized APIs. 

Key highlights of what I bring to {resolved_company}:
• Expertise in {skills_str} with production-ready code quality
• Strong foundation in system architecture, automated testing, and performance optimization
• Dedication to rapid continuous learning and transparent team collaboration

I would welcome the opportunity to discuss how my background and problem-solving skills align with your current goals for the {resolved_title} role.

Thank you for your time and consideration.

Best regards,
{candidate_name}
{user.email}
"""

        # 3. Formal Cover Letter
        cover_letter = f"""{candidate_name}
{user.email}

Hiring Committee
{resolved_company}

Re: Application for {resolved_title}

Dear Hiring Manager,

I am writing to formally submit my application for the {resolved_title} role at {resolved_company}. With a rigorous foundation in computer science and practical experience building full-stack and mobile solutions, I am eager to contribute to {resolved_company}'s engineering mission.

Throughout my engineering projects, I have consistently focused on architecting resilient solutions. In my recent work on {top_project or 'production software applications'}, I utilized {skills_str} to implement clean data flows, optimize database and network response times, and ensure seamless user experiences. I thrive in collaborative environments where clean code, automated verification, and performance benchmarks are prioritized.

What attracts me most to {resolved_company} is your commitment to high-quality software craftsmanship. I am confident that my technical skills in {skills_str}, combined with my proactive approach to debugging and system design, will allow me to make immediate, meaningful contributions to your team.

Thank you for reviewing my application. I look forward to the opportunity to speak with you further regarding how my qualifications align with your objectives for the {resolved_title} position.

Sincerely,

{candidate_name}
"""

        return OutreachResponse(
            job_title=resolved_title,
            company=resolved_company,
            linkedin_note=linkedin_note,
            cold_email_subject=cold_email_subject,
            cold_email_body=cold_email_body.strip(),
            cover_letter=cover_letter.strip()
        )
