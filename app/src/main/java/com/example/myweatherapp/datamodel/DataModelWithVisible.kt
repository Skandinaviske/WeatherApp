package com.example.myweatherapp.datamodel

import androidx.room.PrimaryKey
import io.reactivex.annotations.NonNull

data class DataModelWithVisible (
    val city: String,
    var temperature: Int,
    var type: String,
    val cityCN: String,
    var isVisible: String
)