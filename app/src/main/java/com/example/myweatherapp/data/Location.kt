package com.example.myweatherapp.data

import com.google.gson.annotations.SerializedName

class Location(
    @SerializedName("id")
    val id: String? = null,

    @SerializedName("name")
    val cityname: String? = null,

    @SerializedName("country")
    val country: String? = null,

    @SerializedName("path")
    val path: String? = null,

    @SerializedName("timezone")
    val timezone: String? = null,

    @SerializedName("timezone_offset")
    val timezone_offset: String? = null
)