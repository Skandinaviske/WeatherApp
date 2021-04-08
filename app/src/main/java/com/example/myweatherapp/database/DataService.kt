package com.example.myweatherapp.database

import android.content.Context


object DataService {
    fun provideDatabase(context: Context) = AppDatabase.getDatabase(context)

    fun provideCharacterDao(db: AppDatabase) = db.DataDao()
}