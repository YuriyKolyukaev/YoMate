package ru.kolyukaev.yomate.views

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import ru.kolyukaev.yomate.models.MainWeatherModel

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface MainWeatherView: MvpView {
    fun startLoading()
    fun endLoading()
    fun showError(text: String)
    fun weatherRequest(temperature:String, cloudiness: String)
    fun openDetailsWeather()

}