package ru.kolyukaev.yomate.models.link.response

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class MainResponse {
    @SerializedName("coord")
    @Expose
    var coord: Coord? = null
    @SerializedName("weather")
    @Expose
    var weather: List<Weather>? = null
    @SerializedName("base")
    @Expose
    var base: String? = null
    @SerializedName("main")
    @Expose
    var main: Main? = null
    @SerializedName("visibility")
    @Expose
    var visibility = 0
    @SerializedName("wind")
    @Expose
    var wind: Wind? = null
    @SerializedName("clouds")
    @Expose
    var clouds: Clouds? = null
    @SerializedName("dt")
    @Expose
    var dt = 0
    @SerializedName("sys")
    @Expose
    var sys: Sys? = null
    @SerializedName("timezone")
    @Expose
    var timezone = 0
    @SerializedName("id")
    @Expose
    var id = 0
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("cod")
    @Expose
    var cod = 0
}

class Clouds {
    @SerializedName("all")
    @Expose
    var all = 0
}

class Coord {
    @SerializedName("lon")
    @Expose
    var lon = 0.0
    @SerializedName("lat")
    @Expose
    var lat = 0.0
}

class Main {
    @SerializedName("temp")
    @Expose
    var temp = 0.0
    @SerializedName("feels_like")
    @Expose
    var feelsLike = 0.0
    @SerializedName("temp_min")
    @Expose
    var tempMin = 0
    @SerializedName("temp_max")
    @Expose
    var tempMax = 0.0
    @SerializedName("pressure")
    @Expose
    var pressure = 0
    @SerializedName("humidity")
    @Expose
    var humidity = 0
}

class Sys {
    @SerializedName("type")
    @Expose
    var type = 0
    @SerializedName("id")
    @Expose
    var id = 0
    @SerializedName("country")
    @Expose
    var country: String? = null
    @SerializedName("sunrise")
    @Expose
    var sunrise = 0
    @SerializedName("sunset")
    @Expose
    var sunset = 0
}

class Weather {
    @SerializedName("id")
    @Expose
    var id = 0
    @SerializedName("main")
    @Expose
    var main: String? = null
    @SerializedName("description")
    @Expose
    var description: String? = null
    @SerializedName("icon")
    @Expose
    var icon: String? = null
}

class Wind {
    @SerializedName("speed")
    @Expose
    var speed = 0
    @SerializedName("deg")
    @Expose
    var deg = 0
}



