package com.example.myweatherapp.viewmodel

import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.databinding.Bindable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myweatherapp.repository.Repository

class MyViewModel : ViewModel() {
    private var textLiveDataforNow: MutableLiveData<ArrayList<String>>? = null
    private var textLiveDataforLocation: MutableLiveData<ArrayList<String>>? = null

    private var repository = Repository()

    fun init(cityname: String){
        repository = repository.instance
        textLiveDataforNow = repository.getNowInfo(cityname)
        textLiveDataforLocation = repository.getLocationInfo(cityname)
        //textDate = getCurrentDate()
    }


    val repositoryforNow: LiveData<ArrayList<String>>?
        get() = textLiveDataforNow

    val repositoryforLocation: LiveData<ArrayList<String>>?
        get() = textLiveDataforLocation

}