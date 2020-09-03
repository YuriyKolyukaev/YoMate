package ru.kolyukaev.yomate.models.providers

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.kolyukaev.yomate.log
import ru.kolyukaev.yomate.models.MainWeatherModel
import ru.kolyukaev.yomate.models.link.RestClient
import ru.kolyukaev.yomate.models.link.api.ApiMethods
import ru.kolyukaev.yomate.models.link.api.WeatherService
import ru.kolyukaev.yomate.models.link.response.MainResponse
import ru.kolyukaev.yomate.presenters.MainWeatherPresenter

class MainWeatherProvider (var presenter: MainWeatherPresenter) {
    val mApiMethods: ApiMethods = ApiMethods()

    fun loadWeather() {
        val weatherService: WeatherService = RestClient.getRetrofit()
        val call: Call<MainResponse> = weatherService.getWeatherData(mApiMethods.CITY, mApiMethods.KEY, mApiMethods.UNITS)

        call.enqueue(object : Callback<MainResponse> {
            override fun onFailure(call: Call<MainResponse>, t: Throwable) {
                log("${t.message}")
                presenter.onError(t)
            }

            override fun onResponse(call: Call<MainResponse>, response: Response<MainResponse>) {
                if (response.code() == 200) {
                    val weatherResponse = response.body()!!

                    val weatherList: ArrayList<MainWeatherModel> = ArrayList()

                    val weather = MainWeatherModel(
                        temperature = weatherResponse.main!!.temp,
                        cloudiness = weatherResponse.clouds!!.all)
                    weatherList.add(weather)

                    presenter.weatherLoaded(weatherList)
                }
            }
        })
    }
}