"""
Section splitter for JobPilot NLP engine.
Detects common resume section headings and segments normalized text into functional blocks.
"""

import re
from typing import Dict, List, Tuple

# Section category definitions with regex patterns for heading variations
SECTION_PATTERNS: Dict[str, List[str]] = {
    "SUMMARY": [
        r"summary", r"professional\s+summary", r"executive\s+summary", r"career\s+summary",
        r"objective", r"career\s+objective", r"about\s+me", r"profile", r"professional\s+profile"
    ],
    "EDUCATION": [
        r"education", r"academic\s+background", r"academic\s+qualifications",
        r"academic\s+history", r"educational\s+background", r"degrees", r"academics"
    ],
    "SKILLS": [
        r"skills", r"technical\s+skills", r"core\s+competencies", r"key\s+skills",
        r"skills\s*&\s*competencies", r"technologies", r"tech\s+stack",
        r"tools\s*&\s*technologies", r"professional\s+skills", r"technical\s+proficiencies",
        r"areas\s+of\s+expertise"
    ],
    "EXPERIENCE": [
        r"experience", r"work\s+experience", r"professional\s+experience",
        r"employment", r"employment\s+history", r"work\s+history",
        r"internships?", r"professional\s+background", r"career\s+history"
    ],
    "PROJECTS": [
        r"projects", r"personal\s+projects", r"academic\s+projects",
        r"key\s+projects", r"technical\s+projects", r"featured\s+projects"
    ],
    "CERTIFICATIONS": [
        r"certifications?", r"licenses\s*&\s*certifications?", r"certificates?",
        r"professional\s+certifications?", r"accreditations?"
    ],
    "ACHIEVEMENTS": [
        r"achievements?", r"awards?", r"honors?", r"honors?\s*&\s*awards?",
        r"accomplishments?", r"extracurricular\s+activities"
    ],
    "PUBLICATIONS": [
        r"publications?", r"research\s+papers?", r"research"
    ],
    "LANGUAGES": [
        r"languages?", r"known\s+languages?", r"language\s+proficiency"
    ],
    "LINKS": [
        r"links?", r"social\s+profiles?", r"profiles?", r"online\s+presence", r"websites?"
    ]
}


class SectionSplitter:
    @classmethod
    def match_heading(cls, line: str) -> Tuple[str, bool]:
        """
        Determines if a line is a section heading.
        Returns (section_name, is_match).
        """
        cleaned = line.strip()
        # Headings are typically short (< 45 chars) and don't end in full stops
        if not cleaned or len(cleaned) > 45 or cleaned.endswith((".", ",", ";")):
            return "", False

        # Strip trailing colon if present (e.g., "EXPERIENCE:")
        candidate = re.sub(r"[:\-_]+$", "", cleaned).strip()

        for section_name, patterns in SECTION_PATTERNS.items():
            for pattern in patterns:
                # Matches exact standalone line, or with leading/trailing markers like #, ##, --
                regex = rf"^(?:[#\*\-]+\s*)?{pattern}(?:\s*[#\*\-:]*)?$"
                if re.match(regex, candidate, re.IGNORECASE):
                    return section_name, True

        return "", False

    @classmethod
    def split_into_sections(cls, normalized_text: str) -> Dict[str, str]:
        """
        Segments normalized resume text into distinct functional sections.
        Returns a dictionary mapping section keys to their corresponding raw text.
        """
        lines = normalized_text.split("\n")
        sections: Dict[str, List[str]] = {
            "HEADER": [],
            "SUMMARY": [],
            "EDUCATION": [],
            "SKILLS": [],
            "EXPERIENCE": [],
            "PROJECTS": [],
            "CERTIFICATIONS": [],
            "ACHIEVEMENTS": [],
            "PUBLICATIONS": [],
            "LANGUAGES": [],
            "LINKS": []
        }

        current_section = "HEADER"

        for line in lines:
            trimmed = line.strip()
            if not trimmed:
                continue

            matched_sec, is_heading = cls.match_heading(trimmed)
            if is_heading:
                current_section = matched_sec
                continue

            sections[current_section].append(trimmed)

        # Join lines for each section
        return {sec: "\n".join(content_lines).strip() for sec, content_lines in sections.items()}
