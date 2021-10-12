package edu.wpi.ceflanagan_kjmunz.outfit

import androidx.lifecycle.ViewModel
import java.io.File

class OutfitListViewModel() : ViewModel() {
        private val outfitRepository = OutfitRepository.get()
        val outfitLiveData = outfitRepository.getOutfits()

        fun deleteOutfit(outfit: Outfit){
                outfitRepository.deleteOutfit(outfit)
        }

        fun getTopPhotoFile(outfit: Outfit): File {
                return outfitRepository.getTopPhotoFile(outfit)
        }

        fun getBottomPhotoFile(outfit: Outfit): File {
                return outfitRepository.getBottomPhotoFile(outfit)
        }

        fun getAccessoryPhotoFile(outfit: Outfit): File {
                return outfitRepository.getAccessoryPhotoFile(outfit)
        }
}