package ru.kolyukaev.yoweather.views.fragments

import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.ScrollView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.*
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.LinearLayoutManager
import com.arellomobile.mvp.presenter.InjectPresenter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_city.*
import kotlinx.android.synthetic.main.fragment_main.*
import ru.kolyukaev.yoweather.R
import ru.kolyukaev.yoweather.data.models.RwWeatherAfter
import ru.kolyukaev.yoweather.presenters.MainWeatherPresenter
import ru.kolyukaev.yoweather.utils.*
import ru.kolyukaev.yoweather.views.MainWeatherView
import ru.kolyukaev.yoweather.views.activities.MainActivity
import ru.kolyukaev.yoweather.views.adapter.WeatherAdapter


class MainFragment : BaseFragment(), MainWeatherView {

    @InjectPresenter
    lateinit var mainWeatherPresenter: MainWeatherPresenter

    private lateinit var weatherAdapter: WeatherAdapter

    private val animationInterval = 3000L
    private lateinit var animationHandler: Handler

    private var mainImage: Bitmap? = null
    private lateinit var mainActivity: MainActivity

    private var cityId: Int? = null
    private var cityName: String? = null
    private var country: String? = null
    private var lat: Double? = null
    private var lon: Double? = null

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

        mainActivity = activity as MainActivity

        weatherAdapter = WeatherAdapter()
        rv_weather_hours.adapter = weatherAdapter

        val mLayoutManager = LinearLayoutManager(context)
        mLayoutManager.orientation = LinearLayoutManager.HORIZONTAL

        rv_weather_hours.layoutManager = mLayoutManager

        swipe_refresh_layout.setOnRefreshListener {
            mainWeatherPresenter.loadDataOfCity(cityId!!, lat!!, lon!!)
            swipe_refresh_layout.isRefreshing = false
        }


        if (checkBundleData() || checkPreferenceData()) {
            mainWeatherPresenter.loadDataOfCity(cityId!!, lat!!, lon!!)
            showCityAndCountry(cityName, country)
        }

        toolbar_main_fragment.inflateMenu(R.menu.city_info_menu)

        toolbar_main_fragment.setOnMenuItemClickListener { item ->
            if (item?.itemId == R.id.action_search) {
                (mainActivity).commitFragmentTransaction(CityFragment())
            }
            return@setOnMenuItemClickListener true
        }

        tv_change_city.setOnClickListener {
            (mainActivity).commitFragmentTransaction(CityFragment())
        }

        animationHandler = Handler()
        doDelayed(3000) {
//            startRepeatingTask()
        }

        poshitatDvaPlusDva(object : DvaPlusDvaResult {
            override fun onResult(value: Int) {
                log("NASH_RESULTAT $value")
            }
        })

        poshitatDvaPlusDva(dvaPlusDvaObject)

        poshitatDvaPlusDvaKotlin { value, value2 ->
            log("NASH_RESULTAT_KOTLIN ${value}")
        }

//        doDelayed(2500) {
//            hintWeatherAnimation()
//            doDelayed(350) {
//                hintWeatherAnimation()
//            }
//        }
    }

    var dvaPlusDvaObject = object : DvaPlusDvaResult {
        override fun onResult(value: Int) {
            log("NASH_RESULTAT 2 $value")
        }
    }
//
//    var animationRunnable: Runnable = object : Runnable {
//        override fun run() {
//            try {
//                hintWeatherAnimation()
//                doDelayed(330) {
//                    hintWeatherAnimation()
//                }
//            } finally {
//                animationHandler.postDelayed(this, animationInterval)
//                stopRepeatingTask()
//            }
//        }
//    }
//
//    private fun startRepeatingTask() {
//        animationRunnable.run()
//    }
//
//    private fun stopRepeatingTask() {
//        animationHandler.removeCallbacks(animationRunnable)
//    }

    private fun checkBundleData(): Boolean {
        cityId = arguments?.getInt("id")
        cityName = arguments?.getString("name")
        country = arguments?.getString("country")
        lat = arguments?.getDouble("lat")
        lon = arguments?.getDouble("lon")
        return cityId != null
    }

    private fun checkPreferenceData(): Boolean {
        val mainActivity = activity as MainActivity
        cityId = mainActivity.preferences?.getInt("id", 0)!!
        cityName = mainActivity.preferences?.getString("city", "")
        country = mainActivity.preferences?.getString("country", "")
        lat = mainActivity.preferences?.getFloat("lat", 0f)!!.toDouble()
        lon = mainActivity.preferences?.getFloat("lon", 0f)!!.toDouble()
        return cityId != 0
    }

    override fun setIndentTopAndBottom() {
        if (fl_main.height != 0 && cl_big_weather.height != 0) {
            setSwipeRefreshMargin()
            log("true fl_main.height = ${fl_main.height}, cl_big_weather.height = ${cl_big_weather.height} ")
        } else {
            log("false fl_main.height = ${fl_main.height}, cl_big_weather.height = ${cl_big_weather.height} ")
            fl_main.doOnLayout {
                cl_big_weather.doOnLayout {
                    setSwipeRefreshMargin()
                }
            }
        }
    }

    private fun setSwipeRefreshMargin() {
        log("setSwipeRefreeshMargin")
        val constViewsSumHeight = pxFromDp(view!!.context, 750)
        val rvLayoutParams = rv_weather_hours.layoutParams as? ConstraintLayout.LayoutParams

        log("setSwipeRefreeshMargin fl_main = ${fl_main.height}")
        log("setSwipeRefreeshMargin cl_big_weather_layout_height = ${cl_big_weather.height}")

        val swipeRefreshLayoutHeight = fl_main.height
        val clBigWeather = cl_big_weather.height

        val indentTop = swipeRefreshLayoutHeight - clBigWeather - pxFromDp(view!!.context, 8)
        cl_big_weather.setMargins(top = indentTop)

        if (swipeRefreshLayoutHeight > (constViewsSumHeight)) {
            val indentBottom = (swipeRefreshLayoutHeight - constViewsSumHeight) / 2

            rvLayoutParams?.bottomMargin = indentBottom
        }

        Handler().post {
            scroll_view?.isSmoothScrollingEnabled = false
            scroll_view?.fullScroll(ScrollView.FOCUS_UP)
        }
    }

    override fun startLoading() {
        pb_loading_weather.visibility = View.VISIBLE
    }

    override fun endLoading() {
        pb_loading_weather.visibility = View.INVISIBLE
    }

    override fun showHintWeatherAnimation() {
        log("showHintWeatherAnimation")
        doDelayed(700) {
            hintWeatherAnimation()
            doDelayed(350) {
                hintWeatherAnimation()
            }
        }
    }

    private fun hintWeatherAnimation() {
        cl_big_weather?.animate()
            ?.setDuration(100)
            ?.translationY(pxFromDp(requireContext(), -18).toFloat())
            ?.setInterpolator(DecelerateInterpolator())
            ?.withEndAction {
                cl_big_weather?.animate()
                    ?.setStartDelay(50)
                    ?.setDuration(100)
                    ?.translationY(0f)
            }
    }

    private fun showCityAndCountry(city: String?, country: String?) {
        tv_change_city.visible()
        tv_change_country.visible()
        tv_change_city.text = city
        tv_change_country.text = country
    }

    override fun showComponents() {
        log("showComponents")
        log("TESTTEST")

        cl_main.visible()
        showHintWeatherAnimation()
        setIndentTopAndBottom()
    }

    override fun showError(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    override fun replaceBackground(photoString: String) {
        Glide.with(this)
            .asBitmap()
            .load(photoString)
            .placeholder(iv_background.drawable)
            // плавная смена картинки
            .listener(object : RequestListener<Bitmap> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Bitmap>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Bitmap?,
                    model: Any?,
                    target: Target<Bitmap>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {

                    mainImage = resource
                    if (mainImage != null) {
                        getColorFromBitmap(mainImage!!)
                    }
                    return false
                }
            })
            .transition(BitmapTransitionOptions.withCrossFade())
            .into(iv_background)
    }

    private fun getColorFromBitmap(bitmap: Bitmap) {
        Palette.from(bitmap).generate { palette ->
            val vibrantSwatch = palette?.darkMutedSwatch
            val backgroundColor =
                vibrantSwatch?.rgb ?: ContextCompat.getColor(requireContext(), R.color.color_crystal)
            setAccentColorViews(backgroundColor)
        }
    }

    private fun colorStateListOf(color: Int): ColorStateList {
        return ColorStateList.valueOf(color)
    }

    private fun setAccentColorViews(backgroundColor: Int?) {
        toolbar_main_fragment.backgroundTintList = colorStateListOf(backgroundColor!!)
        cl_big_weather.backgroundTintList = colorStateListOf(backgroundColor)
        ll_detailed_weather.backgroundTintList = colorStateListOf(backgroundColor)
        rv_weather_hours.backgroundTintList = colorStateListOf(backgroundColor)
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

    override fun onDestroyView() {
        rv_weather_hours.adapter = null

//        stopRepeatingTask()
        super.onDestroyView()
    }

    override fun getWeatherHoursResponse(list: ArrayList<RwWeatherAfter>) {
        weatherAdapter.updateAdapter(list)
    }

}