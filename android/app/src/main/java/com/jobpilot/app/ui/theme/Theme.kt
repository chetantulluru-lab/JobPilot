package com.jobpilot.app.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = Orange500,
    onPrimary = BgWhite,
    primaryContainer = Orange50,
    onPrimaryContainer = Orange700,
    secondary = Slate800,
    onSecondary = BgWhite,
    secondaryContainer = Slate100,
    onSecondaryContainer = Slate900,
    tertiary = Orange400,
    onTertiary = BgWhite,
    background = BgWarmWhite,
    onBackground = Slate900,
    surface = BgWhite,
    onSurface = Slate900,
    surfaceVariant = Slate100,
    onSurfaceVariant = Slate600,
    outline = Slate200,
    outlineVariant = BorderGlass
)

private val DarkColorScheme = darkColorScheme(
    primary = Orange500,
    onPrimary = Color.White,
    primaryContainer = Color(0xFF7C2D12),
    onPrimaryContainer = Orange100,
    secondary = Color(0xFF94A3B8),
    onSecondary = Color(0xFF0F172A),
    secondaryContainer = Color(0xFF334155),
    onSecondaryContainer = Color(0xFFF1F5F9),
    tertiary = Orange400,
    onTertiary = Color.White,
    background = Color(0xFF0F172A),
    onBackground = Color(0xFFF8FAFC),
    surface = Color(0xFF1E293B),
    onSurface = Color(0xFFF8FAFC),
    surfaceVariant = Color(0xFF334155),
    onSurfaceVariant = Color(0xFFCBD5E1),
    outline = Color(0xFF475569),
    outlineVariant = Color(0x33FF6A00)
)

@Composable
fun JobPilotTheme(
    themeMode: ThemeMode = ThemeMode.LIGHT,
    content: @Composable () -> Unit
) {
    // Strictly Light Theme only across the entire application as requested
    val colorScheme = LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            window?.let {
                it.statusBarColor = colorScheme.background.toArgb()
                it.navigationBarColor = colorScheme.surface.toArgb()
                WindowCompat.getInsetsController(it, view).isAppearanceLightStatusBars = true
                WindowCompat.getInsetsController(it, view).isAppearanceLightNavigationBars = true
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = JobPilotTypography,
        shapes = JobPilotShapes,
        content = content
    )
}

val MaterialTheme.textPrimary: Color
    @Composable
    get() = Slate900

val MaterialTheme.textSecondary: Color
    @Composable
    get() = Slate700

val MaterialTheme.textMuted: Color
    @Composable
    get() = Slate500

val MaterialTheme.cardBg: Color
    @Composable
    get() = BgWhite

val MaterialTheme.cardBorder: Color
    @Composable
    get() = Slate200

