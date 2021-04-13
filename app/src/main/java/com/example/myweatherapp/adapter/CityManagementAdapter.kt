package com.example.myweatherapp.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.myweatherapp.R
import com.example.myweatherapp.databinding.CellCitymanagementBinding
import com.example.myweatherapp.util.Util
import com.example.myweatherapp.view.OtherCityActivity
import com.example.myweatherapp.viewmodel.DataModelWithVisible

class CityManagementAdapter(
    private val items: ArrayList<DataModelWithVisible>?,
    private val context: Context,
    private val clickListener: ((binding: CellCitymanagementBinding) -> Unit)
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
            ), clickListener
        )
    }

    override fun getItemCount(): Int {

        return items?.size ?: 0

    }

    override fun onBindViewHolder(holder: CityManagementAdapter.ViewHolder, position: Int) {
        holder.itemViewDataBinding.dataModel = items?.get(position)

        val type = items?.get(position)?.type
        val city = items?.get(position)?.city
        Log.d("Mytype", "City = $city Type = $type")
        when(type?.let { Util.judgeWeatherColor(it) }){
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

    class ViewHolder(val itemViewDataBinding: CellCitymanagementBinding, private val clickListener: ((binding: CellCitymanagementBinding) -> Unit)) :
        RecyclerView.ViewHolder(itemViewDataBinding.root) {
        init {
            itemViewDataBinding.root.setOnClickListener {
                clickListener(itemViewDataBinding)
            }
        }
    }
}