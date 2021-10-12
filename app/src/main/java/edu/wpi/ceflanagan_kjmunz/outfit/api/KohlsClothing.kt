package edu.wpi.ceflanagan_kjmunz.outfit.api

import com.google.gson.annotations.SerializedName
import edu.wpi.ceflanagan_kjmunz.outfit.ClothingType

class KohlsClothing (
    var name: String = "",
    var imageLink: String = "",
    var type: ClothingType = ClothingType.NONE
)