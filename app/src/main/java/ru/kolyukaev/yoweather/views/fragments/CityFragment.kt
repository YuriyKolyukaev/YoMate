package ru.kolyukaev.yoweather.views.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_city.*
import ru.kolyukaev.yoweather.R
import ru.kolyukaev.yoweather.data.models.City
import ru.kolyukaev.yoweather.utils.gone
import ru.kolyukaev.yoweather.utils.visible
import ru.kolyukaev.yoweather.viewmodels.CitiesViewModel
import ru.kolyukaev.yoweather.views.activities.MainActivity
import ru.kolyukaev.yoweather.views.adapter.CitiesListener
import ru.kolyukaev.yoweather.views.adapter.CityAdapter
import ru.kolyukaev.yoweather.views.adapter.RecyclerItemClickListener


class CityFragment : BaseFragment(), CitiesListener {

    private lateinit var cityAdapter: CityAdapter

    lateinit var citiesViewModel: CitiesViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_city, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        citiesViewModel = ViewModelProvider(this).get(CitiesViewModel::class.java)


        cityAdapter = CityAdapter(requireContext(), this)
        rv_city.adapter = cityAdapter
        rv_city.layoutManager = LinearLayoutManager(context)
        rv_city.setHasFixedSize(true)
        rv_city.addOnItemTouchListener(
            RecyclerItemClickListener(rv_city, object : RecyclerItemClickListener.OnItemClickListener {
                override fun onItemClick(view: View, position: Int) {
                }
            })
        )

        val inputStream = resources.openRawResource(R.raw.cities_list_full)

        citiesViewModel.citiesLiveData.observe(viewLifecycleOwner, Observer {
            setupCitiesList(it as ArrayList<City>)
            endLoading()
        })

        citiesViewModel.mainWeatherLiveData.observe(viewLifecycleOwner, Observer {
        })

        citiesViewModel.getCities(inputStream)

        setToolbarTextChangedListener()

        showButtonBack()

//        Функция во вьюмоделе, которая конвертирует json в файл с объектом List<City>
//        val inputStream = resources.openRawResource(R.raw.cities)
//        citiesViewModel.convert(inputStream, requireContext().filesDir.absolutePath)
    }


    private fun setupCitiesList(cities: ArrayList<City>) {
        cityAdapter.setupCities(cities)
    }

    private fun endLoading() {
        pb_loading_city.gone()
        tv_cities_loading.gone()
        et_change_city.visible()
        et_change_city.requestFocus()
        et_change_city.showKeyboard()
    }

    private fun showError(text: String) {
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.city_info_menu, menu)
        menu.clear()
    }

    override fun onItemClick(cityId: Int, cityName: String, country: String, lat: Double, lon: Double) {
        val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()
        val mainFragment = MainFragment()
        val bundle = Bundle()
        bundle.putInt("id", cityId)
        bundle.putString("name", cityName)
        bundle.putString("country", country)
        bundle.putDouble("lat", lat)
        bundle.putDouble("lon", lon)
        mainFragment.arguments = bundle
        transaction.replace(R.id.container, mainFragment)
        transaction.commit()
        saveDataOfCity(cityId, cityName, country, lat, lon)
    }

    private fun saveDataOfCity(cityId: Int, cityName: String, country: String, lat: Double, lon: Double) {
        val pref = (activity as MainActivity).preferences
        val editor = pref?.edit()
        editor?.putInt("id", cityId)
        editor?.putString("city", cityName)
        editor?.putString("country", country)
        editor?.putFloat("lat", lat.toFloat())
        editor?.putFloat("lon", lon.toFloat())
        editor?.apply()
    }

    private fun setToolbarTextChangedListener() {
        et_change_city.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                cityAdapter.filter(s.toString())
            }
        })
    }

    private fun showButtonBack() {
        if (parentFragmentManager.backStackEntryCount > 1) {
            bt_back_cities_fragment.visible()
        }
    }

    override fun onPause() {
        et_change_city.text = null
        et_change_city.hideKeyboard()
        super.onPause()
    }

    override fun onDestroyView() {
        rv_city.adapter = null
        super.onDestroyView()
    }


//    (при использовании тулбара в активити) лямда с помощью который передаем текст из
//    edit text activity в адаптер
//    private fun setEditTextChangeListener() {
//        (activity as? MainActivity?)?.onToolbarTextChanged = { text ->
//            mAdapter.filter(text)
//        }
//    }

}