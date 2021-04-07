package com.example.myweatherapp.data

import com.google.gson.annotations.SerializedName

class Result {
    @SerializedName("results")
    val result: List<Model>? = null
}