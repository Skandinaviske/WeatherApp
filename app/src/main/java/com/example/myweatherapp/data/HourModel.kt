package com.example.myweatherapp.data

import com.google.gson.annotations.SerializedName

class HourModel {
    @SerializedName("temperature")
    val temperature: String? = null

    @SerializedName("text")
    val text: String? = null
}