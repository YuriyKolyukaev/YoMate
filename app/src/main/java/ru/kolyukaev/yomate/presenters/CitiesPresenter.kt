package ru.kolyukaev.yomate.presenters

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.*
import ru.kolyukaev.yomate.log
import ru.kolyukaev.yomate.models.City
import ru.kolyukaev.yomate.views.CitiesView
import java.io.*

@InjectViewState
class CitiesPresenter : MvpPresenter<CitiesView>() {

    fun loadCitiesList(citiesInputStream: InputStream) {
        val randomNumber = 8
//        viewState.startLoading(randomNumber)

        GlobalScope.launch(Dispatchers.IO) {
            val citiesString = getCitiesFromFile(citiesInputStream)
            val cities = parseJsonToCitiesObject(citiesString)

            log("TEST_CITIES ${cities.size}  ${cities[1000]}")
        }
    }

    private fun getCitiesFromFile(citiesInputStream: InputStream): String? {
        try {
            return citiesInputStream.bufferedReader().use(BufferedReader::readText)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }


    private fun parseJsonToCitiesObject(parsedJson: String?): List<City> {
        return Gson().fromJson(parsedJson, object : TypeToken<List<City>>() {}.type)
    }

    fun onError(t: Throwable) {
        viewState.showError(t.message.toString())
    }

}