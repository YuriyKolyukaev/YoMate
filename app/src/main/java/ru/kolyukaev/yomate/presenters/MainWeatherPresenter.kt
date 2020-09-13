package ru.kolyukaev.yomate.presenters

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import ru.kolyukaev.yomate.data.models.MainWeatherModel
import ru.kolyukaev.yomate.data.providers.MainWeatherProvider
import ru.kolyukaev.yomate.views.MainWeatherView


@InjectViewState
class MainWeatherPresenter : MvpPresenter<MainWeatherView>() {

    fun loadingWeatherCity(isSuccess: Boolean, name: String) {
        if (name == "") {
            var name: String = "Moscow"
            loadingWeather(isSuccess, name)
        } else loadingWeather(isSuccess, name)
    }

    fun loadingWeather(isSuccess: Boolean, name: String = "") {
        viewState.startLoading()
            viewState.endLoading()
            if (isSuccess) {
                MainWeatherProvider(presenter = this).loadWeather(name)
            } else {
                viewState.showError("Weather state is incorrect")
            }
    }

    fun weatherLoaded(weatherList: ArrayList<MainWeatherModel>) {
        viewState.endLoading()
        if (weatherList.size == 0) {
        viewState.showError("Weather state is empty")
        } else {
            val temperature = "temperature ${weatherList[0].temperature}"
            val cloudiness = "cloudiness ${weatherList[0].cloudiness}"
        viewState.weatherRequest(temperature, cloudiness)
        }
    }

    fun onError(t: Throwable) {
        viewState.showError(t.message.toString())
    }

    fun loadingWeatherDetails() {
    }

}