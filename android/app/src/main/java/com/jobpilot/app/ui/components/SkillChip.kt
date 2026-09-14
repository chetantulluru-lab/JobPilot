package com.jobpilot.app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jobpilot.app.ui.theme.*

enum class ChipVariant {
    STRONG_MATCH,
    MISSING_GAP,
    NEUTRAL
}

@Composable
fun SkillChip(
    skillName: String,
    variant: ChipVariant = ChipVariant.NEUTRAL,
    modifier: Modifier = Modifier
) {
    val isDark = MaterialTheme.colorScheme.background == Color(0xFF0F172A)
    val (bgColor, textColor, borderColor) = when (variant) {
        ChipVariant.STRONG_MATCH -> Triple(
            if (isDark) Color(0xFF064E3B) else SuccessGreenBg,
            if (isDark) Color(0xFF6EE7B7) else SuccessGreen,
            SuccessGreen.copy(alpha = 0.4f)
        )
        ChipVariant.MISSING_GAP -> Triple(
            if (isDark) Color(0xFF7C2D12) else GapOrangeBg,
            if (isDark) Color(0xFFFDBA74) else GapOrange,
            GapOrange.copy(alpha = 0.4f)
        )
        ChipVariant.NEUTRAL -> Triple(
            if (isDark) Color(0xFF334155) else Slate100,
            if (isDark) Color.White else Slate700,
            if (isDark) Color(0xFF475569) else Slate200
        )
    }

    Surface(
        modifier = modifier,
        shape = ChipShape,
        color = bgColor,
        border = BorderStroke(1.dp, borderColor)
    ) {

        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            when (variant) {
                ChipVariant.STRONG_MATCH -> {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Matched",
                        tint = SuccessGreen,
                        modifier = Modifier.padding(end = 4.dp)
                    )
                }
                ChipVariant.MISSING_GAP -> {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Missing",
                        tint = GapOrange,
                        modifier = Modifier.padding(end = 4.dp)
                    )
                }
                ChipVariant.NEUTRAL -> {}
            }
            Text(
                text = skillName,
                color = textColor,
                fontSize = 12.sp,
                style = JobPilotTypography.labelMedium
            )
        }
    }
}
