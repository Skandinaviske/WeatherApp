package com.example.myweatherapp.retrofit

import com.example.myweatherapp.data.Result
import com.example.myweatherapp.data.Results
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ConnectService {

    //"now.json?key=S69J9uyzmkgblruE-&location={city}&language=zh-Hans&unit=c&start=1&days=7/"
    @GET("now.json")
    fun getStringArrayListfornow(
        @Query("key") key: String,
        @Query("location") location: String,
        @Query("language") language: String,
        @Query("unit") unit: String
    ): Call<Result>

    @GET("daily.json")
    fun getStringArrayListfordaily(
        @Query("key") key: String,
        @Query("location") location: String,
        @Query("language") language: String,
        @Query("unit") unit: String,
        @Query("start") start: String,
        @Query("days") days: String
    ): Call<Result>

    @GET("hourly.json")
    fun getStringArrayListforhourly(
        @Query("key") key: String,
        @Query("location") location: String,
        @Query("language") language: String,
        @Query("unit") unit: String,
        @Query("start") start: String,
        @Query("hours") hours: String
    ): Call<Result>

    @GET("now.json")
    fun getStringArrayListforAir(
        @Query("key") key: String,
        @Query("location") location: String,
        @Query("language") language: String,
        @Query("scope") scope: String
    ): Call<Result>

    @GET("search.json")
    fun getStringArraySearchCity(
        @Query("key") key: String,
        @Query("q") query: String,
        @Query("language") language: String
    ): Call<Results>

    @GET("suggestion.json")
    fun getStringArraySuggestion(
        @Query("key") key: String,
        @Query("location") query: String,
        @Query("language") language: String
    ): Call<Result>
}