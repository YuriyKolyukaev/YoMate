package ru.kolyukaev.yomate.views.fragments

import ru.kolyukaev.yomate.views.CitiesView

class Calculator(val citiesView: CitiesView) {

    fun getCities(int: Int) {
        citiesView.startLoading(int)
    }
}