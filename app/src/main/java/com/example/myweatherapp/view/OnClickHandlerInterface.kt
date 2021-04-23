package com.example.myweatherapp.view

import android.view.View

//Implements the interface to show different action on the activities

interface OnClickHandlerInterface {
    fun onClicktoActivity(view: View)
    fun onFinish(view: View)
    fun onClickFloatingActionButton(view: View)
    fun showCheckBox(view: View)
}