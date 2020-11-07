package ru.kolyukaev.yomate.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_main.*
import ru.kolyukaev.yomate.R
import ru.kolyukaev.yomate.log
import ru.kolyukaev.yomate.presenters.MainWeatherPresenter
import ru.kolyukaev.yomate.views.MainWeatherView
import ru.kolyukaev.yomate.views.activities.MainActivity
import ru.kolyukaev.yomate.visible

class MainFragment : BaseFragment(), MainWeatherView {

    @InjectPresenter
    lateinit var mainWeatherPresenter: MainWeatherPresenter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        log("onCreateView (MainFragment)")
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_update.setOnClickListener {
            mainWeatherPresenter.loadingWeatherCity( true, null)
        }

        btn_details.setOnClickListener {
            (activity as MainActivity).commitFragmentTransaction(CityFragment(), "City")
        }

        val id = arguments?.getInt("id")
        val name = arguments?.getString("name")
        val country = arguments?.getString("country")
        log("TEST_ARGUMENTS ${arguments?.getInt("id")}")

        id?.apply {getBundle(country, id, name) }
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).tv_toolbar.text = "YoMate"
        (activity as MainActivity).tv_change_city.visible()
    }

    private fun getBundle(country: String?, id: Int?, name: String?) {
        log("getBundle")
        mainWeatherPresenter.loadingWeatherCity(true, id)
        (activity as MainActivity).visibleCityandCountry(country,name)
        Toast.makeText(context, "Passed: Updated", Toast.LENGTH_SHORT).show()
    }


    override fun startLoading() {
        btn_update.visibility = View.GONE
        pb_loading.visibility = View.VISIBLE
    }

    override fun endLoading() {
        btn_update.visibility = View.VISIBLE
        pb_loading.visibility = View.GONE
    }

    override fun showComponents() {
        image_clear_sky.visible()
        image_wind.visible()
        image_cloudy.visible()
        btn_details.visible()
        btn_update.visible()
    }

    override fun showError(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }


    override fun weatherRequest(temperature: String, cloudiness: String, wind: String, icon: Int) {
        tv_temperature.text = temperature
        tv_cloudiness.text = cloudiness
        tv_wind.text = wind
        image_clear_sky.setImageResource(icon)

    }
}