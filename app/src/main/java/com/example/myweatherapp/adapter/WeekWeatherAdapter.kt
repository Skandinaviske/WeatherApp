package com.example.myweatherapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.myweatherapp.R
import com.example.myweatherapp.databinding.CellDailyWeatherBinding
import com.example.myweatherapp.datamodel.BasicModel

class WeekWeatherAdapter(
    private val items: ArrayList<BasicModel>
) :
    RecyclerView.Adapter<WeekWeatherAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate
                (
                LayoutInflater.from(parent.context),
                R.layout.cell_daily_weather,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: WeekWeatherAdapter.ViewHolder, position: Int) {
        holder.itemViewDataBinding.basicModel = items[position]
        holder.itemViewDataBinding.executePendingBindings()
    }

    class ViewHolder(val itemViewDataBinding: CellDailyWeatherBinding) :
        RecyclerView.ViewHolder(itemViewDataBinding.root)
}