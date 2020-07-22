package ru.kolyukaev.yomate.views.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.kolyukaev.yomate.R
import ru.kolyukaev.yomate.log
import ru.kolyukaev.yomate.models.link.RestClient
import ru.kolyukaev.yomate.models.link.api.WeatherService
import ru.kolyukaev.yomate.models.link.response.MainResponse
import ru.kolyukaev.yomate.presenters.MainWeatherPresenter
import ru.kolyukaev.yomate.views.MainWeatherView

class MainActivity : MvpAppCompatActivity(), MainWeatherView {

    private lateinit var mTvTemperature: TextView
    private lateinit var mTvCloudiness: TextView
    private lateinit var mBtnUpdate: Button
    private lateinit var mPbLoading: ProgressBar

    @InjectPresenter
    lateinit var mainWeatherPresenter: MainWeatherPresenter

    var q = "Moscow"
    var appid = "70a8211b10721a6d7056612c3ee346e9"
    var units = "metric"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mTvTemperature = tv_temperature
        mTvCloudiness = tv_cloudiness
        mBtnUpdate = btn_update
        mPbLoading = pb_loading

        mBtnUpdate.setOnClickListener {
            mainWeatherPresenter.toUpdate(true)
        }
    }


    override fun startLoading() {
        mBtnUpdate.visibility = View.GONE
        mPbLoading.visibility = View.VISIBLE
    }

    override fun endLoading() {
        mBtnUpdate.visibility = View.VISIBLE
        mPbLoading.visibility = View.GONE
    }

    override fun showError(text: String) {
        Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
    }

    override fun weatherRequest() {

        val weatherService: WeatherService = RestClient.getRetrofit()
        val call: Call<MainResponse> = weatherService.getWeatherData(q, appid, units)

        call.enqueue(object : Callback<MainResponse> {
            override fun onFailure(call: Call<MainResponse>, t: Throwable) {
                log("${t.message}")
                mTvTemperature.text = t.message
            }

            override fun onResponse(call: Call<MainResponse>, response: Response<MainResponse>) {
                if (response.code() == 200) {
                    val weatherResponse = response.body()!!

                    val weatherTemp =
                        "Temperature: " +
                                weatherResponse.main!!.temp

                    val weatherCloudiness = "Cloudiness " +
                            weatherResponse.clouds!!.all

                    mTvTemperature.text = weatherTemp
                    mTvCloudiness.text = weatherCloudiness
                }
            }
        })
    }

    override fun openDetailsWeather() {
        startActivity(Intent(applicationContext, DetailsActivity::class.java))
    }


}