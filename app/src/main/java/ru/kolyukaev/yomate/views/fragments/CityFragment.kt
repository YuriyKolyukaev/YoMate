package ru.kolyukaev.yomate.views.fragments

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.fragment_city.*
import ru.kolyukaev.yomate.R
import ru.kolyukaev.yomate.presenters.CitiesPresenter
import ru.kolyukaev.yomate.viewmodel.CitiesViewModel
import ru.kolyukaev.yomate.views.CitiesView
import java.io.InputStream

class CityFragment: BaseFragment(), CitiesView {

    @InjectPresenter
    lateinit var citiesPresenter: CitiesPresenter

    lateinit var citiesViewModel: CitiesViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_city, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val calculator = Calculator(this)
//        calculator.getCities(10)

        citiesViewModel = ViewModelProvider(this).get(CitiesViewModel::class.java)

        val inputStream: InputStream = resources.openRawResource(R.raw.cities)
        citiesPresenter.loadCitiesList(inputStream)

        citiesViewModel.uraLiveData.observe(viewLifecycleOwner, Observer {

            ed_search_city.setText((it + 1000))
        })

        Handler().postDelayed({
            citiesViewModel.getCities("XEP}")
        }, 3000)
    }

    override fun startLoading(number: Int) {
//        textView2.text = number.toString()
    }

    override fun endLoading() {
    }

    override fun showError(text: String) {
    }
}