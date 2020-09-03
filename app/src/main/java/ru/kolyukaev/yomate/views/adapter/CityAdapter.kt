package ru.kolyukaev.yomate.views.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.cell_city.view.*
import ru.kolyukaev.yomate.R
import ru.kolyukaev.yomate.models.City

class CityAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var mCityList: ArrayList<City> = ArrayList()

    fun setupCities(cityList: ArrayList<City>) {
        mCityList.clear()
        mCityList.addAll(cityList)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemView = layoutInflater.inflate(R.layout.cell_city, parent, false)
        return CityViewHolder(itemView = itemView)
    }

    override fun getItemCount(): Int {
        return mCityList.count()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is CityViewHolder) {
            holder.bind(cityModel = mCityList[position])
        }
    }

    class CityViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private var mTvCityCell: TextView = itemView.tv_city_cell

        fun bind(cityModel: City) {
            mTvCityCell.text = "${cityModel.name}"
        }
    }
}