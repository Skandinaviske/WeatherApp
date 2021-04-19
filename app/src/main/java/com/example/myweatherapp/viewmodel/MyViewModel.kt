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

class MyViewModel(application: Application) : AndroidViewModel(application) {
    private var textLiveDataforNow: MutableLiveData<ArrayList<String>>? = null
    private var textLiveDataforLocation: MutableLiveData<ArrayList<String>>? = null
    private var textLiveDataforDaily: MutableLiveData<ArrayList<BasicModel>>? = null
    private var textLiveDataforHour: MutableLiveData<ArrayList<HourDataModel>>? = null
    private var textLiveDatafromRoom: MutableLiveData<ArrayList<DataModel>>? = null
    private var textLiveDataforAir: MutableLiveData<ArrayList<String>>? = null
    private var textLiveDataforCitySearch: MutableLiveData<ArrayList<CitySearchModel>>? = null
    private var textLiveDataforSuggestion: MutableLiveData<ArrayList<String>>? = null
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
    }

    fun getSearch(query: String) {
        repository = repository.instance
        textLiveDataforCitySearch = repository.getCityListInfo(query)
    }

    fun getData() {
        textLiveDatafromRoom = repository.getData(getApplication())
    }

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

    fun addinDatabase(cityname: String, view: View) {
        view.setBackgroundResource(R.drawable.selector_btn_click_text_color_blue)
        repository = repository.instance
        textLiveDataforNow = repository.getNowInfo(cityname, getApplication())
    }

    fun deleteItemsforDatabase(arrayListDeleteItem: ArrayList<String>) {
        repository.deleteData(getApplication(), arrayListDeleteItem)
    }

    fun updateItemsforDatabase(cityname: String, dataModel: DataModel) {
        repository.updateData(getApplication(), cityname, dataModel)
    }

    val repositoryforNow: LiveData<ArrayList<String>>?
        get() = textLiveDataforNow

    val repositoryforLocation: LiveData<ArrayList<String>>?
        get() = textLiveDataforLocation

    val repositoryforDaily: LiveData<ArrayList<BasicModel>>?
        get() = textLiveDataforDaily

    val repositoryfromDatabase: LiveData<ArrayList<DataModel>>?
        get() = textLiveDatafromRoom

    val repositoryforHourDataModel: LiveData<ArrayList<HourDataModel>>?
        get() = textLiveDataforHour

    val repositoryforAir: LiveData<ArrayList<String>>?
        get() = textLiveDataforAir

    val repositoryforCitySearch: LiveData<ArrayList<CitySearchModel>>?
        get() = textLiveDataforCitySearch

    val repositoryforSuggestion: LiveData<ArrayList<String>>?
        get() = textLiveDataforSuggestion
}