package com.example.myweatherapp.adapter


class BasicModel(
    val _date: String,
    val _type: String,
    val _high: String? = null,
    val _low: String? = null
) {
    val date = _date
    val type = _type
    val high = _high
    val low = _low
}