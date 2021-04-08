package com.example.myweatherapp.view

import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.example.myweatherapp.R
import com.example.myweatherapp.databinding.ActivityAddcityBinding
import com.example.myweatherapp.viewmodel.MyViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog


class AddCityActivity: AppCompatActivity() {

    private var binding: ActivityAddcityBinding? = null
    private var myViewModel: MyViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_addcity
        )

        binding?.lifecycleOwner = this

        myViewModel = ViewModelProviders.of(this).get(MyViewModel::class.java)

        val intent = intent
        val cityName = intent.getStringExtra("cityname")
        if (cityName != null) {
            myViewModel!!.init(cityName)
            binding?.viewModel = myViewModel
        }
    }
}