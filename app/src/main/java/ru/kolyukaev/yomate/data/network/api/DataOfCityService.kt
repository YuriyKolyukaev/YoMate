package ru.kolyukaev.yomate.data.network.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import ru.kolyukaev.yomate.data.network.response.DataOfCityResponse

interface DataOfCityService {
    @GET("nearbysearch/json?")
    fun getDataOfCity(@Query("key") key: String,
                      @Query("location", encoded = true) location: String,
                      @Query("rankby") rankby: String,
//                      @Query("keyword") keyword: String,
//                      @Query("name") name: String,
                      @Query("radius") radius: Int): Call<DataOfCityResponse>


    @GET("photo")
    fun getPhoto(
        @Query("key") key: String,
        @Query("maxwidth") maxWidth: Int,
        @Query("photoreference") photoReference: String
    ): Call<String>
}