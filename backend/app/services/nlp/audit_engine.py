"""
Audit engine for JobPilot resume NLP pipeline.
Detects missing fields, flags uncertain/low-precision data, and computes resume completeness.
"""

from typing import Dict, List, Any


class AuditEngine:
    @staticmethod
    def audit(structured_data: Dict[str, Any]) -> Dict[str, Any]:
        """
        Performs a missing-fields and uncertainty audit on the extracted resume data.
        Returns a dictionary with missing_fields, uncertain_fields, and completion_percentage.
        """
        missing_fields: List[str] = []
        uncertain_fields: List[str] = []
        score = 0

        contact = structured_data.get("personal_info", {})
        social = contact.get("social_profiles", {})
        education = structured_data.get("education", [])
        skills = structured_data.get("skills", [])
        experience = structured_data.get("experience", [])
        projects = structured_data.get("projects", [])
        certifications = structured_data.get("certifications", [])

        # 1. Contact checks
        if contact.get("name"):
            score += 10
        else:
            missing_fields.append("personal_info.name")

        if contact.get("email"):
            score += 10
        else:
            missing_fields.append("personal_info.email")

        if contact.get("phone"):
            score += 5
        else:
            missing_fields.append("personal_info.phone")

        if contact.get("location"):
            score += 5
        else:
            missing_fields.append("personal_info.location")

        # 2. Social profile checks
        if social.get("github"):
            score += 5
        else:
            missing_fields.append("social_profiles.github")

        if social.get("linkedin"):
            score += 5
        else:
            missing_fields.append("social_profiles.linkedin")

        if not social.get("portfolio"):
            missing_fields.append("social_profiles.portfolio")

        # 3. Education checks
        if education:
            score += 15
            for idx, edu in enumerate(education):
                if not edu.get("institution"):
                    missing_fields.append(f"education.{idx}.institution")
                if not edu.get("degree"):
                    missing_fields.append(f"education.{idx}.degree")
                if not edu.get("end_year"):
                    missing_fields.append(f"education.{idx}.end_year")
                if not edu.get("grade"):
                    missing_fields.append(f"education.{idx}.grade")
        else:
            missing_fields.append("education")

        # 4. Skills checks
        if skills and len(skills) >= 3:
            score += 20
        elif skills:
            score += 10
            uncertain_fields.append("skills.low_count")
        else:
            missing_fields.append("skills")

        # 5. Experience checks
        if experience:
            score += 15
            for idx, exp in enumerate(experience):
                if not exp.get("company"):
                    missing_fields.append(f"experience.{idx}.company")
                if not exp.get("role"):
                    missing_fields.append(f"experience.{idx}.role")
                if not exp.get("start_date"):
                    missing_fields.append(f"experience.{idx}.start_date")
                if not exp.get("end_date") and not exp.get("is_current"):
                    missing_fields.append(f"experience.{idx}.end_date")
        else:
            missing_fields.append("experience")

        # 6. Projects checks
        if projects:
            score += 10
            for idx, proj in enumerate(projects):
                if not proj.get("name"):
                    missing_fields.append(f"projects.{idx}.name")
                if not proj.get("start_date"):
                    missing_fields.append(f"projects.{idx}.start_date")
                if not proj.get("github_url"):
                    missing_fields.append(f"projects.{idx}.github_url")
        else:
            missing_fields.append("projects")

        # 7. Certifications
        if certifications:
            score += 5
        else:
            missing_fields.append("certifications")

        # Cap score between 0 and 100
        completion_percentage = min(100, max(0, score))

        # Generate recommendation
        if missing_fields:
            top_missing = [m.split(".")[-1].replace("_", " ").capitalize() for m in missing_fields[:3]]
            recommendation = (
                f"Your resume is {completion_percentage}% complete. "
                f"Consider adding {', '.join(top_missing)} during review to maximize AI match quality."
            )
        else:
            recommendation = "Your resume has high information completeness! Ready for AI job matching."

        return {
            "missing_fields": missing_fields,
            "uncertain_fields": uncertain_fields,
            "completion_percentage": completion_percentage,
            "recommendation": recommendation
        }
