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

        log("TEST_ARGUMENTS ${arguments?.getString("name")}")

        val name = arguments?.getString("name")
        if (name != null) getBundle(name)

        btn_update.setOnClickListener {
            mainWeatherPresenter.loadingWeatherCity(isSuccess = true, name = "")
        }

        btn_details.setOnClickListener {
            (activity as MainActivity).commitFragmentTransaction(CityFragment(), "City")
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).tv_toolbar.text = "YoMate"
    }

    fun getBundle(name: String) {
        mainWeatherPresenter.loadingWeatherCity(true, name)
        Toast.makeText(context, "Passed: ${name}", Toast.LENGTH_SHORT).show()
    }

    override fun startLoading() {
        btn_update.visibility = View.GONE
        pb_loading.visibility = View.VISIBLE
    }

    override fun endLoading() {
        btn_update.visibility = View.VISIBLE
        pb_loading.visibility = View.GONE
    }

    override fun showError(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    override fun weatherRequest(temperature: String, cloudiness: String) {
        tv_temperature.text = temperature
        tv_cloudiness.text = cloudiness
    }
}