package com.example.myweatherapp.viewmodel

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myweatherapp.R
import com.example.myweatherapp.datamodel.BasicModel
import com.example.myweatherapp.application.MyApplication
import com.example.myweatherapp.database.AppDatabase
import com.example.myweatherapp.database.DataModel
import com.example.myweatherapp.datamodel.CitySearchModel
import com.example.myweatherapp.datamodel.HourDataModel
import com.example.myweatherapp.repository.Repository


/*
* File         : MyViewModel
* Description  : The viewModel of this app. It will get data from Repository and transfer data to views.
*                The project also uses Databinding, so we can get data from here to the layout files.
* Author       : Ailwyn Liang
* Date         : 2021-4-23
*/

class MyViewModel(application: Application) : AndroidViewModel(application) {
    private var textLiveDataforNow: MutableLiveData<ArrayList<String>>? = null
    private var textLiveDataforLocation: MutableLiveData<ArrayList<String>>? = null
    private var textLiveDataforDaily: MutableLiveData<ArrayList<BasicModel>>? = null
    private var textLiveDataforHour: MutableLiveData<ArrayList<HourDataModel>>? = null
    private var textLiveDatafromRoom: MutableLiveData<ArrayList<DataModel>>? = null
    private var textLiveDataforAir: MutableLiveData<ArrayList<String>>? = null
    private var textLiveDataforCitySearch: MutableLiveData<ArrayList<CitySearchModel>>? = null
    private var textLiveDataforSuggestion: MutableLiveData<ArrayList<String>>? = null
    private var textLiveDataforOutofRequest: MutableLiveData<String>? = null
    private var repository = Repository()

    fun init(cityname: String) {
        if (textLiveDatafromRoom != null) {
            return
        }
        repository = repository.instance
        textLiveDataforNow = repository.getNowInfo(cityname, getApplication())
        textLiveDataforLocation = repository.getLocationInfo(cityname)
        textLiveDataforDaily = repository.getDailyInfo(cityname)
        textLiveDataforHour = repository.getHourlyInfo(cityname)
        textLiveDataforAir = repository.getAirInfo(cityname)
        textLiveDataforSuggestion = repository.getSuggestion(cityname)
        textLiveDataforOutofRequest = repository.outofRequest()
    }

    //get the city search results
    fun getSearch(query: String) {
        repository = repository.instance
        textLiveDataforCitySearch = repository.getCityListInfo(query)
    }

    //get the data from database
    fun getData() {
        textLiveDatafromRoom = repository.getData(getApplication())
    }

    //update data
    fun updateData() {
        val db = AppDatabase.getDatabase(getApplication())
        val arrayListDataModel =
            db.DataDao().getAllData() as ArrayList<DataModel>

        var deleteCity = -1
        for ((start, i) in arrayListDataModel.withIndex()) {
            if (i.city == MyApplication.currentLocation) {
                deleteCity = start
            }
        }

        val dataModel: DataModel

        if (deleteCity != -1) {
            dataModel = arrayListDataModel[deleteCity]
            arrayListDataModel.removeAt(deleteCity)
            arrayListDataModel.add(0, dataModel)
        }

        textLiveDatafromRoom?.postValue(arrayListDataModel)
    }

    //add data into database
    fun addinDatabase(cityname: String, view: View) {
        view.setBackgroundResource(R.drawable.selector_btn_click_text_color_blue)
        repository = repository.instance
        textLiveDataforNow = repository.getNowInfo(cityname, getApplication())
    }

    //delete data in database
    fun deleteItemsforDatabase(arrayListDeleteItem: ArrayList<String>) {
        repository.deleteData(getApplication(), arrayListDeleteItem)
    }

    //update data into database
    fun updateItemsforDatabase(cityname: String, dataModel: DataModel) {
        repository.updateData(getApplication(), cityname, dataModel)
    }

    //update data about now
    fun updateNowInfo(cityname: String){
        repository.getNowInfo(cityname, getApplication())
    }

    //update data about daily
    fun updateDailyInfo(cityname: String){
        repository.getDailyInfo(cityname)
    }

    //update data about hour
    fun updateHourInfo(cityname: String){
        repository.getHourlyInfo(cityname)
    }

    //update data about hour
    fun updateAirInfo(cityname: String){
        repository.getAirInfo(cityname)
    }

    //update data about hour
    fun updateSuggestionInfo(cityname: String){
        repository.getSuggestion(cityname)
    }

    //return the livedata about the current weather information
    val repositoryforNow: LiveData<ArrayList<String>>?
        get() = textLiveDataforNow

    //return the livedata about the location information
    val repositoryforLocation: LiveData<ArrayList<String>>?
        get() = textLiveDataforLocation

    //return the livedata about the weather information in 7 days
    val repositoryforDaily: LiveData<ArrayList<BasicModel>>?
        get() = textLiveDataforDaily

    //return all the data from database
    val repositoryfromDatabase: LiveData<ArrayList<DataModel>>?
        get() = textLiveDatafromRoom

    //return the livedata about the the weather information per hour
    val repositoryforHourDataModel: LiveData<ArrayList<HourDataModel>>?
        get() = textLiveDataforHour

    //return the livedata about the current air condition
    val repositoryforAir: LiveData<ArrayList<String>>?
        get() = textLiveDataforAir

    //return the livedata about the city search results
    val repositoryforCitySearch: LiveData<ArrayList<CitySearchModel>>?
        get() = textLiveDataforCitySearch

    //return the livedata about the life suggestions
    val repositoryforSuggestion: LiveData<ArrayList<String>>?
        get() = textLiveDataforSuggestion

    val repositoryforOutofRequest: LiveData<String>?
        get() = textLiveDataforOutofRequest
}