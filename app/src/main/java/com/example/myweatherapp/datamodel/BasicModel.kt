package com.example.myweatherapp.datamodel

data class BasicModel(
    val date: String,
    val type: String,
    val high: String?,
    val low: String?,
    val weekday: String?,
    val weatherIcon: Int
)