package ru.kolyukaev.yomate.models.link.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import ru.kolyukaev.yomate.models.link.response.MainResponse
import javax.xml.transform.Result

interface WeatherService {
    @GET("/data/2.5/weather?q=Moscow&appid=70a8211b10721a6d7056612c3ee346e9&units=metric")
    fun getWeatherData(@Query("q") q: String, @Query("appid") app_id: String, @Query("units") units: String): Call<MainResponse>
}
