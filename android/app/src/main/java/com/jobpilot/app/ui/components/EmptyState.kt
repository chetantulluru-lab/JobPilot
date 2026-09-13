package com.jobpilot.app.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jobpilot.app.ui.theme.Slate500
import com.jobpilot.app.ui.theme.Slate900

@Composable
fun EmptyState(
    title: String,
    message: String,
    modifier: Modifier = Modifier,
    actionButtonText: String? = null,
    onActionClick: (() -> Unit)? = null
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AIOrb(size = 80.dp, showRings = true, showSparkle = true)

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = Slate900,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = Slate500,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        if (actionButtonText != null && onActionClick != null) {
            Spacer(modifier = Modifier.height(24.dp))
            JobPilotButton(
                text = actionButtonText,
                onClick = onActionClick,
                modifier = Modifier.widthIn(max = 240.dp)
            )
        }
    }
}

@Composable
fun LoadingState(
    message: String = "AI analyzing career parameters...",
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AIOrb(size = 90.dp)

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = Slate500,
            textAlign = TextAlign.Center
        )
    }
}
