package com.example.myweatherapp.data

import com.google.gson.annotations.SerializedName

class Now(
    @SerializedName("text")
    val type: String? = null,

    @SerializedName("code")
    val code: String? = null,

    @SerializedName("temperature")
    val temperature: String? = null,

    @SerializedName("feels_like")
    val feels_like: String? = null,

    @SerializedName("humidity")
    val humidity: String? = null,

    @SerializedName("wind_direction")
    val wind_direction: String? = null,

    @SerializedName("pressure")
    val pressure: String? = null,

    @SerializedName("visibility")
    val visibility: String? = null,

    @SerializedName("wind_speed")
    val wind_speed: String? = null,

    @SerializedName("wind_scale")
    val wind_scale: String? = null,

    @SerializedName("clouds")
    val clouds: String? = null
)