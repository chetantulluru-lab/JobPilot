package com.jobpilot.app.ui.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.jobpilot.app.data.AppContainer
import com.jobpilot.app.ui.screens.applications.ApplicationDetailScreen
import com.jobpilot.app.ui.screens.applications.ApplicationListScreen
import com.jobpilot.app.ui.screens.assistant.AICareerAssistantScreen
import com.jobpilot.app.ui.screens.auth.ForgotPasswordScreen
import com.jobpilot.app.ui.screens.auth.LoginScreen
import com.jobpilot.app.ui.screens.auth.RegisterScreen
import com.jobpilot.app.ui.screens.dashboard.DashboardScreen
import com.jobpilot.app.ui.screens.jobs.JobDetailScreen
import com.jobpilot.app.ui.screens.jobs.JobListScreen
import com.jobpilot.app.ui.screens.jobs.SkillGapDetailScreen
import com.jobpilot.app.ui.screens.notifications.NotificationCenterScreen
import com.jobpilot.app.ui.screens.onboarding.CareerProfileOnboardingScreen
import com.jobpilot.app.ui.screens.onboarding.OnboardingScreen
import com.jobpilot.app.ui.screens.profile.ProfileScreen
import com.jobpilot.app.ui.screens.profile.SmartCompletionScreen
import com.jobpilot.app.ui.screens.resume.CreateResumeScreen
import com.jobpilot.app.ui.screens.resume.ResumeBuilderScreen
import com.jobpilot.app.ui.screens.resume.ResumeHubScreen
import com.jobpilot.app.ui.screens.resume.ResumeUploadFlowScreen
import com.jobpilot.app.ui.screens.roadmap.DayLearningScreen
import com.jobpilot.app.ui.screens.roadmap.RoadmapCreateScreen
import com.jobpilot.app.ui.screens.roadmap.RoadmapDetailScreen
import com.jobpilot.app.ui.screens.roadmap.RoadmapHubScreen
import com.jobpilot.app.ui.screens.settings.ConnectedAccountsScreen
import com.jobpilot.app.ui.screens.settings.SettingsScreen
import com.jobpilot.app.ui.screens.splash.SplashScreen
import com.jobpilot.app.ui.screens.interview.InterviewReportScreen
import com.jobpilot.app.ui.screens.interview.InterviewSetupScreen
import com.jobpilot.app.ui.screens.interview.LiveInterviewScreen
import com.jobpilot.app.ui.theme.BgWarmWhite
import com.jobpilot.app.ui.theme.Orange50
import com.jobpilot.app.ui.theme.Orange500
import com.jobpilot.app.ui.theme.Slate400
import com.jobpilot.app.ui.viewmodel.*

private data class BottomNavItem(
    val title: String,
    val route: String,
    val icon: ImageVector
)

@Composable
fun JobPilotNavGraph(
    container: AppContainer,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    // ViewModels scoped to Graph
    val authViewModel: AuthViewModel = viewModel(
        factory = JobPilotViewModelFactory(container)
    )
    val dashboardViewModel: DashboardViewModel = viewModel(
        factory = JobPilotViewModelFactory(container)
    )
    val roadmapViewModel: RoadmapViewModel = viewModel(
        factory = JobPilotViewModelFactory(container)
    )
    val profileViewModel: ProfileViewModel = viewModel(
        factory = JobPilotViewModelFactory(container)
    )
    val jobViewModel: JobViewModel = viewModel(
        factory = JobPilotViewModelFactory(container)
    )
    val applicationViewModel: ApplicationViewModel = viewModel(
        factory = JobPilotViewModelFactory(container)
    )
    val resumeViewModel: ResumeViewModel = viewModel(
        factory = JobPilotViewModelFactory(container)
    )
    val notificationViewModel: NotificationViewModel = viewModel(
        factory = JobPilotViewModelFactory(container)
    )
    val connectedAccountsViewModel: ConnectedAccountsViewModel = viewModel(
        factory = JobPilotViewModelFactory(container)
    )
    val assistantViewModel: AssistantViewModel = viewModel(
        factory = JobPilotViewModelFactory(container)
    )
    val interviewViewModel: InterviewViewModel = viewModel(
        factory = JobPilotViewModelFactory(container)
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // MVP 5-Tab Navigation: 1. Home, 2. Roadmap, 3. Resume, 4. Profile, 5. Settings
    val bottomNavItems = listOf(
        BottomNavItem("Home", Screen.Dashboard.route, Icons.Default.Home),
        BottomNavItem("Roadmap", Screen.RoadmapHub.route, Icons.Default.Explore),
        BottomNavItem("Resume", Screen.ResumeHub.route, Icons.Default.Description),
        BottomNavItem("Profile", Screen.Profile.route, Icons.Default.Person),
        BottomNavItem("Settings", Screen.Settings.route, Icons.Default.Settings)
    )

    val showBottomBar = bottomNavItems.any { it.route == currentRoute }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            AnimatedVisibility(
                visible = showBottomBar,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it })
            ) {
                NavigationBar(
                    containerColor = BgWarmWhite,
                    tonalElevation = 6.dp
                ) {
                    bottomNavItems.forEach { item ->
                        val selected = currentRoute == item.route
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                if (currentRoute != item.route) {
                                    navController.navigate(item.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.title
                                )
                            },
                            label = {
                                Text(
                                    text = item.title,
                                    fontSize = 11.sp
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Orange500,
                                selectedTextColor = Orange500,
                                indicatorColor = Orange50,
                                unselectedIconColor = Slate400,
                                unselectedTextColor = Slate400
                            )
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Splash.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            // 1. Splash
            composable(Screen.Splash.route) {
                SplashScreen(
                    onTimeout = {
                        val destination = when {
                            authViewModel.hasActiveSession() -> Screen.Dashboard.route
                            authViewModel.isOnboardingCompleted() -> Screen.Login.route
                            else -> Screen.Onboarding.route
                        }
                        navController.navigate(destination) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    }
                )
            }

            // 2. Onboarding
            composable(Screen.Onboarding.route) {
                OnboardingScreen(
                    onFinished = {
                        authViewModel.setOnboardingCompleted(true)
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Onboarding.route) { inclusive = true }
                        }
                    }
                )
            }

            // 3. Auth: Login
            composable(Screen.Login.route) {
                LoginScreen(
                    viewModel = authViewModel,
                    onLoginSuccess = {
                        navController.navigate(Screen.Dashboard.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                    onNavigateToRegister = {
                        navController.navigate(Screen.Register.route)
                    },
                    onNavigateToForgotPassword = {
                        navController.navigate(Screen.ForgotPassword.route)
                    }
                )
            }

            // 4. Auth: Register
            composable(Screen.Register.route) {
                RegisterScreen(
                    viewModel = authViewModel,
                    onRegisterSuccess = {
                        navController.navigate(Screen.CareerProfileOnboarding.route) {
                            popUpTo(Screen.Register.route) { inclusive = true }
                        }
                    },
                    onNavigateToLogin = {
                        navController.popBackStack()
                    }
                )
            }

            // 5. Auth: Forgot Password (2-Step OTP)
            composable(Screen.ForgotPassword.route) {
                ForgotPasswordScreen(
                    viewModel = authViewModel,
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }

            // 6. Career Profile Onboarding (Next -> Next: Personal -> Education -> Optional Resume)
            composable(Screen.CareerProfileOnboarding.route) {
                CareerProfileOnboardingScreen(
                    profileViewModel = profileViewModel,
                    onFinishToHome = {
                        navController.navigate(Screen.Dashboard.route) {
                            popUpTo(Screen.CareerProfileOnboarding.route) { inclusive = true }
                        }
                    },
                    onUploadResume = {
                        navController.navigate(Screen.ResumeUpload.route)
                    },
                    onCreateResume = {
                        navController.navigate(Screen.ResumeBuilder.route)
                    }
                )
            }

            // 7. Tab 1: Dashboard / Home
            composable(Screen.Dashboard.route) {
                DashboardScreen(
                    viewModel = dashboardViewModel,
                    onNavigateToRoadmaps = { navController.navigate(Screen.RoadmapHub.route) },
                    onNavigateToRoadmapDetail = { roadmapId ->
                        navController.navigate(Screen.RoadmapDetail.createRoute(roadmapId))
                    },
                    onCreateRoadmap = { navController.navigate(Screen.RoadmapCreate.route) },
                    onNavigateToResume = { navController.navigate(Screen.ResumeHub.route) },
                    onNavigateToProfile = { navController.navigate(Screen.Profile.route) },
                    onNavigateToAssistant = { navController.navigate(Screen.AIAssistant.route) },
                    onNavigateToJobs = { navController.navigate(Screen.JobList.route) },
                    onNavigateToApplications = { navController.navigate(Screen.ApplicationList.route) },
                    onNavigateToInterview = { role ->
                        navController.navigate(Screen.InterviewSetup.createRoute(role))
                    }
                )
            }

            // 8. Tab 2: Roadmap Hub
            composable(Screen.RoadmapHub.route) {
                RoadmapHubScreen(
                    viewModel = roadmapViewModel,
                    onCreateRoadmap = { navController.navigate(Screen.RoadmapCreate.route) },
                    onSelectRoadmap = { id ->
                        navController.navigate(Screen.RoadmapDetail.createRoute(id))
                    }
                )
            }

            // 9. Roadmap Create Screen
            composable(Screen.RoadmapCreate.route) {
                RoadmapCreateScreen(
                    viewModel = roadmapViewModel,
                    onNavigateBack = { navController.popBackStack() },
                    onRoadmapCreated = { id ->
                        navController.navigate(Screen.RoadmapDetail.createRoute(id)) {
                            popUpTo(Screen.RoadmapCreate.route) { inclusive = true }
                        }
                    }
                )
            }

            // 10. Roadmap Detail Screen
            composable(
                route = Screen.RoadmapDetail.route,
                arguments = listOf(navArgument("roadmapId") { type = NavType.StringType })
            ) { backStackEntry ->
                val roadmapId = backStackEntry.arguments?.getString("roadmapId") ?: ""
                RoadmapDetailScreen(
                    roadmapId = roadmapId,
                    viewModel = roadmapViewModel,
                    onNavigateBack = { navController.popBackStack() },
                    onOpenDay = { rId, dId ->
                        navController.navigate(Screen.DayLearning.createRoute(rId, dId))
                    }
                )
            }

            // 11. Day Learning Screen
            composable(
                route = Screen.DayLearning.route,
                arguments = listOf(
                    navArgument("roadmapId") { type = NavType.StringType },
                    navArgument("dayId") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val roadmapId = backStackEntry.arguments?.getString("roadmapId") ?: ""
                val dayId = backStackEntry.arguments?.getString("dayId") ?: ""
                DayLearningScreen(
                    roadmapId = roadmapId,
                    dayId = dayId,
                    viewModel = roadmapViewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            // 12. Tab 3: Create Resume
            composable(Screen.ResumeHub.route) {
                CreateResumeScreen(
                    resumeViewModel = resumeViewModel,
                    profileViewModel = profileViewModel,
                    roadmapViewModel = roadmapViewModel
                )
            }

            // 13. Resume Upload Flow
            composable(Screen.ResumeUpload.route) {
                ResumeUploadFlowScreen(
                    viewModel = resumeViewModel,
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToProfile = { navController.navigate(Screen.Profile.route) }
                )
            }

            // 14. Resume Builder
            composable(Screen.ResumeBuilder.route) {
                ResumeBuilderScreen(
                    viewModel = resumeViewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            // 15. Tab 4: Profile
            composable(Screen.Profile.route) {
                ProfileScreen(
                    viewModel = profileViewModel,
                    onNavigateToSmartCompletion = { navController.navigate(Screen.SmartCompletion.route) }
                )
            }

            // 16. Smart Completion
            composable(Screen.SmartCompletion.route) {
                SmartCompletionScreen(
                    viewModel = profileViewModel,
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToConnectedAccounts = { navController.navigate(Screen.ConnectedAccounts.route) }
                )
            }

            // 17. Tab 5: Settings
            composable(Screen.Settings.route) {
                SettingsScreen(
                    authViewModel = authViewModel,
                    onLogout = {
                        authViewModel.logout {
                            navController.navigate(Screen.Login.route) {
                                popUpTo(Screen.Dashboard.route) { inclusive = true }
                            }
                        }
                    }
                )
            }

            // 18. Secondary Screens (Isolated from Bottom Tabs)
            composable(Screen.JobList.route) {
                JobListScreen(
                    viewModel = jobViewModel,
                    onNavigateToJobDetail = { jobId ->
                        navController.navigate(Screen.JobDetail.createRoute(jobId))
                    }
                )
            }

            composable(
                route = Screen.JobDetail.route,
                arguments = listOf(navArgument("jobId") { type = NavType.StringType })
            ) { backStackEntry ->
                val jobId = backStackEntry.arguments?.getString("jobId") ?: ""
                JobDetailScreen(
                    jobId = jobId,
                    viewModel = jobViewModel,
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToSkillGap = { id ->
                        navController.navigate(Screen.SkillGapDetail.createRoute(id))
                    },
                    onNavigateToInterview = { role ->
                        navController.navigate(Screen.InterviewSetup.createRoute(role))
                    }
                )
            }

            composable(
                route = Screen.SkillGapDetail.route,
                arguments = listOf(navArgument("jobId") { type = NavType.StringType })
            ) { backStackEntry ->
                val jobId = backStackEntry.arguments?.getString("jobId") ?: ""
                SkillGapDetailScreen(
                    jobId = jobId,
                    viewModel = jobViewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(Screen.ApplicationList.route) {
                ApplicationListScreen(
                    viewModel = applicationViewModel,
                    onNavigateToDetail = { appId ->
                        navController.navigate(Screen.ApplicationDetail.createRoute(appId))
                    }
                )
            }

            composable(
                route = Screen.ApplicationDetail.route,
                arguments = listOf(navArgument("appId") { type = NavType.StringType })
            ) { backStackEntry ->
                val appId = backStackEntry.arguments?.getString("appId") ?: ""
                ApplicationDetailScreen(
                    appId = appId,
                    viewModel = applicationViewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(Screen.Notifications.route) {
                NotificationCenterScreen(
                    viewModel = notificationViewModel,
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToApplication = { appId ->
                        navController.navigate(Screen.ApplicationDetail.createRoute(appId))
                    }
                )
            }

            composable(Screen.ConnectedAccounts.route) {
                ConnectedAccountsScreen(
                    viewModel = connectedAccountsViewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(Screen.AIAssistant.route) {
                AICareerAssistantScreen(
                    viewModel = assistantViewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            // 19. AI Mock Interview Setup
            composable(
                route = Screen.InterviewSetup.route,
                arguments = listOf(navArgument("role") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                })
            ) { backStackEntry ->
                val roleArg = backStackEntry.arguments?.getString("role")
                val decodedRole = if (!roleArg.isNullOrBlank()) {
                    try {
                        java.net.URLDecoder.decode(roleArg, "UTF-8")
                    } catch (_: Exception) {
                        roleArg
                    }
                } else null
                InterviewSetupScreen(
                    initialRole = decodedRole,
                    viewModel = interviewViewModel,
                    onNavigateBack = { navController.popBackStack() },
                    onStartInterview = { navController.navigate(Screen.LiveInterview.route) },
                    onViewReport = { sessionId ->
                        navController.navigate(Screen.InterviewReport.createRoute(sessionId))
                    }
                )
            }

            // 20. Live AI Interview Room
            composable(Screen.LiveInterview.route) {
                LiveInterviewScreen(
                    viewModel = interviewViewModel,
                    onNavigateBack = { navController.popBackStack() },
                    onInterviewComplete = { sessionId ->
                        navController.navigate(Screen.InterviewReport.createRoute(sessionId)) {
                            popUpTo(Screen.InterviewSetup.route) { inclusive = true }
                        }
                    }
                )
            }

            // 21. AI Interview Evaluation Report
            composable(
                route = Screen.InterviewReport.route,
                arguments = listOf(navArgument("sessionId") { type = NavType.StringType })
            ) { backStackEntry ->
                val sessionId = backStackEntry.arguments?.getString("sessionId") ?: ""
                InterviewReportScreen(
                    sessionId = sessionId,
                    viewModel = interviewViewModel,
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToRoadmaps = { navController.navigate(Screen.RoadmapHub.route) }
                )
            }
        }
    }
}
