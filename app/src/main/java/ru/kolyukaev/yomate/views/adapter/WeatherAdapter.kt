package ru.kolyukaev.yomate.views.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.kolyukaev.yomate.R
import ru.kolyukaev.yomate.data.models.RvWeatherModel

class WeatherAdapter(list: ArrayList<RvWeatherModel>) :
    RecyclerView.Adapter<WeatherAdapter.WeatherHolder>() {

    var listArray = list

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherHolder {
        val inflater = LayoutInflater.from(parent.context)
        return WeatherHolder(inflater.inflate(R.layout.cell_weather, parent, false))
    }

    override fun onBindViewHolder(holder: WeatherHolder, position: Int) {
        holder.bind(listArray[position])
    }

    override fun getItemCount(): Int {
        return listArray.size
    }

    class WeatherHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvHourTemp = itemView.findViewById<TextView>(R.id.tv_hour_temp)
        val tvHourPressure = itemView.findViewById<TextView>(R.id.tv_hour_pressure)

        fun bind(weatherList: RvWeatherModel) {
            tvHourTemp.text = weatherList.temperature.toString()
            tvHourPressure.text = weatherList.pressure.toString()
        }
    }

    fun updateAdapter(listItems: ArrayList<RvWeatherModel>){
        listArray.clear()
        listArray.addAll(listItems)
        notifyDataSetChanged()
    }
}