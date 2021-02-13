package ru.kolyukaev.yoweather.views

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import ru.kolyukaev.yoweather.data.models.RwWeatherAfter

@StateStrategyType(value = SingleStateStrategy::class)
interface MainWeatherView: MvpView {
    fun startLoading()
    fun endLoading()
    fun showComponents()
    fun setIndentTopAndBottom()
    fun showError(text: String)
    fun replaceBackground(photoString: String)
    fun getWeatherResponse(weather: String, temperature: String, feelsLike: String, pressure: String, humidity: String, cloudiness: String, wind: String, icon: Int, visibility: String, precipitation: String)
    fun getWeatherHoursResponse(list: ArrayList<RwWeatherAfter>)

}