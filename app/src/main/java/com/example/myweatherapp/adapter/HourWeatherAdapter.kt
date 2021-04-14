package com.example.myweatherapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.myweatherapp.R
import com.example.myweatherapp.databinding.CellHourweatherBinding
import com.example.myweatherapp.datamodel.HourDataModel

class HourWeatherAdapter(
    private val items: ArrayList<HourDataModel>
) :
    RecyclerView.Adapter<HourWeatherAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate
                (
                LayoutInflater.from(parent.context),
                R.layout.cell_hourweather,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: HourWeatherAdapter.ViewHolder, position: Int) {
        holder.itemViewDataBinding.hourModel = items[position]
        holder.itemViewDataBinding.executePendingBindings()
    }

    class ViewHolder(val itemViewDataBinding: CellHourweatherBinding) :
        RecyclerView.ViewHolder(itemViewDataBinding.root)
}