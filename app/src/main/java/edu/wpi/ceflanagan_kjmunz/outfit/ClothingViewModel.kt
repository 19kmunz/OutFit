package edu.wpi.ceflanagan_kjmunz.outfit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import java.util.*

class ClothingViewModel : ViewModel() {
    private val clothingRepository = ClothingRepository.get()
    private val clothingIdLiveData = MutableLiveData<UUID>()

    var clothingLiveData: LiveData<Clothing?> =
        Transformations.switchMap(clothingIdLiveData) { clothingID ->
            clothingRepository.getClothing(clothingID)
        }

    fun loadClothing(clothingID: UUID) {
        clothingIdLiveData.value = clothingID
    }

    fun addClothing(clothing: Clothing) {
        clothingRepository.addScore(clothing)
    }

    fun saveClothing(clothing: Clothing) {
        clothingRepository.updateScore(clothing)
    }
}