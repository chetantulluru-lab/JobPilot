package com.jobpilot.app.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jobpilot.app.ui.theme.Orange400
import com.jobpilot.app.ui.theme.Orange500
import com.jobpilot.app.ui.theme.Orange600

/**
 * High-aesthetic glowing JobPilot brand icon with animated ambient aura.
 */
@Composable
fun JobPilotGlowingBadge(
    modifier: Modifier = Modifier,
    size: Dp = 44.dp
) {
    val infiniteTransition = rememberInfiniteTransition(label = "JobPilotGlowTransition")

    // Pulsing aura scale
    val auraScale by infiniteTransition.animateFloat(
        initialValue = 0.92f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "AuraScale"
    )

    // Pulsing aura alpha
    val auraAlpha by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 0.65f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "AuraAlpha"
    )

    Box(
        modifier = modifier.size(size * 1.35f),
        contentAlignment = Alignment.Center
    ) {
        // 1. Ambient animated glowing halo behind icon
        Box(
            modifier = Modifier
                .size(size * 1.3f)
                .scale(auraScale)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Orange500.copy(alpha = auraAlpha),
                            Orange400.copy(alpha = auraAlpha * 0.4f),
                            Color.Transparent
                        )
                    )
                )
        )

        // 2. Core Icon Disc with gradient, border and elevated shadow
        Box(
            modifier = Modifier
                .size(size)
                .shadow(elevation = 6.dp, shape = CircleShape, spotColor = Orange500, ambientColor = Orange400)
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(
                        colors = listOf(Orange400, Orange500, Orange600)
                    )
                )
                .border(width = 1.5.dp, color = Color.White.copy(alpha = 0.7f), shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = "JobPilot App Icon",
                tint = Color.White,
                modifier = Modifier.size(size * 0.52f)
            )
        }
    }
}
