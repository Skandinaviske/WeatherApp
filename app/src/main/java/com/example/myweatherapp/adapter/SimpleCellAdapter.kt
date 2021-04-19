package com.example.myweatherapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.myweatherapp.R

class SimpleCellAdapter(
    private val context: Context,
    private val arrayList: ArrayList<String>
) : BaseAdapter() {

    override fun getItem(position: Int): Any? {
        return arrayList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return arrayList.size
    }

    @SuppressLint("InflateParams", "ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val retView: View

        if (convertView == null) {
            retView = LayoutInflater.from(context).inflate(R.layout.simple_cell, parent, false);
            val currentItem = arrayList[position]
            val text = retView?.findViewById<TextView>(R.id.path)
            text?.text = currentItem
        } else {
            retView = convertView
        }
        return retView
    }
}