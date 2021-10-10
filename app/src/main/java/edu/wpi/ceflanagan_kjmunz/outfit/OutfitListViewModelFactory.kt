package edu.wpi.ceflanagan_kjmunz.outfit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class OutfitListViewModelFactory() : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(OutfitListViewModel::class.java)) {
                return OutfitListViewModel() as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }

    }