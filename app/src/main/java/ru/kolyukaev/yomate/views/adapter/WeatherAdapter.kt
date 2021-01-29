package ru.kolyukaev.yomate.views.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.kolyukaev.yomate.R
import ru.kolyukaev.yomate.data.models.RwWeatherAfter

class WeatherAdapter : RecyclerView.Adapter<WeatherAdapter.WeatherHolder>() {

    private val listArray = mutableListOf<RwWeatherAfter>()

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
        val tvHourDtTxtDate = itemView.findViewById<TextView>(R.id.tv_hour_dt_txt_date)
        val tvHourDtTxtTime = itemView.findViewById<TextView>(R.id.tv_hour_dt_txt_time)
        val tvHourTemp = itemView.findViewById<TextView>(R.id.tv_hour_temp)
        val ivHourIcon = itemView.findViewById<ImageView>(R.id.iv_hour_icon)
        val tvHourHumidity = itemView.findViewById<TextView>(R.id.tv_hour_humidity)

        fun bind(weatherList: RwWeatherAfter) {
            tvHourDtTxtDate.text = weatherList.dtTxtDate
            tvHourDtTxtTime.text = weatherList.dtTxtTime
            tvHourTemp.text = weatherList.temperature
            ivHourIcon.setImageResource(weatherList.icon)
            tvHourHumidity.text = weatherList.humidity
        }
    }

    fun updateAdapter(listItems: ArrayList<RwWeatherAfter>){
        listArray.clear()
        listArray.addAll(listItems)
        notifyDataSetChanged()
    }
}