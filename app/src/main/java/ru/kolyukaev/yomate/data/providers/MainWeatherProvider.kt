package ru.kolyukaev.yomate.data.providers

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.kolyukaev.yomate.data.models.PhotoOfCityModel
import ru.kolyukaev.yomate.data.models.MainWeatherModel
import ru.kolyukaev.yomate.data.models.RwWeatherBefore
import ru.kolyukaev.yomate.data.network.RestClient
import ru.kolyukaev.yomate.data.network.api.ApiMethods
import ru.kolyukaev.yomate.data.network.response.DataOfCityResponse
import ru.kolyukaev.yomate.data.network.response.MainResponseAll
import ru.kolyukaev.yomate.presenters.MainWeatherPresenter
import ru.kolyukaev.yomate.utils.log

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
                        weather = weatherResponse.list!![0].weather!![0]!!.main!!,
                        temperature = weatherResponse.list[0].main!!.temp!!,
                        feelsLike = weatherResponse.list[0].main!!.feelsLike!!,
                        pressure = weatherResponse.list[0].main!!.pressure!!,
                        humidity = weatherResponse.list[0].main!!.humidity!!,
                        cloudiness = weatherResponse.list[0].clouds!!.all!!,
                        wind = weatherResponse.list[0].wind!!.speed!!,
                        icon = weatherResponse.list[0].weather!![0]!!.icon!!.toString(),
                        visibility = weatherResponse.list[0].visibility!!,
                        precipitation = weatherResponse.list[0].pop!!
                    )

                    mainWeatherList.add(mainWeather)
                    presenter.mainWeatherLoaded(mainWeatherList)

                    val rvWeatherList: ArrayList<RwWeatherBefore> = ArrayList()

                    weatherResponse.list.forEach() {
                        val rvWeather = RwWeatherBefore(
                            dtTxtDate = it.dtTxt.toString(),
                            dtTxtTime = it.dtTxt.toString(),
                            temperature = it.main!!.temp!!,
                            icon = it.weather!!.first()!!.icon!!,
                            humidity = it.main.humidity!!
                        )
                        rvWeatherList.add(rvWeather)
                    }

                    presenter.rvWeatherLoaded(rvWeatherList)
                }
            }
        })
    }

    fun loadPhoto(lat: Double, lon: Double) {
        val location = "${lat},${lon}"
        log("location = $location")
        val photoOfCityList: ArrayList<PhotoOfCityModel> = ArrayList()

        val call: Call<DataOfCityResponse> = placesService.getDataOfCity(
            key = ApiMethods.KEYG,
            location = location,
            rankby = ApiMethods.rankby,
            radius = ApiMethods.radius
        )

        call.enqueue(object : Callback<DataOfCityResponse> {
            override fun onFailure(call: Call<DataOfCityResponse>, t: Throwable) {
                presenter.onError("City of data request error")
            }

            override fun onResponse(
                call: Call<DataOfCityResponse>,
                response: Response<DataOfCityResponse>

            ) {
                log("response.code = ${response.code()}")
                if (response.code() == 200) {

                    val dataOfCityResponse = response.body()!!
                    val resultList = dataOfCityResponse.results?.firstOrNull()
                    val photosList = dataOfCityResponse.results?.firstOrNull()?.photos

                    if (resultList != null && photosList != null) {

                        dataOfCityResponse.results?.forEach() {
                            val photoReference = it.photos?.firstOrNull()?.photoReference ?: ""
                            val photoHeight = it.photos?.firstOrNull()?.height ?: 0
                            val photoWidth = it.photos?.firstOrNull()?.width ?: 0

                            if (photoReference != "" && photoHeight >= 600 && photoWidth >= 600) {
                                val photo = PhotoOfCityModel(
                                    photoReference = photoReference
                                )
                                photoOfCityList.add(photo)
                            }
                        }

                        val data = PhotoOfCityModel(
                            photoReference = photoOfCityList.random().photoReference

                        )

                        val photoString = getPhotoString(data.photoReference)
                        photoOfCityList.add(data)
                        presenter.photoLoaded(photoString)
                    }
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