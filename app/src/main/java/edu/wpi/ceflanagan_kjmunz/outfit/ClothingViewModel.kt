package edu.wpi.ceflanagan_kjmunz.outfit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import java.io.File
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
        clothingRepository.addClothing(clothing)
    }

    fun saveClothing(clothing: Clothing) {
        clothingRepository.updateClothing(clothing)
    }

    fun getPhotoFile(clothing: Clothing): File {
        return clothingRepository.getPhotoFile(clothing)
    }

}