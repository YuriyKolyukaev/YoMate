package ru.kolyukaev.yomate.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CitiesViewModel : ViewModel() {

    val uraLiveData = MutableLiveData<String>()
    
    fun getCities(number: String) {
        ///
        ///
//        Здесь получаем города
//        и передаем в LiveData
        
        
        uraLiveData.setValue(number)
    }
    
}