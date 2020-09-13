package ru.kolyukaev.yomate.views.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.fragment_city.*
import ru.kolyukaev.yomate.R
import ru.kolyukaev.yomate.data.models.City
import ru.kolyukaev.yomate.gone
import ru.kolyukaev.yomate.viewmodels.CitiesViewModel
import ru.kolyukaev.yomate.views.activities.MainActivity
import ru.kolyukaev.yomate.views.adapter.CitiesListener
import ru.kolyukaev.yomate.views.adapter.CityAdapter
import ru.kolyukaev.yomate.views.adapter.RecyclerItemClickListener
import ru.kolyukaev.yomate.visible
import java.io.InputStream

class CityFragment : BaseFragment(), CitiesListener {

    private lateinit var mAdapter: CityAdapter

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

        val activity = (activity as MainActivity)
        activity.visibleToolbar()

        mAdapter = CityAdapter(requireContext(),this)
        rv_city.adapter = mAdapter
        rv_city.layoutManager = LinearLayoutManager(context)
        rv_city.setHasFixedSize(true)
        rv_city.addOnItemTouchListener(
            RecyclerItemClickListener
                (rv_city, object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                    }
                }))

        setEditTextChangeListener()

        val inputStream: InputStream = resources.openRawResource(R.raw.citiesandcountry)

        citiesViewModel = ViewModelProvider(this).get(CitiesViewModel::class.java)

        citiesViewModel.citiesLiveData.observe(viewLifecycleOwner, Observer {
            setupCitiesList(it)
            pb_load.gone()
        })

        citiesViewModel.mainWeatherLiveData.observe(viewLifecycleOwner, Observer {
        })

        citiesViewModel.getCities(inputStream)
//        citiesViewModel.getWeathers()
    }

    private fun setEditTextChangeListener() {
        (activity as? MainActivity?)?.onToolbarTextChanged =  { text ->
            mAdapter.filter(text)
        }
    }

    override fun onDestroy() {
        // Очищаем ссылку при уничтожении фрагмента чтобы избежать утечки
        (activity as? MainActivity?)?.onToolbarTextChanged = null
        (activity as? MainActivity?)?.goneToolbar()
        super.onDestroy()
    }

    private fun setupCitiesList(cities: List<City>) {
        mAdapter.setupCities(cities)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.city_info_menu, menu)
        menu.clear()
    }

    override fun onItemClick(name: String) {
        val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()
        val mainFragment = MainFragment()
        val bundle = Bundle()
        bundle.putString("name", name)
        mainFragment.arguments = bundle
        transaction.replace(R.id.container, mainFragment)
        transaction.commitNow()
    }
}