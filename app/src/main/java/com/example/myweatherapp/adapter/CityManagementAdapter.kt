package com.example.myweatherapp.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.myweatherapp.R
import com.example.myweatherapp.databinding.CellCitymanagementBinding
import com.example.myweatherapp.util.Util
import com.example.myweatherapp.activity.OtherCityActivity
import com.example.myweatherapp.datamodel.DataModelWithVisible


/*
* File         : CityManagementAdapter
* Description  : This adapter relates to the recyclerview on CityManagementActivity
* Date         : 2021-4-23
*/

class CityManagementAdapter(
    private val items: ArrayList<DataModelWithVisible>,
    context: Context,
    var onCheckBoxClickedListener: OnCheckBoxClickedListener
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

        //We can change the background color according to the different weather type
        when (Util.judgeWeatherColor(type)) {
            1 -> holder.itemViewDataBinding.root.setBackgroundResource(R.drawable.shapelightblue)
            2 -> holder.itemViewDataBinding.root.setBackgroundResource(R.drawable.shapelightgreen)
            3 -> holder.itemViewDataBinding.root.setBackgroundResource(R.drawable.shapelightgray)
            4 -> holder.itemViewDataBinding.root.setBackgroundResource(R.drawable.shapeadditemgray)
            5 -> holder.itemViewDataBinding.root.setBackgroundResource(R.drawable.shapeadditemdust)
        }

        holder.itemView.setOnClickListener {
            if (position == 0) {
                (context as Activity).finish()
            } else {
                val intent = Intent(context, OtherCityActivity::class.java)
                intent.putExtra("cityname", city)
                context.startActivity(intent)
            }
        }

        val checkBox = holder.itemViewDataBinding.checkbox
        checkBox.setOnClickListener {
            onCheckBoxClickedListener.OnCheckBoxClicked(city, checkBox.isChecked)
        }

        holder.itemViewDataBinding.executePendingBindings()
    }

    class ViewHolder(val itemViewDataBinding: CellCitymanagementBinding) :
        RecyclerView.ViewHolder(itemViewDataBinding.root)

    interface OnCheckBoxClickedListener {
        fun OnCheckBoxClicked(city: String, needDelete: Boolean)
    }
}