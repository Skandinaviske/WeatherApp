package com.example.myweatherapp.repository

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.myweatherapp.data.Result
import com.example.myweatherapp.retrofit.ConnectService
import com.example.myweatherapp.retrofit.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class Repository {
    private var repository: Repository? = null
    private var textLiveDataforNow = MutableLiveData<ArrayList<String>>()
    private var textLiveDataforLocation = MutableLiveData<ArrayList<Any>>()

    val instance: Repository
        get() {
            if (repository == null) {
                repository = Repository()
            }
            return repository!!
        }

    private val connectService: ConnectService =
        RetrofitService.createService(ConnectService::class.java)

    fun getNowInfo(): MutableLiveData<ArrayList<String>> {
        val call: Call<Result> = connectService.getStringArrayList()
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
                        feels_like != null && humidity != null && wind_direction != null &&
                        date != null
                    ) {
                        val weatherBackground = judgeWeatherType(Integer.parseInt(code))
                        Log.d("CurrentWeather", weatherBackground)
                        arrayList.add(temperature)
                        arrayList.add(weatherBackground)
                        arrayList.add(weathertype)
                        arrayList.add(feels_like)
                        arrayList.add(humidity)
                        arrayList.add(wind_direction)
                        arrayList.add(date)
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

    fun getLocationInfo(): MutableLiveData<ArrayList<Any>> {
        val call: Call<Result> = connectService.getStringArrayList()
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

                    val arrayList = arrayListOf<Any>()
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

    @SuppressLint("SimpleDateFormat")
    fun getCurrentDate(): String{
        var dateFormat: DateFormat = SimpleDateFormat("MMM d日")
        return dateFormat.format(Calendar.getInstance().time)
        //textDate?.postValue(dateFormat.format(Calendar.getInstance().time))
    }

    fun judgeWeatherType(weatherCode: Int): String{
        var result:String = "sunny"
        when(weatherCode){
            0,2 -> result = "sunny"
            1,3 -> result = "sunnyNight"
            4,5,7 -> result = "cloudy"
            6,8 -> result = "cloudyNight"
            9,32,33,34,35,36 -> result = "overcast"
            10,13,19 -> result = "lightRainy"
            11,12 -> result = "thunder"
            14 -> result = "middleRainy"
            15,16,17,18 -> result = "heavyRainy"
            20,21,22 -> result ="lightSnow"
            23 -> result = "middleSnow"
            24,25 -> result = "heavySnow"
            26,27,28,29 -> result = "dusty"
            30 -> result = "foggy"
            31 -> result = "hazy"
        }
        return result
    }
}