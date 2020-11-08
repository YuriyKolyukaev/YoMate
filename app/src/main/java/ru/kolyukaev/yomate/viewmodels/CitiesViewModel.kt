package ru.kolyukaev.yomate.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.kolyukaev.yomate.data.models.City
import ru.kolyukaev.yomate.data.models.MainWeatherModel
import ru.kolyukaev.yomate.log
import java.io.*

class CitiesViewModel : ViewModel() {

    val mainWeatherLiveData = MutableLiveData<List<MainWeatherModel>>()

    val citiesLiveData = MutableLiveData<List<City>>()

//    var namesOfCities: ArrayList<String> = ArrayList()

    fun getCities(citiesInputStream: InputStream) {

        // корутины
        viewModelScope.launch(Dispatchers.Default) {
            log("fun viewModelScope.launch")
            val citiesList = citiesMapToList(citiesMap)

            try {
                val objectInputStream = ObjectInputStream(citiesInputStream)
                val citiesList = objectInputStream.readObject() as List<City>
                citiesLiveData.postValue(citiesList)
            } catch (e: Exception) {

            }

        }
    }

//    fun getWeathers() {
//        val weatherService: WeatherService = RestClient.getRetrofit()
//
//        viewModelScope.launch(Dispatchers.Default) {
//            try {
//                val weatherResponse = weatherService.getWeatherDataSuspend(
//                    ApiMethods.ID,
//                    ApiMethods.KEY,
//                    ApiMethods.UNITS)
//
//                val weatherList: ArrayList<MainWeatherModel> = ArrayList()
//
//                val weather = MainWeatherModel(
//                    temperature = weatherResponse.main!!.temp,
//                    cloudiness = weatherResponse.clouds!!.all,
//                    wind = weatherResponse.wind!!.speed
//
//                )
//                weatherList.add(weather)
//
//                mainWeatherLiveData.postValue(weatherList)
//
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//    }

}