package com.example.myweatherapp.activity

import android.view.View

interface OnClickHandlerInterface {
    fun onClicktoActivity(view: View)
    fun onFinish(view: View)
    fun onClickFloatingActionButton(view: View)
    fun showCheckBox(view: View)
}