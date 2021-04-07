package com.example.myweatherapp.data

import com.google.gson.annotations.SerializedName

class DailyModel (
    @SerializedName("date")
    var date: String = "",

    @SerializedName("text_day")
    val type: String = "",

    @SerializedName("high")
    val high: String = "",

    @SerializedName("low")
    val low: String = ""
)