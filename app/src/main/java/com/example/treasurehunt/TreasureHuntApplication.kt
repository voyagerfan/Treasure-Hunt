package com.example.treasurehunt

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TreasureHuntApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize app-wide configurations here
    }
}
