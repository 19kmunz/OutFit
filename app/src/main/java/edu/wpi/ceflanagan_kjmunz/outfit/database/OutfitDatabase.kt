package edu.wpi.ceflanagan_kjmunz.outfit.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import edu.wpi.ceflanagan_kjmunz.outfit.Outfit

@Database(entities = [Outfit::class], version = 1)
@TypeConverters(OutfitTypeConverter::class)
abstract class OutfitDatabase : RoomDatabase() {
    abstract fun outfitDao(): OutfitDao
}