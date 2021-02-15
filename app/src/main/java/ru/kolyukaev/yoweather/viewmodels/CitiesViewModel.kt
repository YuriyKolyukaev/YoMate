package ru.kolyukaev.yoweather.viewmodels

import android.os.Environment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.kolyukaev.yoweather.data.models.City
import ru.kolyukaev.yoweather.data.models.MainWeatherModel
import ru.kolyukaev.yoweather.utils.fromJson
import ru.kolyukaev.yoweather.utils.log
import java.io.*

class CitiesViewModel : ViewModel() {

    val mainWeatherLiveData = MutableLiveData<List<MainWeatherModel>>()

    val citiesLiveData = MutableLiveData<List<City>>()

    fun getCities(citiesInputStream: InputStream) {

        // корутины
        viewModelScope.launch(Dispatchers.Default) {
            log("fun viewModelScope.launch")

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
     * Функция конвертирует список городов из джейсона, сохраненного в raw ресурсах,
     * в объект List<City>, и сохраняет этот объект в памяти устройства. Это сделано для того,
     * чтобы использовать этот сохраненный объект, который занимает меньше места и читается быстрее.
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
                    ), "cities_list_fullll.txt"
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

}