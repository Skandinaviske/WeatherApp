package com.example.myweatherapp.view

import android.view.View

interface OnClickHandlerInterface {
    fun onClicktoAddCity(view: View, cityname: String)
    fun onFinish(view: View)
    fun onClickFloatingActionButton(view: View)
}