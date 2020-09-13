package ru.kolyukaev.yomate.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.kolyukaev.yomate.data.models.City
import ru.kolyukaev.yomate.data.models.MainWeatherModel
import ru.kolyukaev.yomate.data.network.RestClient
import ru.kolyukaev.yomate.data.network.api.ApiMethods
import ru.kolyukaev.yomate.data.network.api.WeatherService
import ru.kolyukaev.yomate.log
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream

class CitiesViewModel : ViewModel() {

    val mainWeatherLiveData = MutableLiveData<List<MainWeatherModel>>()

    val citiesLiveData = MutableLiveData<List<City>>()

    var namesOfCities: ArrayList<String> = ArrayList()

    fun getCities(citiesInputStream: InputStream) {



        // корутины
        viewModelScope.launch(Dispatchers.Default) {
            val citiesString = getCitiesFromFile(citiesInputStream)
            val cities: List<City> = parseJsonToCitiesObject(citiesString)
            log("fun viewModelScope.launch")

            val testCities = mutableListOf<City>()

            for (i in 0..30) {
                testCities.add(cities[i])
            }

            citiesLiveData.postValue(testCities)
        }
    }

    fun parseJsonToCitiesObject(parsedJson: String?): List<City> {
        log("fun parseJsonToCitiesObject")
        return Gson().fromJson(parsedJson, object : TypeToken<List<City>>() {}.type)
    }

    fun getCitiesFromFile(citiesInputStream: InputStream): String? {
        log("getCitiesFromFile")
        try {
            return citiesInputStream.bufferedReader().use(BufferedReader::readText)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }


    fun getWeathers() {
        val weatherService: WeatherService = RestClient.getRetrofit()

        viewModelScope.launch(Dispatchers.Default) {
            try {
                val weatherResponse = weatherService.getWeatherDataSuspend(
                    ApiMethods.CITY,
                    ApiMethods.KEY,
                    ApiMethods.UNITS
                )

                val weatherList: ArrayList<MainWeatherModel> = ArrayList()

                val weather = MainWeatherModel(
                    temperature = weatherResponse.main!!.temp,
                    cloudiness = weatherResponse.clouds!!.all
                )
                weatherList.add(weather)

                mainWeatherLiveData.postValue(weatherList)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}