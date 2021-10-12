package edu.wpi.ceflanagan_kjmunz.outfit

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Outfit(
    @PrimaryKey
    val id: UUID = UUID. randomUUID(),
    var name: String = "default",
    var top: UUID?,
    var bottom: UUID?,
    var accessory: UUID?
)