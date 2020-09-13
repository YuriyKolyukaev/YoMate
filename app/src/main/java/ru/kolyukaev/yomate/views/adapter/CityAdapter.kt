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

class CityAdapter(
    private val context: Context,
    private val listener: CitiesListener
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mSourceList: MutableList<City> = mutableListOf()
    private var mCityList: MutableList<City> = mutableListOf()

    fun setupCities(cities: List<City>) {
        mSourceList.clear()
        mSourceList.addAll(cities)
        filter(query = "")
    }

    fun filter(query: String) {
        mCityList.clear()
        mSourceList.forEach {
            if (it.city.contains(query, ignoreCase = true)) {
                mCityList.add(it)
            }
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val itemView = layoutInflater.inflate(R.layout.cell_city, parent, false)
        val viewHolder = CityViewHolder(itemView = itemView)
        itemView.setOnClickListener {
            val currentPosition = viewHolder.adapterPosition
            val name = mCityList[currentPosition].city
            listener.onItemClick(name)
        }
        return viewHolder
    }

    override fun getItemCount(): Int {
        return mCityList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is CityViewHolder) {
            holder.bind(model = mCityList[position], context = context)
        }
    }

    class CityViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private var mTvCityCell: TextView = itemView.tv_city_cell
        private var mTvCountyCell: TextView = itemView.tv_country_cell


        fun bind(model: City, context: Context?) {
            mTvCityCell.text = model.city
            mTvCountyCell.text = model.country

        }
    }
}