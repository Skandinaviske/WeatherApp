package com.example.myweatherapp.data

import com.google.gson.annotations.SerializedName

class City {
    @SerializedName("aqi")
    val aqi: String? = null

    @SerializedName("pm25")
    val pm25: String? = null

    @SerializedName("pm10")
    val pm10: String? = null

    @SerializedName("so2")
    val so2: String? = null

    @SerializedName("no2")
    val no2: String? = null

    @SerializedName("co")
    val co: String? = null

    @SerializedName("o3")
    val o3: String? = null

    @SerializedName("quality")
    val quality: String? = null
}