package ru.kolyukaev.yomate.data.providers

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.kolyukaev.yomate.log
import ru.kolyukaev.yomate.data.models.MainWeatherModel
import ru.kolyukaev.yomate.data.network.RestClient
import ru.kolyukaev.yomate.data.network.api.ApiMethods
import ru.kolyukaev.yomate.data.network.api.WeatherService
import ru.kolyukaev.yomate.data.network.response.MainResponse
import ru.kolyukaev.yomate.presenters.MainWeatherPresenter

class MainWeatherProvider (var presenter: MainWeatherPresenter) {

    fun loadWeather(nameCity: String) {
        val weatherService: WeatherService = RestClient.getRetrofit()
        val call: Call<MainResponse> = weatherService.getWeatherData(nameCity, ApiMethods.KEY, ApiMethods.UNITS)

        call.enqueue(object : Callback<MainResponse> {
            override fun onFailure(call: Call<MainResponse>, t: Throwable) {
                log("${t.message}")
                presenter.onError(t)
            }

            override fun onResponse(call: Call<MainResponse>, response: Response<MainResponse>) {
                if (response.code() == 200) {
                    val weatherResponse = response.body()!!
                    log("name = $weatherResponse)")


                    val weatherList: ArrayList<MainWeatherModel> = ArrayList()

                    val weather = MainWeatherModel(
                        temperature = weatherResponse.main!!.temp,
                        cloudiness = weatherResponse.clouds!!.all)
                    weatherList.add(weather)

                    Log.e("TEST_TEST", "weather ${weatherResponse.base} ${weatherResponse.clouds} ${weatherResponse.cod} ${weatherResponse.main}")


                    presenter.weatherLoaded(weatherList)
                }
            }
        })
    }
}