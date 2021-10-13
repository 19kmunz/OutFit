package edu.wpi.ceflanagan_kjmunz.outfit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import java.util.*

class OutfitViewModel : ViewModel() {
    private val outfitRepository = OutfitRepository.get()
    private val outfitIdLiveData = MutableLiveData<UUID>()

    var outfitLiveData: LiveData<Outfit?> =
        Transformations.switchMap(outfitIdLiveData) { outfitID ->
            outfitRepository.getOutfit(outfitID)
        }

    fun loadOutfit(outfitID: UUID) {
        outfitIdLiveData.postValue(outfitID)
    }

    fun addOutfit(outfit: Outfit) {
        outfitRepository.addOutfit(outfit)
    }

    fun saveOutfit(outfit: Outfit) {
        outfitRepository.updateOutfit(outfit)
    }

}