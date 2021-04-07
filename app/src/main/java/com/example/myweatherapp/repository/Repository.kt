package com.example.myweatherapp.repository

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.example.myweatherapp.R
import com.example.myweatherapp.adapter.BasicModel
import com.example.myweatherapp.data.Result
import com.example.myweatherapp.retrofit.ConnectService
import com.example.myweatherapp.retrofit.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList

class Repository {
    private var repository: Repository? = null
    private var textLiveDataforNow = MutableLiveData<ArrayList<String>>()
    private var textLiveDataforLocation = MutableLiveData<ArrayList<String>>()
    private var textLiveDataforDaily = MutableLiveData<ArrayList<BasicModel>>()
    private val TIANQI_API_SECRET_KEY = "S69J9uyzmkgblruE-"
    private val LANGUAGE_NAME = "zh-Hans"
    private val UNIT = "c"
    val instance: Repository
        get() {
            if (repository == null) {
                repository = Repository()
            }
            return repository!!
        }

    private val connectService: ConnectService =
        RetrofitService.createService(ConnectService::class.java)

    fun getNowInfo(cityname: String): MutableLiveData<ArrayList<String>> {
        val call: Call<Result> =
            connectService.getStringArrayListfornow(
                TIANQI_API_SECRET_KEY,
                cityname,
                LANGUAGE_NAME,
                UNIT
            )
        call.enqueue(object : Callback<Result> {
            override fun onResponse(call: Call<Result>, response: Response<Result>) {
                val response: Result? = response.body()
                if (response?.result != null) {
                    val users = response?.result;
                    val model = users[0]
                    val now = model.now
                    val temperature = now?.temperature
                    val code = now?.code
                    val weathertype = now?.type
                    val feels_like = now?.feels_like
                    val humidity = now?.humidity
                    val wind_direction = now?.wind_direction
                    val date = getCurrentDate()

                    val arrayList = arrayListOf<String>()
                    if (temperature != null && code != null && weathertype != null &&
                        feels_like != null && humidity != null && wind_direction != null
                    ) {
                        val weatherBackground = judgeWeatherType(Integer.parseInt(code))
                        arrayList.add(temperature)
                        arrayList.add(weatherBackground)
                        arrayList.add(weathertype)
                        arrayList.add(feels_like)
                        arrayList.add(humidity)
                        arrayList.add(wind_direction)
                        arrayList.add(date)
                        arrayList.add("℃")
                        arrayList.add("gone")
                        arrayList.add(getWeekOfDate())
                        //Log.d("CurrentWeather", temperature + weatherBackground + weathertype)
                    }

                    textLiveDataforNow.postValue(arrayList)
                }
            }

            @SuppressLint("LongLogTag")
            override fun onFailure(call: Call<Result>, t: Throwable) {
                t.message?.let { Log.e("Print Error in Weather App:", it) }
            }

        })
        return textLiveDataforNow
    }

    fun getLocationInfo(cityname: String): MutableLiveData<ArrayList<String>> {
        val call: Call<Result> = connectService.getStringArrayListfornow(
            TIANQI_API_SECRET_KEY,
            cityname,
            LANGUAGE_NAME,
            UNIT
        )
        call.enqueue(object : Callback<Result> {
            override fun onResponse(call: Call<Result>, response: Response<Result>) {
                val response: Result? = response.body()
                if (response?.result != null) {
                    val users = response?.result;
                    val model = users[0]
                    val now = model.location
                    val id = now?.id
                    val cityname = now?.cityname
                    val country = now?.country
                    val path = now?.path
                    val timezone = now?.timezone
                    val timezone_offset = now?.timezone_offset

                    val arrayList = arrayListOf<String>()
                    if (id != null && cityname != null && country != null &&
                        path != null && timezone != null && timezone_offset != null
                    ) {
                        arrayList.add(id)
                        arrayList.add(cityname)
                        Log.d("CurrentCity", cityname)
                        arrayList.add(country)
                        arrayList.add(path)
                        arrayList.add(timezone)
                        arrayList.add(timezone_offset)
                    }

                    textLiveDataforLocation.postValue(arrayList)
                }
            }

            @SuppressLint("LongLogTag")
            override fun onFailure(call: Call<Result>, t: Throwable) {
                t.message?.let { Log.e("Print Error in Weather App:", it) }
            }

        })
        return textLiveDataforLocation
    }

    fun getDailyInfo(cityname: String): MutableLiveData<ArrayList<BasicModel>> {
        val call: Call<Result> = connectService.getStringArrayListfordaily(
            TIANQI_API_SECRET_KEY,
            cityname,
            LANGUAGE_NAME,
            UNIT,
            "1",
            "7"
        )
        call.enqueue(object : Callback<Result> {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(call: Call<Result>, response: Response<Result>) {
                val response: Result? = response.body()
                if (response?.result != null) {
                    var i = 0
                    val users = response.result;
                    val model = users[0]
                    val dailyModel = model.daily
                    val result = ArrayList<BasicModel>()
                    while (i < 7) {
                        val date = dailyModel?.get(i)?.date
                        val type = dailyModel?.get(i)?.type
                        val high = dailyModel?.get(i)?.high
                        val low = dailyModel?.get(i)?.low
                        val weekday: String? = date?.let { getWeekOfDate(it) }
                        val weatherIcon: Int? = type?.let { judgeWeatherType(it) }
                        Log.d(
                            "TestLiang",
                            "date = $date type = $type high = $high low = $low day = $weekday weatherIcon = $weatherIcon ${R.drawable.overcast}"
                        )
                        if (date != null && type != null &&
                            high != null && low != null &&
                            weekday != null && weatherIcon != null
                        ) {
                            val basicModel =
                                BasicModel(
                                    date,
                                    type,
                                    high,
                                    low,
                                    weekday,
                                    weatherIcon
                                )
                            result.add(basicModel)
                        }
                        i++
                    }
                    textLiveDataforDaily.postValue(result)
                }
            }

            @SuppressLint("LongLogTag")
            override fun onFailure(call: Call<Result>, t: Throwable) {
                t.message?.let { Log.e("Print Error in Weather App:", it) }
            }

        })
        return textLiveDataforDaily
    }

    @SuppressLint("SimpleDateFormat")
    fun getCurrentDate(): String {
        val dateFormat: DateFormat = SimpleDateFormat("MMM d日")
        return dateFormat.format(Calendar.getInstance().time)
        //textDate?.postValue(dateFormat.format(Calendar.getInstance().time))
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
    fun getWeekOfDate(date: String): String {
        val weekDays = arrayListOf<String>("星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六")
        val calendar = Calendar.getInstance()
        calendar.time = SimpleDateFormat("yyyy-MM-dd").parse(date)
        var week = calendar.get(Calendar.DAY_OF_WEEK) - 1
        if (week < 0)
            week = 0
        return weekDays[week]
    }

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

}