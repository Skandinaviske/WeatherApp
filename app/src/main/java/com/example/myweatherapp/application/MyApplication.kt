package com.example.myweatherapp.application

import android.app.Application

class MyApplication : Application() {
    companion object {
        @JvmField
        var currentLocation: String = "";
    }
}