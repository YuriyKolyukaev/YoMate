package ru.kolyukaev.yoweather.presenters

import android.annotation.SuppressLint
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import ru.kolyukaev.yoweather.R
import ru.kolyukaev.yoweather.data.models.MainWeatherModel
import ru.kolyukaev.yoweather.data.models.RwWeatherAfter
import ru.kolyukaev.yoweather.data.models.RwWeatherBefore
import ru.kolyukaev.yoweather.data.providers.MainWeatherProvider
import ru.kolyukaev.yoweather.utils.log
import ru.kolyukaev.yoweather.views.MainWeatherView
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@InjectViewState
class MainWeatherPresenter : MvpPresenter<MainWeatherView>() {

    fun loadDataOfCity(cityId: Int) {
        log("loadingDataOfCity")
        viewState.startLoading()
        MainWeatherProvider(presenter = this).loadWeather(cityId)
    }

    fun onGeneralWeatherLoaded(weatherList: ArrayList<MainWeatherModel>) {
        viewState.endLoading()
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

    @SuppressLint("SimpleDateFormat")
    fun onListWeatherLoaded(weatherList: ArrayList<RwWeatherBefore>) {
        val rvWeatherList: ArrayList<RwWeatherAfter> = ArrayList()
        weatherList.forEach() {
            log("it.dtTxtDate  ${it.dtTxtDate} ")
            val strDate = it.dtTxtDate
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val date: Date = dateFormat.parse(strDate)
            val calendar = Calendar.getInstance()
            calendar.time = date
            log("it.dtTxtDate  ${it.dtTxtDate} ||| ${it.dtTxtTime} |||  ${calendar.time}")
            System.out.println(date)

            val rvWeather = RwWeatherAfter(
                dtTxtDate = getSubstringDate(it.dtTxtDate),
                dtTxtTime = getSubstringTime(it.dtTxtTime),
                temperature = "t = ${it.temperature.toInt()} С°",
                icon = getIconWeather(it.icon),
                humidity = "φ = ${it.humidity} %"
            )
            rvWeatherList.add(rvWeather)
        }

        viewState.getWeatherHoursResponse(rvWeatherList)
        viewState.showComponents()
    }

    fun getSubstringDate(s: String): String {

        val sub = s
        val substring = sub.substring(0, 10)

        return substring
    }

    fun getSubstringTime(s: String): String {

        val sub = s
        val substring = sub.substring(11, 16)

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