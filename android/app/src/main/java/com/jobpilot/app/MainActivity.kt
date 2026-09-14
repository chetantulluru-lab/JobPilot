package com.jobpilot.app

import android.content.Intent
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val app = application as JobPilotApplication
        handleOAuthIntent(intent)

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

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleOAuthIntent(intent)
    }

    private fun handleOAuthIntent(intent: Intent?) {
        val data = intent?.data ?: return
        if (data.scheme == "jobpilot" && data.host == "oauth") {
            val app = application as? JobPilotApplication ?: return
            CoroutineScope(Dispatchers.IO).launch {
                app.container.connectedAccountRepository.refreshAccounts()
            }
        }
    }
}
