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
            val citiesMap: Map<String, List<String>> = parseJsonToCitiesObject(citiesString)
            log("fun viewModelScope.launch")
            val citiesList = citiesMapToList(citiesMap)

            citiesLiveData.postValue(citiesList)
        }
    }

    private fun citiesMapToList(citiesMap: Map<String, List<String>>): List<City> {
        val cities = mutableListOf<City>()
        citiesMap.forEach { (country, citiesList) ->
            citiesList.forEach { city ->
                val cityModel = City(
                    city = city,
                    country = country
                )
                cities.add(cityModel)
            }
        }
        return cities
    }

    fun parseJsonToCitiesObject(parsedJson: String?): Map<String, List<String>> {
        log("fun parseJsonToCitiesObject")
        val data = Gson().fromJson<Map<String, List<String>>>(parsedJson, object : TypeToken<HashMap<String, List<String>>>() {}.type)
        return data
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