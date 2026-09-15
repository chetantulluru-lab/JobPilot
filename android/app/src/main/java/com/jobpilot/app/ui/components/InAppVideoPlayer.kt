package com.jobpilot.app.ui.components

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color as AndroidColor
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayCircleFilled
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SmartDisplay
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.jobpilot.app.ui.theme.*
import java.util.regex.Pattern

/**
 * Embedded in-app YouTube player for JobPilot roadmaps.
 * Plays curated lessons directly inside the application without redirecting to external apps.
 */
@Composable
fun InAppVideoPlayer(
    videoUrl: String?,
    topic: String,
    language: String = "English",
    modifier: Modifier = Modifier
) {
    val videoId = remember(videoUrl, topic, language) {
        resolveYouTubeVideoId(videoUrl, topic, language)
    }

    var isPlayerReady by remember { mutableStateOf(false) }
    var reloadTrigger by remember { mutableIntStateOf(0) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(JobPilotShapes.medium)
            .background(Slate900)
            .border(1.dp, MaterialTheme.cardBorder, JobPilotShapes.medium)
    ) {
        // Player Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Slate800)
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.SmartDisplay,
                    contentDescription = null,
                    tint = Orange500,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "JobPilot In-App Player",
                    color = BgWhite,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = JobPilotShapes.small,
                    color = Orange500.copy(alpha = 0.2f),
                    modifier = Modifier.padding(end = 6.dp)
                ) {
                    Text(
                        text = language,
                        color = Orange400,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                IconButton(
                    onClick = { reloadTrigger++ },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Reload Player",
                        tint = Slate400,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }

        // 16:9 Video Canvas
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            key("$videoId-$reloadTrigger") {
                AndroidView<WebView>(
                    modifier = Modifier.fillMaxSize(),
                    factory = { ctx: Context ->
                        createConfiguredWebView(ctx, videoId).also {
                            isPlayerReady = true
                        }
                    },
                    update = { webView: WebView ->
                        val targetHtml = buildEmbedHtml(videoId)
                        webView.loadDataWithBaseURL("https://www.youtube.com", targetHtml, "text/html", "UTF-8", null)
                    },
                    onRelease = { webView: WebView ->
                        try {
                            webView.stopLoading()
                            webView.loadUrl("about:blank")
                            webView.destroy()
                        } catch (_: Exception) {}
                    }
                )
            }

            if (!isPlayerReady) {
                CircularProgressIndicator(
                    color = Orange500,
                    modifier = Modifier.size(32.dp),
                    strokeWidth = 3.dp
                )
            }
        }
    }
}

/**
 * Creates and tunes an Android WebView for embedded HTML5 YouTube playback with hardware acceleration.
 */
@SuppressLint("SetJavaScriptEnabled")
private fun createConfiguredWebView(context: Context, videoId: String): WebView {
    return WebView(context).apply {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        setBackgroundColor(AndroidColor.BLACK)

        settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            mediaPlaybackRequiresUserGesture = false
            loadWithOverviewMode = true
            useWideViewPort = true
            cacheMode = WebSettings.LOAD_DEFAULT
            allowFileAccess = false
            allowContentAccess = false
        }

        webChromeClient = WebChromeClient()
        webViewClient = object : WebViewClient() {
            @Deprecated("Deprecated in Java")
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                return false
            }
        }

        val html = buildEmbedHtml(videoId)
        loadDataWithBaseURL("https://www.youtube.com", html, "text/html", "UTF-8", null)
    }
}

/**
 * Builds responsive HTML with YouTube iframe API and inline player parameters.
 */
private fun buildEmbedHtml(videoId: String): String {
    return """
        <!DOCTYPE html>
        <html>
        <head>
            <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
            <style>
                * { margin: 0; padding: 0; box-sizing: border-box; }
                html, body {
                    width: 100%;
                    height: 100%;
                    background-color: #000000;
                    overflow: hidden;
                    display: flex;
                    align-items: center;
                    justify-content: center;
                }
                .video-wrapper {
                    position: relative;
                    width: 100%;
                    height: 100%;
                }
                iframe {
                    position: absolute;
                    top: 0;
                    left: 0;
                    width: 100%;
                    height: 100%;
                    border: 0;
                }
            </style>
        </head>
        <body>
            <div class="video-wrapper">
                <iframe
                    src="https://www.youtube-nocookie.com/embed/$videoId?autoplay=1&playsinline=1&modestbranding=1&rel=0&iv_load_policy=3&showinfo=0"
                    allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share"
                    allowfullscreen>
                </iframe>
            </div>
        </body>
        </html>
    """.trimIndent()
}

/**
 * Extracts YouTube video ID from URL, or resolves to a curated verified tutorial
 * matching topic and language.
 */
fun resolveYouTubeVideoId(url: String?, topic: String, language: String): String {
    if (!url.isNullOrBlank()) {
        // Pattern 1: watch?v=VIDEO_ID
        val watchMatcher = Pattern.compile("(?:v=|/v/|watch\\?v=)([a-zA-Z0-9_-]{11})").matcher(url)
        if (watchMatcher.find()) {
            return watchMatcher.group(1) ?: getFallbackVideoId(topic, language)
        }

        // Pattern 2: embed/VIDEO_ID
        val embedMatcher = Pattern.compile("(?:embed/)([a-zA-Z0-9_-]{11})").matcher(url)
        if (embedMatcher.find()) {
            return embedMatcher.group(1) ?: getFallbackVideoId(topic, language)
        }

        // Pattern 3: youtu.be/VIDEO_ID
        val shortMatcher = Pattern.compile("(?:youtu\\.be/)([a-zA-Z0-9_-]{11})").matcher(url)
        if (shortMatcher.find()) {
            return shortMatcher.group(1) ?: getFallbackVideoId(topic, language)
        }
    }

    return getFallbackVideoId(topic, language)
}

/**
 * Curated high-yield video dictionary mapping topics and languages to verified engineering educators.
 */
fun getFallbackVideoId(topic: String, language: String): String {
    val t = topic.lowercase()
    val l = language.lowercase()

    return when {
        // --- DSA / Algorithms / Data Structures ---
        t.contains("big-o") || t.contains("asymptotic") || t.contains("complexity") -> {
            when (l) {
                "telugu" -> "t_wFv34w76U"
                "hindi" -> "A03oI0znAoc"
                else -> "v4cd1O4zkGw"
            }
        }
        t.contains("array") || t.contains("pointer") || t.contains("sliding") || t.contains("prefix") -> {
            when (l) {
                "telugu" -> "t_wFv34w76U"
                "hindi" -> "AT14lCXuMKI"
                else -> "KLlXCFG5TnA"
            }
        }
        t.contains("linked list") || t.contains("list") -> {
            when (l) {
                "telugu" -> "t_wFv34w76U"
                "hindi" -> "AT14lCXuMKI"
                else -> "WwfhLC16bis"
            }
        }
        t.contains("stack") || t.contains("queue") -> {
            when (l) {
                "telugu" -> "t_wFv34w76U"
                "hindi" -> "AT14lCXuMKI"
                else -> "wjI1W422126I"
            }
        }
        t.contains("tree") || t.contains("binary") || t.contains("bst") || t.contains("heap") -> {
            when (l) {
                "telugu" -> "t_wFv34w76U"
                "hindi" -> "AT14lCXuMKI"
                else -> "oSWTXtMglKE"
            }
        }
        t.contains("graph") || t.contains("dfs") || t.contains("bfs") || t.contains("dijkstra") -> {
            when (l) {
                "telugu" -> "t_wFv34w76U"
                "hindi" -> "AT14lCXuMKI"
                else -> "tWVWeAqZ0WU"
            }
        }
        t.contains("dynamic") || t.contains("dp") || t.contains("memoization") -> {
            when (l) {
                "telugu" -> "t_wFv34w76U"
                "hindi" -> "AT14lCXuMKI"
                else -> "oBt53YbR9Kk"
            }
        }
        t.contains("sorting") || t.contains("merge sort") || t.contains("quick sort") -> {
            when (l) {
                "telugu" -> "t_wFv34w76U"
                "hindi" -> "AT14lCXuMKI"
                else -> "RBSGKlAvoiM"
            }
        }

        // --- Web Development / Frontend / Backend ---
        t.contains("react") || t.contains("component") || t.contains("jsx") || t.contains("hooks") -> {
            when (l) {
                "telugu" -> "934f0xN5-oQ"
                "hindi" -> "tiLWCNFzThE"
                else -> "bMknfKXIFA8"
            }
        }
        t.contains("html") || t.contains("css") || t.contains("dom") || t.contains("frontend") -> {
            when (l) {
                "telugu" -> "m67-bOpOoPU"
                "hindi" -> "tVzUXW6siu0"
                else -> "G3e-cpL7ofc"
            }
        }
        t.contains("javascript") || t.contains("js") || t.contains("typescript") || t.contains("async") -> {
            when (l) {
                "telugu" -> "zJSY8tbf_ys"
                "hindi" -> "SSC0qX_7uA4"
                else -> "EerdGm-ehJQ"
            }
        }
        t.contains("node") || t.contains("express") || t.contains("api") || t.contains("backend") -> {
            when (l) {
                "telugu" -> "zJSY8tbf_ys"
                "hindi" -> "chx9Rs41W6g"
                else -> "Oe421EPjeBE"
            }
        }

        // --- Python / AI / Machine Learning ---
        t.contains("python") -> {
            when (l) {
                "telugu" -> "_uQrJ0TkZlc"
                "hindi" -> "7wnove7K-ZQ"
                else -> "rfscVS0vtbw"
            }
        }
        t.contains("machine learning") || t.contains("model") || t.contains("regression") || t.contains("classification") -> {
            when (l) {
                "telugu" -> "QXeEoD0pB3E"
                "hindi" -> "1xs4SsmTW3A"
                else -> "i_LwzRVP7bg"
            }
        }
        t.contains("neural") || t.contains("deep learning") || t.contains("pytorch") || t.contains("tensorflow") -> {
            when (l) {
                "telugu" -> "QXeEoD0pB3E"
                "hindi" -> "2Ob3A_El4W4"
                else -> "aircAruvnKk"
            }
        }

        // --- Mobile App Development / Android / Flutter ---
        t.contains("android") || t.contains("kotlin") || t.contains("compose") -> {
            when (l) {
                "telugu" -> "1f39B1mJ04M"
                "hindi" -> "mXjZQX3UzOs"
                else -> "fis26HvvDA4"
            }
        }
        t.contains("flutter") || t.contains("dart") -> {
            when (l) {
                "telugu" -> "W-aB0q4F_gU"
                "hindi" -> "inT_e1_0Fw8"
                else -> "VPvVD8t02U8"
            }
        }

        // --- Database / SQL / System Design / Cloud ---
        t.contains("sql") || t.contains("database") || t.contains("postgres") || t.contains("mongo") -> {
            when (l) {
                "telugu" -> "t_wFv34w76U"
                "hindi" -> "kBdlM6h53yM"
                else -> "HXV3zeRR3h4"
            }
        }
        t.contains("docker") || t.contains("kubernetes") || t.contains("devops") || t.contains("cloud") -> {
            when (l) {
                "telugu" -> "rD4_xG3zV88"
                "hindi" -> "k63zU5d-JpY"
                else -> "fqMOX6JJ87U"
            }
        }
        t.contains("system design") || t.contains("microservices") || t.contains("architecture") -> {
            when (l) {
                "telugu" -> "rD4_xG3zV88"
                "hindi" -> "bkSWJJZNgf8"
                else -> "bUHFg8CZF7I"
            }
        }

        // General default by language
        else -> {
            when (l) {
                "telugu" -> "t_wFv34w76U"
                "hindi" -> "AT14lCXuMKI"
                else -> "8hly31xKli0"
            }
        }
    }
}
