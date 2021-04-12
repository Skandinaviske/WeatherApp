package com.example.myweatherapp.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.myweatherapp.R
import com.example.myweatherapp.database.DataModel
import com.example.myweatherapp.databinding.CellCitymanagementBinding
import com.example.myweatherapp.util.Util
import com.example.myweatherapp.view.OtherCityActivity

class CityManagementAdapter(
    private val items: ArrayList<DataModel>,
    context: Context
) :
    RecyclerView.Adapter<CityManagementAdapter.ViewHolder>() {

    private val context: Context = context

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
            1 -> holder.itemViewDataBinding.root.setBackgroundResource(R.drawable.shapelightblue)
            2 -> holder.itemViewDataBinding.root.setBackgroundResource(R.drawable.shapelightgreen)
            3 -> holder.itemViewDataBinding.root.setBackgroundResource(R.drawable.shapelightgray)
            4 -> holder.itemViewDataBinding.root.setBackgroundResource(R.drawable.shapeadditemgray)
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(context, OtherCityActivity::class.java)
            intent.putExtra("cityname", city)
            context.startActivity(intent)
        }

        holder.itemViewDataBinding.executePendingBindings()
    }

    class ViewHolder(val itemViewDataBinding: CellCitymanagementBinding) :
        RecyclerView.ViewHolder(itemViewDataBinding.root)
}