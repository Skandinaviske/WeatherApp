package com.example.myweatherapp.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myweatherapp.R
import com.example.myweatherapp.adapter.BasicModel
import com.example.myweatherapp.database.AppDatabase
import com.example.myweatherapp.database.DataModel
import com.example.myweatherapp.databinding.BottomSheetDialogBinding
import com.example.myweatherapp.repository.Repository
import com.google.android.material.bottomsheet.BottomSheetDialog


class MyViewModel(application: Application) : AndroidViewModel(application) {
    private var textLiveDataforNow: MutableLiveData<ArrayList<String>>? = null
    private var textLiveDataforLocation: MutableLiveData<ArrayList<String>>? = null
    private var textLiveDataforDaily: MutableLiveData<ArrayList<BasicModel>>? = null
    private var textLiveDatafromRoom: MutableLiveData<ArrayList<DataModel>>? = null
    private var repository = Repository()

    fun init(cityname: String) {
        repository = repository.instance
        textLiveDataforNow = repository.getNowInfo(cityname, getApplication())
        textLiveDataforLocation = repository.getLocationInfo(cityname)
        textLiveDataforDaily = repository.getDailyInfo(cityname)

        textLiveDatafromRoom = repository.getData(getApplication())
    }

    fun addinDatabase(cityname: String) {
        repository = repository.instance
        Log.d("TestLiang", "Let us know that we r still rock n roll")
        textLiveDataforNow = repository.getNowInfo(cityname, getApplication())
    }

//    fun getAllData(){
//        repository = repository.instance
//        textLiveDatafromRoom = repository.getData(getApplication())
//    }

    fun test() {
        Log.d("TestLiang", "Let us know Winner")
    }

    fun showDatabase() {
        val db = AppDatabase.getDatabase(getApplication())
        val arrListDatabase: List<DataModel> = db.DataDao().getAllData()
        for (i in arrListDatabase) {
            Log.d("DataItem", "城市=" + i.city + " 温度=" + i.temperature + " 天气" + i.type)
        }
    }

    val repositoryforNow: LiveData<ArrayList<String>>?
        get() = textLiveDataforNow

    val repositoryforLocation: LiveData<ArrayList<String>>?
        get() = textLiveDataforLocation

    val repositoryforDaily: LiveData<ArrayList<BasicModel>>?
        get() = textLiveDataforDaily

    val repositoryfromDatabase: LiveData<ArrayList<DataModel>>?
        get() = textLiveDatafromRoom

}