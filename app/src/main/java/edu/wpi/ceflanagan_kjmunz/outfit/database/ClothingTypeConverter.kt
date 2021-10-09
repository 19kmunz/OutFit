package edu.wpi.ceflanagan_kjmunz.outfit.database

import androidx.room.TypeConverter
import edu.wpi.ceflanagan_kjmunz.outfit.ClothingType
import java.util.*

class ClothingTypeConverter {
    @TypeConverter
    fun toUUID(uuid: String?): UUID? {
        return UUID.fromString(uuid)
    }
    @TypeConverter
    fun fromUUID(uuid: UUID?): String? {
        return uuid?.toString()
    }
    @TypeConverter
    fun toClothingType(value: String) = enumValueOf<ClothingType>(value)

    @TypeConverter
    fun fromHealth(value: ClothingType) = value.name
}