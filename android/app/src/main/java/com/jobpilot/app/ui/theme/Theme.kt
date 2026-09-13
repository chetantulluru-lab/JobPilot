package com.jobpilot.app.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
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

@Composable
fun JobPilotTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            window?.let {
                it.statusBarColor = BgWarmWhite.toArgb()
                it.navigationBarColor = BgWhite.toArgb()
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
