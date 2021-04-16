package com.example.myweatherapp.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Executors

object RetrofitService {
//    var demo = GetData()
//
//    var url: String = demo.generateGetDiaryWeatherURL(
//        "shanghai",
//        "zh-Hans",
//        "c",
//        "1",
//        "7"
//    );

    private const val baseurl =
        "https://api.seniverse.com/v3/weather/"

    private const val baseurlAir =
        "https://api.seniverse.com/v3/air/"

    private const val baseurlCity =
        "https://api.seniverse.com/v3/location/"

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(baseurl)
        .addConverterFactory(GsonConverterFactory.create())
        .callbackExecutor(Executors.newSingleThreadExecutor())
        .build()

    private val retrofitAir: Retrofit = Retrofit.Builder()
        .baseUrl(baseurlAir)
        .addConverterFactory(GsonConverterFactory.create())
        .callbackExecutor(Executors.newSingleThreadExecutor())
        .build()

    private val retrofitCityList: Retrofit = Retrofit.Builder()
        .baseUrl(baseurlCity)
        .addConverterFactory(GsonConverterFactory.create())
        .callbackExecutor(Executors.newSingleThreadExecutor())
        .build()

    fun <S> createService(serviceClass: Class<S>): S {
        return retrofit.create(serviceClass)
    }

    fun <S> createServiceAir(serviceClass: Class<S>): S {
        return retrofitAir.create(serviceClass)
    }

    fun <S> createServiceCityList(serviceClass: Class<S>): S {
        return retrofitCityList.create(serviceClass)
    }
}