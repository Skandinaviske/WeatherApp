package com.example.myweatherapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import io.reactivex.annotations.NonNull

//The database data model

@Entity(tableName = "weatherdata")
data class DataModel(
    @PrimaryKey
    val city: String,
    val temperature: Int,
    val type: String
)