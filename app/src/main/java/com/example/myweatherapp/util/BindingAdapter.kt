package com.example.myweatherapp.util

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
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
            view.visibility = if (value == "visible") View.VISIBLE else View.GONE
        }

        @BindingAdapter("imageResource")
        @JvmStatic
        fun setImageResource(imageView: ImageView, resource: Int) {
            imageView.setImageResource(resource)
        }

//        @BindingAdapter("bottomSheetBehaviorState")
//        @JvmStatic
//        fun setState(v: View, bottomSheetBehaviorState: Int) {
//            val viewBottomSheetBehavior =
//                BottomSheetBehavior.from(v)
//            viewBottomSheetBehavior.state = bottomSheetBehaviorState
//        }
    }
}