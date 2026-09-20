package com.jobpilot.app.ui.screens.interview

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.jobpilot.app.ui.components.*
import com.jobpilot.app.ui.theme.*
import com.jobpilot.app.ui.viewmodel.InterviewViewModel
import java.util.Locale
import java.util.concurrent.Executors

@OptIn(ExperimentalGetImage::class)
@Composable
fun LiveInterviewScreen(
    viewModel: InterviewViewModel,
    onNavigateBack: () -> Unit,
    onInterviewComplete: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val session = uiState.currentSession
    val questions = session?.questions ?: emptyList()
    val currentIndex = uiState.currentQuestionIndex
    val currentQuestion = questions.getOrNull(currentIndex)

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // Camera permission state
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        )
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasCameraPermission = granted
    }

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    // TTS Setup
    var tts: TextToSpeech? by remember { mutableStateOf(null) }
    var isTtsReady by remember { mutableStateOf(false) }

    DisposableEffect(context) {
        var speech: TextToSpeech? = null
        speech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                speech?.language = Locale.US
                isTtsReady = true
            }
        }
        tts = speech
        onDispose {
            speech?.stop()
            speech?.shutdown()
        }
    }

    // Speak question whenever question index changes
    LaunchedEffect(currentIndex, isTtsReady, uiState.isTtsMuted) {
        if (isTtsReady && !uiState.isTtsMuted && currentQuestion != null) {
            tts?.speak(currentQuestion.question, TextToSpeech.QUEUE_FLUSH, null, "q_$currentIndex")
        }
    }

    // Voice recognition launcher (Speech to text)
    val speechLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val spoken = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.firstOrNull()
            if (!spoken.isNullOrBlank()) {
                val currentText = uiState.currentAnswerText
                val combined = if (currentText.isBlank()) spoken else "$currentText $spoken"
                viewModel.onAnswerChanged(combined)
            }
        }
    }

    var showHints by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BgWarmWhite)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.Close, contentDescription = "Exit Interview")
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = session?.title ?: "AI Mock Interview",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Slate900
                        )
                        Text(
                            text = "Question ${currentIndex + 1} of ${questions.size}",
                            fontSize = 12.sp,
                            color = Orange600,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    IconButton(onClick = { viewModel.toggleTtsMute() }) {
                        Icon(
                            imageVector = if (uiState.isTtsMuted) Icons.Default.VolumeOff else Icons.Default.VolumeUp,
                            contentDescription = "Mute / Unmute TTS",
                            tint = if (uiState.isTtsMuted) Slate400 else Orange500
                        )
                    }
                }

                // Progress Bar
                LinearProgressIndicator(
                    progress = { if (questions.isNotEmpty()) (currentIndex + 1).toFloat() / questions.size else 0f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp)),
                    color = Orange500,
                    trackColor = Slate200
                )
            }
        },
        bottomBar = {
            Surface(
                color = BgWhite,
                tonalElevation = 8.dp,
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (currentIndex > 0) {
                        OutlinedButton(
                            onClick = { viewModel.prevQuestion() },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Previous", color = Slate700)
                        }
                    }

                    val isLastQuestion = currentIndex == questions.size - 1
                    Button(
                        onClick = {
                            if (isLastQuestion) {
                                viewModel.submitInterview(onSuccess = onInterviewComplete)
                            } else {
                                viewModel.nextQuestion()
                            }
                        },
                        modifier = Modifier.weight(if (currentIndex > 0) 1.5f else 1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Orange500),
                        enabled = !uiState.isSubmitting
                    ) {
                        if (uiState.isSubmitting) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Evaluating...")
                        } else {
                            Text(if (isLastQuestion) "Finish & Evaluate 🎯" else "Next Question →")
                        }
                    }
                }
            }
        },
        containerColor = BgWarmWhite
    ) { paddingValues ->
        if (currentQuestion == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No questions found in this session.", color = Slate500)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 20.dp, vertical = 12.dp)
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Top Camera & Face Detection Card
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = BgWhite
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Camera PIP Preview or Simulator Badge
                        Box(
                            modifier = Modifier
                                .size(width = 110.dp, height = 85.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Slate900)
                                .border(1.5.dp, if (uiState.faceDetected) SuccessGreen else GapOrange, RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            if (hasCameraPermission) {
                                AndroidView(
                                    modifier = Modifier.fillMaxSize(),
                                    factory = { ctx ->
                                        val previewView = PreviewView(ctx)
                                        val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
                                        val cameraExecutor = Executors.newSingleThreadExecutor()

                                        val faceDetectorOptions = FaceDetectorOptions.Builder()
                                            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
                                            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_NONE)
                                            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_NONE)
                                            .build()
                                        val faceDetector = FaceDetection.getClient(faceDetectorOptions)

                                        cameraProviderFuture.addListener({
                                            val cameraProvider = cameraProviderFuture.get()
                                            val preview = Preview.Builder().build().also {
                                                it.setSurfaceProvider(previewView.surfaceProvider)
                                            }

                                            val imageAnalysis = ImageAnalysis.Builder()
                                                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                                                .build()

                                            imageAnalysis.setAnalyzer(cameraExecutor) { imageProxy ->
                                                val mediaImage = imageProxy.image
                                                if (mediaImage != null) {
                                                    val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
                                                    faceDetector.process(image)
                                                        .addOnSuccessListener { faces ->
                                                            viewModel.recordFaceDetection(faces.isNotEmpty())
                                                        }
                                                        .addOnCompleteListener {
                                                            imageProxy.close()
                                                        }
                                                } else {
                                                    imageProxy.close()
                                                }
                                            }

                                            try {
                                                cameraProvider.unbindAll()
                                                cameraProvider.bindToLifecycle(
                                                    lifecycleOwner,
                                                    CameraSelector.DEFAULT_FRONT_CAMERA,
                                                    preview,
                                                    imageAnalysis
                                                )
                                            } catch (_: Exception) {
                                                // If front camera unavailable, fallback to default
                                                try {
                                                    cameraProvider.bindToLifecycle(
                                                        lifecycleOwner,
                                                        CameraSelector.DEFAULT_BACK_CAMERA,
                                                        preview,
                                                        imageAnalysis
                                                    )
                                                } catch (_: Exception) {}
                                            }
                                        }, ContextCompat.getMainExecutor(ctx))

                                        previewView
                                    }
                                )
                            } else {
                                Icon(Icons.Default.Videocam, contentDescription = null, tint = Slate400, modifier = Modifier.size(32.dp))
                            }
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        // Face Presence Detection Status
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(if (uiState.faceDetected) SuccessGreen else GapOrange)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (uiState.faceDetected) "Face Centered" else "Center Face In Frame",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = if (uiState.faceDetected) SuccessGreen else GapOrange
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "ML Kit Presence: ${uiState.facePresencePercentage}%",
                                fontSize = 12.sp,
                                color = Slate700,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "Posture, framing & visual attentiveness will be evaluated in your final score.",
                                fontSize = 10.sp,
                                color = Slate500,
                                lineHeight = 14.sp
                            )
                        }
                    }
                }

                // Question Card
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = Orange50.copy(alpha = 0.5f),
                    borderColor = Orange300.copy(alpha = 0.5f)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Orange500)
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = currentQuestion.category.uppercase(),
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            IconButton(
                                onClick = {
                                    if (isTtsReady) {
                                        tts?.speak(currentQuestion.question, TextToSpeech.QUEUE_FLUSH, null, "replay")
                                    }
                                }
                            ) {
                                Icon(Icons.Default.PlayCircle, contentDescription = "Replay Question Audio", tint = Orange600)
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = currentQuestion.question,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Slate900,
                            lineHeight = 22.sp
                        )
                    }
                }

                // Expandable Hints Card
                if (currentQuestion.hints.isNotEmpty() || currentQuestion.expectedConcepts.isNotEmpty()) {
                    GlassCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showHints = !showHints },
                        backgroundColor = BgWhite
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Lightbulb, contentDescription = null, tint = Orange500, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Structuring Advice & Concepts",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = Slate800
                                    )
                                }
                                Icon(
                                    imageVector = if (showHints) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                    contentDescription = null,
                                    tint = Slate500
                                )
                            }

                            AnimatedVisibility(visible = showHints) {
                                Column(modifier = Modifier.padding(top = 10.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                    currentQuestion.hints.forEach { hint ->
                                        Row {
                                            Text("• ", color = Orange500, fontWeight = FontWeight.Bold)
                                            Text(hint, fontSize = 12.sp, color = Slate700)
                                        }
                                    }
                                    if (currentQuestion.expectedConcepts.isNotEmpty()) {
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text("Key terms to mention:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Slate500)
                                        Text(
                                            currentQuestion.expectedConcepts.joinToString(", "),
                                            fontSize = 12.sp,
                                            color = Orange700,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Candidate Answer Section
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Your Answer",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Slate900
                        )

                        // Voice Dictation Button
                        FilledTonalButton(
                            onClick = {
                                val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                                    putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                                    putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
                                    putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak your answer clearly...")
                                }
                                try {
                                    speechLauncher.launch(intent)
                                } catch (_: Exception) {}
                            },
                            colors = ButtonDefaults.filledTonalButtonColors(containerColor = Orange50)
                        ) {
                            Icon(Icons.Default.Mic, contentDescription = "Voice Input", tint = Orange600, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Speak Answer", color = Orange600, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = uiState.currentAnswerText,
                        onValueChange = { viewModel.onAnswerChanged(it) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 150.dp, max = 220.dp),
                        placeholder = {
                            Text(
                                "Speak via the microphone or type your complete response here. Be specific about your decisions, frameworks, and metrics...",
                                fontSize = 13.sp,
                                color = Slate400
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Orange500,
                            unfocusedBorderColor = Slate300,
                            focusedContainerColor = BgWhite,
                            unfocusedContainerColor = BgWhite
                        )
                    )
                }

                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}
