package com.example.myweatherapp.viewmodel

import android.app.Application
import android.util.Log
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myweatherapp.MyApplication
import com.example.myweatherapp.adapter.BasicModel
import com.example.myweatherapp.database.AppDatabase
import com.example.myweatherapp.database.DataModel
import com.example.myweatherapp.repository.Repository
import com.google.android.material.bottomsheet.BottomSheetBehavior


class MyViewModel(application: Application) : AndroidViewModel(application) {
    private var textLiveDataforNow: MutableLiveData<ArrayList<String>>? = null
    private var textLiveDataforLocation: MutableLiveData<ArrayList<String>>? = null
    private var textLiveDataforDaily: MutableLiveData<ArrayList<BasicModel>>? = null
    private var textLiveDatafromRoom: MutableLiveData<ArrayList<DataModel>>? = null

    private var repository = Repository()
    var isVisibility = "visible"
    val isVisible : ObservableField<Boolean> = ObservableField();

    fun init(cityname: String) {
        if(textLiveDatafromRoom != null){
            return
        }
        repository = repository.instance
        textLiveDataforNow = repository.getNowInfo(cityname, getApplication())
        textLiveDataforLocation = repository.getLocationInfo(cityname)
        textLiveDataforDaily = repository.getDailyInfo(cityname)
        //textLiveDatafromRoom = repository.getData(getApplication())
        isVisibility = "visible"
    }

    fun getData(){
        textLiveDatafromRoom = repository.getData(getApplication())
    }

    fun updateData(){
        val db = AppDatabase.getDatabase(getApplication())
        val arrayListDataModel =
            db.DataDao().getAllData() as ArrayList<DataModel>

//        Log.d("TestData", "Before------------------------------")
//        for (i in arrListDatabase) {
//            Log.d("TestData", "City=${i.city} 城市=${i.cityCN}")
//        }
//
//        Log.d("TestData", "Show:${MyApplication.currentLocation}")

        var result = -1
        for ((start, i) in arrayListDataModel.withIndex()) {
            if (i.cityCN == "")
                result = start
        }

        if (result != -1)
            arrayListDataModel.removeAt(result)

        var deleteCity = -1
        for ((start, i) in arrayListDataModel.withIndex()) {
            if (i.city == MyApplication.currentLocation) {
                deleteCity = start
            }
        }

        val dataModel:DataModel

        if (deleteCity != -1) {
            dataModel = arrayListDataModel[deleteCity]
            arrayListDataModel.removeAt(deleteCity)
            arrayListDataModel.add(0,dataModel)
        }

        for (i in arrayListDataModel) {
            Log.d("TestDataII", "City=${i.city} 城市=${i.cityCN}")
        }

        textLiveDatafromRoom?.postValue(arrayListDataModel)
    }

    fun addinDatabase(cityname: String) {
        repository = repository.instance
        textLiveDataforNow = repository.getNowInfo(cityname, getApplication())
    }

//    fun getAllData(){
//        repository = repository.instance
//        textLiveDatafromRoom = repository.getData(getApplication())
//    }

    fun test():String {
        return "visible"
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

    var getVisibility:String
        set(value) {}
        get() = isVisibility
}