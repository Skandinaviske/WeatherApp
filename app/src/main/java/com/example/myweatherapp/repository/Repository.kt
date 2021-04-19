package com.example.myweatherapp.repository

import android.annotation.SuppressLint
import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.example.myweatherapp.R
import com.example.myweatherapp.application.MyApplication
import com.example.myweatherapp.data.HourModel
import com.example.myweatherapp.data.Result
import com.example.myweatherapp.data.Results
import com.example.myweatherapp.database.AppDatabase
import com.example.myweatherapp.database.DataModel
import com.example.myweatherapp.datamodel.BasicModel
import com.example.myweatherapp.datamodel.CitySearchModel
import com.example.myweatherapp.datamodel.HourDataModel
import com.example.myweatherapp.retrofit.ConnectService
import com.example.myweatherapp.retrofit.RetrofitService
import com.example.myweatherapp.util.Util
import com.example.myweatherapp.util.Util.judgeColor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class Repository {
    private var repository: Repository? = null
    private var textLiveDataforNow = MutableLiveData<ArrayList<String>>()
    private var textLiveDataforLocation = MutableLiveData<ArrayList<String>>()
    private var textLiveDataforDaily = MutableLiveData<ArrayList<BasicModel>>()
    private var textLiveDataforHourDataModel = MutableLiveData<ArrayList<HourDataModel>>()
    private var textLiveDataforAir = MutableLiveData<ArrayList<String>>()
    private var textLiveDataforCitySearch = MutableLiveData<ArrayList<CitySearchModel>>()
    private var textLiveDataforSuggestion = MutableLiveData<ArrayList<String>>()
    private var textLiveDatafromRoom = MutableLiveData<ArrayList<DataModel>>()
    private var arrayListDataModel: ArrayList<DataModel> = ArrayList<DataModel>()
    private val TIANQI_API_SECRET_KEY = "SsWmmG_GwpNLboKR6"
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

    private val connectServiceforAir: ConnectService =
        RetrofitService.createServiceAir(ConnectService::class.java)

    private val connectServiceforCityList: ConnectService =
        RetrofitService.createServiceCityList(ConnectService::class.java)

    private val createServiceSuggestion: ConnectService =
        RetrofitService.createServiceSuggestion(ConnectService::class.java)

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
                    val pressure = now?.pressure
                    val visibility = now?.visibility
                    val wind_speed = now?.wind_speed
                    val wind_scale = now?.wind_scale
                    val clouds = now?.clouds
                    Log.d("DDD","城市：$cityname , 温度： $temperature")
                    if (temperature != null && code != null && weathertype != null &&
                        feels_like != null && humidity != null && wind_direction != null &&
                        pressure != null && visibility != null && wind_speed != null &&
                        wind_scale != null && clouds != null
                    ) {
                        val weatherBackground = Util.judgeWeatherType(Integer.parseInt(code))
                        arrayList.add(temperature)             //0
                        arrayList.add(weatherBackground)
                        arrayList.add(weathertype)
                        arrayList.add(feels_like)
                        arrayList.add(humidity)
                        arrayList.add(wind_direction)          //5
                        arrayList.add(date)
                        arrayList.add("℃")
                        arrayList.add("gone")
                        arrayList.add(Util.getWeekOfCurrentDate())
                        arrayList.add("visible")               //10
                        arrayList.add(pressure)
                        arrayList.add(visibility)
                        arrayList.add(wind_speed)
                        arrayList.add(wind_scale)
                        arrayList.add(clouds)                  //15
                        val dataModel = DataModel(
                            cityname,
                            temperature.toInt(),
                            weathertype,
                            Util.ENtoCN(cityname)
                        )
                        val db = AppDatabase.getDatabase(application)
                        db.DataDao().insert(dataModel)
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
            "0",
            "8"
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
                    while (i < 8) {
                        val date = dailyModel?.get(i)?.date
                        val type = dailyModel?.get(i)?.type
                        val high = dailyModel?.get(i)?.high
                        val low = dailyModel?.get(i)?.low
                        var weekday: String? = ""
                        weekday =
                            if (date?.let { Util.getWeekOfDate(it) } == Util.getWeekOfTomorrow())
                                "明天"
                            else
                                date?.let { Util.getWeekOfDate(it) }
                        val weatherIcon: Int? = type?.let { Util.judgeWeatherType(it) }
//                        Log.d(
//                            "TestLiang",
//                            "date = $date type = $type high = $high low = $low day = $weekday weatherIcon = $weatherIcon ${R.drawable.overcast}"
//                        )
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

    fun getHourlyInfo(cityname: String): MutableLiveData<ArrayList<HourDataModel>> {
        val call: Call<Result> = connectService.getStringArrayListforhourly(
            TIANQI_API_SECRET_KEY,
            cityname,
            LANGUAGE_NAME,
            UNIT,
            "0",
            "24"
        )
        call.enqueue(object : Callback<Result> {
            override fun onResponse(call: Call<Result>, response: Response<Result>) {
                val response: Result? = response.body()
                if (response?.result != null) {
                    val users = response?.result;
                    val model = users[0]
                    val hourly: List<HourModel>? = model.hourly
                    var i = 0
                    val result = ArrayList<HourDataModel>()
                    val rightNow: Calendar = Calendar.getInstance()
                    var currentTime = rightNow.get(Calendar.HOUR_OF_DAY)
                    while (i < 24) {
                        val temperature = hourly?.get(i)?.temperature
                        val type = hourly?.get(i)?.text
                        val weatherIcon: Int? = type?.let { Util.judgeWeatherType(it) }

                        if (temperature != null && type != null && weatherIcon != null) {
                            val hourDataModel =
                                HourDataModel(
                                    currentTime.toString(),
                                    type,
                                    temperature,
                                    weatherIcon
                                )
                            result.add(hourDataModel)
                        }
                        currentTime++
                        if (currentTime == 24)
                            currentTime = 0
                        i++
                    }
                    textLiveDataforHourDataModel.postValue(result)
                }
            }

            @SuppressLint("LongLogTag")
            override fun onFailure(call: Call<Result>, t: Throwable) {
                t.message?.let { Log.e("Print Error in Weather App:", it) }
            }
        })
        return textLiveDataforHourDataModel
    }

    fun getAirInfo(cityname: String): MutableLiveData<ArrayList<String>> {
        val call: Call<Result> = connectServiceforAir.getStringArrayListforAir(
            TIANQI_API_SECRET_KEY,
            cityname,
            LANGUAGE_NAME,
            "city"
        )
        call.enqueue(object : Callback<Result> {
            override fun onResponse(call: Call<Result>, response: Response<Result>) {
                val response: Result? = response.body()
                if (response?.result != null) {
                    val users = response?.result;
                    val model = users[0]
                    val air = model.air
                    val city = air?.city
                    val aqi = city?.aqi
                    val pm25 = city?.pm25
                    val pm10 = city?.pm10
                    val so2 = city?.so2
                    val no2 = city?.no2
                    val co = city?.co
                    val o3 = city?.o3
                    val quality = city?.quality
                    val airModel = ArrayList<String>()
                    if (aqi != null && pm25 != null && pm10 != null && so2 != null && no2 != null && co != null && o3 != null && quality != null) {
                        airModel.add(aqi)
                        airModel.add(pm25)
                        airModel.add(pm10)
                        airModel.add(so2)
                        airModel.add(no2)
                        airModel.add(co)
                        airModel.add(o3)
                        airModel.add(quality)
                        airModel.add(judgeColor(quality).toString())
                    }

                    textLiveDataforAir.postValue(airModel)
                }
            }
            @SuppressLint("LongLogTag")
            override fun onFailure(call: Call<Result>, t: Throwable) {
                t.message?.let { Log.e("Print Error in Weather App:", it) }
            }
        })
        return textLiveDataforAir
    }

    fun getCityListInfo(query: String) : MutableLiveData<ArrayList<CitySearchModel>>{
        val call: Call<Results> = connectServiceforCityList.getStringArraySearchCity(
            TIANQI_API_SECRET_KEY,
            query,
            LANGUAGE_NAME
        )
        call.enqueue(object : Callback<Results> {
            override fun onResponse(call: Call<Results>, response: Response<Results>) {
                val response: Results? = response.body()
                if (response?.result != null) {
                    val users = response?.result;
                    val arrayListCitySearch = ArrayList<CitySearchModel>()
                    for(i in users){
                        val name = i.name
                        val path = i.path
                        if(name!=null&&path!=null){
                            val citySearchModel = CitySearchModel(name, path)
                            arrayListCitySearch.add(citySearchModel)
                        }
                        //Log.d("ZZZZZZTTTT","名称：${i.name} from: ${i.path}")
                    }
                    textLiveDataforCitySearch.postValue(arrayListCitySearch)
                }
            }
            @SuppressLint("LongLogTag")
            override fun onFailure(call: Call<Results>, t: Throwable) {
                t.message?.let { Log.e("Print Error in Weather App:", it) }
            }
        })
        return textLiveDataforCitySearch
    }

    fun getSuggestion(cityname: String) : MutableLiveData<ArrayList<String>>{
        val call: Call<Result> = createServiceSuggestion.getStringArraySuggestion(
            TIANQI_API_SECRET_KEY,
            cityname,
            LANGUAGE_NAME
        )
        call.enqueue(object : Callback<Result> {
            override fun onResponse(call: Call<Result>, response: Response<Result>) {
                val response: Result? = response.body()
                if (response?.result != null) {
                    val arraylistSuggestion = ArrayList<String>()
                    val users = response?.result
                    val model = users[0]
                    val suggestion = model.suggestion
                    val airpollution = suggestion?.airpollution
                    val brief = airpollution?.brief
                    val details = airpollution?.details

                    if (brief != null&&details!=null) {
                        arraylistSuggestion.add(brief)
                        arraylistSuggestion.add(details)
                    }

                    textLiveDataforSuggestion.postValue(arraylistSuggestion)
                }
            }
            @SuppressLint("LongLogTag")
            override fun onFailure(call: Call<Result>, t: Throwable) {
                t.message?.let { Log.e("Print Error in Weather App:", it) }
            }
        })
        return textLiveDataforSuggestion
    }

    fun getData(application: Application): MutableLiveData<ArrayList<DataModel>> {
        val db = AppDatabase.getDatabase(application)
        arrayListDataModel =
            db.DataDao().getAllData() as ArrayList<DataModel>

        var result = -1
        for ((start, i) in arrayListDataModel.withIndex()) {
            if (i.cityCN == "")
                result = start
        }

        if (result != -1)
            arrayListDataModel.removeAt(result)

        var deleteCity = -1
        for ((start, i) in arrayListDataModel.withIndex()) {
            if (i.city == MyApplication.currentLocation) {
                deleteCity = start
            }
        }

        val dataModel: DataModel

        if (deleteCity != -1) {
            dataModel = arrayListDataModel[deleteCity]
            arrayListDataModel.removeAt(deleteCity)
            arrayListDataModel.add(0, dataModel)
        }

        textLiveDatafromRoom.postValue(arrayListDataModel)
        return textLiveDatafromRoom
    }

    fun deleteData(application: Application, arrayListDeleteItem: ArrayList<String>) {
        val db = AppDatabase.getDatabase(application)

        for (i in arrayListDeleteItem) {
            if (i != MyApplication.currentLocation)
                db.DataDao().deleteData(i)
        }
    }

    fun updateData(application: Application, cityname: String, dataModel: DataModel) {
        val db = AppDatabase.getDatabase(application)
        db.DataDao().updateData(dataModel.city, dataModel.temperature, dataModel.type)
    }
}