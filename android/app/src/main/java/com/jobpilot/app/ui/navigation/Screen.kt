package com.jobpilot.app.ui.navigation

sealed class Screen(val route: String) {
    // Initial Flow
    object Splash : Screen("splash")
    object Onboarding : Screen("onboarding")
    object Login : Screen("login")
    object Register : Screen("register")
    object ForgotPassword : Screen("forgot_password")
    object CareerProfileOnboarding : Screen("career_profile_onboarding")

    // Main MVP App Flow (5 Bottom Tabs + Roadmap Sub-flows)
    object MainContainer : Screen("main_container")
    object Dashboard : Screen("dashboard")
    object RoadmapHub : Screen("roadmap_hub")
    object RoadmapCreate : Screen("roadmap_create")
    object RoadmapDetail : Screen("roadmap_detail/{roadmapId}") {
        fun createRoute(roadmapId: String) = "roadmap_detail/$roadmapId"
    }
    object DayLearning : Screen("day_learning/{roadmapId}/{dayId}") {
        fun createRoute(roadmapId: String, dayId: String) = "day_learning/$roadmapId/$dayId"
    }
    object ResumeHub : Screen("resume_hub")
    object ResumeUpload : Screen("resume_upload")
    object ResumeBuilder : Screen("resume_builder")
    object Profile : Screen("profile")
    object Settings : Screen("settings")
    object SmartCompletion : Screen("smart_completion")

    // Isolated / Deactivated Marketplace and Secondary Screens
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
    object ConnectedAccounts : Screen("connected_accounts")
    object AIAssistant : Screen("ai_assistant")
}
