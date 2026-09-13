package com.jobpilot.app.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jobpilot.app.ui.theme.Orange400
import com.jobpilot.app.ui.theme.Orange500
import com.jobpilot.app.ui.theme.Orange600
import kotlin.math.cos
import kotlin.math.sin

/**
 * Signature JobPilot AI Visual: Floating Glowing AI Core
 * Reusable across Splash, Onboarding, Dashboard, AI processing states, and Job Matching.
 */
@Composable
fun AIOrb(
    modifier: Modifier = Modifier,
    size: Dp = 120.dp,
    showRings: Boolean = true,
    showSparkle: Boolean = true
) {
    val infiniteTransition = rememberInfiniteTransition(label = "AIOrbTransition")

    // Gentle floating pulse
    val scalePulse by infiniteTransition.animateFloat(
        initialValue = 0.94f,
        targetValue = 1.06f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "OrbScalePulse"
    )

    // Orbital ring rotation
    val ringRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 18000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "OrbRingRotation"
    )

    // Central sparkle rotation
    val sparkleRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 9000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "OrbSparkleRotation"
    )

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size)) {
            val centerOffset = Offset(this.size.width / 2f, this.size.height / 2f)
            val baseRadius = (this.size.minDimension / 2f) * 0.42f * scalePulse

            // 1. Ambient blurred outer halo
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Orange500.copy(alpha = 0.35f),
                        Orange400.copy(alpha = 0.15f),
                        Color.Transparent
                    ),
                    center = centerOffset,
                    radius = baseRadius * 2.2f
                ),
                radius = baseRadius * 2.2f,
                center = centerOffset
            )

            // 2. Subtle orbiting rings & particles
            if (showRings) {
                rotate(degrees = ringRotation, pivot = centerOffset) {
                    drawCircle(
                        color = Orange500.copy(alpha = 0.25f),
                        radius = baseRadius * 1.55f,
                        center = centerOffset,
                        style = Stroke(width = 1.5f)
                    )
                    // Small orbital particle 1 (45 degrees = PI / 4)
                    val p1Angle = 0.7853982f
                    val p1X = centerOffset.x + (baseRadius * 1.55f * cos(p1Angle))
                    val p1Y = centerOffset.y + (baseRadius * 1.55f * sin(p1Angle))
                    drawCircle(
                        color = Orange500,
                        radius = 3.5f,
                        center = Offset(p1X, p1Y)
                    )
                }

                rotate(degrees = -ringRotation * 0.65f, pivot = centerOffset) {
                    drawCircle(
                        color = Orange400.copy(alpha = 0.18f),
                        radius = baseRadius * 1.9f,
                        center = centerOffset,
                        style = Stroke(width = 1.2f)
                    )
                    // Small orbital particle 2 (210 degrees = 3.6651914 radians)
                    val p2Angle = 3.6651914f
                    val p2X = centerOffset.x + (baseRadius * 1.9f * cos(p2Angle))
                    val p2Y = centerOffset.y + (baseRadius * 1.9f * sin(p2Angle))
                    drawCircle(
                        color = Orange400,
                        radius = 2.8f,
                        center = Offset(p2X, p2Y)
                    )
                }
            }

            // 3. Main Orb Sphere Gradient
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color.White,
                        Color(0xFFFFF4EB),
                        Orange400,
                        Orange600
                    ),
                    center = Offset(centerOffset.x - baseRadius * 0.25f, centerOffset.y - baseRadius * 0.25f),
                    radius = baseRadius
                ),
                radius = baseRadius,
                center = centerOffset
            )

            // 4. Central Radiant AI Sparkle ✦
            if (showSparkle) {
                rotate(degrees = sparkleRotation, pivot = centerOffset) {
                    val spSize = baseRadius * 0.45f
                    // Draw 4-point sparkle cross
                    drawLine(
                        color = Color.White.copy(alpha = 0.95f),
                        start = Offset(centerOffset.x, centerOffset.y - spSize),
                        end = Offset(centerOffset.x, centerOffset.y + spSize),
                        strokeWidth = 2.5f
                    )
                    drawLine(
                        color = Color.White.copy(alpha = 0.95f),
                        start = Offset(centerOffset.x - spSize, centerOffset.y),
                        end = Offset(centerOffset.x + spSize, centerOffset.y),
                        strokeWidth = 2.5f
                    )
                    // Center core glint dot
                    drawCircle(
                        color = Color.White,
                        radius = 3.5f,
                        center = centerOffset
                    )
                }
            }
        }
    }
}
