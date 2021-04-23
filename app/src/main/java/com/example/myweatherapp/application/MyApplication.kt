package com.example.myweatherapp.application

import android.app.Application


/*
* File         : MyApplication
* Description  : This class defines the global variable( current location)
* Author       : Ailwyn Liang
* Date         : 2021-4-23
*/

class MyApplication : Application() {
    companion object {
        @JvmField
        var currentLocation: String = "";
    }
}