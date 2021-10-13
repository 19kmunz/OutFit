package edu.wpi.ceflanagan_kjmunz.outfit

import androidx.lifecycle.*
import edu.wpi.ceflanagan_kjmunz.outfit.api.KohlsFetcher

class KohlsViewModel : ViewModel() {
    private val kohlsApi = KohlsFetcher()
    val keywordsLiveData = MutableLiveData<String>()
    val typeLiveData = MutableLiveData<ClothingType>()

    val combinedValues = MediatorLiveData<Pair<ClothingType?, String?>>().apply {
        addSource(typeLiveData) {
            value = Pair(it, keywordsLiveData.value)
        }
        addSource(keywordsLiveData) {
            value = Pair(typeLiveData.value, it)
        }
    }

    var kohlsLiveData: LiveData<String> =
        Transformations.switchMap(combinedValues) { pair ->
            if(pair.second != null && pair.second != "") {
                pair.first?.let { kohlsApi.fetchClothing(it, pair.second!!) }
            } else {
                pair.first?.let { kohlsApi.fetchClothing(it) }
            }
        }

    fun loadClothes(keywords: String) {
        keywordsLiveData.value = keywords
    }

    fun loadClothes(type: ClothingType) {
        typeLiveData.value = type
    }
}