package com.example.myweatherapp.data

import com.google.gson.annotations.SerializedName

class Suggestion {
    @SerializedName("air_pollution")
    val airpollution: AirPollution? = null

    @SerializedName("sport")
    val sport: Sport? = null

    @SerializedName("car_washing")
    val carWashing: CarWashing? = null

    @SerializedName("makeup")
    val makeup: Makeup? = null

    @SerializedName("sunscreen")
    val sunscreen: Sunscreen? = null

    @SerializedName("travel")
    val travel: Travel? = null

    @SerializedName("dressing")
    val dressing: Dressing? = null

    @SerializedName("traffic")
    val traffic: Traffic? = null

    @SerializedName("flu")
    val flu: Flu? = null
}