package com.example.myweatherapp.datamodel

//the data model used in WeekWeatherAdapter

data class DataModelWithVisible(
    val city: String,
    var temperature: Int,
    var type: String,
    var isVisible: String,
    var icon: String
)