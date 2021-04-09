package com.example.myweatherapp.view

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myweatherapp.R
import com.example.myweatherapp.adapter.CityManagementAdapter
import com.example.myweatherapp.database.AppDatabase
import com.example.myweatherapp.database.DataModel
import com.example.myweatherapp.databinding.ActivityAddcityBinding
import com.example.myweatherapp.databinding.BottomSheetDialogBinding
import com.example.myweatherapp.viewmodel.MyViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog


class AddCityActivity : OnClickHandlerInterface, AppCompatActivity() {

    private var binding: ActivityAddcityBinding? = null
    private var myViewModel: MyViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_addcity
        )

        binding?.lifecycleOwner = this

        myViewModel = ViewModelProviders.of(this).get(MyViewModel::class.java)

        val intent = intent
        val cityName = intent.getStringExtra("cityname")
        if (cityName != null) {
            myViewModel!!.init(cityName)
            binding?.viewModel = myViewModel
            binding?.clickHandler = this

//            val db = AppDatabase.getDatabase(this)
//            var arrListDatabase: ArrayList<DataModel> = db.DataDao().getAllData() as ArrayList<DataModel>
//            var result = 0;
//            for((start, i) in arrListDatabase.withIndex()){
//                if(i.cityCN=="")
//                    result = start
//            }
//            arrListDatabase.removeAt(result)

            myViewModel!!.repositoryfromDatabase?.observe(this,
            Observer<ArrayList<DataModel>> { t ->
                binding?.recyclerview?.layoutManager = LinearLayoutManager(this)
                val adapter = CityManagementAdapter(t)
                binding?.recyclerview?.adapter = adapter
            })
        }
    }

    override fun onClicktoAddCity(view: View, cityname: String) {
    }

    override fun onFinish(view: View) {
        (view.context as Activity).finish()
    }

    override fun onClickFloatingActionButton(view: View) {
        val binding: BottomSheetDialogBinding = DataBindingUtil.inflate(
            LayoutInflater.from(view.context),
            R.layout.bottom_sheet_dialog,
            null,
            false
        )
        binding.viewModel = myViewModel
        val bottomSheetDialog = BottomSheetDialog(view.context)
        bottomSheetDialog.setContentView(binding.root);
        bottomSheetDialog.show()
    }
}