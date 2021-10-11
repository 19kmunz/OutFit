package edu.wpi.ceflanagan_kjmunz.outfit

import androidx.room.PrimaryKey
import java.util.*


data class Outfit(
    @PrimaryKey
    val id: UUID = UUID. randomUUID(),
    var name: String = "default",
    var top: Clothing? = null,
    var bottom: Clothing? = null,
    var accessory: Clothing? = null
)