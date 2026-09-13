package com.jobpilot.app.ui.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.jobpilot.app.ui.screens.assistant.AICareerAssistantScreen

import com.jobpilot.app.ui.screens.applications.ApplicationListScreen
import com.jobpilot.app.ui.screens.auth.ForgotPasswordScreen
import com.jobpilot.app.ui.screens.auth.LoginScreen
import com.jobpilot.app.ui.screens.auth.RegisterScreen
import com.jobpilot.app.ui.screens.dashboard.DashboardScreen
import com.jobpilot.app.ui.screens.jobs.JobDetailScreen
import com.jobpilot.app.ui.screens.jobs.JobListScreen
import com.jobpilot.app.ui.screens.jobs.SkillGapDetailScreen
import com.jobpilot.app.ui.screens.notifications.NotificationCenterScreen
import com.jobpilot.app.ui.screens.onboarding.OnboardingScreen
import com.jobpilot.app.ui.screens.profile.ProfileScreen
import com.jobpilot.app.ui.screens.profile.SmartCompletionScreen
import com.jobpilot.app.ui.screens.resume.ResumeBuilderScreen
import com.jobpilot.app.ui.screens.resume.ResumeHubScreen
import com.jobpilot.app.ui.screens.resume.ResumeUploadFlowScreen
import com.jobpilot.app.ui.screens.settings.ConnectedAccountsScreen
import com.jobpilot.app.ui.screens.settings.SettingsScreen
import com.jobpilot.app.ui.screens.splash.SplashScreen
import com.jobpilot.app.ui.theme.BgWarmWhite
import com.jobpilot.app.ui.theme.Orange500
import com.jobpilot.app.ui.theme.Orange50
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


    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomNavItems = listOf(
        BottomNavItem("Dashboard", Screen.Dashboard.route, Icons.Default.Dashboard),
        BottomNavItem("Jobs", Screen.JobList.route, Icons.Default.Work),
        BottomNavItem("Applications", Screen.ApplicationList.route, Icons.Default.Assignment),
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
                        navController.navigate(Screen.Onboarding.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    }
                )
            }

            // 2. Onboarding
            composable(Screen.Onboarding.route) {
                OnboardingScreen(
                    onFinished = {
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
                        navController.navigate(Screen.Dashboard.route) {
                            popUpTo(Screen.Register.route) { inclusive = true }
                        }
                    },
                    onNavigateToLogin = {
                        navController.popBackStack()
                    }
                )
            }

            // 5. Auth: Forgot Password
            composable(Screen.ForgotPassword.route) {
                ForgotPasswordScreen(
                    viewModel = authViewModel,
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }

            // 6. Dashboard
            composable(Screen.Dashboard.route) {
                DashboardScreen(
                    viewModel = dashboardViewModel,
                    onNavigateToProfile = { navController.navigate(Screen.Profile.route) },
                    onNavigateToSmartCompletion = { navController.navigate(Screen.SmartCompletion.route) },
                    onNavigateToJobs = { navController.navigate(Screen.JobList.route) },
                    onNavigateToJobDetail = { jobId ->
                        navController.navigate(Screen.JobDetail.createRoute(jobId))
                    },
                    onNavigateToApplications = { navController.navigate(Screen.ApplicationList.route) },
                    onNavigateToApplicationDetail = { appId ->
                        navController.navigate(Screen.ApplicationDetail.createRoute(appId))
                    },
                    onNavigateToResume = { navController.navigate(Screen.ResumeHub.route) },
                    onNavigateToNotifications = { navController.navigate(Screen.Notifications.route) },
                    onNavigateToAssistant = { navController.navigate(Screen.AIAssistant.route) }
                )
            }


            // 7. Profile
            composable(Screen.Profile.route) {
                ProfileScreen(
                    viewModel = profileViewModel,
                    onNavigateToSmartCompletion = { navController.navigate(Screen.SmartCompletion.route) }
                )
            }

            // 8. Smart Completion
            composable(Screen.SmartCompletion.route) {
                SmartCompletionScreen(
                    viewModel = profileViewModel,
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToConnectedAccounts = { navController.navigate(Screen.ConnectedAccounts.route) }
                )
            }

            // 9. Resume Hub
            composable(Screen.ResumeHub.route) {
                ResumeHubScreen(
                    viewModel = resumeViewModel,
                    onNavigateToUpload = { navController.navigate(Screen.ResumeUpload.route) },
                    onNavigateToBuilder = { navController.navigate(Screen.ResumeBuilder.route) }
                )
            }

            // 10. Resume Upload Flow
            composable(Screen.ResumeUpload.route) {
                ResumeUploadFlowScreen(
                    viewModel = resumeViewModel,
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToProfile = { navController.navigate(Screen.Profile.route) }
                )
            }

            // 11. Resume Builder
            composable(Screen.ResumeBuilder.route) {
                ResumeBuilderScreen(
                    viewModel = resumeViewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            // 12. Job List
            composable(Screen.JobList.route) {
                JobListScreen(
                    viewModel = jobViewModel,
                    onNavigateToJobDetail = { jobId ->
                        navController.navigate(Screen.JobDetail.createRoute(jobId))
                    }
                )
            }

            // 13. Job Detail
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
                    }
                )
            }

            // 14. Skill Gap Detail
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

            // 15. Application List
            composable(Screen.ApplicationList.route) {
                ApplicationListScreen(
                    viewModel = applicationViewModel,
                    onNavigateToDetail = { appId ->
                        navController.navigate(Screen.ApplicationDetail.createRoute(appId))
                    }
                )
            }

            // 16. Application Detail
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

            // 17. Notification Center
            composable(Screen.Notifications.route) {
                NotificationCenterScreen(
                    viewModel = notificationViewModel,
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToApplication = { appId ->
                        navController.navigate(Screen.ApplicationDetail.createRoute(appId))
                    }
                )
            }

            // 18. Settings
            composable(Screen.Settings.route) {
                SettingsScreen(
                    authViewModel = authViewModel,
                    onNavigateToConnectedAccounts = {
                        navController.navigate(Screen.ConnectedAccounts.route)
                    },
                    onLogout = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }

            // 19. Connected Accounts
            composable(Screen.ConnectedAccounts.route) {
                ConnectedAccountsScreen(
                    viewModel = connectedAccountsViewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            // 20. AI Career Coach Assistant
            composable(Screen.AIAssistant.route) {
                AICareerAssistantScreen(
                    viewModel = assistantViewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}

