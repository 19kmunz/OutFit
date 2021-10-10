package edu.wpi.ceflanagan_kjmunz.outfit

import android.app.Application
import android.util.Log

class ClothingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        ClothingRepository.initialize(this)
    }
}