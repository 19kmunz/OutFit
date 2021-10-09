package edu.wpi.ceflanagan_kjmunz.outfit

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import edu.wpi.ceflanagan_kjmunz.outfit.database.ClothingDatabase
import java.util.*

private const val DATABASE_NAME = "clothing-database"
class ClothingRepository private constructor(context: Context) {
    private val database : ClothingDatabase = Room.databaseBuilder(
        context.applicationContext,
        ClothingDatabase::class.java,
        DATABASE_NAME
    ).build()

    private val clothingDao = database.clothingDao()

    fun getClothes(): LiveData<List<Clothing>> = clothingDao.getClothes()
    fun getTops(): LiveData<List<Clothing>> = clothingDao.getClothesByType(ClothingType.TOP)
    fun getBottoms(): LiveData<List<Clothing>> = clothingDao.getClothesByType(ClothingType.BOTTOM)
    fun getAccessories(): LiveData<List<Clothing>> = clothingDao.getClothesByType(ClothingType.ACCESSORY)
    fun getClothing(id: UUID): LiveData<Clothing> = clothingDao.getClothing(id)

    companion object {
        private var INSTANCE: ClothingRepository? = null
        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = ClothingRepository(context)
            }
        }
        fun get(): ClothingRepository {
            return INSTANCE ?:throw IllegalStateException("ClothingRepository must be initialized")
        }
    }
}