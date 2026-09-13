package com.jobpilot.app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jobpilot.app.ui.theme.*

/**
 * Reusable Glassmorphism Card for JobPilot
 */
@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    shape: Shape = CardShapeLarge,
    backgroundColor: Color = BgGlass,
    borderColor: Color = BorderGlass,
    elevation: Dp = 2.dp,
    contentPadding: Dp = 18.dp,
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val cardModifier = if (onClick != null) {
        modifier.clickable(onClick = onClick)
    } else {
        modifier
    }

    Card(
        modifier = cardModifier,
        shape = shape,
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        border = BorderStroke(1.dp, borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = elevation)
    ) {
        Box(modifier = Modifier.padding(contentPadding)) {
            content()
        }
    }
}
