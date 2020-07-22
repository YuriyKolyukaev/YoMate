package ru.kolyukaev.yomate.presenters

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import ru.kolyukaev.yomate.views.MainWeatherView
import java.util.logging.Handler

@InjectViewState
class MainWeatherPresenter : MvpPresenter<MainWeatherView>() {

    fun toUpdate(isSuccess: Boolean) {
        if (isSuccess) {
            viewState.weatherRequest()
        } else {
            viewState.showError("Weather state is incorrect")
        }
    }

}