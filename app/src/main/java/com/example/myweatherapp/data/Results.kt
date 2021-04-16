package com.example.myweatherapp.data

import com.google.gson.annotations.SerializedName

class Results {
    @SerializedName("results")
    val result: List<CitySearch>? = null
}