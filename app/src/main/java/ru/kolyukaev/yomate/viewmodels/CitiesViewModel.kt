package ru.kolyukaev.yomate.viewmodels

import android.os.Environment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.kolyukaev.yomate.data.models.City
import ru.kolyukaev.yomate.data.models.MainWeatherModel
import ru.kolyukaev.yomate.data.network.RestClient
import ru.kolyukaev.yomate.data.network.api.ApiMethods
import ru.kolyukaev.yomate.data.network.api.WeatherService
import ru.kolyukaev.yomate.utils.fromJson
import ru.kolyukaev.yomate.utils.log
import java.io.*

class CitiesViewModel : ViewModel() {

    val mainWeatherLiveData = MutableLiveData<List<MainWeatherModel>>()

    val citiesLiveData = MutableLiveData<List<City>>()

//    var namesOfCities: ArrayList<String> = ArrayList()

    fun getCities(citiesInputStream: InputStream) {

        // корутины
        viewModelScope.launch(Dispatchers.Default) {
            log("fun viewModelScope.launch")
//            val citiesList = citiesMapToList(citiesMap)

            try {
                val objectInputStream = ObjectInputStream(citiesInputStream)
                val citiesList = objectInputStream.readObject() as List<City>
                citiesLiveData.postValue(citiesList)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }


    /**
     * Функция, которая конвертирует список городов из джейсона, сохраненного в raw ресурсах,
     * в объект List<City>, и сохраняет этот объект в памяти устройства. Это сделано для того,
     * чтобы использовать этот соъхраненный объект, который занимает меньше метса и ячитается быстрее.
     *
     * @param citiesInputStream - инпут стрим с текстовым джейсоном городов
     *
     * @param citiesFileDirectory - расположение файла с городами типа List<City>
     * */
    fun convert(citiesInputStream: InputStream, citiesFileDirectory: String) {

        try {
            val citiesJsonString = citiesInputStream.bufferedReader().use { it.readText() }
            val cities = citiesJsonString.fromJson<List<City>>()

            val fos = FileOutputStream(
                File(
                    Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOWNLOADS
                    ), "cities_list_full.txt"
                )
            )
            val os = ObjectOutputStream(fos)
            os.writeObject(cities)
            os.close()

            log("File has been written")
        } catch (e: Exception) {
            e.printStackTrace()
            log("File didn't write")
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