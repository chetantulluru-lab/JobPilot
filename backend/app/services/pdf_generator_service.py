"""
ATS-Friendly PDF Generator for JobPilot AI Resume Builder.
Generates genuine, selectable PDF documents across 4 distinct professional templates
using ReportLab without external paid dependencies.
"""

import io
from typing import Dict, List, Optional, Any
from reportlab.lib.pagesizes import letter
from reportlab.lib import colors
from reportlab.lib.styles import getSampleStyleSheet, ParagraphStyle
from reportlab.platypus import SimpleDocTemplate, Paragraph, Spacer, Table, TableStyle, HRFlowable
from reportlab.lib.enums import TA_CENTER, TA_LEFT, TA_RIGHT


# Template styling presets
TEMPLATE_CONFIGS = {
    "Minimal": {
        "primary_color": colors.HexColor("#1E293B"),   # Deep Slate
        "accent_color": colors.HexColor("#475569"),    # Medium Slate
        "line_color": colors.HexColor("#CBD5E1"),      # Light Gray
        "font_family": "Helvetica",
        "has_accent_bar": False,
    },
    "Modern": {
        "primary_color": colors.HexColor("#FF6B00"),   # JobPilot Vibrant Orange
        "accent_color": colors.HexColor("#0F172A"),    # Dark Slate
        "line_color": colors.HexColor("#FFD8B2"),      # Soft Orange line
        "font_family": "Helvetica",
        "has_accent_bar": True,
    },
    "Professional": {
        "primary_color": colors.HexColor("#1E3A8A"),   # Corporate Navy
        "accent_color": colors.HexColor("#334155"),    # Slate
        "line_color": colors.HexColor("#93C5FD"),      # Blue line
        "font_family": "Helvetica",
        "has_accent_bar": True,
    },
    "Executive": {
        "primary_color": colors.HexColor("#0F172A"),   # Rich Charcoal
        "accent_color": colors.HexColor("#0284C7"),    # Executive Cyan
        "line_color": colors.HexColor("#0284C7"),      # Accent divider
        "font_family": "Helvetica",
        "has_accent_bar": True,
    }
}


class ResumePdfGenerator:
    @classmethod
    def generate_pdf(
        cls,
        resume_data: Dict[str, Any],
        template_type: str = "Modern"
    ) -> bytes:
        """
        Builds and returns a complete ATS-friendly PDF document as raw bytes.
        """
        preset = TEMPLATE_CONFIGS.get(template_type, TEMPLATE_CONFIGS["Modern"])
        buffer = io.BytesIO()

        # Target 0.5 inch margins for ATS density and single/two-page compliance
        doc = SimpleDocTemplate(
            buffer,
            pagesize=letter,
            leftMargin=36,
            rightMargin=36,
            topMargin=36,
            bottomMargin=36
        )

        styles = getSampleStyleSheet()
        
        # Define clean, professional paragraph styles
        name_style = ParagraphStyle(
            "ResumeName",
            parent=styles["Normal"],
            fontName="Helvetica-Bold",
            fontSize=20,
            leading=24,
            textColor=preset["primary_color"],
            alignment=TA_CENTER
        )

        contact_style = ParagraphStyle(
            "ResumeContact",
            parent=styles["Normal"],
            fontName="Helvetica",
            fontSize=9,
            leading=12,
            textColor=colors.HexColor("#475569"),
            alignment=TA_CENTER
        )

        section_heading_style = ParagraphStyle(
            "ResumeSectionHeading",
            parent=styles["Normal"],
            fontName="Helvetica-Bold",
            fontSize=11,
            leading=14,
            textColor=preset["primary_color"],
            spaceAfter=3
        )

        body_style = ParagraphStyle(
            "ResumeBody",
            parent=styles["Normal"],
            fontName="Helvetica",
            fontSize=9,
            leading=13,
            textColor=colors.HexColor("#1E293B")
        )

        item_title_style = ParagraphStyle(
            "ResumeItemTitle",
            parent=styles["Normal"],
            fontName="Helvetica-Bold",
            fontSize=9.5,
            leading=13,
            textColor=colors.HexColor("#0F172A")
        )

        item_subtitle_style = ParagraphStyle(
            "ResumeItemSubtitle",
            parent=styles["Normal"],
            fontName="Helvetica-Oblique",
            fontSize=9,
            leading=12,
            textColor=colors.HexColor("#475569")
        )

        date_style = ParagraphStyle(
            "ResumeDate",
            parent=styles["Normal"],
            fontName="Helvetica",
            fontSize=8.5,
            leading=12,
            textColor=colors.HexColor("#64748B"),
            alignment=TA_RIGHT
        )

        story = []

        # 1. Header: Full Name and Contact Row
        contact = resume_data.get("contact") or {}
        name = contact.get("name") or resume_data.get("title") or "Candidate Name"
        story.append(Paragraph(name.upper(), name_style))
        story.append(Spacer(1, 4))

        contact_parts = []
        if contact.get("email"):
            contact_parts.append(contact["email"])
        if contact.get("phone"):
            contact_parts.append(contact["phone"])
        if contact.get("location"):
            contact_parts.append(contact["location"])
        if contact.get("linkedin"):
            contact_parts.append(f"LinkedIn: {contact['linkedin']}")
        if contact.get("github"):
            contact_parts.append(f"GitHub: {contact['github']}")
        if contact.get("portfolio"):
            contact_parts.append(contact["portfolio"])

        contact_text = " | ".join(contact_parts)
        if contact_text:
            story.append(Paragraph(contact_text, contact_style))
            story.append(Spacer(1, 8))

        # Divider line
        story.append(HRFlowable(width="100%", thickness=1.2, color=preset["line_color"], spaceBefore=2, spaceAfter=8))

        # 2. Professional Summary
        summary = resume_data.get("summary_text") or resume_data.get("summary")
        if summary:
            story.append(Paragraph("PROFESSIONAL SUMMARY", section_heading_style))
            story.append(HRFlowable(width="100%", thickness=0.6, color=preset["line_color"], spaceBefore=1, spaceAfter=4))
            story.append(Paragraph(summary, body_style))
            story.append(Spacer(1, 8))

        # 3. Technical Skills
        skills = resume_data.get("skills") or []
        if skills:
            story.append(Paragraph("TECHNICAL SKILLS", section_heading_style))
            story.append(HRFlowable(width="100%", thickness=0.6, color=preset["line_color"], spaceBefore=1, spaceAfter=4))
            
            skill_names = [s.get("name") if isinstance(s, dict) else str(s) for s in skills]
            skills_str = " • ".join(skill_names)
            story.append(Paragraph(skills_str, body_style))
            story.append(Spacer(1, 8))

        # 4. Work Experience
        experiences = resume_data.get("experience") or []
        if experiences:
            story.append(Paragraph("EXPERIENCE", section_heading_style))
            story.append(HRFlowable(width="100%", thickness=0.6, color=preset["line_color"], spaceBefore=1, spaceAfter=4))

            for exp in experiences:
                role = exp.get("title") or exp.get("role", "Role")
                company = exp.get("company", "Company")
                start = exp.get("start_date", "")
                end = "Present" if exp.get("is_current") else exp.get("end_date", "")
                date_str = f"{start} - {end}".strip(" - ")

                # Table for Role | Date
                row_table = Table(
                    [[Paragraph(f"{role} — {company}", item_title_style), Paragraph(date_str, date_style)]],
                    colWidths=[400, 140]
                )
                row_table.setStyle(TableStyle([
                    ("VALIGN", (0, 0), (-1, -1), "TOP"),
                    ("LEFTPADDING", (0, 0), (-1, -1), 0),
                    ("RIGHTPADDING", (0, 0), (-1, -1), 0),
                    ("TOPPADDING", (0, 0), (-1, -1), 1),
                    ("BOTTOMPADDING", (0, 0), (-1, -1), 1),
                ]))
                story.append(row_table)

                desc = exp.get("description")
                if desc:
                    # Render bullets if multi-line
                    for line in desc.split("\n"):
                        l = line.strip()
                        if l:
                            bullet = f"• {l}" if not l.startswith("•") and not l.startswith("-") else l
                            story.append(Paragraph(bullet, body_style))
                story.append(Spacer(1, 5))

            story.append(Spacer(1, 4))

        # 5. Projects
        projects = resume_data.get("projects") or []
        if projects:
            story.append(Paragraph("KEY PROJECTS", section_heading_style))
            story.append(HRFlowable(width="100%", thickness=0.6, color=preset["line_color"], spaceBefore=1, spaceAfter=4))

            for proj in projects:
                title = proj.get("title") or proj.get("name", "Project")
                tech = proj.get("tech_stack") or ", ".join(proj.get("technologies", []))
                tech_display = f" ({tech})" if tech else ""

                story.append(Paragraph(f"{title}{tech_display}", item_title_style))
                desc = proj.get("description")
                if desc:
                    for line in desc.split("\n"):
                        l = line.strip()
                        if l:
                            bullet = f"• {l}" if not l.startswith("•") and not l.startswith("-") else l
                            story.append(Paragraph(bullet, body_style))
                story.append(Spacer(1, 5))

            story.append(Spacer(1, 4))

        # 6. Education
        educations = resume_data.get("education") or []
        if educations:
            story.append(Paragraph("EDUCATION", section_heading_style))
            story.append(HRFlowable(width="100%", thickness=0.6, color=preset["line_color"], spaceBefore=1, spaceAfter=4))

            for edu in educations:
                degree = edu.get("degree", "Degree")
                field = edu.get("field_of_study") or edu.get("field", "")
                inst = edu.get("institution", "Institution")
                years = f"{edu.get('start_year', '')} - {edu.get('end_year', '')}".strip(" - ")

                row_table = Table(
                    [[Paragraph(f"{degree} in {field} — {inst}", item_title_style), Paragraph(years, date_style)]],
                    colWidths=[400, 140]
                )
                row_table.setStyle(TableStyle([
                    ("VALIGN", (0, 0), (-1, -1), "TOP"),
                    ("LEFTPADDING", (0, 0), (-1, -1), 0),
                    ("RIGHTPADDING", (0, 0), (-1, -1), 0),
                    ("TOPPADDING", (0, 0), (-1, -1), 1),
                    ("BOTTOMPADDING", (0, 0), (-1, -1), 1),
                ]))
                story.append(row_table)
                story.append(Spacer(1, 4))

        # Build PDF
        doc.build(story)
        pdf_bytes = buffer.getvalue()
        buffer.close()
        return pdf_bytes
