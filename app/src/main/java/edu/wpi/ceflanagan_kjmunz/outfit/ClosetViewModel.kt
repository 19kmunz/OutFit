package edu.wpi.ceflanagan_kjmunz.outfit

import androidx.lifecycle.ViewModel

class ClosetViewModel : ViewModel() {
    val tops = mutableListOf<Clothing>()
    val bottoms = mutableListOf<Clothing>()
    val accs = mutableListOf<Clothing>()

    init {
        for(i in 0 until 15){
            tops += Clothing("Top #$i", ClothingType.TOP)
            bottoms += Clothing("Bottom #$i", ClothingType.BOTTOM)
            accs += Clothing("Accessory #$i", ClothingType.ACCESSORY)
        }
    }
}