package com.example.myweatherapp.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.myweatherapp.R
import com.example.myweatherapp.database.DataModel
import com.example.myweatherapp.databinding.CellCitymanagementBinding
import com.example.myweatherapp.util.Util

class CityManagementAdapter(
    private val items: ArrayList<DataModel>
) :
    RecyclerView.Adapter<CityManagementAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate
                (
                LayoutInflater.from(parent.context),
                R.layout.cell_citymanagement,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: CityManagementAdapter.ViewHolder, position: Int) {
        holder.itemViewDataBinding.dataModel = items[position]

        val type = items[position].type
        val city = items[position].city
        Log.d("Mytype", "City = $city Type = $type")
        when(Util.judgeWeatherColor(type)){
            2 -> holder.itemViewDataBinding.root.setBackgroundResource(R.drawable.shapelightgreen)
            3 -> holder.itemViewDataBinding.root.setBackgroundResource(R.drawable.shapelightgray)
            4 -> holder.itemViewDataBinding.root.setBackgroundResource(R.drawable.shapeadditemgray)
        }

        holder.itemViewDataBinding.executePendingBindings()
    }

    class ViewHolder(val itemViewDataBinding: CellCitymanagementBinding) :
        RecyclerView.ViewHolder(itemViewDataBinding.root)
}