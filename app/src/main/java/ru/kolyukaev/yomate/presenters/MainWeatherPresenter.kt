package ru.kolyukaev.yomate.presenters

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import ru.kolyukaev.yomate.R
import ru.kolyukaev.yomate.data.models.MainWeatherModel
import ru.kolyukaev.yomate.data.network.api.ApiMethods
import ru.kolyukaev.yomate.data.providers.MainWeatherProvider
import ru.kolyukaev.yomate.log
import ru.kolyukaev.yomate.views.MainWeatherView

@InjectViewState
class MainWeatherPresenter : MvpPresenter<MainWeatherView>() {

    fun loadingWeatherCity(isSuccess: Boolean, id: Int?) {
        log("loadingWeatherCity")
        val id1: Int = id ?: ApiMethods.ID
        viewState.startLoading()
            viewState.endLoading()
            if (isSuccess) {
                MainWeatherProvider(presenter = this).loadWeather(id1)
            } else {
                viewState.showError("Weather state is incorrect")
            }
    }

    fun weatherLoaded(weatherList: ArrayList<MainWeatherModel>) {
        log("weatherLoaded")
        viewState.endLoading()
        if (weatherList.size == 0) {
        viewState.showError("Weather state is empty")
        } else {
            val temperature = "${weatherList[0].temperature} С°"
            val pressure = "Давление ${weatherList[0].pressure} mm Hg"
            val humidity = "Влажность ${weatherList[0].humidity} %"
            val cloudiness = "Облачность ${weatherList[0].cloudiness}%"
            val wind = "Скорость ветра ${weatherList[0].wind} m/s"
            val icon = getIconWeather(weatherList[0].icon)
            viewState.getWeatherResponse(temperature, pressure, humidity, cloudiness, wind, icon)
            viewState.showComponents()
        }
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

    fun loadingWeatherDetails() {
    }
}