package ru.kolyukaev.yomate.models.link

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.kolyukaev.yomate.models.link.api.WeatherService

class RestClient {

    companion object Factory {
        fun getRetrofit(): WeatherService {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://api.openweathermap.org/")
                .build()
            return retrofit.create(WeatherService::class.java);
        }
    }

}
