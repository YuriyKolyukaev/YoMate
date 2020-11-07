package ru.kolyukaev.yomate.data.network.response

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

data class MainResponse (
    @SerializedName("coord")
    @Expose
    var coord: Coord? = null,
    @SerializedName("weather")
    @Expose
    var weather: List<Weather>? = null,
    @SerializedName("base")
    @Expose
    var base: String? = null,
    @SerializedName("main")
    @Expose
    var main: Main? = null,
    @SerializedName("visibility")
    @Expose
    var visibility: Int = 0,
    @SerializedName("wind")
    @Expose
    var wind: Wind? = null,
    @SerializedName("clouds")
    @Expose
    var clouds: Clouds? = null,
    @SerializedName("dt")
    @Expose
    var dt: Int = 0,
    @SerializedName("sys")
    @Expose
    var sys: Sys? = null,
    @SerializedName("timezone")
    @Expose
    var timezone: Int = 0,
    @SerializedName("id")
    @Expose
    var id: Int= 0,
    @SerializedName("name")
    @Expose
    var name: String? = null,
    @SerializedName("cod")
    @Expose
    var cod: Int = 0
)

data class Clouds (
    @SerializedName("all")
    @Expose
    var all: Int = 0
)

data class Coord (
    @SerializedName("lon")
    @Expose
    var lon: Double = 0.0,
    @SerializedName("lat")
    @Expose
    var lat: Double = 0.0
)

data class Main (
    @SerializedName("temp")
    @Expose
    var temp: Double = 0.0,
    @SerializedName("feels_like")
    @Expose
    var feelsLike: Double = 0.0,
    @SerializedName("temp_min")
    @Expose
    var tempMin: Double = 0.0,
    @SerializedName("temp_max")
    @Expose
    var tempMax: Double = 0.0,
    @SerializedName("pressure")
    @Expose
    var pressure: Double = 0.0,
    @SerializedName("humidity")
    @Expose
    var humidity: Int = 0
)

data class Sys (
    @SerializedName("type")
    @Expose
    var type: Int = 0,
    @SerializedName("id")
    @Expose
    var id: Int = 0,
    @SerializedName("country")
    @Expose
    var country: String? = null,
    @SerializedName("sunrise")
    @Expose
    var sunrise: Int = 0,
    @SerializedName("sunset")
    @Expose
    var sunset: Int = 0
)

data class Weather (
    @SerializedName("id")
    @Expose
    var id: Int = 0,
    @SerializedName("main")
    @Expose
    var main: String? = null,
    @SerializedName("description")
    @Expose
    var description: String? = null,
    @SerializedName("icon")
    @Expose
    var icon: String? = null
)

class Wind (
    @SerializedName("speed")
    @Expose
    var speed: Double = 0.0,
    @SerializedName("deg")
    @Expose
    var deg: Int = 0
)



