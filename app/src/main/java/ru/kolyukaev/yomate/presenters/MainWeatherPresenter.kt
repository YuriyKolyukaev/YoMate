package ru.kolyukaev.yomate.presenters

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import ru.kolyukaev.yomate.R
import ru.kolyukaev.yomate.data.models.MainWeatherModel
import ru.kolyukaev.yomate.data.models.RwWeatherAfter
import ru.kolyukaev.yomate.data.models.RwWeatherBefore
import ru.kolyukaev.yomate.data.network.api.ApiMethods
import ru.kolyukaev.yomate.data.providers.MainWeatherProvider
import ru.kolyukaev.yomate.utils.log
import ru.kolyukaev.yomate.views.MainWeatherView

@InjectViewState
class MainWeatherPresenter : MvpPresenter<MainWeatherView>() {

    fun loadingWeatherOfCity(isSuccess: Boolean, id: Int?) {
        log("loadingWeatherCity")
        val id1: Int = id ?: ApiMethods.ID
        viewState.startLoading()
        if (isSuccess) {
            MainWeatherProvider(presenter = this).loadWeather(id1)
        } else {
            viewState.showError("Weather state is incorrect")
        }
    }

    fun loadingDataOfCity(lat: Double?, lon: Double?) {
        log("loadingDataOfCity")
        val lat1 = lat ?: ApiMethods.lat
        val lon1 = lon ?: ApiMethods.lon
        MainWeatherProvider(presenter = this).loadPhoto(lat1, lon1)
    }

    fun mainWeatherLoaded(weatherList: ArrayList<MainWeatherModel>) {
        log("weatherLoaded")
        viewState.endLoading()
        viewState.showComponents()
        if (weatherList.size == 0) {
            viewState.showError("Weather state is empty")
        } else {
            val weather = weatherList[0].weather
            val temperature = "${weatherList[0].temperature.toInt()} С°"
            val feelsLike = "Feels like ${weatherList[0].feelsLike.toInt()} С°"
            val icon = getIconWeather(weatherList[0].icon)
            val humidity = "${weatherList[0].humidity} %"
            val cloudiness = "${weatherList[0].cloudiness} %"
            val pressure = "${weatherList[0].pressure.toInt()} mm Hg"
            val wind = "${weatherList[0].wind.toInt()} m/s"
            val visibility = "${weatherList[0].visibility} m"
            val presipitation = "${weatherList[0].precipitation}"
            viewState.getWeatherResponse(
                weather,
                temperature,
                feelsLike,
                pressure,
                humidity,
                cloudiness,
                wind,
                icon,
                visibility,
                presipitation
            )
        }
    }

    fun rvWeatherLoaded(weatherList: ArrayList<RwWeatherBefore>) {

        val rvWeatherList: ArrayList<RwWeatherAfter> = ArrayList()

        log("weatherList ${weatherList.size}")
        weatherList.forEach() {

            val rvWeather = RwWeatherAfter(
                dtTxtDate = getSubstringDate(it.dtTxtDate),
                dtTxtTime = getSubstringTime(it.dtTxtTime),
                temperature = "t = ${it.temperature.toInt()} С°",
                icon = getIconWeather(it.icon),
                humidity = "φ = ${it.humidity} %"
            )
            rvWeatherList.add(rvWeather)
        }

        viewState.setIndentTopAndBottom()
        viewState.getWeatherHoursResponse(rvWeatherList)
    }

    fun getSubstringDate(s: String): String {

        val sub = s
        val substring = sub.substring(0, 10)

        return substring
    }

    fun getSubstringTime(s: String): String {

        val sub = s
        val substring = sub.substring(12, 16)

        return substring
    }

    fun photoLoaded(photoString: String) {
        viewState.replaceBackground(photoString)

    }

    fun onError(t: String) {
        viewState.showError(t)
    }

    fun getIconWeather(icon: String): Int {
        return when (icon) {
            "01d" -> R.drawable.d_01
            "01n" -> R.drawable.n_01
            "02d" -> R.drawable.d_02
            "02n" -> R.drawable.n_02
            "03d" -> R.drawable.d_03
            "03n" -> R.drawable.n_03
            "04d" -> R.drawable.d_04
            "04n" -> R.drawable.n_04
            "09d" -> R.drawable.d_09
            "09n" -> R.drawable.n_09
            "10d" -> R.drawable.d_10
            "10n" -> R.drawable.n_10
            "11d" -> R.drawable.d_11
            "11n" -> R.drawable.n_11
            "13d" -> R.drawable.d_13
            "13n" -> R.drawable.n_13
            "50d" -> R.drawable.d_50
            "50n" -> R.drawable.n_50
            else -> R.drawable.cancel
        }
    }

}