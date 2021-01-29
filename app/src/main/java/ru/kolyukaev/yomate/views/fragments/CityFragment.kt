package ru.kolyukaev.yomate.views.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_city.*
import kotlinx.android.synthetic.main.fragment_main.*
import ru.kolyukaev.yomate.R
import ru.kolyukaev.yomate.data.models.City
import ru.kolyukaev.yomate.utils.gone
import ru.kolyukaev.yomate.utils.visible
import ru.kolyukaev.yomate.viewmodels.CitiesViewModel
import ru.kolyukaev.yomate.views.adapter.CitiesListener
import ru.kolyukaev.yomate.views.adapter.CityAdapter
import ru.kolyukaev.yomate.views.adapter.RecyclerItemClickListener

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


//        Функция во вьюмоделе, которая конвертирует json в файл с объектом List<City>
//        val inputStream = resources.openRawResource(R.raw.cities)
//        citiesViewModel.convert(inputStream, requireContext().filesDir.absolutePath)
    }


    fun setupCitiesList(cities: ArrayList<City>) {
        cityAdapter.setupCities(cities)
    }

    override fun onPause() {
        et_change_city.text = null
        et_change_city.hideKeyboard()
        super.onPause()
    }

    fun endLoading() {
        pb_load.gone()
        tv_cities_loading.gone()
        et_change_city.visible()
    }

    fun showError(text: String) {
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.city_info_menu, menu)
        menu.clear()
    }

    override fun onItemClick(country: String, id: Int, name: String, lat: Double, lon: Double) {
        val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()
        val mainFragment = MainFragment()
        val bundle = Bundle()
        bundle.putInt("id", id)
        bundle.putString("name", name)
        bundle.putString("country", country)
        bundle.putDouble("lat", lat)
        bundle.putDouble("lon", lon)
        mainFragment.arguments = bundle
        transaction.replace(R.id.container, mainFragment)
        transaction.commit()
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