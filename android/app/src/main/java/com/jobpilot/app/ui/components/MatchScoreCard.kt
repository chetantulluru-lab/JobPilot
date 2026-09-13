package com.jobpilot.app.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jobpilot.app.ui.theme.*

@Composable
fun MatchScoreRing(
    score: Int,
    size: Dp = 64.dp,
    strokeWidth: Dp = 6.dp,
    modifier: Modifier = Modifier
) {
    val ringColor = when {
        score >= 90 -> Orange500
        score >= 75 -> Orange400
        score >= 60 -> WarningAmber
        else -> GapOrange
    }

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size)) {
            val strokePx = strokeWidth.toPx()
            val radius = (size.toPx() - strokePx) / 2f
            val sweepAngle = (score / 100f) * 360f

            // Background track
            drawCircle(
                color = Slate200.copy(alpha = 0.6f),
                radius = radius,
                style = Stroke(width = strokePx)
            )

            // Progress arc
            drawArc(
                color = ringColor,
                startAngle = -90f,
                sweepAngle = sweepAngle,
                useCenter = false,
                style = Stroke(width = strokePx, cap = StrokeCap.Round)
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "$score%",
                style = MaterialTheme.typography.titleMedium,
                color = Slate900,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun MatchScoreBadge(
    score: Int,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor) = when {
        score >= 90 -> Pair(SuccessGreenBg, SuccessGreen)
        score >= 75 -> Pair(Orange50, Orange600)
        score >= 60 -> Pair(WarningAmberBg, WarningAmber)
        else -> Pair(GapOrangeBg, GapOrange)
    }

    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(bgColor)
            .padding(horizontal = 10.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "$score% Match",
            color = textColor,
            style = JobPilotTypography.labelLarge,
            fontSize = 12.sp
        )
    }
}
