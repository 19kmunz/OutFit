package edu.wpi.ceflanagan_kjmunz.outfit

import androidx.lifecycle.ViewModel

class ClosetViewModel : ViewModel() {
    val tops = mutableListOf<Clothing>()
    val bottoms = mutableListOf<Clothing>()
    val accs = mutableListOf<Clothing>()

    init {
        for(i in 0 until 15){
            tops += Clothing(i,"Top #$i", ClothingType.TOP)
            bottoms += Clothing(i,"Bottom #$i", ClothingType.BOTTOM)
            accs += Clothing(i,"Accessory #$i", ClothingType.ACCESSORY)
        }
    }
}