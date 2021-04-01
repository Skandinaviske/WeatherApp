package com.example.myweatherapp.data

import com.example.myweatherapp.data.Model
import com.google.gson.annotations.SerializedName

class Result {
    @SerializedName("results")
    val result: List<Model>? = null
}