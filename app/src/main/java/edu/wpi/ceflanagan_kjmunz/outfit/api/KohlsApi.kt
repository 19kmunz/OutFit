package edu.wpi.ceflanagan_kjmunz.outfit.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

//https://kohls.p.rapidapi.com/products/
interface KohlsApi {
    @Headers(
        "x-rapidapi-host: kohls.p.rapidapi.com",
        "x-rapidapi-key: 8c8cfd433dmsh3d44305ebcebdf1p1af1ffjsnafee9ab21346"
    )
    @GET(
        "list?limit=48&offset=1"
        + "&dimensionValueID=Category%3ABottoms"
    )
    fun fetchBottoms(): Call<String>

    @Headers(
        "x-rapidapi-host: kohls.p.rapidapi.com",
        "x-rapidapi-key: 8c8cfd433dmsh3d44305ebcebdf1p1af1ffjsnafee9ab21346"
    )
    @GET(
        "list?limit=48&offset=1"
                + "&dimensionValueID=Category%3ABottoms"
    )
    fun fetchBottoms(@Query("keyword") keyword: String): Call<String>

    @Headers(
        "x-rapidapi-host: kohls.p.rapidapi.com",
        "x-rapidapi-key: 8c8cfd433dmsh3d44305ebcebdf1p1af1ffjsnafee9ab21346"
    )
    @GET(
        "list?limit=48&offset=1"
                + "&dimensionValueID=Category%3ATops"
    )
    fun fetchTops(): Call<String>

    @Headers(
        "x-rapidapi-host: kohls.p.rapidapi.com",
        "x-rapidapi-key: 8c8cfd433dmsh3d44305ebcebdf1p1af1ffjsnafee9ab21346"
    )
    @GET(
        "list?limit=48&offset=1"
                + "&dimensionValueID=Category%3ATops"
    )
    fun fetchTops(@Query("keyword") keyword: String): Call<String>

    @Headers(
        "x-rapidapi-host: kohls.p.rapidapi.com",
        "x-rapidapi-key: 8c8cfd433dmsh3d44305ebcebdf1p1af1ffjsnafee9ab21346"
    )
    @GET(
        "list?limit=48&offset=1"
                + "&dimensionValueID=Category%3AAccessories"
    )
    fun fetchAccessories(): Call<String>

    @Headers(
        "x-rapidapi-host: kohls.p.rapidapi.com",
        "x-rapidapi-key: 8c8cfd433dmsh3d44305ebcebdf1p1af1ffjsnafee9ab21346"
    )
    @GET(
        "list?limit=48&offset=1"
                + "&dimensionValueID=Category%3AAccessories"
    )
    fun fetchAccessories(@Query("keyword") keyword: String): Call<String>
}