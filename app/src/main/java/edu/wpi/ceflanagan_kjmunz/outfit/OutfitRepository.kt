package edu.wpi.ceflanagan_kjmunz.outfit

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import edu.wpi.ceflanagan_kjmunz.outfit.database.ClothingDatabase
import edu.wpi.ceflanagan_kjmunz.outfit.database.OutfitDatabase
import java.util.*
import java.util.concurrent.Executors

private const val DATABASE_NAME = "outfit-database"
class OutfitRepository private constructor(context: Context) {
    private val database : OutfitDatabase = Room.databaseBuilder(
        context.applicationContext,
        OutfitDatabase::class.java,
        DATABASE_NAME
    ).build()

    private val outfitDao = database.outfitDao()

    private val executor = Executors.newSingleThreadExecutor()

    fun getOutfits(): LiveData<List<Outfit>> = outfitDao.getOutfits()
    fun getOutfit(id: UUID): LiveData<Outfit?> = outfitDao.getOutfit(id)

    fun updateOutfit(outfit: Outfit) {
        executor.execute {
            outfitDao.updateOutfit(outfit)
        }
    }

    fun addOutfit(outfit: Outfit) {
        executor.execute {
            outfitDao.addOutfit(outfit)
        }
    }

    fun deleteOutfit(outfit: Outfit) {
        executor.execute{
            outfitDao.deleteOutfit(outfit)
        }
    }
    companion object {
        private var INSTANCE: OutfitRepository? = null
        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = OutfitRepository(context)
            }
        }
        fun get(): OutfitRepository {
            return INSTANCE ?:throw IllegalStateException("OutfitRepository must be initialized")
        }
    }
}