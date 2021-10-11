package edu.wpi.ceflanagan_kjmunz.outfit

import androidx.lifecycle.ViewModel

class OutfitListViewModel() : ViewModel() {
        private val outfitRepository = OutfitRepository.get()
        val outfitLiveData = outfitRepository.getOutfits()


        fun deleteOutfit(outfit: Outfit){
                outfitRepository.deleteOutfit(outfit)
        }
}