package com.example.myweatherapp.database

import androidx.lifecycle.LiveData
import androidx.room.*

/*
* File         : AppDatabase
* Description  : This class defines the basic database operating functions
* Date         : 2021-4-23
*/


@Dao
interface DataDao {
    @Query("SELECT * FROM weatherdata")
    fun getAllData(): List<DataModel>

    @Query("SELECT * FROM weatherdata WHERE city IN (:city)")
    fun getData(city: String): DataModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(dataModel: List<DataModel>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(dataModel: DataModel)

    @Insert
    fun insertin(dataModel: DataModel)

    @Query("DELETE FROM weatherdata WHERE city = :city")
    fun deleteData(city: String)

    @Query("UPDATE weatherdata SET temperature = :temperature , type = :type WHERE city = :city")
    fun updateData(city: String, temperature: Int, type: String)
}