package ru.kolyukaev.yoweather.data.providers

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.kolyukaev.yoweather.data.models.PhotoOfCityModel
import ru.kolyukaev.yoweather.data.models.MainWeatherModel
import ru.kolyukaev.yoweather.data.models.RwWeatherBefore
import ru.kolyukaev.yoweather.data.network.RestClient
import ru.kolyukaev.yoweather.data.network.api.ApiMethods
import ru.kolyukaev.yoweather.data.network.response.DataOfCityResponse
import ru.kolyukaev.yoweather.data.network.response.MainResponseAll
import ru.kolyukaev.yoweather.presenters.MainWeatherPresenter
import ru.kolyukaev.yoweather.utils.log

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

                    val lat = weatherResponse.city?.coord?.lat ?: 0.0
                    val lon = weatherResponse.city?.coord?.lon ?: 0.0
                    loadPhoto(lat,lon)

                    val mainWeatherList: ArrayList<MainWeatherModel> = ArrayList()

                    val mainWeather = MainWeatherModel(
                        weather = weatherResponse.list?.firstOrNull()?.weather?.firstOrNull()?.main ?: "",
                        temperature = weatherResponse.list?.firstOrNull()?.main?.temp ?: 0.0,
                        feelsLike = weatherResponse.list?.firstOrNull()?.main?.feelsLike ?: 0.0,
                        pressure = weatherResponse.list?.firstOrNull()?.main?.pressure ?: 0.0,
                        humidity = weatherResponse.list?.firstOrNull()?.main?.humidity ?: 0,
                        cloudiness = weatherResponse.list?.firstOrNull()?.clouds?.all ?: 0,
                        wind = weatherResponse.list?.firstOrNull()?.wind?.speed ?: 0.0,
                        icon = weatherResponse.list?.firstOrNull()?.weather?.firstOrNull()?.icon ?: "",
                        visibility = weatherResponse.list?.firstOrNull()?.visibility ?: 0,
                        precipitation = weatherResponse.list?.firstOrNull()?.pop ?: 0.0
                    )

                    mainWeatherList.add(mainWeather)
                    presenter.onGeneralWeatherLoaded(mainWeatherList)

                    val rvWeatherList: ArrayList<RwWeatherBefore> = ArrayList()

                    weatherResponse.list?.forEach() {
                        val rvWeather = RwWeatherBefore(
                            dtTxtDate = it.dtTxt.toString(),
                            dtTxtTime = it.dtTxt.toString(),
                            temperature = it.main?.temp ?: 0.0,
                            icon = it.weather?.first()?.icon ?: "",
                            humidity = it.main?.humidity ?: 0
                        )
                        rvWeatherList.add(rvWeather)
                    }

                    presenter.onListWeatherLoaded(rvWeatherList)
                }
            }
        })
    }

    fun loadPhoto(lat: Double, lon: Double) {
        val location = "${lat},${lon}"
        log("location = $location")

        val call: Call<DataOfCityResponse> = placesService.getDataOfCity(
            key = ApiMethods.KEYG,
            location = location,
            rankby = ApiMethods.RANKBY,
            keyword = ApiMethods.NAME,
            radius = ApiMethods.RADIUS
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

                    if (resultList != null) {
                        val photoOfList = setFilterByDimensionsAndReference(dataOfCityResponse)


                        if (photoOfList.size != 0) {
                            val data = PhotoOfCityModel(
                                photoReference = photoOfList.random().photoReference

                            )
                            val photoString = getPhotoString(data.photoReference)
                            presenter.photoLoaded(photoString)
                        }
                    }
                }
            }
        })

    }

    fun setFilterByDimensionsAndReference(
        dataOfCityResponse: DataOfCityResponse
    ): ArrayList<PhotoOfCityModel> {
        val photoOfCityList: ArrayList<PhotoOfCityModel> = ArrayList()
        dataOfCityResponse.results?.forEach() {
            val photoReference = it?.photos?.firstOrNull()?.photoReference ?: ""
            val photoHeight = it?.photos?.firstOrNull()?.height ?: 0
            val photoWidth = it?.photos?.firstOrNull()?.width ?: 0
            if (photoReference != "" && photoHeight >= ApiMethods.PHOTO_HEIGHT && photoWidth >= ApiMethods.PHOTO_WIDTH) {
                val photo = PhotoOfCityModel(
                    photoReference = photoReference
                )
                photoOfCityList.add(photo)
            }
        }
        return photoOfCityList
    }

    fun getPhotoString(photoReference: String): String {
        val photoUrl = placesService.getPhoto(
            key = ApiMethods.KEYG,
            maxWidth = ApiMethods.MAXWIDTH,
            photoReference = photoReference
        ).request().url
        return photoUrl.toString()
    }

}