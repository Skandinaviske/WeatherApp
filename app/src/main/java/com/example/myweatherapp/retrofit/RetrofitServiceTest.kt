package com.example.myweatherapp.retrofit

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitServiceTest {
    private const val baseurlSuggestion =
        "https://api.seniverse.com/v3/life/"
    val retrofitSuggestion by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseurlSuggestion)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
        return@lazy retrofit.create(ConnectService::class.java)
    }
}