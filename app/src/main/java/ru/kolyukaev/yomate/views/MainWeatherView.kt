package ru.kolyukaev.yomate.views

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import ru.kolyukaev.yomate.data.network.response.Wind

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface MainWeatherView: MvpView {
    fun startLoading()
    fun endLoading()
    fun showComponents()
    fun showError(text: String)
    fun replaceBackground(photoString: String)
    fun getWeatherResponse(temperature: String, pressure: String, humidity: String, cloudiness: String, wind: String, icon: Int)

}