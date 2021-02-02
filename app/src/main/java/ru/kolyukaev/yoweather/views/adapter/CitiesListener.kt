package ru.kolyukaev.yoweather.views.adapter

interface CitiesListener {
    fun onItemClick( cityId: Int, cityName: String, country: String, lat:Double, lon:Double)
}