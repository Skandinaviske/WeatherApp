package com.example.myweatherapp.util

import android.annotation.SuppressLint
import com.example.myweatherapp.R
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

object Util {
    fun judgeWeatherType(weatherCode: Int): String {
        var result: String = "sunny"
        when (weatherCode) {
            0, 2 -> result = "sunny"
            1, 3 -> result = "sunnyNight"
            4, 5, 7 -> result = "cloudy"
            6, 8 -> result = "cloudyNight"
            9, 32, 33, 34, 35, 36 -> result = "overcast"
            10, 13, 19 -> result = "lightRainy"
            11, 12 -> result = "thunder"
            14 -> result = "middleRainy"
            15, 16, 17, 18 -> result = "heavyRainy"
            20, 21, 22 -> result = "lightSnow"
            23 -> result = "middleSnow"
            24, 25 -> result = "heavySnow"
            26, 27, 28, 29 -> result = "dusty"
            30 -> result = "foggy"
            31 -> result = "hazy"
        }
        return result
    }

    fun judgeWeatherType(weatherType: String): Int {
        var result: Int = 0
        when (weatherType) {
            "小雨" -> result = R.drawable.lightrainy
            "中雨" -> result = R.drawable.middlerainy
            "大雨", "暴雨" -> result = R.drawable.heavyrainy
            "小雪" -> result = R.drawable.lightsnow
            "中雪" -> result = R.drawable.middlesnow
            "大雪" -> result = R.drawable.heavysnow
            "多云" -> result = R.drawable.cloudy
            "晴" -> result = R.drawable.sunny
            "阴" -> result = R.drawable.overcast
            "雾" -> result = R.drawable.foggy
        }
        return result
    }

    @SuppressLint("SimpleDateFormat")
    fun getWeekOfDate(date: String): String {
        val weekDays = arrayListOf<String>("星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六")
        val calendar = Calendar.getInstance()
        calendar.time = SimpleDateFormat("yyyy-MM-dd").parse(date)
        var week = calendar.get(Calendar.DAY_OF_WEEK) - 1
        if (week < 0)
            week = 0
        return weekDays[week]
    }

    fun getWeekOfDate(): String {
        val weekDays = arrayListOf<String>("星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六")
        val calendar = Calendar.getInstance()
        calendar.time = Calendar.getInstance().time
        var week = calendar.get(Calendar.DAY_OF_WEEK) - 1
        if (week < 0)
            week = 0
        return weekDays[week]
    }

    @SuppressLint("SimpleDateFormat")
    fun getCurrentDate(): String {
        val dateFormat: DateFormat = SimpleDateFormat("MMM d日")
        return dateFormat.format(Calendar.getInstance().time)
        //textDate?.postValue(dateFormat.format(Calendar.getInstance().time))
    }
}