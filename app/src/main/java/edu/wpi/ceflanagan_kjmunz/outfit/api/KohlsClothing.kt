package edu.wpi.ceflanagan_kjmunz.outfit.api

import edu.wpi.ceflanagan_kjmunz.outfit.Clothing
import edu.wpi.ceflanagan_kjmunz.outfit.ClothingType
import java.util.*

class KohlsClothing (
    var name: String = "",
    var imageLink: String = "",
    var type: ClothingType = ClothingType.ACCESSORY
) {
    fun toClothing() : Clothing {
        return Clothing(UUID.randomUUID(), name, type)
    }
}