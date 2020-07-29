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
import ru.kolyukaev.yomate.R
import ru.kolyukaev.yomate.presenters.MainWeatherPresenter
import ru.kolyukaev.yomate.views.MainWeatherView

class MainActivity : MvpAppCompatActivity(), MainWeatherView {

    private lateinit var mTvTemperature: TextView
    private lateinit var mTvCloudiness: TextView
    private lateinit var mBtnUpdate: Button
    private lateinit var mPbLoading: ProgressBar

    @InjectPresenter
    lateinit var mainWeatherPresenter: MainWeatherPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mTvTemperature = tv_temperature
        mTvCloudiness = tv_cloudiness
        mBtnUpdate = btn_update
        mPbLoading = pb_loading

        mBtnUpdate.setOnClickListener {
            mainWeatherPresenter.loadingWeather(true)
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

    override fun weatherRequest(temperature: String, cloudiness: String) {
        mTvTemperature.text = temperature
        mTvCloudiness.text = cloudiness

    }

    override fun openDetailsWeather() {
        startActivity(Intent(applicationContext, DetailsActivity::class.java))
    }


}