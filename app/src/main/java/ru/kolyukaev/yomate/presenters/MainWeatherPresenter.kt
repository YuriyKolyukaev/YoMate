package ru.kolyukaev.yomate.presenters

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import ru.kolyukaev.yomate.models.MainWeatherModel
import ru.kolyukaev.yomate.models.providers.MainWeatherProvider
import ru.kolyukaev.yomate.views.MainWeatherView
import ru.kolyukaev.yomate.views.activities.MainActivity


@InjectViewState
class MainWeatherPresenter : MvpPresenter<MainWeatherView>() {

    fun loadingWeather(isSuccess: Boolean) {
        viewState.startLoading()
        android.os.Handler().postDelayed({
            viewState.endLoading()
            if (isSuccess) {
                MainWeatherProvider(presenter = this).loadWeather()
            } else {
                viewState.showError("Weather state is incorrect")
            }
        }, 500)
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