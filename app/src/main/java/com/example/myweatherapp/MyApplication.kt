package com.example.myweatherapp

import android.app.Application

class MyApplication : Application() {
    companion object {
        @JvmField
        var currentLocation: String = "";
    }
}