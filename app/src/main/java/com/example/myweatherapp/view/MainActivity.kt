package com.example.myweatherapp.view

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.example.myweatherapp.R
import com.example.myweatherapp.databinding.ActivityMainBinding
import com.example.myweatherapp.viewmodel.MyViewModel
import com.jaeger.library.StatusBarUtil
import com.rainy.weahter_bg_plug.WeatherBg
import com.rainy.weahter_bg_plug.utils.WeatherUtil

class MainActivity : AppCompatActivity() {

    private var textViewType: TextView? = null
    private var textViewTemperature: TextView? = null
    private var textViewDate: TextView? = null
    private var myViewModel: MyViewModel? = null
    private var binding: ActivityMainBinding? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,
            R.layout.activity_main
        )

        binding?.lifecycleOwner = this

        StatusBarUtil.setTransparent(this)
        //setContentView(R.layout.activity_main)

        val window = this.window
        when(resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK){
            Configuration.UI_MODE_NIGHT_YES -> window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_VISIBLE
            Configuration.UI_MODE_NIGHT_NO -> window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

//        textViewType = findViewById(R.id.weather_type)
//        textViewTemperature = findViewById(R.id.temperature)
//        textViewDate = findViewById(R.id.date)

        myViewModel = ViewModelProviders.of(this).get(MyViewModel::class.java)
        myViewModel!!.init()

        binding?.viewModel = myViewModel

//        myViewModel!!.repositoryforText?.observe(this,
//            Observer<ArrayList<Any>> { t ->
//                if (t != null) {
//                    textViewTemperature?.text = t[0] as String?
//                }
//            })
    }
}