package com.jobpilot.app.ui.navigation

sealed class Screen(val route: String) {
    // Initial Flow
    object Splash : Screen("splash")
    object Onboarding : Screen("onboarding")
    object Login : Screen("login")
    object Register : Screen("register")
    object ForgotPassword : Screen("forgot_password")

    // Main App Flow
    object MainContainer : Screen("main_container")
    object Dashboard : Screen("dashboard")
    object Profile : Screen("profile")
    object SmartCompletion : Screen("smart_completion")
    object ResumeHub : Screen("resume_hub")
    object ResumeUpload : Screen("resume_upload")
    object ResumeBuilder : Screen("resume_builder")
    object JobList : Screen("job_list")
    object JobDetail : Screen("job_detail/{jobId}") {
        fun createRoute(jobId: String) = "job_detail/$jobId"
    }
    object SkillGapDetail : Screen("skill_gap_detail/{jobId}") {
        fun createRoute(jobId: String) = "skill_gap_detail/$jobId"
    }
    object ApplicationList : Screen("application_list")
    object ApplicationDetail : Screen("application_detail/{appId}") {
        fun createRoute(appId: String) = "application_detail/$appId"
    }
    object Notifications : Screen("notifications")
    object Settings : Screen("settings")
    object ConnectedAccounts : Screen("connected_accounts")
    object AIAssistant : Screen("ai_assistant")
}

