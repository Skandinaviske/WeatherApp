package com.example.myweatherapp.repository

import android.annotation.SuppressLint
import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.example.myweatherapp.R
import com.example.myweatherapp.adapter.BasicModel
import com.example.myweatherapp.data.Result
import com.example.myweatherapp.database.AppDatabase
import com.example.myweatherapp.database.DataModel
import com.example.myweatherapp.retrofit.ConnectService
import com.example.myweatherapp.retrofit.RetrofitService
import com.example.myweatherapp.util.Util
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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

    fun getNowInfo(cityname: String, application: Application): MutableLiveData<ArrayList<String>> {
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
                    val date = Util.getCurrentDate()
                    val arrayList = arrayListOf<String>()
                    if (temperature != null && code != null && weathertype != null &&
                        feels_like != null && humidity != null && wind_direction != null
                    ) {
                        val weatherBackground = Util.judgeWeatherType(Integer.parseInt(code))
                        arrayList.add(temperature)
                        arrayList.add(weatherBackground)
                        arrayList.add(weathertype)
                        arrayList.add(feels_like)
                        arrayList.add(humidity)
                        arrayList.add(wind_direction)
                        arrayList.add(date)
                        arrayList.add("℃")
                        arrayList.add("gone")
                        arrayList.add(Util.getWeekOfDate())
                        arrayList.add("visible")
                        Log.d("TestLiang", "CITYNAME =${cityname}")
                        var dataModel: DataModel = DataModel(cityname, temperature.toInt(), weathertype)
                        val db = AppDatabase.getDatabase(application)
                        db.DataDao().insert(dataModel)

//                        val basicModel1 = db.DataDao().getData("chengdu")
//                        Log.d("TestLiang", "result =${basicModel1.temperature}")
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
                        val weekday: String? = date?.let { Util.getWeekOfDate(it) }
                        val weatherIcon: Int? = type?.let { Util.judgeWeatherType(it) }
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
}