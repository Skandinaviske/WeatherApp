package com.example.myweatherapp.retrofit

import com.example.myweatherapp.data.Result
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ConnectService {

//    var demo = GetData()
//
//    var url: String = demo.generateGetDiaryWeatherURL(
//        "shanghai",
//        "zh-Hans",
//        "c",
//        "1",
//        "7"
//    );

    //"now.json?key=S69J9uyzmkgblruE-&location={city}&language=zh-Hans&unit=c&start=1&days=7/"
    @GET("now.json")
    fun getStringArrayListfornow(
        @Query("key") key: String,
        @Query("location") location: String,
        @Query("language") language :String,
        @Query("unit") unit:String
    ): Call<Result>

    //https://api.seniverse.com/v3/weather/daily.json?key=S69J9uyzmkgblruE-&location=chengdu&language=zh-Hans&unit=c&start=1&days=7
    @GET("daily.json")
    fun getStringArrayListfordaily(
        @Query("key") key: String,
        @Query("location") location: String,
        @Query("language") language :String,
        @Query("unit") unit:String,
        @Query("start") start:String,
        @Query("days") days:String
    ): Call<Result>
}