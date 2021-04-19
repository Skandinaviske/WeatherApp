package com.example.myweatherapp.datamodel

data class HourDataModel(
    val hour: String,
    val type: String,
    val temperature: String,
    val weatherIcon: Int
)