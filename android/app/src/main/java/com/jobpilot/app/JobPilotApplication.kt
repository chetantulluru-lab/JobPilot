package com.jobpilot.app

import android.app.Application
import com.jobpilot.app.data.AppContainer
import com.jobpilot.app.data.DefaultAppContainer

class JobPilotApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
        com.jobpilot.app.ui.theme.ThemeManager.init(this)
    }
}
