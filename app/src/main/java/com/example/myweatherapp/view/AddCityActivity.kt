package com.example.myweatherapp.view

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myweatherapp.R
import com.example.myweatherapp.adapter.CityManagementAdapter
import com.example.myweatherapp.database.DataModel
import com.example.myweatherapp.databinding.ActivityAddcityBinding
import com.example.myweatherapp.databinding.BottomSheetDialogBinding
import com.example.myweatherapp.datamodel.DataModelWithVisible
import com.example.myweatherapp.viewmodel.MyViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton


class AddCityActivity : OnClickHandlerInterface, AppCompatActivity() {

    private var binding: ActivityAddcityBinding? = null
    private var myViewModel: MyViewModel? = null
    private var arraylistDataModel: ArrayList<DataModel>? = null
    private var adapter: CityManagementAdapter? = null
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private var isVisible = "gone"
    private val arrayListDataModelWithVisible: ArrayList<DataModelWithVisible> = ArrayList()
    private val arrayListDeleteItem: ArrayList<String> = ArrayList()

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
            myViewModel!!.getData()
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
                    if (arraylistDataModel == null) {

                        Log.d("DDDDDDDDDDDDD","------------------------")
                        for (i in t) {
                            Log.d("DDDDDDDDDDDDD", "中文名：${i.cityCN}  英文名： ${i.city}")
                        }
                        Log.d("DDDDDDDDDDDDD","------------------------")

                        //val arrayListDataModelWithVisible:ArrayList<DataModelWithVisible> = ArrayList()
                        var model: DataModelWithVisible

                        //Start
                        for (i in t) {
                            model =
                                DataModelWithVisible(
                                    i.city,
                                    i.temperature,
                                    i.type,
                                    i.cityCN,
                                    isVisible
                                )
                            if (!arrayListDataModelWithVisible.contains(model))
                                arrayListDataModelWithVisible.add(model)
                        }
                        //End

//                        var arrayListAdd: ArrayList<DataModelWithVisible> = ArrayList()
//                        var index = 0
//                        while(index < t.size){
//                            var indexVisible = 0
//                            while (indexVisible < arrayListDataModelWithVisible.size){
//
//
//
//                                index++
//                            }
//                            index++
//                        }


                        binding?.recyclerview?.layoutManager = LinearLayoutManager(this)
                        adapter = CityManagementAdapter(arrayListDataModelWithVisible, this,
                            object : CityManagementAdapter.OnCheckBoxClickedListener {
                                override fun OnCheckBoxClicked(city: String, needDelete: Boolean) {
                                    Log.d("cherryPick", "city = $city")
                                    Log.d("cherryPicker", "needDelete = $needDelete")
                                    if (needDelete) {
                                        arrayListDeleteItem.add(city)
                                    } else if (arrayListDeleteItem.contains(city) ){
                                        arrayListDeleteItem.remove(city)
                                    }

                                    Log.d("NowItems", "-------------------")
                                    for(i in arrayListDeleteItem){
                                        Log.d("NowItems", "city = $i")
                                    }
                                    Log.d("NowItems", "-------------------")
                                }
                            })
                        binding?.recyclerview?.adapter = adapter
                    } else {
                        adapter?.notifyDataSetChanged()
                    }
                })
        }
    }

    override fun onClicktoActivity(view: View, cityname: String) {
    }

    override fun onFinish(view: View) {
        (view.context as Activity).finish()
    }

    override fun onClickFloatingActionButton(view: View) {
        if (isVisible == "gone") {
            val binding: BottomSheetDialogBinding = DataBindingUtil.inflate(
                LayoutInflater.from(view.context),
                R.layout.bottom_sheet_dialog,
                null,
                false
            )
            binding.viewModel = myViewModel
            val bottomSheetDialog = BottomSheetDialog(view.context)
            bottomSheetDialog.setContentView(binding.root)
            bottomSheetDialog.setOnDismissListener {
                myViewModel?.updateData()
            }

            bottomSheetDialog.show()
        } else {
            myViewModel?.deleteItemsforDatabase(arrayListDeleteItem)
            myViewModel?.updateData()
            adapter?.notifyDataSetChanged()
        }
    }

    override fun showCheckBox(view: View) {
        isVisible = if (isVisible == "gone")
            "visible"
        else
            "gone"
        for (i in arrayListDataModelWithVisible) {
            i.isVisible = isVisible
        }
        adapter?.notifyDataSetChanged()
        val floatingActionButton =
            binding?.root?.findViewById<FloatingActionButton>(R.id.floatingactionbutton)
        if (isVisible == "visible")
            floatingActionButton?.setImageResource(R.drawable.delete)
        else
            floatingActionButton?.setImageResource(R.drawable.add)
    }
}