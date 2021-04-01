package com.example.myweatherapp.retrofit

import com.example.myweatherapp.data.Result
import retrofit2.Call
import retrofit2.http.GET

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

    @GET("now.json?key=S69J9uyzmkgblruE-&location=shanghai&language=zh-Hans&unit=c&start=1&days=7/")
    fun getStringArrayList(): Call<Result>
}