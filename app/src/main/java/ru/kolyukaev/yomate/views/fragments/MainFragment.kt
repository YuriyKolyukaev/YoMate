package ru.kolyukaev.yomate.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.fragment_main.*
import ru.kolyukaev.yomate.R
import ru.kolyukaev.yomate.utils.log
import ru.kolyukaev.yomate.presenters.MainWeatherPresenter
import ru.kolyukaev.yomate.views.MainWeatherView
import ru.kolyukaev.yomate.views.activities.MainActivity
import ru.kolyukaev.yomate.utils.visible

class MainFragment : BaseFragment(), MainWeatherView {

    @InjectPresenter
    lateinit var mainWeatherPresenter: MainWeatherPresenter

    override val toolbarName: String
        get() = getString(R.string.app_name)

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


        swipe_refresh_layout.setOnRefreshListener {
                getBundle()
                swipe_refresh_layout.isRefreshing = false
        }
        swipe_refresh_layout.setColorSchemeColors(
            resources.getColor(R.color.colorProgressBar)

        )

        btn_details.setOnClickListener {
            (activity as MainActivity).commitFragmentTransaction(DetailsFragment(),true)
        }

        getBundle()
    }

    fun getBundle() {
        val id = arguments?.getInt("id")
        val name = arguments?.getString("name")
        val country = arguments?.getString("country")
        log("TEST_ARGUMENTS ${arguments?.getInt("id")}")

        id?.apply {sendBundle(country, id, name) } ?: Toast.makeText(context, "Enter your city",
            Toast.LENGTH_SHORT).show()
    }

    private fun sendBundle(country: String?, id: Int?, name: String?) {
        log("getBundle $name")
        if (country == null || name == null || id == 0) {
            showError("Error request from the list of cities")
        } else {
            mainWeatherPresenter.loadingWeatherCity(true, id)
            (activity as MainActivity).visibleCityandCountry(country, name)
        }
    }

    override fun startLoading() {
        pb_loading.visibility = View.VISIBLE
    }

    override fun endLoading() {
        pb_loading.visibility = View.GONE
    }

    override fun showComponents() {
        image_clear_sky.visible()
        image_wind.visible()
        image_cloudiness.visible()
        image_pressure.visible()
        btn_details.visible()
        image_humidity.visible()
        btn_details.visible()
        fl_transparent.visible()
        Toast.makeText(context, "Passed: Updated", Toast.LENGTH_SHORT).show()
    }

    override fun showError(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    override fun getWeatherResponse(temperature: String, pressure: String, humidity: String, cloudiness: String, wind: String, icon: Int) {
        tv_temperature.text = temperature
        tv_pressure.text = pressure
        tv_humidity.text = humidity
        tv_cloudiness.text = cloudiness
        tv_wind.text = wind
        image_clear_sky.setImageResource(icon)
    }

}