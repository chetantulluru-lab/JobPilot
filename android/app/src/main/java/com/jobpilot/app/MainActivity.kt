package com.jobpilot.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.jobpilot.app.ui.navigation.JobPilotNavGraph
import com.jobpilot.app.ui.theme.BgWarmWhite
import com.jobpilot.app.ui.theme.JobPilotTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val app = application as JobPilotApplication

        setContent {
            JobPilotTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = BgWarmWhite
                ) {
                    JobPilotNavGraph(container = app.container)
                }
            }
        }
    }
}
