package ru.kolyukaev.yomate.views.adapter

interface CitiesListener {
    fun onItemClick(country: String, id: Int, name: String, lat:Double, lon:Double)
}