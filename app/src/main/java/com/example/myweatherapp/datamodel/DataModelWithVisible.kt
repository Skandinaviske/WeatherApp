package com.example.myweatherapp.datamodel

data class DataModelWithVisible(
    val city: String,
    var temperature: Int,
    var type: String,
    var isVisible: String,
    var icon: String
)