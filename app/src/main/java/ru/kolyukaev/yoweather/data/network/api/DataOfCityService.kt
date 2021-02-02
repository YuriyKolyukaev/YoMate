package ru.kolyukaev.yoweather.data.network.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import ru.kolyukaev.yoweather.data.network.response.DataOfCityResponse

interface DataOfCityService {
    @GET("nearbysearch/json?")
    fun getDataOfCity(@Query("key") key: String,
                      @Query("location", encoded = true) location: String,
                      @Query("rankby") rankby: String,
                      @Query("name") keyword: String,
//                      @Query("name") name: String,
                      @Query("radius") radius: Int): Call<DataOfCityResponse>


    @GET("photo")
    fun getPhoto(
        @Query("key") key: String,
        @Query("maxwidth") maxWidth: Int,
        @Query("photoreference") photoReference: String
    ): Call<String>
}