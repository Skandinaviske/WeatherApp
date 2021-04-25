package com.example.myweatherapp.util

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import com.example.myweatherapp.R
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


/*
* File         : Util
* Description  : This class contains all the utils in this app, which can make coding more convenient.
* Date         : 2021-4-23
*/

object Util {
    //judge the weather type by the return codes
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

    //judge the weather type and return the weather icon
    fun judgeWeatherType(weatherType: String): Int {
        var result: Int = 0
        when (weatherType) {
            "小雨", "阵雨", "雷阵雨" -> result = R.drawable.lightrainy
            "中雨" -> result = R.drawable.middlerainy
            "大雨", "暴雨" -> result = R.drawable.heavyrainy
            "小雪" -> result = R.drawable.lightsnow
            "中雪" -> result = R.drawable.middlesnow
            "大雪" -> result = R.drawable.heavysnow
            "多云" -> result = R.drawable.cloudy
            "晴" -> result = R.drawable.sunny
            "阴" -> result = R.drawable.overcast
            "雾" -> result = R.drawable.foggy
            "扬沙" -> result = R.drawable.dusty
        }
        return result
    }

    //get the weekday of the given date
    @SuppressLint("SimpleDateFormat")
    fun getWeekdayOfDate(date: String): String {
        val weekDays = arrayListOf<String>("星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六")
        val calendar = Calendar.getInstance()
        calendar.time = SimpleDateFormat("yyyy-MM-dd").parse(date)
        var week = calendar.get(Calendar.DAY_OF_WEEK) - 1
        if (week < 0)
            week = 0
        return weekDays[week]
    }

    //get the weekday of the given date
    fun getWeekdayOfCurrentDate(): String {
        val weekDays = arrayListOf<String>("星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六")
        val calendar = Calendar.getInstance()
        calendar.time = Calendar.getInstance().time
        var week = calendar.get(Calendar.DAY_OF_WEEK) - 1
        if (week < 0)
            week = 0
        return weekDays[week]
    }

    //get the weekday of tomorrow
    fun getWeekdayOfTomorrow(): String {
        val weekDays = arrayListOf<String>("星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六")
        val calendar = Calendar.getInstance()
        calendar.time = Calendar.getInstance().time
        var week = calendar.get(Calendar.DAY_OF_WEEK)
        if (week < 0)
            week = 0
        return weekDays[week]
    }

    //get current date
    @SuppressLint("SimpleDateFormat")
    fun getCurrentDate(): String {
        val dateFormat: DateFormat = SimpleDateFormat("MMMdd日")
        return dateFormat.format(Calendar.getInstance().time)
        //textDate?.postValue(dateFormat.format(Calendar.getInstance().time))
    }

    @SuppressLint("SimpleDateFormat")
    fun getCurrentTime(): String {
        val currentTime = System.currentTimeMillis()
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(currentTime)
    }

    //judge weather and return color codes
    fun judgeWeatherColor(weatherType: String): Int {
        var result: Int = 0
        when (weatherType) {
            "晴" -> result = 1
            "多云" -> result = 2
            "小雨", "中雨", "大雨", "暴雨", "小雪", "中雪", "大雪", "阵雨", "雷阵雨" -> result = 3
            "阴", "雾" -> result = 4
            "扬沙" -> result = 5
        }
        return result
    }

    //judge air quality and return color of the text
    fun judgeColor(quality: String): Int {
        var colorCode = 0
        when (quality) {
            "优" -> colorCode = R.color.green
            "良" -> colorCode = R.color.yellow
            "轻度污染" -> colorCode = R.color.red
            "中度污染" -> colorCode = R.color.orange
            "重度污染" -> colorCode = R.color.brown
            "严重污染" -> colorCode = R.color.superbrown
        }
        return colorCode
    }

    //judge whether the recyclerview slides to the bottom
    fun isSlideToBottom(recyclerView: RecyclerView?): Boolean {
        if (recyclerView == null)
            return false
        return recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange()
    }

    //give sun screen suggestions
    fun giveSunscreenBrief(brief: String): String {
        var giveBrief = ""
        when (brief) {
            "弱" -> giveBrief = "不需要防晒"
            "较弱" -> giveBrief = "轻微防嗮"
            "中等" -> giveBrief = "适当防晒"
            "强" -> giveBrief = "注意防嗮"
            "极强" -> giveBrief = "特别注意防晒"
        }
        return giveBrief
    }

    //give sun dressing suggestions
    fun giveDressingBrief(brief: String): String {
        var giveBrief = ""
        when (brief) {
            "炎热" -> giveBrief = "穿薄衣服"
            "热" -> giveBrief = "穿较薄衣服"
            "舒适" -> giveBrief = "穿衣舒适"
            "较舒适" -> giveBrief = "穿衣较舒适"
            "较冷" -> giveBrief = "穿稍厚衣服"
            "冷" -> giveBrief = "穿厚衣服"
            "寒冷" -> giveBrief = "穿保暖棉衣"
        }
        return giveBrief
    }
}