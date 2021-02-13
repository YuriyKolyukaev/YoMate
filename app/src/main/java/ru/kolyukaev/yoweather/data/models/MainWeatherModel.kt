package ru.kolyukaev.yoweather.data.models

data class MainWeatherModel(
    var weather: String,
    var temperature: Double,
    var feelsLike: Double,
    var pressure: Double,
    var humidity: Int,
    var cloudiness: Int,
    var wind: Double,
    var icon: String,
    var visibility: Int,
    var precipitation: Double
)