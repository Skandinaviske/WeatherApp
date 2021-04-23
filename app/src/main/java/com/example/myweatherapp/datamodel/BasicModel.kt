package com.example.myweatherapp.datamodel

//the data model used in CityManagementAdapter

data class BasicModel(
    val date: String,
    val type: String,
    val high: String?,
    val low: String?,
    val weekday: String?,
    val weatherIcon: Int
)