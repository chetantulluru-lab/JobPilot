package com.jobpilot.app.util

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import com.jobpilot.app.data.model.ResumeTemplateType
import java.io.OutputStream

data class AtsResumeProject(
    val name: String,
    val tech: String,
    val description: String,
    val link: String = ""
)

data class AtsResumeData(
    val fullName: String,
    val email: String,
    val phone: String,
    val location: String,
    val githubUrl: String = "",
    val linkedinUrl: String = "",
    val summary: String = "",
    val college: String = "",
    val degree: String = "",
    val branch: String = "",
    val gradYear: String = "",
    val cgpa: String = "",
    val skills: List<String> = emptyList(),
    val projects: List<AtsResumeProject> = emptyList(),
    val experience: String = "",
    val achievements: String = "",
    val templateType: ResumeTemplateType = ResumeTemplateType.MODERN
)

/**
 * High-performance, single-page ATS-compliant PDF generator using Android native PdfDocument.
 * Produces standard A4 (595 x 842 pt) single-page recruiter-friendly resumes.
 */
object AtsResumePdfGenerator {

    private const val PAGE_WIDTH = 595
    private const val PAGE_HEIGHT = 842
    private const val MARGIN_LEFT = 40f
    private const val MARGIN_RIGHT = 555f
    private const val CONTENT_WIDTH = 515

    fun generate(data: AtsResumeData, outputStream: OutputStream) {
        val document = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, 1).create()
        val page = document.startPage(pageInfo)
        val canvas: Canvas = page.canvas

        // Background: clean crisp white
        canvas.drawColor(Color.WHITE)

        // Choose Accent Color based on template
        val accentColor = when (data.templateType) {
            ResumeTemplateType.MODERN -> Color.rgb(255, 106, 0) // Orange500
            ResumeTemplateType.MINIMAL -> Color.rgb(15, 23, 42) // Slate900
            ResumeTemplateType.PROFESSIONAL -> Color.rgb(2, 132, 199) // InfoBlue
            ResumeTemplateType.EXECUTIVE -> Color.rgb(51, 65, 85) // Slate700
        }

        val textPaint = TextPaint().apply {
            isAntiAlias = true
            color = Color.rgb(15, 23, 42)
            textSize = 9.5f
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
        }

        val boldPaint = TextPaint().apply {
            isAntiAlias = true
            color = Color.rgb(15, 23, 42)
            textSize = 9.5f
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
        }

        val namePaint = TextPaint().apply {
            isAntiAlias = true
            color = Color.rgb(15, 23, 42)
            textSize = 20f
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
        }

        val sectionHeadingPaint = TextPaint().apply {
            isAntiAlias = true
            color = accentColor
            textSize = 10.5f
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
        }

        val dividerPaint = Paint().apply {
            color = accentColor
            strokeWidth = 1.2f
            style = Paint.Style.STROKE
        }

        val subtleLinePaint = Paint().apply {
            color = Color.rgb(226, 232, 240)
            strokeWidth = 0.8f
            style = Paint.Style.STROKE
        }

        var currentY = 40f

        // 1. Header: Full Name
        val displayName = data.fullName.ifBlank { "Candidate Name" }
        canvas.drawText(displayName, MARGIN_LEFT, currentY, namePaint)
        currentY += 15f

        // 2. Contact Line
        val contactParts = mutableListOf<String>()
        if (data.email.isNotBlank()) contactParts.add(data.email)
        if (data.phone.isNotBlank()) contactParts.add(data.phone)
        if (data.location.isNotBlank()) contactParts.add(data.location)
        if (data.linkedinUrl.isNotBlank()) contactParts.add("LinkedIn: ${data.linkedinUrl}")
        if (data.githubUrl.isNotBlank()) contactParts.add("GitHub: ${data.githubUrl}")

        val contactText = contactParts.joinToString("  |  ")
        val contactLayout = StaticLayout.Builder.obtain(
            contactText,
            0,
            contactText.length,
            textPaint,
            CONTENT_WIDTH
        ).setAlignment(Layout.Alignment.ALIGN_NORMAL).build()

        canvas.save()
        canvas.translate(MARGIN_LEFT, currentY)
        contactLayout.draw(canvas)
        canvas.restore()
        currentY += contactLayout.height + 10f

        // Top Accent Divider Line
        canvas.drawLine(MARGIN_LEFT, currentY, MARGIN_RIGHT, currentY, dividerPaint)
        currentY += 14f

        fun drawSectionHeader(title: String) {
            canvas.drawText(title.uppercase(), MARGIN_LEFT, currentY, sectionHeadingPaint)
            currentY += 4f
            canvas.drawLine(MARGIN_LEFT, currentY, MARGIN_RIGHT, currentY, subtleLinePaint)
            currentY += 10f
        }

        // 3. Professional Summary (if present)
        if (data.summary.isNotBlank()) {
            drawSectionHeader("Professional Summary")
            val summaryLayout = StaticLayout.Builder.obtain(
                data.summary,
                0,
                data.summary.length,
                textPaint,
                CONTENT_WIDTH
            ).setAlignment(Layout.Alignment.ALIGN_NORMAL).build()

            canvas.save()
            canvas.translate(MARGIN_LEFT, currentY)
            summaryLayout.draw(canvas)
            canvas.restore()
            currentY += summaryLayout.height + 12f
        }

        // 4. Education
        if (data.degree.isNotBlank() || data.college.isNotBlank()) {
            drawSectionHeader("Education")
            val eduHeading = buildString {
                if (data.degree.isNotBlank()) append(data.degree)
                if (data.branch.isNotBlank()) append(" in ${data.branch}")
            }
            if (eduHeading.isNotBlank()) {
                canvas.drawText(eduHeading, MARGIN_LEFT, currentY, boldPaint)
            }

            val rightEdu = buildString {
                if (data.gradYear.isNotBlank()) append("Graduation: ${data.gradYear}")
                if (data.cgpa.isNotBlank()) {
                    if (isNotEmpty()) append("  |  ")
                    append("CGPA: ${data.cgpa}")
                }
            }
            if (rightEdu.isNotBlank()) {
                val rightWidth = textPaint.measureText(rightEdu)
                canvas.drawText(rightEdu, MARGIN_RIGHT - rightWidth, currentY, textPaint)
            }
            currentY += 13f

            if (data.college.isNotBlank()) {
                canvas.drawText(data.college, MARGIN_LEFT, currentY, textPaint)
                currentY += 14f
            }
        }

        // 5. Technical Skills
        if (data.skills.isNotEmpty()) {
            drawSectionHeader("Technical Skills")
            val skillsText = data.skills.joinToString("  •  ")
            val skillsLayout = StaticLayout.Builder.obtain(
                skillsText,
                0,
                skillsText.length,
                textPaint,
                CONTENT_WIDTH
            ).setAlignment(Layout.Alignment.ALIGN_NORMAL).build()

            canvas.save()
            canvas.translate(MARGIN_LEFT, currentY)
            skillsLayout.draw(canvas)
            canvas.restore()
            currentY += skillsLayout.height + 12f
        }

        // 6. Projects
        if (data.projects.isNotEmpty()) {
            drawSectionHeader("Key Technical Projects")
            for (proj in data.projects.take(3)) {
                if (proj.name.isBlank()) continue

                // Project title + Tech
                val pTitle = buildString {
                    append(proj.name)
                    if (proj.tech.isNotBlank()) append("  |  ${proj.tech}")
                }
                canvas.drawText(pTitle, MARGIN_LEFT, currentY, boldPaint)

                if (proj.link.isNotBlank()) {
                    val linkText = proj.link
                    val linkWidth = textPaint.measureText(linkText)
                    if (MARGIN_RIGHT - linkWidth > MARGIN_LEFT + 200) {
                        canvas.drawText(linkText, MARGIN_RIGHT - linkWidth, currentY, textPaint)
                    }
                }
                currentY += 13f

                // Bullet description
                if (proj.description.isNotBlank()) {
                    val bulletText = "•  ${proj.description}"
                    val descLayout = StaticLayout.Builder.obtain(
                        bulletText,
                        0,
                        bulletText.length,
                        textPaint,
                        CONTENT_WIDTH - 10
                    ).setAlignment(Layout.Alignment.ALIGN_NORMAL).build()

                    canvas.save()
                    canvas.translate(MARGIN_LEFT + 8f, currentY)
                    descLayout.draw(canvas)
                    canvas.restore()
                    currentY += descLayout.height + 6f
                }
            }
            currentY += 6f
        }

        // 7. Experience / Achievements
        if (data.experience.isNotBlank() || data.achievements.isNotBlank()) {
            drawSectionHeader("Experience & Key Achievements")
            if (data.experience.isNotBlank()) {
                val expText = "•  ${data.experience}"
                val expLayout = StaticLayout.Builder.obtain(
                    expText,
                    0,
                    expText.length,
                    textPaint,
                    CONTENT_WIDTH - 10
                ).setAlignment(Layout.Alignment.ALIGN_NORMAL).build()

                canvas.save()
                canvas.translate(MARGIN_LEFT + 8f, currentY)
                expLayout.draw(canvas)
                canvas.restore()
                currentY += expLayout.height + 6f
            }

            if (data.achievements.isNotBlank()) {
                val achText = "•  ${data.achievements}"
                val achLayout = StaticLayout.Builder.obtain(
                    achText,
                    0,
                    achText.length,
                    textPaint,
                    CONTENT_WIDTH - 10
                ).setAlignment(Layout.Alignment.ALIGN_NORMAL).build()

                canvas.save()
                canvas.translate(MARGIN_LEFT + 8f, currentY)
                achLayout.draw(canvas)
                canvas.restore()
                currentY += achLayout.height + 6f
            }
        }

        // Footer: Subtle ATS Verification Watermark
        val footerText = "Generated via JobPilot ATS Engine  |  Single Page Standard A4"
        val footerWidth = textPaint.measureText(footerText)
        val footerPaint = TextPaint().apply {
            isAntiAlias = true
            color = Color.rgb(148, 163, 184)
            textSize = 8f
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
        }
        canvas.drawText(footerText, (PAGE_WIDTH - footerWidth) / 2f, PAGE_HEIGHT - 25f, footerPaint)

        document.finishPage(page)
        document.writeTo(outputStream)
        document.close()
    }
}
