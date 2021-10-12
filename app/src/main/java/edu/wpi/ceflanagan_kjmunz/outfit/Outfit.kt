package edu.wpi.ceflanagan_kjmunz.outfit

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*


@Entity
data class Outfit(
    @PrimaryKey
    val id: UUID = UUID. randomUUID(),
    var name: String = "default",
    var top: String?,
    var bottom: String?,
    var accessory: String?,
    var topPhotoFileName: String?,
    var bottomPhotoFileName: String?,
    var accessoryPhotoFileName: String?
)