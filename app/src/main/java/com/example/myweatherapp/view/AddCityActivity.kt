package com.example.myweatherapp.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ListView
import android.widget.SearchView
import android.widget.SimpleAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myweatherapp.R
import com.example.myweatherapp.adapter.CityManagementAdapter
import com.example.myweatherapp.adapter.HourWeatherAdapter
import com.example.myweatherapp.database.DataModel
import com.example.myweatherapp.databinding.ActivityAddcityBinding
import com.example.myweatherapp.databinding.BottomSheetDialogCityBinding
import com.example.myweatherapp.datamodel.CitySearchModel
import com.example.myweatherapp.datamodel.DataModelWithVisible
import com.example.myweatherapp.datamodel.HourDataModel
import com.example.myweatherapp.viewmodel.MyViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AddCityActivity : OnClickHandlerInterface, AppCompatActivity() {

    private var binding: ActivityAddcityBinding? = null
    private var myViewModel: MyViewModel? = null
    private var arraylistDataModel: ArrayList<DataModel>? = null
    private var adapter: CityManagementAdapter? = null
    private var isVisible = "gone"
    private var icon = "gone"
    private var arrayListDataModelWithVisible: ArrayList<DataModelWithVisible> = ArrayList()
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

            myViewModel!!.repositoryfromDatabase?.observe(this,
                Observer<ArrayList<DataModel>> { t ->
                    if (arraylistDataModel == null) {
                        var model: DataModelWithVisible

                        arrayListDataModelWithVisible = ArrayList()

                        //Start
                        for (i in t) {
                            model =
                                DataModelWithVisible(
                                    i.city,
                                    i.temperature,
                                    i.type,
                                    isVisible,
                                    icon
                                )
                            arrayListDataModelWithVisible.add(model)
                        }
                        arrayListDataModelWithVisible[0].icon = "visible"
                        arrayListDataModelWithVisible[0].isVisible = "gone"
                        //End

                        binding?.recyclerview?.layoutManager = LinearLayoutManager(this)
                        adapter = CityManagementAdapter(arrayListDataModelWithVisible, this,
                            object : CityManagementAdapter.OnCheckBoxClickedListener {
                                override fun OnCheckBoxClicked(city: String, needDelete: Boolean) {
                                    if (needDelete) {
                                        arrayListDeleteItem.add(city)
                                    } else if (arrayListDeleteItem.contains(city)) {
                                        arrayListDeleteItem.remove(city)
                                    }
                                }
                            })
                        binding?.recyclerview?.adapter = adapter
                    } else {
                        adapter?.notifyDataSetChanged()
                    }
                })
        }
    }

    override fun onClicktoActivity(view: View) {
    }

    override fun onFinish(view: View) {
        (view.context as Activity).finish()
    }

    override fun onClickFloatingActionButton(view: View) {
        if (isVisible == "gone") {
            val binding: BottomSheetDialogCityBinding = DataBindingUtil.inflate(
                LayoutInflater.from(view.context),
                R.layout.bottom_sheet_dialog_city,
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

            val searchView = bottomSheetDialog.findViewById<SearchView>(R.id.searchview)

            if (searchView != null) {
                searchView.isIconified = false
                searchView.queryHint = "搜索城市"
                searchView.setOnQueryTextListener(object :
                    SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {

//                        if (query != null&&query != "") {
//                            Log.d("ZZZZZZZZZZZ", "Search words")
//                            val cityList = ArrayList<String>()
//                            val pathList = ArrayList<String>()
//                            myViewModel!!.getSearch(query)
//                            myViewModel!!.repositoryforCitySearch?.observe(
//                                this@AddCityActivity,
//                                Observer<ArrayList<CitySearchModel>> { t ->
//                                    t[0].name?.let { myViewModel!!.addinDatabase(it,view) }
//                                    t[0].name?.let { Log.d("DDDDTT", it) }
//                                })
//                        }

                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        if (newText != null&&newText != "") {
                            Log.d("ZZZZZZZZZZZ", "Search words")
                            val cityList = ArrayList<String>()
                            val pathList = ArrayList<String>()
                            myViewModel!!.getSearch(newText)
                            myViewModel!!.repositoryforCitySearch?.observe(
                                this@AddCityActivity,
                                Observer<ArrayList<CitySearchModel>> { t ->


//                                    for(i in t){
//                                        i.name?.let {
//                                            Log.d("DDDDTT", i.name)
//                                            cityList.add(it) }
//                                        i.path?.let { pathList.add(it)
//                                            Log.d("DDDTTT", i.path)
//                                        }
//                                    }

//                                    val listItems = ArrayList<Map<String, Any>>()
//                                    for(i in cityList.indices){
//                                        val listItem = HashMap<String, Any>()
//                                        listItem["name"] = cityList[i]
//                                        listItem["path"] = pathList[i]
//                                        listItems.add(listItem)
//                                    }
//                                    val simpleAdapter = SimpleAdapter(this@AddCityActivity, listItems,
//                                    R.layout.simple_cell, arrayOf("name", "path"), intArrayOf(R.id.name, R.id.path))
//                                    val list = bottomSheetDialog.findViewById<ListView>(R.id.searchList)
//                                    list?.adapter = simpleAdapter
                                })
                        }

                        return true
                    }
                })
            }

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
        arrayListDataModelWithVisible[0].isVisible = "gone"
        adapter?.notifyDataSetChanged()
        val floatingActionButton =
            binding?.root?.findViewById<FloatingActionButton>(R.id.floatingactionbutton)
        if (isVisible == "visible")
            floatingActionButton?.setImageResource(R.drawable.delete)
        else
            floatingActionButton?.setImageResource(R.drawable.add)
    }

    override fun onNewIntent(intent: Intent?) {
        val weatherType = intent?.getStringExtra("weathertype")
        val temperature = intent?.getStringExtra("temperature")
        val cityname = intent?.getStringExtra("cityname")

        var isChanged: Boolean = false

        for (i in arrayListDataModelWithVisible) {
            if (i.city == cityname) {
                if (temperature != null && i.temperature != temperature.toInt()) {
                    i.temperature = temperature.toInt()
                    isChanged = true
                }
                if (weatherType != null && i.type != weatherType) {
                    i.type = weatherType
                    isChanged = true
                }
            }
        }
        if (isChanged)
            adapter?.notifyDataSetChanged()
        super.onNewIntent(intent)
    }
}