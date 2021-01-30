package ru.kolyukaev.yomate.views.fragments

import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
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
import ru.kolyukaev.yomate.R
import ru.kolyukaev.yomate.data.models.RwWeatherAfter
import ru.kolyukaev.yomate.presenters.MainWeatherPresenter
import ru.kolyukaev.yomate.utils.*
import ru.kolyukaev.yomate.views.MainWeatherView
import ru.kolyukaev.yomate.views.activities.MainActivity
import ru.kolyukaev.yomate.views.adapter.WeatherAdapter


class MainFragment : BaseFragment(), MainWeatherView {

    @InjectPresenter
    lateinit var mainWeatherPresenter: MainWeatherPresenter

    private lateinit var weatherAdapter: WeatherAdapter

    private val animationInterval = 3000L
    private lateinit var animationHandler: Handler

    private var mainImage: Bitmap? = null


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

        val mainActivity = activity as MainActivity

        weatherAdapter = WeatherAdapter()
        rv_weather_hours.adapter = weatherAdapter

        val mLayoutManager = LinearLayoutManager(context)
        mLayoutManager.orientation = LinearLayoutManager.HORIZONTAL

        rv_weather_hours.layoutManager = mLayoutManager

        swipe_refresh_layout.setOnRefreshListener {
            getBundle()
            swipe_refresh_layout.isRefreshing = false
        }

        getBundle()

        toolbar_main_fragment.inflateMenu(R.menu.city_info_menu)

        toolbar_main_fragment.setOnMenuItemClickListener { item ->
            Log.i("QWE", "ITEMTEST  ${item.menuInfo} ${item}")
            if (item?.itemId == R.id.action_search) {
                (mainActivity).commitFragmentTransaction(CityFragment())
                log("commitFragmentTransaction fragment2")
            }
            return@setOnMenuItemClickListener true
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

        doDelayed(2500) {
            hintWeatherAnimation()
            doDelayed(350) {
                hintWeatherAnimation()
            }
        }

    }

        var dvaPlusDvaObject = object : DvaPlusDvaResult{
        override fun onResult(value: Int) {
            log("NASH_RESULTAT 2 $value")
        }
    }

    var animationRunnable: Runnable = object : Runnable {
        override fun run() {
            try {
                hintWeatherAnimation()
                doDelayed(330) {
                    hintWeatherAnimation()
                }
            } finally {
                animationHandler.postDelayed(this, animationInterval)
                stopRepeatingTask()
            }
        }
    }

    private fun startRepeatingTask() {
        animationRunnable.run()
    }

    private fun stopRepeatingTask() {
        animationHandler.removeCallbacks(animationRunnable)
    }

    private fun hintWeatherAnimation() {
            cl_big_weather?.animate()
                ?.setDuration(100)
                ?.translationY(pxFromDp(requireContext(), -16).toFloat())
                ?.setInterpolator(DecelerateInterpolator())
                ?.withEndAction {
                    cl_big_weather?.animate()
                        ?.setStartDelay(50)
                        ?.setDuration(100)
                        ?.translationY(0f)
                }
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

    override fun setIndentTopAndBottom() {

        val constViewsSumHeight = pxFromDp(view!!.context, 700)
        val rvLayoutParams = rv_weather_hours.layoutParams as? ConstraintLayout.LayoutParams
        log("setIndentTopAndBottom ${swipe_refresh_layout.isVisible}")

        swipe_refresh_layout.doOnLayout {
            cl_big_weather.doOnLayout {

                log("swipe_refresh_layout_height = ${swipe_refresh_layout.height}")
                log("cl_big_weather_layout_height = ${cl_big_weather.height}")

                val swipeRefreshLayoutHeight = swipe_refresh_layout.height
                val clBigWeather = cl_big_weather.height

                val indentTop =
                    swipeRefreshLayoutHeight - clBigWeather - pxFromDp(view!!.context, 8)
                cl_big_weather.setMargins(top = indentTop)

                if (swipeRefreshLayoutHeight > (constViewsSumHeight)) {
                    val indentBottom = (swipeRefreshLayoutHeight - constViewsSumHeight) / 2

                    rvLayoutParams?.bottomMargin = indentBottom
                }
            }
        }
    }

    override fun startLoading() {
        pb_loading.visibility = View.VISIBLE
    }

    override fun endLoading() {
        pb_loading.visibility = View.GONE
    }

    override fun showComponents() {
        sv_background.visible()
        Toast.makeText(context, "Updated", Toast.LENGTH_SHORT).show()
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

    fun colorStateListOf(color: Int): ColorStateList {
        return ColorStateList.valueOf(color)
    }

    private fun setAccentColorViews(backgroundColor: Int?) {
        toolbar_main_fragment.backgroundTintList = colorStateListOf(backgroundColor!!)
        cl_big_weather.backgroundTintList = colorStateListOf(backgroundColor)
        ll_transparent.backgroundTintList = colorStateListOf(backgroundColor)
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
        stopRepeatingTask()
        super.onDestroyView()
    }

    override fun getWeatherHoursResponse(list: ArrayList<RwWeatherAfter>) {
        weatherAdapter.updateAdapter(list)
    }

}