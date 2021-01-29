package ru.kolyukaev.yomate.views.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.cell_city.view.*
import ru.kolyukaev.yomate.R
import ru.kolyukaev.yomate.data.models.City
import ru.kolyukaev.yomate.utils.log

class CityAdapter(
    private val context: Context,
    private val listener: CitiesListener
): RecyclerView.Adapter<CityAdapter.CityViewHolder>() {

    private var mSourceList: ArrayList<City> = ArrayList()
    private var mCityList: ArrayList<City> = ArrayList()

    fun setupCities(cityList: ArrayList<City>) {
        mSourceList.clear()
        mSourceList.addAll(cityList)
    }

    fun filter(query: String) {
        mCityList.clear()
        mSourceList.forEach {
            if (query.length > 2) {
            if (it.name.contains(query, ignoreCase = true)) {
                mCityList.add(it)
            }
        }
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val itemView = layoutInflater.inflate(R.layout.cell_city, parent, false)
        val viewHolder = CityViewHolder(itemView = itemView)
        itemView.setOnClickListener {
            val currentPosition = viewHolder.adapterPosition
            val country = mCityList[currentPosition].country
            val id = mCityList[currentPosition].id
            val name = mCityList[currentPosition].name
            val lat = mCityList[currentPosition].coord.lat
            val lon = mCityList[currentPosition].coord.lon
            log("idCity = $id, country = $country  coord = ${mCityList[currentPosition].coord}")
            listener.onItemClick(country, id, name, lat, lon)
        }
        return viewHolder
    }

    override fun getItemCount(): Int {
        return mCityList.size
    }


    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        if(true) {
            holder.bind(cityModel = mCityList[position], context = context)
        }
    }

    class CityViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private var mTvCityCell: TextView = itemView.tv_city_cell
        private var mTvCountry: TextView = itemView.tv_country_cell
        private var mTvState: TextView = itemView.tv_state_cell


        fun bind(cityModel: City, context: Context?) {
            mTvCityCell.text = cityModel.name
            mTvCountry.text = cityModel.country
            mTvState.text = cityModel.state
        }
    }

}