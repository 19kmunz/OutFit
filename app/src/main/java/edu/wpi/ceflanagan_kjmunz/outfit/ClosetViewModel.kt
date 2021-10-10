package edu.wpi.ceflanagan_kjmunz.outfit

import androidx.lifecycle.ViewModel
import java.util.*

class ClosetViewModel : ViewModel() {
    /*
    val tops = mutableListOf<Clothing>()
    val bottoms = mutableListOf<Clothing>()
    val accs = mutableListOf<Clothing>()

    init {
        for(i in 0 until 15){
            tops += Clothing(UUID.randomUUID(), "Top #$i", ClothingType.TOP)
            bottoms += Clothing(UUID.randomUUID(), "Bottom #$i", ClothingType.BOTTOM)
            accs += Clothing(UUID.randomUUID(), "Accessory #$i", ClothingType.ACCESSORY)
        }
    }
     */
    private val clothingRepository = ClothingRepository.get()
    val topsLiveData = clothingRepository.getTops()
    val bottomsLiveData = clothingRepository.getBottoms()
    val accsLiveData = clothingRepository.getAccessories()

    fun deleteClothing(clothing: Clothing){
        clothingRepository.deleteClothing(clothing)
    }
}