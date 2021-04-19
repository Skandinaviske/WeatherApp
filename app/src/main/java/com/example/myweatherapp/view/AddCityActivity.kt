package com.example.myweatherapp.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myweatherapp.R
import com.example.myweatherapp.adapter.CityManagementAdapter
import com.example.myweatherapp.adapter.SimpleCellAdapter
import com.example.myweatherapp.database.DataModel
import com.example.myweatherapp.databinding.ActivityAddcityBinding
import com.example.myweatherapp.databinding.BottomSheetDialogCityBinding
import com.example.myweatherapp.datamodel.CitySearchModel
import com.example.myweatherapp.datamodel.DataModelWithVisible
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
                    Log.d("Camehere", "TYYYYYYYYY")
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
                            Log.d("Camehere", i.city)
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
                Log.d("Goooooooo", "????????????")
                myViewModel?.updateData()
                Log.d("Goooooooo", "YYYYYYYYYYYY")
            }

            bottomSheetDialog.show()

            val searchView = bottomSheetDialog.findViewById<SearchView>(R.id.searchview)

            searchView?.setOnClickListener {
                val linearLayout =
                    bottomSheetDialog.findViewById<LinearLayout>(R.id.bottomSheetLayout)
                val layoutParams = linearLayout?.layoutParams
                //getScreenHeightDp(this)
                layoutParams?.height = 2800
                linearLayout?.requestLayout()
                searchView.isIconified = false
                searchView.queryHint = "搜索城市"
                searchView.setOnQueryTextListener(object :
                    SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {

                        if (query != null && query != "") {
                            myViewModel!!.getSearch(query)
                            myViewModel!!.repositoryforCitySearch?.observe(
                                this@AddCityActivity,
                                Observer<ArrayList<CitySearchModel>> { t ->
                                    t[0].name?.let { myViewModel!!.addinDatabase(it, view) }
                                })
                        }

                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        if (newText != null && newText != "") {
                            val cityList = ArrayList<String>()
                            val pathList = ArrayList<String>()
                            myViewModel!!.getSearch(newText)
                            myViewModel!!.repositoryforCitySearch?.observe(
                                this@AddCityActivity,
                                Observer<ArrayList<CitySearchModel>> { t ->
                                    for (i in t) {
                                        i.path?.let {
                                            pathList.add(it)
                                        }
                                        i.name?.let { it1 ->
                                            cityList.add(it1)
                                        }
                                    }

                                    val adapter = SimpleCellAdapter(
                                        this@AddCityActivity,
                                        pathList
                                    )
                                    val list =
                                        bottomSheetDialog.findViewById<ListView>(R.id.searchList)
                                    list?.adapter = adapter

                                    list?.onItemClickListener =
                                        OnItemClickListener { parent, view, position, id ->
                                            myViewModel!!.addinDatabase(cityList[position], view)
                                            bottomSheetDialog.dismiss()
                                        }
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