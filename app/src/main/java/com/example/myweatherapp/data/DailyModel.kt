package com.example.myweatherapp.data

import com.google.gson.annotations.SerializedName

class DailyModel(
    @SerializedName("date")
    var date: String? = null,

    @SerializedName("text_day")
    val type: String? = null,

    @SerializedName("high")
    val high: String? = null,

    @SerializedName("low")
    val low: String? = null
)