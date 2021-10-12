package edu.wpi.ceflanagan_kjmunz.outfit.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import edu.wpi.ceflanagan_kjmunz.outfit.Clothing

@Database(entities = [Clothing::class], version = 1, exportSchema = false)
@TypeConverters(ClothingTypeConverter::class)
abstract class ClothingDatabase : RoomDatabase() {
    abstract fun clothingDao(): ClothingDao
}