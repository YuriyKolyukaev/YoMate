package ru.kolyukaev.yoweather.data.network.api

class ApiMethods {

    companion object
    {
        /*Api constants for OpenWeatherMap*/
        const val KEY: String = "70a8211b10721a6d7056612c3ee346e9"
        const val UNITS: String = "metric"

        /*Api constants for Google*/
        const val KEYG: String = "AIzaSyAGlPA88bG2cXDAQcityAY4jMh4IcyL9as"
        const val RANKBY: String = "prominence"
        const val MAXWIDTH: Int = 1200
        const val NAME: String = "point of interest"
//        const val name: String = "establishment"
        const val RADIUS: Int = 4500

        /*Constants*/
        const val ID: String = "id"
        const val CITY: String = "city"
        const val COUNTRY: String = "country"
        const val PHOTO_HEIGHT = 600
        const val PHOTO_WIDTH = 600
        const val PROMPT = "prompt"
    }
}