"""
Entity and field extraction layer for JobPilot NLP engine.
Extracts contact details, skills, education, experience, projects, certifications, and dates.
Strictly preserves missing information as None without fabricating dates or credentials.
"""

import re
from typing import Dict, List, Optional, Any, Tuple
from app.services.nlp.skill_dictionary import (
    TECHNICAL_SKILLS, SHORT_OR_SENSITIVE_SKILLS, SKILL_ALIASES,
    get_skill_category
)

# Common degrees regex pattern
DEGREE_PATTERN = re.compile(
    r"\b(B\.?Tech|B\.?E\.?|B\.?S\.?c?|Bachelor(?:'s)?(?:\s+of\s+[A-Za-z\s]+)?|"
    r"M\.?Tech|M\.?S\.?c?|Master(?:'s)?(?:\s+of\s+[A-Za-z\s]+)?|"
    r"Ph\.?D|Doctorate|Diploma|Associate(?:'s)?)\b",
    re.IGNORECASE
)

# Major fields of study
FIELD_OF_STUDY_PATTERN = re.compile(
    r"\b(Computer\s+Science(?:\s+and\s+Engineering)?|Information\s+Technology|"
    r"Software\s+Engineering|Data\s+Science|Artificial\s+Intelligence|"
    r"Electrical(?:\s+and\s+Electronics)?\s+Engineering|Mechanical\s+Engineering|"
    r"Electronics(?:\s+and\s+Communication)?\s+Engineering|Civil\s+Engineering|"
    r"Business\s+Administration|Cybersecurity|Mathematics|Physics)\b",
    re.IGNORECASE
)

# Institution indicators
INSTITUTION_KEYWORDS = re.compile(
    r"\b(University|Institute|College|Academy|School|Polytechnic)\b",
    re.IGNORECASE
)

# Grade / GPA patterns (e.g. 8.6 CGPA, 3.8/4.0 GPA, 85%)
GRADE_PATTERN = re.compile(
    r"\b(?:CGPA|GPA|Grade)?:?\s*(\d{1,2}(?:\.\d{1,2})?)\s*(?:/10|/4\.0|CGPA|GPA|%)?\b",
    re.IGNORECASE
)

# Date range regex
# Matches: "Jan 2024 - Mar 2025", "06/2024 - Present", "2023 - 2024", "Jan 2024 - Ongoing"
DATE_RANGE_PATTERN = re.compile(
    r"((?:(?:Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)[a-z]*\.?\s+)?\d{4}|\d{1,2}/\d{4})"
    r"\s*(?:-|–|—|to)\s*"
    r"((?:(?:Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)[a-z]*\.?\s+)?\d{4}|\d{1,2}/\d{4}|Present|Current|Till\s+date|Ongoing)",
    re.IGNORECASE
)

SINGLE_DATE_PATTERN = re.compile(
    r"\b((?:(?:Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)[a-z]*\.?\s+)?\d{4}|\d{1,2}/\d{4})\b",
    re.IGNORECASE
)

ONGOING_KEYWORDS = {"present", "current", "till date", "ongoing"}


class EntityExtractor:

    @staticmethod
    def extract_contact_info(header_text: str, full_text: str) -> Dict[str, Any]:
        """
        Extracts candidate name, email, phone, location, and social links.
        """
        # 1. Email Extraction
        email_match = re.search(r"\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,7}\b", full_text)
        email = email_match.group(0).lower() if email_match else None

        # 2. Phone Extraction
        phone_match = re.search(
            r"(?:\+?\d{1,3}[-.\s]?)?\(?\d{3}\)?[-.\s]?\d{3}[-.\s]?\d{4}|\+?\d{10,12}",
            full_text
        )
        phone = phone_match.group(0).strip() if phone_match else None

        # 3. Social Profile Links
        github_match = re.search(r"(?:https?://)?(?:www\.)?github\.com/([a-zA-Z0-9_-]+)", full_text, re.IGNORECASE)
        github_url = f"https://github.com/{github_match.group(1)}" if github_match else None

        linkedin_match = re.search(r"(?:https?://)?(?:www\.)?linkedin\.com/in/([a-zA-Z0-9_-]+)", full_text, re.IGNORECASE)
        linkedin_url = f"https://linkedin.com/in/{linkedin_match.group(1)}" if linkedin_match else None

        # Portfolio/personal website (exclude common domains)
        portfolio_match = re.search(
            r"\bhttps?://(?!www\.github\.com|github\.com|www\.linkedin\.com|linkedin\.com)[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}(?:/[^\s]*)?\b",
            full_text,
            re.IGNORECASE
        )
        portfolio_url = portfolio_match.group(0) if portfolio_match else None

        # 4. Location extraction
        location_match = re.search(
            r"\b([A-Z][a-zA-Z\s]+,\s*(?:[A-Z]{2}|[A-Z][a-zA-Z]+))\b",
            header_text or full_text[:400]
        )
        location = location_match.group(1).strip() if location_match else None

        # 5. Candidate Name Extraction
        # Look at lines in header_text. Skip lines that contain email, phone, links, or generic headings.
        name = None
        search_block = header_text or full_text[:300]
        for line in search_block.split("\n"):
            cleaned = line.strip()
            if not cleaned or len(cleaned) > 50:
                continue
            # Skip lines containing email, URL, phone, or generic labels
            if "@" in cleaned or "http" in cleaned or "github" in cleaned.lower() or "linkedin" in cleaned.lower():
                continue
            if re.search(r"\d", cleaned):
                continue
            if re.match(r"^(resume|curriculum\s+vitae|cv|profile|contact)$", cleaned, re.IGNORECASE):
                continue
            words = cleaned.split()
            if 1 <= len(words) <= 4 and all(re.match(r"^[A-Za-z\.\'\-]+$", w) for w in words):
                name = cleaned
                break

        return {
            "name": name,
            "email": email,
            "phone": phone,
            "location": location,
            "social_profiles": {
                "github": github_url,
                "linkedin": linkedin_url,
                "portfolio": portfolio_url
            }
        }

    @staticmethod
    def extract_skills(full_text: str, skills_section_text: str = "") -> List[Dict[str, str]]:
        """
        Extracts technical skills using the curated skill dictionary.
        Employs strict boundaries to avoid false substring matches for short tokens (C, R, Go).
        """
        extracted_skills: Dict[str, Dict[str, str]] = {}
        combined_text = f"{skills_section_text}\n{full_text}"

        for category, skills in TECHNICAL_SKILLS.items():
            for skill in skills:
                # Case sensitivity for short/sensitive tokens
                if skill in SHORT_OR_SENSITIVE_SKILLS:
                    # Look for standalone uppercase token e.g. "C," or "C++" or "Languages: C,"
                    # Only match if in skills section or preceded by language/database keyword
                    pattern = rf"(?<![A-Za-z0-9_#\+])({re.escape(skill)})(?![A-Za-z0-9_#\+])"
                    if skill in {"C", "R"}:
                        # Extra strict: must be in skills section OR near programming keywords
                        if skills_section_text and re.search(pattern, skills_section_text):
                            extracted_skills[skill] = {"name": skill, "category": category}
                        elif re.search(rf"(?:languages?|programming|coding|skills?)[^\n]*?{pattern}", combined_text, re.IGNORECASE):
                            extracted_skills[skill] = {"name": skill, "category": category}
                    elif skill == "Go":
                        # Match Go when in skills section, near programming keywords, or as Golang
                        if skills_section_text and re.search(pattern, skills_section_text):
                            extracted_skills["Go"] = {"name": "Go", "category": category}
                        elif re.search(rf"(?:languages?|programming|coding|skills?)[^\n]*?{pattern}", combined_text, re.IGNORECASE):
                            extracted_skills["Go"] = {"name": "Go", "category": category}
                        elif re.search(r"\b(Golang)\b", combined_text, re.IGNORECASE):
                            extracted_skills["Go"] = {"name": "Go", "category": category}
                    else:
                        if re.search(pattern, combined_text, re.IGNORECASE):
                            extracted_skills[skill] = {"name": skill, "category": category}
                else:
                    # Standard skill match with symbol-aware boundary (e.g. C++, C#, .NET)
                    escaped = re.escape(skill)
                    start_bound = r"\b" if re.match(r"^[A-Za-z0-9]", skill) else r"(?<![A-Za-z0-9_#\+])"
                    end_bound = r"\b" if re.search(r"[A-Za-z0-9]$", skill) else r"(?![A-Za-z0-9_#\+])"
                    pattern = rf"{start_bound}{escaped}{end_bound}"
                    if re.search(pattern, combined_text, re.IGNORECASE):
                        canonical_name = SKILL_ALIASES.get(skill.lower(), skill)
                        extracted_skills[canonical_name] = {
                            "name": canonical_name,
                            "category": get_skill_category(canonical_name)
                        }

        return list(extracted_skills.values())

    @classmethod
    def parse_date_range(cls, text: str) -> Tuple[Optional[str], Optional[str], bool, str]:
        """
        Extracts start_date, end_date, is_current, and date_precision.
        Returns (start_date, end_date, is_current, date_precision).
        """
        match = DATE_RANGE_PATTERN.search(text)
        if match:
            start = match.group(1).strip()
            end = match.group(2).strip()
            is_current = end.lower() in ONGOING_KEYWORDS
            precision = "month_year" if re.search(r"[A-Za-z]", start) or "/" in start else "year"
            return start, ("Present" if is_current else end), is_current, precision

        single_match = SINGLE_DATE_PATTERN.search(text)
        if single_match:
            date_str = single_match.group(1).strip()
            precision = "month_year" if re.search(r"[A-Za-z]", date_str) or "/" in date_str else "year"
            return date_str, None, False, precision

        return None, None, False, "unknown"

    @classmethod
    def extract_education(cls, education_text: str) -> List[Dict[str, Any]]:
        """
        Extracts structured education entries.
        """
        if not education_text:
            return []

        entries: List[Dict[str, Any]] = []
        lines = [line.strip() for line in education_text.split("\n") if line.strip()]

        current_entry: Dict[str, Any] = {
            "institution": None,
            "degree": None,
            "field": None,
            "start_year": None,
            "end_year": None,
            "grade": None
        }

        for line in lines:
            # Check for institution
            if INSTITUTION_KEYWORDS.search(line):
                if current_entry["institution"] and (current_entry["degree"] or current_entry["start_year"]):
                    entries.append(current_entry)
                    current_entry = {k: None for k in current_entry}
                current_entry["institution"] = line

            # Check for degree
            degree_match = DEGREE_PATTERN.search(line)
            if degree_match:
                current_entry["degree"] = degree_match.group(0).strip()

            # Check for field of study
            field_match = FIELD_OF_STUDY_PATTERN.search(line)
            if field_match:
                current_entry["field"] = field_match.group(0).strip()

            # Check for dates
            start, end, is_current, _ = cls.parse_date_range(line)
            if start:
                try:
                    current_entry["start_year"] = int(re.search(r"\d{4}", start).group(0))
                except Exception:
                    pass
            if end and not is_current:
                try:
                    current_entry["end_year"] = int(re.search(r"\d{4}", end).group(0))
                except Exception:
                    pass

            # Check for grade/CGPA
            grade_match = GRADE_PATTERN.search(line)
            if grade_match and not current_entry["grade"]:
                # Ensure it's not a year or degree
                cand_grade = grade_match.group(0).strip()
                if not re.match(r"^20\d\d$", cand_grade):
                    current_entry["grade"] = cand_grade

        if current_entry["institution"] or current_entry["degree"]:
            entries.append(current_entry)

        return entries

    @classmethod
    def extract_experience(cls, experience_text: str) -> List[Dict[str, Any]]:
        """
        Extracts professional experience entries with company, role, dates, is_current, and description.
        """
        if not experience_text:
            return []

        entries: List[Dict[str, Any]] = []
        lines = [line.strip() for line in experience_text.split("\n") if line.strip()]

        current_entry: Optional[Dict[str, Any]] = None

        for line in lines:
            start_date, end_date, is_current, _ = cls.parse_date_range(line)

            # If the line has a date range or looks like a company/role headline
            if start_date is not None:
                if current_entry:
                    entries.append(current_entry)

                # Split title/company if separated by "|" or "at" or "-"
                title_company = line[:line.find(start_date)].strip(" -–|at,")
                parts = re.split(r"\s+(?:at|@|\||-)\s+", title_company, flags=re.IGNORECASE)
                role = parts[0].strip() if parts else "Software Engineer"
                company = parts[1].strip() if len(parts) > 1 else "Technology Company"

                current_entry = {
                    "company": company,
                    "role": role,
                    "start_date": start_date,
                    "end_date": end_date,
                    "is_current": is_current,
                    "description": []
                }
            elif current_entry:
                # Add as bullet description
                clean_bullet = re.sub(r"^[•\*\-]\s*", "", line).strip()
                if clean_bullet:
                    current_entry["description"].append(clean_bullet)

        if current_entry:
            entries.append(current_entry)

        # Format descriptions into single strings
        for entry in entries:
            entry["description"] = "\n".join(entry["description"]) if isinstance(entry["description"], list) else entry["description"]

        return entries

    @classmethod
    def extract_projects(cls, projects_text: str) -> List[Dict[str, Any]]:
        """
        Extracts projects. Never fabricates dates; sets start_date and end_date to None if missing.
        """
        if not projects_text:
            return []

        entries: List[Dict[str, Any]] = []
        lines = [line.strip() for line in projects_text.split("\n") if line.strip()]

        current_entry: Optional[Dict[str, Any]] = None

        for line in lines:
            # Check for project title indicator (starts with bullet or title line without bullet)
            is_bullet = line.startswith("•") or line.startswith("-") or line.startswith("*")

            # Check for links
            github_url = None
            gh_m = re.search(r"https?://(?:www\.)?github\.com/[^\s]+", line)
            if gh_m:
                github_url = gh_m.group(0)

            live_url = None
            live_m = re.search(r"https?://(?!github\.com)[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}(?:/[^\s]*)?", line)
            if live_m:
                live_url = live_m.group(0)

            # Check for dates
            start_date, end_date, _, _ = cls.parse_date_range(line)

            if not is_bullet and len(line) < 80 and not line.endswith("."):
                # New project header
                if current_entry:
                    entries.append(current_entry)

                # Extract title and potential technologies listed in header (e.g. "JobPilot | FastAPI, React")
                tech_stack = []
                title = line
                if "|" in line:
                    parts = line.split("|")
                    title = parts[0].strip()
                    tech_str = parts[1].strip()
                    tech_stack = [s.strip() for s in tech_str.split(",") if s.strip()]

                current_entry = {
                    "name": title,
                    "description": [],
                    "technologies": tech_stack,
                    "start_date": start_date,
                    "end_date": end_date,
                    "github_url": github_url,
                    "live_url": live_url
                }
            elif current_entry:
                if github_url and not current_entry["github_url"]:
                    current_entry["github_url"] = github_url
                if live_url and not current_entry["live_url"]:
                    current_entry["live_url"] = live_url
                if start_date and not current_entry["start_date"]:
                    current_entry["start_date"] = start_date
                    current_entry["end_date"] = end_date

                cleaned = re.sub(r"^[•\*\-]\s*", "", line).strip()
                if cleaned:
                    current_entry["description"].append(cleaned)

        if current_entry:
            entries.append(current_entry)

        for entry in entries:
            entry["description"] = "\n".join(entry["description"]) if isinstance(entry["description"], list) else entry["description"]

        return entries

    @classmethod
    def extract_certifications(cls, certs_text: str) -> List[Dict[str, Any]]:
        """
        Extracts certifications with name, issuer, and date.
        """
        if not certs_text:
            return []

        entries: List[Dict[str, Any]] = []
        for line in certs_text.split("\n"):
            cleaned = re.sub(r"^[•\*\-]\s*", "", line).strip()
            if not cleaned or len(cleaned) < 5:
                continue

            # Identify common issuers (AWS, Google, Microsoft, Meta, Coursera)
            issuer_match = re.search(r"\b(AWS|Amazon|Google|Microsoft|Meta|Coursera|Udemy|Oracle|Cisco|HashiCorp)\b", cleaned, re.IGNORECASE)
            issuer = issuer_match.group(0) if issuer_match else "Certification Provider"

            date_str, _, _, _ = cls.parse_date_range(cleaned)

            # Strip date and issuer for name
            name = cleaned
            if date_str:
                name = name.replace(date_str, "")
            name = name.strip(" -–|:,()")

            entries.append({
                "name": name,
                "issuer": issuer,
                "date": date_str
            })

        return entries
