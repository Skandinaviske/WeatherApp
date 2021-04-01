package com.example.myweatherapp.data

import com.google.gson.annotations.SerializedName

class Now (
    @SerializedName("text")
    val type: String = "",

    @SerializedName("code")
    val code: String = "",

    @SerializedName("temperature")
    val temperature: String = "",

    @SerializedName("feels_like")
    val feels_like: String = "",

    @SerializedName("humidity")
    val humidity: String = "",

    @SerializedName("wind_direction")
    val wind_direction: String = ""
)