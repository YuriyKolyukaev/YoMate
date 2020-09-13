package ru.kolyukaev.yomate.views

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import ru.kolyukaev.yomate.data.models.City

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface CitiesView : MvpView {
    fun startLoading(number: Int)
    fun setupCitiesList(cities: ArrayList<City>)
    fun endLoading()
    fun showError(text: String)
}