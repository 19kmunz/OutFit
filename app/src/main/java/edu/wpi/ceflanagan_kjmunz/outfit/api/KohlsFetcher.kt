package edu.wpi.ceflanagan_kjmunz.outfit.api

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import edu.wpi.ceflanagan_kjmunz.outfit.ClothingType
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

private const val TAG = "KohlsFetcher";
class KohlsFetcher {
    private val kohlsApi: KohlsApi
    init {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://kohls.p.rapidapi.com/products/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        kohlsApi = retrofit.create(KohlsApi::class.java)
    }

    fun fetchClothing(): LiveData<String> {
        val responseLiveData: MutableLiveData<String> = MutableLiveData()
        val flickrRequest: Call<String> = kohlsApi.fetchAccessories()
        flickrRequest.enqueue(object : Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e(TAG, "Failed to fetch clothing", t)
            }

            override fun onResponse(
                call: Call<String>,
                response: Response<String>
            ) {
                Log.d(TAG, "Response received" + response.body())
                responseLiveData.value = response.body()
            }
        })
        return responseLiveData
    }

    fun fetchClothing(type : ClothingType): LiveData<String> {
        val responseLiveData: MutableLiveData<String> = MutableLiveData()
        val flickrRequest: Call<String> = fetchClothesByType(type)
        flickrRequest.enqueue(object : Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e(TAG, "Failed to fetch clothes of type $type", t)
            }

            override fun onResponse(
                call: Call<String>,
                response: Response<String>
            ) {
                Log.d(TAG, "Response received" + response.body())
                responseLiveData.value = response.body()
            }
        })
        return responseLiveData
    }

    fun fetchClothing(type : ClothingType, keywords: String): LiveData<String> {
        val responseLiveData: MutableLiveData<String> = MutableLiveData()
        val flickrRequest: Call<String> = fetchClothesByType(type, keywords)
        flickrRequest.enqueue(object : Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e(TAG, "Failed to fetch clothes of type $type with keywords '$keywords'", t)
            }

            override fun onResponse(
                call: Call<String>,
                response: Response<String>
            ) {
                Log.d(TAG, "Response received" + response.body())
                responseLiveData.value = response.body()
            }
        })
        return responseLiveData
    }


    fun fetchClothesByType(type: ClothingType): Call<String> {
        return when (type) {
            ClothingType.TOP -> {
                kohlsApi.fetchTops()
            }
            ClothingType.BOTTOM -> {
                kohlsApi.fetchBottoms()
            }
            else -> {
                kohlsApi.fetchAccessories()
            }
        }
    }

    fun fetchClothesByType(type: ClothingType, keywords: String): Call<String> {
        return when (type) {
            ClothingType.TOP -> {
                kohlsApi.fetchTops(keywords)
            }
            ClothingType.BOTTOM -> {
                kohlsApi.fetchBottoms(keywords)
            }
            else -> {
                kohlsApi.fetchAccessories(keywords)
            }
        }
    }
}