package ru.kolyukaev.yomate.data.providers

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.kolyukaev.yomate.data.models.DataOfCityModel
import ru.kolyukaev.yomate.utils.log
import ru.kolyukaev.yomate.data.models.MainWeatherModel
import ru.kolyukaev.yomate.data.models.RvWeatherModel
import ru.kolyukaev.yomate.data.network.RestClient
import ru.kolyukaev.yomate.data.network.api.ApiMethods
import ru.kolyukaev.yomate.data.network.response.DataOfCityResponse
import ru.kolyukaev.yomate.data.network.response.MainResponse
import ru.kolyukaev.yomate.data.network.response.MainResponseAll
import ru.kolyukaev.yomate.presenters.MainWeatherPresenter
import kotlin.reflect.jvm.kotlinFunction

class MainWeatherProvider(var presenter: MainWeatherPresenter) {

    val weatherService = RestClient.createRetrofitWeather()
    val placesService = RestClient.createRetrofitPlace()

    fun loadWeather(id: Int) {
        log("loadWeather id = $id")
        val call: Call<MainResponseAll> =
            weatherService.getWeatherData(id, ApiMethods.KEY, ApiMethods.UNITS)

        call.enqueue(object : Callback<MainResponseAll> {
            override fun onFailure(call: Call<MainResponseAll>, t: Throwable) {
                log("${t.message}")
                presenter.onError("Weather request error")
            }

            override fun onResponse(
                call: Call<MainResponseAll>,
                response: Response<MainResponseAll>
            ) {
                if (response.code() == 200) {
                    val weatherResponse = response.body()!!
                    log("weatherResponse = $weatherResponse)")

                    val mainWeatherList: ArrayList<MainWeatherModel> = ArrayList()

                    val mainWeather = MainWeatherModel(
                        temperature = weatherResponse.list!![0].main!!.temp!!,
                        pressure = weatherResponse.list[0].main!!.pressure!!,
                        humidity = weatherResponse.list[0].main!!.humidity!!,
                        cloudiness = weatherResponse.list[0].clouds!!.all!!,
                        wind = weatherResponse.list[0].wind!!.speed!!,
                        icon = weatherResponse.list[0].weather!![0]!!.icon!!.toString()
                    )

                    mainWeatherList.add(mainWeather)
                    presenter.mainWeatherLoaded(mainWeatherList)

                    val rvWeatherList: ArrayList<RvWeatherModel> = ArrayList()

                    weatherResponse.list.forEach {
                        val rvWeather = RvWeatherModel(
                            temperature = it.main!!.temp!!,
                            pressure = it.main.pressure!!
                        )
                        rvWeatherList.add(rvWeather)
                    }

//                    for (i in 1..39) {
//                        val rvWeather = RvWeatherModel(
//                            temperature = weatherResponse.list[i].main!!.temp!!,
//                            pressure = weatherResponse.list[i].main!!.pressure!!
//                        )
//
//                        rvWeatherList.add(rvWeather)
//                    }
                    presenter.rvWeatherLoaded(rvWeatherList)
                }
            }
        })
    }

    fun loadData(lat: Double, lon: Double) {
        val location: String = "${lat},${lon}"
        log("location = $location")
        val call: Call<DataOfCityResponse> = placesService.getDataOfCity(
            key = ApiMethods.KEYG,
            location = location,
            rankby = ApiMethods.rankby,
            radius = ApiMethods.radius
        )

        call.enqueue(object : Callback<DataOfCityResponse> {
            override fun onFailure(call: Call<DataOfCityResponse>, t: Throwable) {
                log("onFailure = ${t.message}")
                presenter.onError("City of data request error")
            }

            override fun onResponse(
                call: Call<DataOfCityResponse>,
                response: Response<DataOfCityResponse>
            ) {
                log("response.code = ${response.code()}")
                if (response.code() == 200) {
                    val dataOfCityResponse = response.body()!!
                    log("dataOfCityResponse = $dataOfCityResponse)")

                    val dataOfCityList: ArrayList<DataOfCityModel> = ArrayList()

                    val data = DataOfCityModel(
                        photoReference = dataOfCityResponse.results?.firstOrNull()?.photos?.firstOrNull()?.photoReference
                            ?: ""
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