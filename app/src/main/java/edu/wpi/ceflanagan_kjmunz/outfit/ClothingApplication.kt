package edu.wpi.ceflanagan_kjmunz.outfit

import android.app.Application
import android.util.Log
private const val TAG = "ClothingApplication"

class ClothingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        ClothingRepository.initialize(this)
        OutfitRepository.initialize(this)
        Log.d(TAG, "ClothingRepository and OutfitRepository initialized")

    }
}