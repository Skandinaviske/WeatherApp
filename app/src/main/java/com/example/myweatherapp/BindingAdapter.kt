package com.example.myweatherapp

import android.view.View
import androidx.databinding.BindingAdapter
import com.rainy.weahter_bg_plug.WeatherBg

class BindingAdapter {
    companion object {
        @BindingAdapter("app:setWeather")
        @JvmStatic
        fun setWeather(weatherBg: WeatherBg, value: String?) {
            when (value) {
                "sunny" -> weatherBg.changeWeather("sunny")
                "sunnyNight" -> weatherBg.changeWeather("sunnyNight")
                "cloudy" -> weatherBg.changeWeather("cloudy")
                "cloudyNight" -> weatherBg.changeWeather("cloudyNight")
                "overcast" -> weatherBg.changeWeather("overcast")
                "lightRainy" -> weatherBg.changeWeather("lightRainy")
                "thunder" -> weatherBg.changeWeather("thunder")
                "middleRainy" -> weatherBg.changeWeather("middleRainy")
                "heavyRainy" -> weatherBg.changeWeather("heavyRainy")
                "lightSnow" -> weatherBg.changeWeather("lightSnow")
                "middleSnow" -> weatherBg.changeWeather("middleSnow")
                "heavySnow" -> weatherBg.changeWeather("heavySnow")
                "dusty" -> weatherBg.changeWeather("dusty")
                "foggy" -> weatherBg.changeWeather("foggy")
                "hazy" -> weatherBg.changeWeather("hazy")
            }
        }

        @BindingAdapter("android:visibility")
        @JvmStatic
        fun setVisibility(view: View, value: String?) {
            view.visibility = if (value == "gone") View.GONE else View.VISIBLE
        }
    }
}