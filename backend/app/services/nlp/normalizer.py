"""
Text normalization layer for JobPilot NLP engine.
Cleans raw extracted text, standardizes formatting artifacts, and preserves critical entity patterns.
"""

import re


class TextNormalizer:
    @staticmethod
    def normalize(text: str) -> str:
        if not text:
            return ""

        # 1. Unify line breaks
        normalized = text.replace("\r\n", "\n").replace("\r", "\n")

        # 2. Replace non-breaking spaces and special whitespace
        normalized = normalized.replace("\xa0", " ").replace("\u200b", "")

        # 3. Standardize tabs to spaces
        normalized = normalized.replace("\t", "    ")

        # 4. Standardize dashes (en-dash, em-dash to hyphen)
        normalized = re.sub(r"[\u2013\u2014\u2212]", "-", normalized)

        # 5. Standardize bullet symbols at the start of lines/clauses
        # e.g. •, ▪, ▫, ■, ●, ◦, ♦, *, -, +
        normalized = re.sub(r"^[ \t]*[•▪▫■●◦♦\*]\s*", "• ", normalized, flags=re.MULTILINE)
        normalized = re.sub(r"^[ \t]*-\s+(?=[A-Z0-9])", "• ", normalized, flags=re.MULTILINE)

        # 6. Normalize multiple spaces within a line (without merging across newlines)
        lines = []
        for line in normalized.split("\n"):
            # Collapse consecutive horizontal spaces
            cleaned_line = re.sub(r"[^\S\n]+", " ", line).strip()
            lines.append(cleaned_line)

        reconstructed = "\n".join(lines)

        # 7. Collapse 3+ consecutive newlines into 2 (clean paragraph separation)
        reconstructed = re.sub(r"\n{3,}", "\n\n", reconstructed)

        return reconstructed.strip()
