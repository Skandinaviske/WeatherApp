package com.example.myweatherapp.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.myweatherapp.R
import com.example.myweatherapp.database.DataModel
import com.example.myweatherapp.databinding.CellCitymanagementBinding

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
        val type = holder.itemViewDataBinding?.dataModel?.type
        val city = holder.itemViewDataBinding?.dataModel?.city
        if(type!=null&&city!=null) {
            Log.d("Mytype", "City = $city Type = $type")
            if (type == "小雨" || type == "阴")
                holder.itemViewDataBinding.root.setBackgroundResource(R.drawable.shapeadditemgray)
        }
            holder.itemViewDataBinding.executePendingBindings()
    }

    class ViewHolder(val itemViewDataBinding: CellCitymanagementBinding) :
        RecyclerView.ViewHolder(itemViewDataBinding.root)
}