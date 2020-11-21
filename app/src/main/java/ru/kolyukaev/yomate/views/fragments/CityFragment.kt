package ru.kolyukaev.yomate.views.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_city.*
import ru.kolyukaev.yomate.R
import ru.kolyukaev.yomate.data.models.City
import ru.kolyukaev.yomate.utils.gone
import ru.kolyukaev.yomate.viewmodels.CitiesViewModel
import ru.kolyukaev.yomate.views.activities.MainActivity
import ru.kolyukaev.yomate.views.adapter.CitiesListener
import ru.kolyukaev.yomate.views.adapter.CityAdapter
import ru.kolyukaev.yomate.views.adapter.RecyclerItemClickListener


class CityFragment : BaseFragment(), CitiesListener {

    private lateinit var mAdapter: CityAdapter

    lateinit var citiesViewModel: CitiesViewModel

    override val toolbarName: String
        get() = getString(R.string.cities_fragment_name)

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

        val activity = (activity as MainActivity)
        activity.visibleToolbar()
        activity.enlargeToolbar()
        activity.goneCityandCountry()


        mAdapter = CityAdapter(requireContext(), this)
        rv_city.adapter = mAdapter
        rv_city.layoutManager = LinearLayoutManager(context)
        rv_city.setHasFixedSize(true)
        rv_city.addOnItemTouchListener(
            RecyclerItemClickListener
                (rv_city, object : RecyclerItemClickListener.OnItemClickListener {
                override fun onItemClick(view: View, position: Int) {
                }
            })
        )

        setEditTextChangeListener()

        val inputStream = resources.openRawResource(R.raw.cities_list_full)

        citiesViewModel.citiesLiveData.observe(viewLifecycleOwner, Observer {
            setupCitiesList(it as ArrayList<City>)
            startLoading()
        })

        citiesViewModel.mainWeatherLiveData.observe(viewLifecycleOwner, Observer {
        })

        citiesViewModel.getCities(inputStream)


//        Функция во вьюмоделе, которая конвертирует json в файл с объектом List<City>
//        val inputStream = resources.openRawResource(R.raw.cities)
//        citiesViewModel.convert(inputStream, requireContext().filesDir.absolutePath)

    }


    private fun setEditTextChangeListener() {
        (activity as? MainActivity?)?.onToolbarTextChanged = { text ->
            mAdapter.filter(text)
        }
    }

    override fun onDestroyView() {
        // Очищаем ссылку при уничтожении фрагмента, чтобы избежать утечки
        val activity = (activity as? MainActivity)
        activity?.onToolbarTextChanged = null
        activity?.goneToolbar()
        activity?.decrease()
        activity?.clearEditText()
        super.onDestroyView()
    }


    fun startLoading() {
        pb_load.gone()
    }

    fun setupCitiesList(cities: ArrayList<City>) {
        mAdapter.setupCities(cities)
    }

    fun endLoading() {
    }

    fun showError(text: String) {
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.city_info_menu, menu)
        menu.clear()
    }

    override fun onItemClick(country: String, id: Int, name: String) {
        val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()
        val mainFragment = MainFragment()
        val bundle = Bundle()
        bundle.putInt("id", id)
        bundle.putString("name", name)
        bundle.putString("country", country)
        mainFragment.arguments = bundle
        transaction.replace(R.id.container, mainFragment)
        transaction.commit()
    }
}