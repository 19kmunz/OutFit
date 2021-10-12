package edu.wpi.ceflanagan_kjmunz.outfit

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Clothing(
    @PrimaryKey
    val id: UUID = UUID. randomUUID(),
    var name: String = "default",
    var type: ClothingType = ClothingType.NONE
    // ADD PHOTO
) {
    val photoFileName
        get() = "IMG_$id.jpg"
}