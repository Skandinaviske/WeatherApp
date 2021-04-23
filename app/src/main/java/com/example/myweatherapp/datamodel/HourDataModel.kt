package com.example.myweatherapp.datamodel

//the data model used in the HourWeatherAdapter

data class HourDataModel(
    val hour: String,
    val type: String,
    val temperature: String,
    val weatherIcon: Int
)