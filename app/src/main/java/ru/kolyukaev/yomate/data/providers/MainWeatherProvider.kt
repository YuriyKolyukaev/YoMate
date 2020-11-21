package ru.kolyukaev.yomate.data.providers

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.kolyukaev.yomate.utils.log
import ru.kolyukaev.yomate.data.models.MainWeatherModel
import ru.kolyukaev.yomate.data.network.RestClient
import ru.kolyukaev.yomate.data.network.api.ApiMethods
import ru.kolyukaev.yomate.data.network.api.WeatherService
import ru.kolyukaev.yomate.data.network.response.MainResponse
import ru.kolyukaev.yomate.presenters.MainWeatherPresenter

class MainWeatherProvider (var presenter: MainWeatherPresenter) {

    fun loadWeather(id: Int) {
        log("$id")
        val weatherService: WeatherService = RestClient.getRetrofit()
        val call: Call<MainResponse> = weatherService.getWeatherData(id, ApiMethods.KEY, ApiMethods.UNITS)

        call.enqueue(object : Callback<MainResponse> {
            override fun onFailure(call: Call<MainResponse>, t: Throwable) {
                log("${t.message}")
                presenter.onError("Network request error")
            }

            override fun onResponse(call: Call<MainResponse>, response: Response<MainResponse>) {
                if (response.code() == 200) {
                    val weatherResponse = response.body()!!
                    log("name = $weatherResponse)")

                    val weatherList: ArrayList<MainWeatherModel> = ArrayList()

                    val weather = MainWeatherModel(
                        temperature = weatherResponse.main!!.temp,
                        pressure = weatherResponse.main!!.pressure,
                        humidity = weatherResponse.main!!.humidity,
                        cloudiness = weatherResponse.clouds!!.all,
                        wind = weatherResponse.wind!!.speed,
                        icon = weatherResponse.weather!!.get(0).icon.toString()
                    )
                    weatherList.add(weather)


                    presenter.weatherLoaded(weatherList)
                }
            }
        })
    }
}