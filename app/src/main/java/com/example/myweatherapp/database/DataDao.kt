package com.example.myweatherapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DataDao {
    @Query("SELECT * FROM weatherdata")
    fun getAllData() : List<DataModel>

    @Query("SELECT * FROM weatherdata WHERE city IN (:city)")
    fun getData(city: String): DataModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(dataModel: List<DataModel>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(dataModel: DataModel)

    @Insert
    fun insertin(dataModel: DataModel)
}