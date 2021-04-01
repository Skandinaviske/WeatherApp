package com.example.myweatherapp.data

import com.google.gson.annotations.SerializedName

class Location (
    @SerializedName("id")
    val id: String = "",

    @SerializedName("name")
    val cityname: String = "",

    @SerializedName("country")
    val country: String = "",

    @SerializedName("path")
    val path: String = "",

    @SerializedName("timezone")
    val timezone: String = "",

    @SerializedName("timezone_offset")
    val timezone_offset: String = ""
)