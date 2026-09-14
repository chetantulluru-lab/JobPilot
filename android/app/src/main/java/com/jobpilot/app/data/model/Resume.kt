package com.jobpilot.app.data.model

enum class ResumeTemplateType(
    val templateName: String,
    val description: String,
    val recommendedFor: String,
    val accentColorHex: String
) {
    MINIMAL(
        templateName = "Minimal",
        description = "Clean black-and-white layout with high readability and optimal whitespace.",
        recommendedFor = "Software Engineers, Backend Developers",
        accentColorHex = "#0F172A"
    ),
    MODERN(
        templateName = "Modern",
        description = "Contemporary styling with subtle warm orange highlights and clean two-column header.",
        recommendedFor = "Full Stack, AI/ML Engineers, Product Seekers",
        accentColorHex = "#FF6A00"
    ),
    PROFESSIONAL(
        templateName = "Professional",
        description = "Structured corporate layout emphasizing chronological achievements and metrics.",
        recommendedFor = "Enterprise roles, Consultancies, Data Analysts",
        accentColorHex = "#0284C7"
    ),
    EXECUTIVE(
        templateName = "Executive",
        description = "Distinguished serif typography and prominent leadership summary block.",
        recommendedFor = "Senior roles, Technical Leads, Research Fellows",
        accentColorHex = "#475569"
    )
}

data class Resume(
    val id: String,
    val title: String,
    val templateType: ResumeTemplateType,
    val lastModified: String,
    val profileSnapshot: CareerProfile,
    val isDefault: Boolean = false,
    val tailoredForJobTitle: String? = null
)

data class ResumeParsedData(
    val resumeId: String? = null,
    val detectedName: String,
    val detectedEmail: String,
    val detectedPhone: String,
    val detectedEducation: List<Education>,
    val detectedSkills: List<Skill>,
    val detectedExperience: List<Experience>,
    val detectedProjects: List<Project>,
    val missingFields: List<MissingField>
)

data class MissingField(
    val fieldKey: String,
    val fieldLabel: String,
    val reason: String,
    val suggestedAction: String
)

data class ResumeAnalysis(
    val resumeId: String,
    val atsScore: Int,
    val label: String = "AI-Powered ATS-Style Analysis",
    val summary: String,
    val strengths: List<String> = emptyList(),
    val weaknesses: List<String> = emptyList(),
    val missingSkills: List<String> = emptyList(),
    val contentImprovements: List<String> = emptyList(),
    val formattingNotes: List<String> = emptyList(),
    val disclaimer: String = "Informational guidance based on industry standards. JobPilot makes no employment or interview guarantees."
)
