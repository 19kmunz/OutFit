package edu.wpi.ceflanagan_kjmunz.outfit.database

import androidx.room.TypeConverter
import java.util.*

class OutfitTypeConverter {
    @TypeConverter
    fun toUUID(uuid: String?): UUID? {
        return if (uuid == null) null else UUID.fromString(uuid)
    }

    @TypeConverter
    fun fromUUID(uuid: UUID?): String? {
        return if (uuid == null) null else uuid?.toString()
    }


}