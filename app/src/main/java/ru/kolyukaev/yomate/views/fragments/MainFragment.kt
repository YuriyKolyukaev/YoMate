package ru.kolyukaev.yomate.views.fragments

import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.arellomobile.mvp.presenter.InjectPresenter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_main.*
import ru.kolyukaev.yomate.R
import ru.kolyukaev.yomate.data.models.RwWeatherAfter
import ru.kolyukaev.yomate.utils.log
import ru.kolyukaev.yomate.presenters.MainWeatherPresenter
import ru.kolyukaev.yomate.views.MainWeatherView
import ru.kolyukaev.yomate.views.activities.MainActivity
import ru.kolyukaev.yomate.utils.visible
import ru.kolyukaev.yomate.views.adapter.WeatherAdapter

class MainFragment : BaseFragment(), MainWeatherView {

    @InjectPresenter
    lateinit var mainWeatherPresenter: MainWeatherPresenter

    val weatherAdapter = WeatherAdapter(ArrayList())

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

        rv_hours_weather.adapter = weatherAdapter

        val mLayoutManager = LinearLayoutManager(context)
        mLayoutManager.orientation = LinearLayoutManager.HORIZONTAL

        rv_hours_weather.layoutManager = mLayoutManager


        swipe_refresh_layout.setOnRefreshListener {
            getBundle()
            swipe_refresh_layout.isRefreshing = false
        }

        swipe_refresh_layout.setColorSchemeColors(
            resources.getColor(R.color.colorProgressBar)
        )

        getBundle()

        toolbar_main_fragment.inflateMenu(R.menu.city_info_menu)

        toolbar_main_fragment.setOnMenuItemClickListener { item ->
            Log.i("QWE", "ITEMTEST  ${item.menuInfo} ${item}")
            if (item?.itemId == R.id.action_search) {
                (activity as MainActivity).commitFragmentTransaction(CityFragment())
                log("commitFragmentTransaction fragment2")
            }
            return@setOnMenuItemClickListener true

        }

        setIndentTopAndBottom()
    }

    fun getBundle() {
        val id = arguments?.getInt("id")
        val name = arguments?.getString("name")
        val country = arguments?.getString("country")
        val lat = arguments?.getDouble("lat")
        val lon = arguments?.getDouble("lon")

        log("TEST_ARGUMENTS ${arguments?.getInt("id")}")

        id?.apply { sendBundle(country, id, name, lat, lon) } ?: Toast.makeText(
            context, "Enter your city",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun sendBundle(country: String?, id: Int?, city: String?, lat: Double?, lon: Double?) {
        log("getBundle $city")
        if (country == null || city == null || id == 0) {
            showError("Error request from the list of cities")
        } else {
            mainWeatherPresenter.loadingWeatherOfCity(true, id)
            changeCityAndCountry(country, city)
            mainWeatherPresenter.loadingDataOfCity(lat, lon)
        }
    }

    fun changeCityAndCountry(country: String?, city: String?) {
        tv_change_city.visible()
        tv_change_country.visible()
        tv_change_city.text = city
        tv_change_country.text = country
    }

    fun setIndentTopAndBottom () {
        val displayMetrics = DisplayMetrics()

        (activity as MainActivity).windowManager.defaultDisplay.getMetrics(displayMetrics)

        val indentTop = cl_transparent.layoutParams as? ConstraintLayout.LayoutParams

        val indentBottom = rv_hours_weather.layoutParams as? ConstraintLayout.LayoutParams

        val height = displayMetrics.heightPixels

        indentTop?.topMargin = height - 500

        if (height > 1900) {
            val h = (height - 1900)/2

            log("heightH = $h")

            indentBottom?.bottomMargin = h
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
        ll_transparent.visible()
        rv_hours_weather.visible()
        Toast.makeText(context, "Passed: Updated", Toast.LENGTH_SHORT).show()
    }

    override fun showError(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    override fun replaceBackground(photoString: String) {
        Glide.with(this)
            .load(photoString)
            .placeholder(iv_background.drawable)
            // плавная смена картинки
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(iv_background)
    }

    override fun getWeatherResponse(
        weather: String, temperature: String, feelsLike: String, pressure: String, humidity: String,
        cloudiness: String, wind: String, icon: Int, visibility: String, precipitation: String
    ) {
        tv_weather_condition.text = weather
        tv_temperature.text = temperature
        tv_feels_like.text = feelsLike
        image_clear_sky.setImageResource(icon)
        tv_pressure2.text = pressure
        tv_humidity2.text = humidity
        tv_cloudiness2.text = cloudiness
        tv_wind2.text = wind
        tv_visibility2.text = visibility
        tv_precipitation2.text = precipitation
    }

    override fun getWeatherHoursResponse(list: ArrayList<RwWeatherAfter>) {
        weatherAdapter.updateAdapter(list)
    }
}