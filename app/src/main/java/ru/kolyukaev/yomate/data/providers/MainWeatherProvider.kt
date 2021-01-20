package ru.kolyukaev.yomate.data.providers

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.kolyukaev.yomate.data.models.DataOfCityModel
import ru.kolyukaev.yomate.utils.log
import ru.kolyukaev.yomate.data.models.MainWeatherModel
import ru.kolyukaev.yomate.data.network.RestClient
import ru.kolyukaev.yomate.data.network.api.ApiMethods
import ru.kolyukaev.yomate.data.network.response.DataOfCityResponse
import ru.kolyukaev.yomate.data.network.response.MainResponse
import ru.kolyukaev.yomate.presenters.MainWeatherPresenter

class MainWeatherProvider (var presenter: MainWeatherPresenter) {

    val weatherService = RestClient.createRetrofitWeather()
    val placesService = RestClient.createRetrofitPlace()

    fun loadWeather(id: Int) {
        log("loadWeather id = $id")
        val call: Call<MainResponse> = weatherService.getWeatherData(id, ApiMethods.KEY, ApiMethods.UNITS)

        call.enqueue(object : Callback<MainResponse> {
            override fun onFailure(call: Call<MainResponse>, t: Throwable) {
                log("${t.message}")
                presenter.onError("Weather request error")
            }

            override fun onResponse(call: Call<MainResponse>, response: Response<MainResponse>) {
                if (response.code() == 200) {
                    val weatherResponse = response.body()!!
                    log("weatherResponse = $weatherResponse)")

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

    fun loadData (lat: Double, lon: Double) {
        val location: String = "${lat},${lon}"
        log("location = $location")
        val call: Call<DataOfCityResponse> = placesService.getDataOfCity(
            key = ApiMethods.KEYG,
            location = location,
            rankby = ApiMethods.rankby,
//            keyword = ApiMethods.keyword,
//            name = ApiMethods.name,
            radius = ApiMethods.radius
        )

        call.enqueue(object: Callback<DataOfCityResponse> {
            override fun onFailure(call: Call<DataOfCityResponse>, t: Throwable) {
                log("onFailure = ${t.message}")
                presenter.onError("City of data request error")
            }
            override fun onResponse(call: Call<DataOfCityResponse>, response: Response<DataOfCityResponse>) {
                log("response.code = ${response.code()}")
                if (response.code() == 200) {
                    val dataOfCityResponse = response.body()!!
                    log("dataOfCityResponse = $dataOfCityResponse)")

                    val dataOfCityList: ArrayList<DataOfCityModel> = ArrayList()

                    val data = DataOfCityModel(
                        photoReference = dataOfCityResponse.results?.firstOrNull()?.photos?.firstOrNull()?.photoReference ?: ""
                    )
                    log("photoreference = ${data.photoReference}")

                    val photoString = getPhotoString(data.photoReference)

                    presenter.photoLoaded(photoString)

                    dataOfCityList.add(data)
                }
            }
        })
    }

    fun getPhotoString(photoReference: String): String {
        val photoUrl = placesService.getPhoto(
            key = ApiMethods.KEYG,
            maxWidth = ApiMethods.maxWidth,
            photoReference = photoReference
        ).request().url
        return photoUrl.toString()
    }
}