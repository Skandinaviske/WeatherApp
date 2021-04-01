package com.example.myweatherapp.data

import com.google.gson.annotations.SerializedName

class Model{
    @SerializedName("location")
    val location: Location? = null

    @SerializedName("now")
    val now: Now? = null
}