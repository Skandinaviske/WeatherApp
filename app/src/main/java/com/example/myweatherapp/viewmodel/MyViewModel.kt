package com.example.myweatherapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myweatherapp.repository.Repository
import kotlin.collections.ArrayList

class MyViewModel : ViewModel() {
    private var textLiveDataforNow: MutableLiveData<ArrayList<Any>>? = null
    private var textLiveDataforLocation: MutableLiveData<ArrayList<Any>>? = null

    private var repository = Repository()

    fun init(){
        repository = repository.instance
        textLiveDataforNow = repository.getNowInfo()
        textLiveDataforLocation = repository.getLocationInfo()
        //textDate = getCurrentDate()
    }


    val repositoryforNow: LiveData<ArrayList<Any>>?
        get() = textLiveDataforNow

    val repositoryforLocation: LiveData<ArrayList<Any>>?
        get() = textLiveDataforLocation

}