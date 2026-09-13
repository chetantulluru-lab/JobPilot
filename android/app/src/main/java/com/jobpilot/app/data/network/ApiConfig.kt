package com.jobpilot.app.data.network

/**
 * Centralized API Base URL Configuration for JobPilot Android Client.
 *
 * NOTE FOR ANDROID DEVELOPERS:
 * When running inside the Android Emulator, "10.0.2.2" routes to the host development
 * machine's localhost:8000.
 * If running on a physical Android device via USB/Wi-Fi, change this to your computer's LAN IP
 * (e.g., "http://192.168.1.100:8000/api/v1/").
 */
object ApiConfig {
    // Development default (Android Emulator -> Host FastAPI)
    const val DEV_EMULATOR_BASE_URL = "http://10.0.2.2:8000/api/v1/"

    // Local loopback (for unit tests / desktop JVM testing)
    const val DEV_LOCAL_BASE_URL = "http://localhost:8000/api/v1/"

    // Production URL placeholder
    const val PROD_BASE_URL = "https://api.jobpilot.app/api/v1/"

    // Active Base URL used by Retrofit
    var BASE_URL: String = DEV_EMULATOR_BASE_URL
}
