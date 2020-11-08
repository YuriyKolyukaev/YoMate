package ru.kolyukaev.yomate.data.models

data class MainWeatherModel(var temperature: Double, var pressure: Double, var humidity: Int, var cloudiness: Int, var wind: Double, var icon: String)