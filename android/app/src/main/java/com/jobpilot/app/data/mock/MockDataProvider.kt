package com.jobpilot.app.data.mock

import com.jobpilot.app.data.model.*

object MockDataProvider {

    val currentUser = User(
        id = "user-chetan-01",
        fullName = "Chetan",
        email = "chetan@example.com",
        profileStrength = 86
    )

    val currentProfile = CareerProfile(
        id = "profile-chetan-01",
        userId = "user-chetan-01",
        personalInfo = PersonalInfo(
            fullName = "Chetan",
            email = "chetan@example.com",
            phone = "+91 98765 43210",
            location = "Bengaluru, India",
            professionalSummary = "Computer Science undergraduate passionate about backend systems, distributed services, and applied NLP. Experienced in building Python REST APIs, relational database schemas, and modern web clients."
        ),
        education = listOf(
            Education(
                id = "edu-1",
                degree = "Bachelor of Technology (B.Tech)",
                college = "National Institute of Technology",
                branch = "Computer Science and Engineering",
                startDate = "Aug 2023",
                endDate = "May 2027 (Expected)",
                grade = "8.85 CGPA"
            )
        ),
        skills = listOf(
            Skill("s1", "Python", SkillCategory.PROGRAMMING_LANGUAGE, "Proficient"),
            Skill("s2", "SQL", SkillCategory.DATABASE, "Proficient"),
            Skill("s3", "Git", SkillCategory.TOOL, "Proficient"),
            Skill("s4", "FastAPI", SkillCategory.FRAMEWORK, "Intermediate"),
            Skill("s5", "PostgreSQL", SkillCategory.DATABASE, "Intermediate"),
            Skill("s6", "Kotlin", SkillCategory.PROGRAMMING_LANGUAGE, "Intermediate"),
            Skill("s7", "Jetpack Compose", SkillCategory.FRAMEWORK, "Intermediate"),
            Skill("s8", "Docker", SkillCategory.TOOL, "Beginner"),
            Skill("s9", "Data Structures & Algorithms", SkillCategory.OTHER, "Proficient")
        ),
        experience = listOf(
            Experience(
                id = "exp-1",
                company = "Open Source Contributor",
                role = "Software Development Intern",
                startDate = "Jun 2025",
                endDate = "Aug 2025",
                description = "Implemented backend endpoints for tabular data parsing, optimized SQL query indices, and authored automated unit test suites.",
                technologies = listOf("Python", "PostgreSQL", "PyTest", "Git")
            )
        ),
        projects = listOf(
            Project(
                id = "proj-1",
                name = "JobPilot AI Assistant",
                description = "Architected an NLP-driven career companion Android app using Jetpack Compose and semantic matching algorithms.",
                technologies = listOf("Kotlin", "Jetpack Compose", "Material 3", "Coroutines"),
                startDate = "Jan 2026",
                endDate = "Present",
                githubUrl = "https://github.com/example/jobpilot",
                liveUrl = "https://jobpilot.app"
            ),
            Project(
                id = "proj-2",
                name = "Distributed Task Queue",
                description = "Constructed an asynchronous worker queue in Python with Redis backing and exponential backoff retry mechanisms.",
                technologies = listOf("Python", "Redis", "FastAPI", "Docker"),
                startDate = "Sep 2025",
                endDate = "Dec 2025",
                githubUrl = "https://github.com/example/task-queue"
            )
        ),
        certifications = listOf(
            Certification(
                id = "cert-1",
                name = "Certified Associate Python Programmer",
                issuingOrganization = "Python Institute",
                date = "Oct 2024",
                credentialUrl = "https://verify.example.org/cert-py-101"
            )
        ),
        socialProfiles = listOf(
            SocialProfile("sp-1", "GitHub", "https://github.com/chetan-dev", isVerified = true),
            SocialProfile("sp-2", "LinkedIn", "https://linkedin.com/in/chetan-dev", isVerified = true),
            SocialProfile("sp-3", "Portfolio", "https://chetan.dev", isVerified = false)
        ),
        jobPreferences = JobPreference(
            targetRoles = listOf("Python Developer Intern", "Backend Engineer", "Android Developer"),
            preferredLocations = listOf("Bengaluru", "Hyderabad", "Remote"),
            workMode = "Hybrid / Remote",
            employmentType = "Internship (6 Months)",
            salaryExpectation = "₹35,000 - ₹50,000 / month",
            preferredTechnologies = listOf("Python", "Kotlin", "FastAPI", "PostgreSQL")
        ),
        profileStrengthScore = 86
    )

    val mockJobs = listOf(
        Job(
            id = "job-1",
            title = "Python Developer Intern",
            company = "Nexus Cloud Labs",
            location = "Remote / Bengaluru",
            workMode = "Remote",
            employmentType = "Internship (6 Months)",
            stipendOrSalary = "₹45,000 / month",
            description = "Build scalable backend microservices, author clean REST APIs with asynchronous Python frameworks, and collaborate on data ingestion pipelines.",
            postedDaysAgo = 1,
            requirements = listOf("Python", "SQL", "FastAPI", "Git", "Docker"),
            matchDetails = JobMatch(
                matchScore = 94,
                matchTier = MatchTier.EXCEPTIONAL,
                strongMatches = listOf("Python", "SQL", "Git"),
                missingSkills = listOf("Docker"),
                partialMatches = listOf("FastAPI"),
                whyItMatchesExplanation = "Your demonstrated expertise in Python and relational databases strongly aligns with our core backend stack. Learning Docker basics will completely close the remaining gap."
            )
        ),
        Job(
            id = "job-2",
            title = "AI / ML Research Intern",
            company = "Cortex Vision AI",
            location = "Hybrid / Hyderabad",
            workMode = "Hybrid",
            employmentType = "Internship (3-6 Months)",
            stipendOrSalary = "₹50,000 / month",
            description = "Assist in benchmarking NLP architectures, fine-tuning transformer models, and evaluating automated text classification pipelines.",
            postedDaysAgo = 2,
            requirements = listOf("Python", "NLP", "Pandas", "Scikit-Learn", "PyTorch"),
            matchDetails = JobMatch(
                matchScore = 89,
                matchTier = MatchTier.STRONG,
                strongMatches = listOf("Python", "NLP", "Pandas"),
                missingSkills = listOf("PyTorch"),
                partialMatches = listOf("Scikit-Learn"),
                whyItMatchesExplanation = "Solid foundation in Python and text processing concepts. Your analytical coursework makes you an excellent candidate for the research track."
            )
        ),
        Job(
            id = "job-3",
            title = "Full Stack Engineer (Junior)",
            company = "Aether Systems",
            location = "Pune, India",
            workMode = "On-site",
            employmentType = "Full-time Entry Level",
            stipendOrSalary = "₹9.5 LPA + Equity",
            description = "Develop modern client interfaces and implement resilient Node.js services backed by PostgreSQL and caching layers.",
            postedDaysAgo = 4,
            requirements = listOf("React", "TypeScript", "Node.js", "PostgreSQL"),
            matchDetails = JobMatch(
                matchScore = 86,
                matchTier = MatchTier.STRONG,
                strongMatches = listOf("React", "PostgreSQL"),
                missingSkills = listOf("Node.js", "TypeScript"),
                whyItMatchesExplanation = "Strong data persistence and frontend logic foundation. Transitioning from Python to Node.js backend services will be smooth."
            )
        ),
        // LOW MATCH / SKILL GAP SHOWCASE (CORE REQUIREMENT)
        Job(
            id = "job-4",
            title = "Python Backend Developer (Cloud)",
            company = "HyperScale Cloud Systems",
            location = "Gurugram / On-site",
            workMode = "On-site",
            employmentType = "Full-time",
            stipendOrSalary = "₹12 LPA - ₹15 LPA",
            description = "Lead production cloud backend services using asynchronous FastAPI, containerized deployments on AWS ECS, and test-driven architecture.",
            postedDaysAgo = 3,
            requirements = listOf("Python", "SQL", "Git", "FastAPI", "Docker", "AWS", "Unit Testing"),
            matchDetails = JobMatch(
                matchScore = 58,
                matchTier = MatchTier.DEVELOPING,
                strongMatches = listOf("Python", "SQL", "Git"),
                missingSkills = listOf("FastAPI", "Docker", "AWS", "Unit Testing"),
                whyItMatchesExplanation = "You meet the core programming requirements (Python, SQL, Git), but this role requires heavy production cloud infrastructure and container orchestration.",
                improvementPlan = listOf(
                    ImprovementStep(
                        stepNumber = 1,
                        title = "Learn FastAPI & Async Python",
                        actionDescription = "Complete an asynchronous API project handling async database sessions.",
                        estimatedDays = 4,
                        recommendedResource = "FastAPI Official Interactive Tutorial"
                    ),
                    ImprovementStep(
                        stepNumber = 2,
                        title = "Build a REST API Microservice",
                        actionDescription = "Author a complete CRUD service with authentication tokens and swagger documentation.",
                        estimatedDays = 5,
                        recommendedResource = "Hands-on Project: Python API"
                    ),
                    ImprovementStep(
                        stepNumber = 3,
                        title = "Learn Docker Basics & Containerization",
                        actionDescription = "Write a Dockerfile and docker-compose.yml to containerize your Python service and PostgreSQL DB.",
                        estimatedDays = 3,
                        recommendedResource = "Docker for Developers Guide"
                    ),
                    ImprovementStep(
                        stepNumber = 4,
                        title = "Practice Unit Testing with PyTest",
                        actionDescription = "Implement test coverage for endpoints, error handlers, and edge cases using PyTest fixtures.",
                        estimatedDays = 3,
                        recommendedResource = "Test-Driven Development in Python"
                    )
                )
            )
        )
    )

    val mockApplications = listOf(
        JobApplication(
            id = "app-1",
            jobId = "job-1",
            jobTitle = "Python Developer Intern",
            company = "Nexus Cloud Labs",
            location = "Remote",
            currentStatus = ApplicationStatus.INTERVIEW,
            appliedDate = "Sep 05, 2026",
            lastUpdated = "Sep 12, 2026",
            notes = "Technical round scheduled for Thursday at 3:00 PM IST via Google Meet.",
            recruiterContact = "Priya Sharma (Tech Recruiter)",
            matchScoreAtApplication = 94,
            timeline = listOf(
                ApplicationEvent("ev-1", ApplicationStatus.APPLIED, "Application Submitted", "Profile and resume sent via JobPilot workflow.", "Sep 05, 2026"),
                ApplicationEvent("ev-2", ApplicationStatus.APPLICATION_RECEIVED, "Application Received", "Nexus HR system confirmed receipt of submission.", "Sep 06, 2026"),
                ApplicationEvent("ev-3", ApplicationStatus.ASSESSMENT, "Coding Assessment Passed", "Score: 98% on Python Data Structures assessment.", "Sep 09, 2026"),
                ApplicationEvent("ev-4", ApplicationStatus.INTERVIEW, "Technical Interview Scheduled", "Invited to 45-minute architectural interview.", "Sep 12, 2026")
            )
        ),
        JobApplication(
            id = "app-2",
            jobId = "job-2",
            jobTitle = "AI / ML Research Intern",
            company = "Cortex Vision AI",
            location = "Hybrid / Hyderabad",
            currentStatus = ApplicationStatus.ASSESSMENT,
            appliedDate = "Sep 08, 2026",
            lastUpdated = "Sep 11, 2026",
            notes = "Take-home NLP challenge due in 48 hours.",
            matchScoreAtApplication = 89,
            timeline = listOf(
                ApplicationEvent("ev-5", ApplicationStatus.APPLIED, "Application Submitted", "Profile sent with research portfolio.", "Sep 08, 2026"),
                ApplicationEvent("ev-6", ApplicationStatus.APPLICATION_RECEIVED, "Application Received", "Screened by engineering leadership.", "Sep 09, 2026"),
                ApplicationEvent("ev-7", ApplicationStatus.ASSESSMENT, "Take-home Dataset Challenge Sent", "Received dataset test link via email.", "Sep 11, 2026")
            )
        ),
        JobApplication(
            id = "app-3",
            jobId = "job-3",
            jobTitle = "Full Stack Engineer (Junior)",
            company = "Aether Systems",
            location = "Pune, India",
            currentStatus = ApplicationStatus.APPLIED,
            appliedDate = "Sep 12, 2026",
            lastUpdated = "Sep 12, 2026",
            notes = "Awaiting initial recruiter review.",
            matchScoreAtApplication = 86,
            timeline = listOf(
                ApplicationEvent("ev-8", ApplicationStatus.APPLIED, "Application Submitted", "Submitted via official referral link.", "Sep 12, 2026")
            )
        )
    )

    val mockNotifications = listOf(
        NotificationItem(
            id = "notif-1",
            title = "Interview Update",
            message = "Nexus Cloud Labs moved your application to Technical Interview stage.",
            timestamp = "2 hours ago",
            type = NotificationType.INTERVIEW_UPDATE,
            isRead = false,
            relatedId = "app-1"
        ),
        NotificationItem(
            id = "notif-2",
            title = "Application Reply Detected",
            message = "Cortex Vision AI sent a take-home assessment link to your inbox.",
            timestamp = "Yesterday",
            type = NotificationType.APPLICATION_UPDATE,
            isRead = false,
            relatedId = "app-2"
        ),
        NotificationItem(
            id = "notif-3",
            title = "Profile Improvement Tip",
            message = "Adding FastAPI and Docker could increase your Python match rate by +14%.",
            timestamp = "2 days ago",
            type = NotificationType.PROFILE_IMPROVEMENT,
            isRead = true
        )
    )

    val mockConnectedAccounts = listOf(
        ConnectedAccount(
            provider = AccountProvider.GOOGLE_GMAIL,
            isConnected = false,
            note = "Connect securely via Google OAuth 2.0. We never ask for or store passwords."
        ),
        ConnectedAccount(
            provider = AccountProvider.GITHUB,
            isConnected = true,
            accountEmailOrHandle = "chetan-dev",
            connectedAt = "Sep 02, 2026",
            syncStatus = "Synced (5 public repos verified)"
        ),
        ConnectedAccount(
            provider = AccountProvider.LINKEDIN,
            isConnected = true,
            accountEmailOrHandle = "in/chetan-dev",
            connectedAt = "Sep 02, 2026",
            syncStatus = "Connected"
        ),
        ConnectedAccount(
            provider = AccountProvider.PORTFOLIO,
            isConnected = true,
            accountEmailOrHandle = "https://chetan.dev",
            connectedAt = "Sep 04, 2026"
        )
    )

    val mockEmailEvents = listOf(
        EmailEvent(
            id = "mail-1",
            senderName = "Nexus Recruiting",
            senderEmail = "careers@nexuscloudlabs.com",
            subject = "Invitation to Interview: Python Developer Intern",
            snippet = "Hi Chetan, we were impressed by your profile and would like to invite you for a 45-minute technical conversation...",
            category = EmailCategory.INTERVIEW,
            detectedJobTitle = "Python Developer Intern",
            detectedCompany = "Nexus Cloud Labs",
            receivedDate = "Sep 12, 2026, 11:30 AM"
        ),
        EmailEvent(
            id = "mail-2",
            senderName = "Cortex Vision AI Talent",
            senderEmail = "talent@cortexvision.ai",
            subject = "Next Steps: AI/ML Intern Coding Exercise",
            snippet = "Thank you for applying. Please find your individualized assessment link below...",
            category = EmailCategory.ASSESSMENT,
            detectedJobTitle = "AI / ML Research Intern",
            detectedCompany = "Cortex Vision AI",
            receivedDate = "Sep 11, 2026, 04:15 PM"
        )
    )

    val mockParsedResume = ResumeParsedData(
        detectedName = "Chetan",
        detectedEmail = "chetan@example.com",
        detectedPhone = "+91 98765 43210",
        detectedEducation = currentProfile.education,
        detectedSkills = currentProfile.skills,
        detectedExperience = currentProfile.experience,
        detectedProjects = currentProfile.projects,
        missingFields = listOf(
            MissingField(
                fieldKey = "github",
                fieldLabel = "GitHub Profile URL",
                reason = "Your resume mentions 2 software projects but does not contain a verified GitHub link.",
                suggestedAction = "Connect GitHub"
            ),
            MissingField(
                fieldKey = "linkedin",
                fieldLabel = "LinkedIn Profile",
                reason = "Recruiters evaluate verified LinkedIn credentials for student applications.",
                suggestedAction = "Connect LinkedIn"
            ),
            MissingField(
                fieldKey = "project_dates",
                fieldLabel = "Project Timeline Dates",
                reason = "Adding start and end dates gives context to your technical growth.",
                suggestedAction = "Add Dates"
            )
        )
    )
}
