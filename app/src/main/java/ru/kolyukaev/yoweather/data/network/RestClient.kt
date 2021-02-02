package ru.kolyukaev.yoweather.data.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.kolyukaev.yoweather.data.network.api.DataOfCityService
import ru.kolyukaev.yoweather.data.network.api.WeatherService

class RestClient {

    companion object Factory {

        private fun createLoggingInterceptor(): Interceptor {
            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            return interceptor
        }

        fun createRetrofitWeather(): WeatherService {
            val loggingInterceptor = createLoggingInterceptor()
            val okHttpClient = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://api.openweathermap.org/")
                .client(okHttpClient)
                .build()
            return retrofit.create(WeatherService::class.java);
        }

        fun createRetrofitPlace(): DataOfCityService {
            val loggingInterceptor = createLoggingInterceptor()
            val okHttpClient = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://maps.googleapis.com/maps/api/place/")
                .client(okHttpClient)
                .build()
            return retrofit.create(DataOfCityService::class.java)
        }
    }

}
