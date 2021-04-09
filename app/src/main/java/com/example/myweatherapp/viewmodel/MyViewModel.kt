package com.example.myweatherapp.viewmodel

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.DataBindingUtil.setContentView
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myweatherapp.R
import com.example.myweatherapp.adapter.BasicModel
import com.example.myweatherapp.database.AppDatabase
import com.example.myweatherapp.database.DataModel
import com.example.myweatherapp.databinding.BottomSheetDialogBinding
import com.example.myweatherapp.repository.Repository
import com.example.myweatherapp.view.AddCityActivity
import com.google.android.material.bottomsheet.BottomSheetDialog


class MyViewModel(application: Application) : AndroidViewModel(application) {
    private var textLiveDataforNow: MutableLiveData<ArrayList<String>>? = null
    private var textLiveDataforLocation: MutableLiveData<ArrayList<String>>? = null
    private var textLiveDataforDaily: MutableLiveData<ArrayList<BasicModel>>? = null

    private var repository = Repository()

    fun init(cityname: String) {
        repository = repository.instance
        textLiveDataforNow = repository.getNowInfo(cityname, getApplication())
        textLiveDataforLocation = repository.getLocationInfo(cityname)
        textLiveDataforDaily = repository.getDailyInfo(cityname)
    }

    fun addinDatabase(cityname: String) {
        repository = repository.instance
        Log.d("TestLiang", "Let us know that we r still rock n roll")
        textLiveDataforNow = repository.getNowInfo(cityname, getApplication())
    }

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

    fun clicktoAddCity(view: View, cityname: String) {
        val context: Context = view.context
        Log.d("TestLiang", "Cityname is $cityname")
        val intent: Intent = Intent(context, AddCityActivity::class.java)
        intent.putExtra("cityname", cityname)
        context.startActivity(intent)
    }

    fun onFinish(view: View) {
        (view.context as Activity).finish()
    }

    fun onClickFloatingActionButton(view: View) {
        showBottomSheetDialog(view.context)
    }

    private fun showBottomSheetDialog(context: Context) {
        val binding: BottomSheetDialogBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.bottom_sheet_dialog,
            null,
            false
        )
        binding.viewModel = this
        val bottomSheetDialog = BottomSheetDialog(context)
        bottomSheetDialog.setContentView(binding.root);
        bottomSheetDialog.show()
    }
}