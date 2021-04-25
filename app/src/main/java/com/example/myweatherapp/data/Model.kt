package com.example.myweatherapp.data

import com.google.gson.annotations.SerializedName

class Model {
    @SerializedName("location")
    val location: Location? = null

    @SerializedName("now")
    val now: Now? = null

    @SerializedName("daily")
    val daily: List<DailyModel>? = null

    @SerializedName("hourly")
    val hourly: List<HourModel>? = null

    @SerializedName("air")
    val air: Air? = null

    @SerializedName("suggestion")
    val suggestion: Suggestion? = null

    @SerializedName("last_update")
    val last_update: String? = null
}