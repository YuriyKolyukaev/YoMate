package ru.kolyukaev.yomate.data.network.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import ru.kolyukaev.yomate.data.network.response.MainResponse

interface WeatherService {
    @GET("/data/2.5/weather")
    fun getWeatherData(@Query("id") id: Int,
                       @Query("appid") app_id: String,
                       @Query("units") units: String): Call<MainResponse>

}
