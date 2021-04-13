package com.example.myweatherapp.viewmodel

import androidx.room.PrimaryKey
import io.reactivex.annotations.NonNull

data class DataModelWithVisible (
    val city: String,
    val temperature: Int,
    val type: String,
    val cityCN: String,
    var isVisible: String
)