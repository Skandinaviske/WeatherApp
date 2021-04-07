package com.example.myweatherapp.viewmodel

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myweatherapp.adapter.BasicModel
import com.example.myweatherapp.repository.Repository
import com.example.myweatherapp.view.AddCityActivity

class MyViewModel : ViewModel() {
    private var textLiveDataforNow: MutableLiveData<ArrayList<String>>? = null
    private var textLiveDataforLocation: MutableLiveData<ArrayList<String>>? = null
    private var textLiveDataforDaily: MutableLiveData<ArrayList<BasicModel>>? = null

    private var repository = Repository()

    fun init(cityname: String){
        repository = repository.instance
        textLiveDataforNow = repository.getNowInfo(cityname)
        textLiveDataforLocation = repository.getLocationInfo(cityname)
        textLiveDataforDaily = repository.getDailyInfo(cityname)
    }

    val repositoryforNow: LiveData<ArrayList<String>>?
        get() = textLiveDataforNow

    val repositoryforLocation: LiveData<ArrayList<String>>?
        get() = textLiveDataforLocation

    val repositoryforDaily: LiveData<ArrayList<BasicModel>>?
        get() = textLiveDataforDaily

    fun clicktoAddCity(view: View, cityname: String){
        val context: Context = view.context
        Log.d("TestLiang", "Cityname is $cityname")
        val intent:Intent = Intent(context, AddCityActivity::class.java)
        intent.putExtra("cityname",cityname)
        context.startActivity(intent)
    }
}