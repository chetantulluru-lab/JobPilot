package com.jobpilot.app.ui.screens.roadmap

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jobpilot.app.data.network.CourseCatalogItemDto
import com.jobpilot.app.ui.components.GlassCard
import com.jobpilot.app.ui.components.JobPilotButton
import com.jobpilot.app.ui.theme.*
import com.jobpilot.app.ui.viewmodel.RoadmapViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RoadmapCreateScreen(
    viewModel: RoadmapViewModel,
    onNavigateBack: () -> Unit,
    onRoadmapCreated: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }
    var selectedDuration by remember { mutableStateOf("6 Months") }

    val categories = listOf("All", "Core CSE", "Programming Languages", "Mobile Development", "AI & Data Science", "Web & Mobile")
    val durations = listOf("3 Months", "6 Months", "12 Months")

    // Default static course list for instant display while network loads
    val defaultCatalog = listOf(
        CourseCatalogItemDto(
            id = "dsa-cse",
            title = "Data Structures & Algorithms (DSA)",
            category = "Core CSE",
            badge = "Core Foundation",
            description = "Master arrays, linked lists, trees, graphs, sorting, and dynamic programming with LeetCode patterns.",
            skills = listOf("Arrays", "Trees", "Graphs", "Dynamic Programming", "LeetCode"),
            totalDays = 10,
            totalPhases = 2
        ),
        CourseCatalogItemDto(
            id = "python-dev",
            title = "Python Developer",
            category = "Programming Languages",
            badge = "High Demand",
            description = "Python 3 OOP, Asyncio, FastAPI microservices, PostgreSQL, Alembic, and Docker containers.",
            skills = listOf("Python 3", "FastAPI", "PostgreSQL", "Asyncio", "Docker"),
            totalDays = 10,
            totalPhases = 2
        ),
        CourseCatalogItemDto(
            id = "java-dev",
            title = "Java Developer",
            category = "Programming Languages",
            badge = "Enterprise Core",
            description = "Core Java, Collections, Multithreading, Spring Boot 3, Hibernate/JPA, and Microservices.",
            skills = listOf("Java 17/21", "Spring Boot", "JPA/Hibernate", "Microservices"),
            totalDays = 10,
            totalPhases = 2
        ),
        CourseCatalogItemDto(
            id = "android-kotlin",
            title = "Android App Development (Kotlin & Jetpack Compose)",
            category = "Mobile Development",
            badge = "Native Android",
            description = "Build modern, reactive Android apps with Kotlin, Jetpack Compose, Coroutines/Flow, Clean Architecture, and Retrofit.",
            skills = listOf("Android", "Kotlin", "Jetpack Compose", "Coroutines", "Flow", "StateFlow", "Retrofit", "Room DB"),
            totalDays = 4,
            totalPhases = 2
        ),
        CourseCatalogItemDto(
            id = "ios-swift",
            title = "iOS App Development (Swift & SwiftUI)",
            category = "Mobile Development",
            badge = "Apple Ecosystem",
            description = "Master modern iOS application engineering with Swift 5+, SwiftUI, Combine, URLSession, and CoreData.",
            skills = listOf("iOS", "Swift", "SwiftUI", "Combine", "URLSession", "CoreData", "MVVM"),
            totalDays = 3,
            totalPhases = 2
        ),
        CourseCatalogItemDto(
            id = "flutter-dart",
            title = "Cross-Platform Mobile Dev (Flutter & Dart)",
            category = "Mobile Development",
            badge = "Multiplatform",
            description = "Develop high-performance cross-platform iOS and Android apps from a single codebase with Flutter and Dart.",
            skills = listOf("Flutter", "Dart", "Widgets", "Provider", "BLoC", "REST APIs", "Dio"),
            totalDays = 2,
            totalPhases = 1
        ),
        CourseCatalogItemDto(
            id = "react-native",
            title = "Cross-Platform Mobile Dev (React Native & Expo)",
            category = "Mobile Development",
            badge = "Cross-Platform",
            description = "Create native mobile apps using JavaScript, TypeScript, React Native, Expo, and native device bridge APIs.",
            skills = listOf("React Native", "TypeScript", "Expo", "React Hooks", "AsyncStorage", "React Navigation"),
            totalDays = 2,
            totalPhases = 1
        ),
        CourseCatalogItemDto(
            id = "deep-learning-pytorch",
            title = "Deep Learning with PyTorch",
            category = "AI & Data Science",
            badge = "Deep Learning",
            description = "Train and evaluate deep neural networks, CNNs, RNNs, and custom architectures using PyTorch and GPU acceleration.",
            skills = listOf("PyTorch", "Deep Learning", "Tensors", "Neural Networks", "CNN", "CUDA"),
            totalDays = 2,
            totalPhases = 1
        ),
        CourseCatalogItemDto(
            id = "generative-ai-llms",
            title = "Generative AI & Large Language Models (LLMs & RAG)",
            category = "AI & Data Science",
            badge = "Trending AI",
            description = "Engineer production LLM applications using LangChain, Prompt Engineering, Vector Databases, and Retrieval-Augmented Generation (RAG).",
            skills = listOf("Generative AI", "LLMs", "LangChain", "RAG", "ChromaDB", "Prompt Engineering"),
            totalDays = 2,
            totalPhases = 1
        ),
        CourseCatalogItemDto(
            id = "nlp-transformers",
            title = "Natural Language Processing & Transformers",
            category = "AI & Data Science",
            badge = "Applied NLP",
            description = "Master NLP from TF-IDF tokenization to Hugging Face Transformers, BERT embeddings, and semantic search.",
            skills = listOf("NLP", "Transformers", "BERT", "Hugging Face", "Tokenization", "TF-IDF"),
            totalDays = 2,
            totalPhases = 1
        ),
        CourseCatalogItemDto(
            id = "data-analytics",
            title = "Data Analytics & Visualization",
            category = "AI & Data Science",
            badge = "Business Intelligence",
            description = "Extract actionable business insights through exploratory data analysis, statistical modeling, Pandas, and interactive dashboards.",
            skills = listOf("Data Analytics", "Python", "Pandas", "NumPy", "Matplotlib", "Seaborn", "SQL"),
            totalDays = 2,
            totalPhases = 1
        ),
        CourseCatalogItemDto(
            id = "rust-lang",
            title = "Rust Systems Programming",
            category = "Programming Languages",
            badge = "Memory Safe",
            description = "Build blazingly fast, memory-safe system software with Rust, ownership, borrowing, lifetimes, and fearless concurrency.",
            skills = listOf("Rust", "Systems Programming", "Ownership", "Borrowing", "Lifetimes", "Tokio"),
            totalDays = 2,
            totalPhases = 1
        ),
        CourseCatalogItemDto(
            id = "typescript-lang",
            title = "TypeScript & Modern Typed JavaScript",
            category = "Programming Languages",
            badge = "Web Standard",
            description = "Level up JavaScript codebases with robust type safety, generics, utility types, and strict compiler configs.",
            skills = listOf("TypeScript", "JavaScript", "Generics", "Type Inference", "Interfaces"),
            totalDays = 2,
            totalPhases = 1
        ),
        CourseCatalogItemDto(
            id = "ml-ai-dev",
            title = "Machine Learning & AI Developer",
            category = "AI & Data Science",
            badge = "Cutting Edge",
            description = "NumPy, Pandas, Scikit-Learn, Deep Learning, PyTorch, LLM RAG pipelines, and HuggingFace.",
            skills = listOf("Machine Learning", "PyTorch", "NumPy", "NLP", "LLMs"),
            totalDays = 10,
            totalPhases = 2
        ),
        CourseCatalogItemDto(
            id = "fullstack-web",
            title = "Full Stack Web Developer",
            category = "Web & Mobile",
            badge = "Popular",
            description = "Modern React, Tailwind CSS, Node.js, Express, PostgreSQL with Prisma, and Full-Stack deployment.",
            skills = listOf("React", "JavaScript", "Node.js", "Express", "Tailwind"),
            totalDays = 10,
            totalPhases = 2
        ),
        CourseCatalogItemDto(
            id = "os-cse",
            title = "Operating Systems & Concurrency",
            category = "Core CSE",
            badge = "Core Subject",
            description = "Kernel architecture, CPU scheduling, thread synchronization, semaphores, paging, and deadlocks.",
            skills = listOf("Process Management", "Concurrency", "Semaphores", "Virtual Memory"),
            totalDays = 10,
            totalPhases = 2
        ),
        CourseCatalogItemDto(
            id = "dbms-cse",
            title = "Database Management Systems (DBMS & SQL)",
            category = "Core CSE",
            badge = "Core Subject",
            description = "ER modeling, complex SQL, window functions, B+ tree indexing, Normalization, and ACID transactions.",
            skills = listOf("SQL", "Relational DB", "Indexing", "Normalization", "ACID"),
            totalDays = 10,
            totalPhases = 2
        ),
        CourseCatalogItemDto(
            id = "cn-cse",
            title = "Computer Networks & Protocols",
            category = "Core CSE",
            badge = "Core Subject",
            description = "OSI 7 Layers, TCP/IP, CIDR subnetting, 3-way handshake, routing protocols, HTTP/HTTPS, and sockets.",
            skills = listOf("TCP/IP", "HTTP/HTTPS", "Subnetting", "DNS", "Sockets"),
            totalDays = 10,
            totalPhases = 2
        ),
        CourseCatalogItemDto(
            id = "system-design-cse",
            title = "System Design & Distributed Systems",
            category = "Core CSE",
            badge = "Advanced Core",
            description = "Horizontal scaling, load balancing, Redis caching, database sharding, message queues, and microservices.",
            skills = listOf("Scalability", "Microservices", "Load Balancing", "Redis", "Kafka"),
            totalDays = 10,
            totalPhases = 2
        )
    )

    val availableCourses = if (uiState.catalogCourses.isNotEmpty()) uiState.catalogCourses else defaultCatalog

    val filteredCourses = availableCourses.filter { course ->
        val matchesCategory = (selectedCategory == "All") || (course.category == selectedCategory)
        val matchesQuery = searchQuery.isBlank() ||
                course.title.contains(searchQuery, ignoreCase = true) ||
                course.description.contains(searchQuery, ignoreCase = true) ||
                course.skills.any { it.contains(searchQuery, ignoreCase = true) }
        matchesCategory && matchesQuery
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Top Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = "Build Structured Roadmap",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.textPrimary
                )
                Text(
                    text = "Select courses to build day-by-day learning phases",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.textSecondary
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search 100+ CSE courses, subjects & topics...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Orange500) },
            trailingIcon = {
                if (searchQuery.isNotBlank()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(Icons.Default.Close, contentDescription = "Clear")
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = JobPilotShapes.medium,
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Category Filter Chips
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(categories) { cat ->
                val isSelected = (selectedCategory == cat)
                FilterChip(
                    selected = isSelected,
                    onClick = { selectedCategory = cat },
                    label = { Text(cat, fontSize = 13.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Orange500,
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Selection Summary Tray
        if (uiState.selectedCourseIds.isNotEmpty()) {
            val isDark = isSystemInDarkTheme()
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = if (isDark) Color(0xFF1E293B) else Orange50
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "${uiState.selectedCourseIds.size} Courses Selected",
                            fontWeight = FontWeight.Bold,
                            color = Orange500,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "Tracks will be combined into a structured sequential roadmap",
                            fontSize = 11.sp,
                            color = MaterialTheme.textSecondary
                        )
                    }
                    TextButton(onClick = { viewModel.clearSelectedCourses() }) {
                        Text("Clear", color = Orange500, fontSize = 12.sp)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Duration Selector
        Text(
            text = "Target Completion Timeline",
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            color = MaterialTheme.textPrimary
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            durations.forEach { d ->
                val isSelected = (selectedDuration == d)
                OutlinedButton(
                    onClick = { selectedDuration = d },
                    modifier = Modifier.weight(1f),
                    shape = JobPilotShapes.small,
                    border = BorderStroke(
                        width = if (isSelected) 2.dp else 1.dp,
                        color = if (isSelected) Orange500 else MaterialTheme.cardBorder
                    ),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = if (isSelected) Orange500.copy(alpha = 0.15f) else MaterialTheme.cardBg
                    )
                ) {
                    Text(
                        text = d,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) Orange500 else MaterialTheme.textPrimary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Course Catalog List
        Text(
            text = "Available Computer Science Modules (${filteredCourses.size})",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = MaterialTheme.textPrimary
        )
        Spacer(modifier = Modifier.height(10.dp))

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            val isDark = isSystemInDarkTheme()
            filteredCourses.forEach { course ->
                val isChecked = uiState.selectedCourseIds.contains(course.id)

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.toggleCourseSelection(course.id) },
                    shape = JobPilotShapes.medium,
                    colors = CardDefaults.cardColors(
                        containerColor = if (isChecked) Orange500.copy(alpha = 0.12f) else MaterialTheme.cardBg
                    ),
                    border = BorderStroke(
                        width = if (isChecked) 2.dp else 1.dp,
                        color = if (isChecked) Orange500 else MaterialTheme.cardBorder
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Checkbox(
                            checked = isChecked,
                            onCheckedChange = { viewModel.toggleCourseSelection(course.id) },
                            colors = CheckboxDefaults.colors(checkedColor = Orange500)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = course.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.textPrimary,
                                    modifier = Modifier.weight(1f)
                                )
                                Surface(
                                    shape = JobPilotShapes.small,
                                    color = if (course.category == "Core CSE") (if (isDark) Color(0xFF1E3A8A) else InfoBlueBg) else (if (isDark) Color(0xFF7C2D12) else Orange100),
                                    modifier = Modifier.padding(start = 6.dp)
                                ) {
                                    Text(
                                        text = course.badge,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = if (course.category == "Core CSE") (if (isDark) Color(0xFF93C5FD) else InfoBlue) else Orange500,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = course.description,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.textSecondary
                            )

                            Spacer(modifier = Modifier.height(8.dp))
                            // Skill chips
                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                course.skills.take(4).forEach { skill ->
                                    Surface(
                                        shape = JobPilotShapes.small,
                                        color = if (isDark) Color(0xFF334155) else Slate100
                                    ) {
                                        Text(
                                            text = skill,
                                            fontSize = 10.sp,
                                            color = if (isDark) Color.White else Slate700,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "⏱ ${course.totalDays} Days • ${course.totalPhases} Structured Phases",
                                fontSize = 11.sp,
                                color = Orange500
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (uiState.errorMessage != null) {
            Text(
                text = uiState.errorMessage ?: "",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Action CTA
        val selectedCount = uiState.selectedCourseIds.size
        val buttonLabel = if (selectedCount == 0) {
            "Select Course(s) to Create Roadmap"
        } else if (selectedCount == 1) {
            "Generate Roadmap (1 Course Selected)"
        } else {
            "Generate Master Roadmap ($selectedCount Tracks Selected)"
        }

        JobPilotButton(
            text = buttonLabel,
            onClick = {
                if (selectedCount > 0) {
                    viewModel.generateRoadmapFromSelectedCourses(
                        duration = selectedDuration,
                        onSuccess = onRoadmapCreated
                    )
                }
            },
            enabled = (selectedCount > 0 && !uiState.isGenerating),
            leadingIcon = {
                if (uiState.isGenerating) {
                    CircularProgressIndicator(modifier = Modifier.size(18.dp), color = Color.White, strokeWidth = 2.dp)
                } else {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color.White)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(30.dp))
    }
}
