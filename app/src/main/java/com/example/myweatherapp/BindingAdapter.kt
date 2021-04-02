package com.example.myweatherapp

import android.widget.ImageView
import com.rainy.weahter_bg_plug.WeatherBg
import androidx.databinding.BindingAdapter
import androidx.databinding.BindingMethod
import androidx.databinding.BindingMethods

class BindingAdapter {
    companion object {
        @BindingAdapter("app:setWeather")
        @JvmStatic
        fun setWeather(weatherBg: WeatherBg, value: String?) {
            when (value) {
                "sunny" -> weatherBg.changeWeather("sunny")
                "heavyRainy" -> weatherBg.changeWeather("heavyRainy")
                "overcast" -> weatherBg.changeWeather("overcast")
            }
        }

        @BindingAdapter("imageUrl")
        fun ImageView.setImageUrl(url: String?) {
            //Glide.with(context).load(url).into(this)
        }
    }
}