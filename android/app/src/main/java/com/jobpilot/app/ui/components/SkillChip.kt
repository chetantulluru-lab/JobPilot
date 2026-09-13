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
    val (bgColor, textColor, borderColor) = when (variant) {
        ChipVariant.STRONG_MATCH -> Triple(SuccessGreenBg, SuccessGreen, SuccessGreen.copy(alpha = 0.4f))
        ChipVariant.MISSING_GAP -> Triple(GapOrangeBg, GapOrange, GapOrange.copy(alpha = 0.4f))
        ChipVariant.NEUTRAL -> Triple(Slate100, Slate700, Slate200)
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
