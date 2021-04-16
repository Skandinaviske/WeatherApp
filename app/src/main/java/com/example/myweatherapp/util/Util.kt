package com.example.myweatherapp.util

import android.annotation.SuppressLint
import androidx.core.content.ContextCompat
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

    fun getWeekOfCurrentDate(): String {
        val weekDays = arrayListOf<String>("星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六")
        val calendar = Calendar.getInstance()
        calendar.time = Calendar.getInstance().time
        var week = calendar.get(Calendar.DAY_OF_WEEK) - 1
        if (week < 0)
            week = 0
        return weekDays[week]
    }

    fun getWeekOfTomorrow(): String {
        val weekDays = arrayListOf<String>("星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六")
        val calendar = Calendar.getInstance()
        calendar.time = Calendar.getInstance().time
        var week = calendar.get(Calendar.DAY_OF_WEEK)
        if (week < 0)
            week = 0
        return weekDays[week]
    }

    @SuppressLint("SimpleDateFormat")
    fun getCurrentDate(): String {
        val dateFormat: DateFormat = SimpleDateFormat("MMMdd日")
        return dateFormat.format(Calendar.getInstance().time)
        //textDate?.postValue(dateFormat.format(Calendar.getInstance().time))
    }

    fun ENtoCN(EnglishName: String): String {
        var result: String = ""
        when (EnglishName) {
            "chengdu" -> result = "成都"
            "beijing" -> result = "北京"
            "shanghai" -> result = "上海"
            "shenzhen" -> result = "深圳"
            "guangzhou" -> result = "广州"
            "wuhan" -> result = "武汉"
            "changsha" -> result = "长沙"
            "nanjing" -> result = "南京"
            "suzhou" -> result = "苏州"
            "xian" -> result = "西安"
            "qingdao" -> result = "青岛"
            "shenyang" -> result = "沈阳"
            "chongqing" -> result = "重庆"
            "hangzhou" -> result = "杭州"
            "hong kong" -> result = "香港"
            "xiamen" -> result = "厦门"
        }
        return result
    }

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

    //优、良、轻度污染、中度污染、重度污染、严重污染
    fun judgeColor(quality: String) : Int {
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
}