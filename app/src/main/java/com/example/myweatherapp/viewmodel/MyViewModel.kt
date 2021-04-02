package com.example.myweatherapp.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myweatherapp.repository.Repository
import kotlin.collections.ArrayList

class MyViewModel : ViewModel() {
    private var textLiveDataforNow: MutableLiveData<ArrayList<String>>? = null
    private var textLiveDataforLocation: MutableLiveData<ArrayList<Any>>? = null

    private var repository = Repository()

    fun init(cityname: String){
        repository = repository.instance
        textLiveDataforNow = repository.getNowInfo(cityname)
        textLiveDataforLocation = repository.getLocationInfo(cityname)
        //textDate = getCurrentDate()
    }


    val repositoryforNow: LiveData<ArrayList<String>>?
        get() = textLiveDataforNow

    val repositoryforLocation: LiveData<ArrayList<Any>>?
        get() = textLiveDataforLocation

}