package com.jobpilot.app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jobpilot.app.ui.theme.*

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.material3.LocalContentColor

/**
 * Reusable Glassmorphism Card for JobPilot with automatic dark/light high-contrast theme adaptation.
 */
@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    shape: Shape = CardShapeLarge,
    backgroundColor: Color = Color.Unspecified,
    borderColor: Color = BorderGlass,
    elevation: Dp = 2.dp,
    contentPadding: Dp = 18.dp,
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val isDark = MaterialTheme.colorScheme.background == Color(0xFF0F172A)
    val resolvedBg = if (backgroundColor == Color.Unspecified) {
        if (isDark) MaterialTheme.colorScheme.surface else BgGlass
    } else if (isDark && (backgroundColor == BgWhite || backgroundColor == BgGlass || backgroundColor == BgSurfaceLight)) {
        MaterialTheme.colorScheme.surface
    } else {
        backgroundColor
    }

    val resolvedBorder = if (isDark && (borderColor == BorderGlass || borderColor == BorderSubtle || borderColor == Slate200)) {
        Color(0x33FF6A00)
    } else {
        borderColor
    }

    val cardModifier = if (onClick != null) {
        modifier.clickable(onClick = onClick)
    } else {
        modifier
    }

    Card(
        modifier = cardModifier,
        shape = shape,
        colors = CardDefaults.cardColors(
            containerColor = resolvedBg,
            contentColor = if (isDark) Color.White else Slate900
        ),
        border = BorderStroke(1.dp, resolvedBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = elevation)
    ) {
        CompositionLocalProvider(
            LocalContentColor provides (if (isDark) Color.White else Slate900)
        ) {
            Box(modifier = Modifier.padding(contentPadding)) {
                content()
            }
        }
    }
}

