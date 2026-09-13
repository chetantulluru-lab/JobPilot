package com.jobpilot.app.ui.screens.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jobpilot.app.ui.components.AIOrb
import com.jobpilot.app.ui.theme.BgWarmWhite
import com.jobpilot.app.ui.theme.Orange500
import com.jobpilot.app.ui.theme.Slate500
import com.jobpilot.app.ui.theme.Slate900
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onTimeout: () -> Unit
) {
    val transition = rememberInfiniteTransition(label = "SplashSparkle")
    val alphaAnim by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
        label = "SplashAlpha"
    )

    LaunchedEffect(Unit) {
        delay(2000)
        onTimeout()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgWarmWhite),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(24.dp)
                .alpha(alphaAnim)
        ) {
            AIOrb(size = 140.dp)

            Spacer(modifier = Modifier.height(32.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "JobPilot",
                    style = MaterialTheme.typography.displayLarge,
                    color = Slate900,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 36.sp
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "✦",
                    color = Orange500,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Your career. Piloted by AI.",
                style = MaterialTheme.typography.bodyLarge,
                color = Slate500,
                fontSize = 16.sp
            )
        }

        Text(
            text = "Android Edition • Powered by NLP",
            style = MaterialTheme.typography.bodySmall,
            color = Slate500.copy(alpha = 0.7f),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
        )
    }
}
