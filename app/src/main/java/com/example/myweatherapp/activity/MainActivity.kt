package com.example.myweatherapp.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.example.myweatherapp.R
import com.example.myweatherapp.adapter.HourWeatherAdapter
import com.example.myweatherapp.adapter.WeekWeatherAdapter
import com.example.myweatherapp.application.MyApplication
import com.example.myweatherapp.databinding.ActivityMainBinding
import com.example.myweatherapp.databinding.BottomSheetDialogAirBinding
import com.example.myweatherapp.datamodel.BasicModel
import com.example.myweatherapp.datamodel.HourDataModel
import com.example.myweatherapp.view.OnClickHandlerInterface
import com.example.myweatherapp.viewmodel.MyViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.jaeger.library.StatusBarUtil
import com.permissionx.guolindev.PermissionX
import me.everything.android.ui.overscroll.IOverScrollDecor
import me.everything.android.ui.overscroll.IOverScrollState.*
import me.everything.android.ui.overscroll.IOverScrollStateListener
import me.everything.android.ui.overscroll.IOverScrollUpdateListener
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper


/*
* File         : MainActivity
* Description  : This class is the first view activity, it shows all the weather information including the weather
*                and the temperature per hour, the weather in 7 days, and some life suggestions
* Author       : Ailwyn Liang
* Date         : 2021-4-23
*/

class MainActivity : OnClickHandlerInterface, AppCompatActivity() {
    private var myViewModel: MyViewModel? = null
    private var binding: ActivityMainBinding? = null
    lateinit var amapLocationClient: AMapLocationClient
    lateinit var mLocationOption: AMapLocationClientOption
    private var cityname: String = "上海"
    private var lastcityname: String = ""

    private var isOverScrolled: Boolean = true

    @SuppressLint(
        "SetTextI18n", "CheckResult", "UseCompatLoadingForDrawables",
        "ClickableViewAccessibility"
    )
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Ask the location permission when the user first use this app
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            PermissionX.init(this)
                .permissions(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                )
                .request { allGranted, grantedList, deniedList ->
                    if (allGranted) {
                        Toast.makeText(this, "所有权限已申请完成", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this, "这些权限被拒绝: $deniedList", Toast.LENGTH_LONG).show()
                    }
                }
        }

        //Set immersion view
        StatusBarUtil.setTransparent(this)

        val window = this.window
        when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_VISIBLE
            Configuration.UI_MODE_NIGHT_NO -> window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        //bind view
        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_main
        )

        binding?.lifecycleOwner = this

//        val mSwipeRefreshLayout =
//            binding?.root?.findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout)
//
//        mSwipeRefreshLayout?.setOnRefreshListener {
//            refreshData()
//            mSwipeRefreshLayout.isRefreshing = false
//        }

        myViewModel = ViewModelProviders.of(this).get(MyViewModel::class.java)

        val scrollView = binding?.root?.findViewById<ScrollView>(R.id.scrollView)

        val decor = OverScrollDecoratorHelper.setUpOverScroll(scrollView)

//        decor.setOverScrollUpdateListener { decor, state, offset ->
//            if (offset > 0) {
//                isOverScrolled = true
//                mSwipeRefreshLayout?.isRefreshing = true
//            } else {
//                if (isOverScrolled) {
//                    refreshData()
//                    mSwipeRefreshLayout?.isRefreshing = false
//                }
//                isOverScrolled = false
//            }
//        }

        decor.setOverScrollStateListener { decor, oldState, newState ->
            when (newState) {
                STATE_IDLE -> {
                }
                STATE_DRAG_START_SIDE -> {
                    val progressBar = binding?.root?.findViewById<ProgressBar>(R.id.progressbar)
                    refreshData()
                }
                STATE_DRAG_END_SIDE -> {
                }
                STATE_BOUNCE_BACK -> if (oldState == STATE_DRAG_START_SIDE) {
                    // Dragging stopped -- view is starting to bounce back from the *left-end* onto natural position.
                } else {
                    // i.e. (oldState == STATE_DRAG_END_SIDE)
                    // View is starting to bounce back from the *right-end*.
                }
            }
        }

        //get current location
        doLocation()

        //When the server gives data back, show specific information
        amapLocationClient.setLocationListener { aMapLocation ->
            // aMapLocation is the position data

            if (aMapLocation != null) {
                //The server has callback
                if (aMapLocation.errorCode == 0) {
                    //Position successfully
                    cityname = aMapLocation.city
                    cityname = cityname.substring(0, cityname.length - 1)

                    MyApplication.currentLocation = cityname
                    MyApplication.currentBuilding = aMapLocation.aoiName

                    if (cityname != lastcityname) {
                        myViewModel!!.init(cityname)
                        binding?.viewModel = myViewModel
                        binding?.clickHandler = this
                        val city = binding?.root?.findViewById<TextView>(R.id.city)

                        city?.text = cityname

                        //Observe daily data, when data changes, refresh the recyclerview
                        myViewModel!!.repositoryforDaily?.observe(
                            this,
                            Observer<ArrayList<BasicModel>> { t ->

                                val temBasicModel = t[0]
                                t.removeAt(0)
                                val textView =
                                    binding?.root?.findViewById<TextView>(R.id.highandlow)
                                textView?.text = "${temBasicModel.high}℃/${temBasicModel.low}℃"

                                binding?.recyclerview?.layoutManager = LinearLayoutManager(this)
                                binding?.recyclerview?.adapter = WeekWeatherAdapter(t)
                                binding?.recyclerview?.setHasFixedSize(false)
                                binding?.root?.findViewById<CoordinatorLayout>(R.id.startupBackground)
                                    ?.setBackgroundResource(0)

                                val initText = binding?.root?.findViewById<TextView>(R.id.initText)
                                if (initText != null) {
                                    initText.visibility = View.GONE
                                }

                                val textViewCity = binding?.root?.findViewById<TextView>(R.id.city)
                                if (textViewCity != null) {
                                    textViewCity.text = "$cityname  ${aMapLocation.aoiName}"
                                    textViewCity.setCompoundDrawablesWithIntrinsicBounds(
                                        null,
                                        null,
                                        this.resources.getDrawable(R.drawable.position_two),
                                        null
                                    )
                                }
                            })

                        //Observe hourly weather information data, when data changes, refresh the recyclerview
                        myViewModel!!.repositoryforHourDataModel?.observe(
                            this,
                            Observer<ArrayList<HourDataModel>> { t ->

                                val linearLayoutManager = LinearLayoutManager(this)
                                linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
                                binding?.recyclerviewHour?.layoutManager = linearLayoutManager
                                binding?.recyclerviewHour?.adapter = HourWeatherAdapter(t)
                                binding?.recyclerviewHour?.setHasFixedSize(false)
                            })
                    }
                    lastcityname = cityname
                } else {
                    //Position failed
                    Log.e(
                        "Amap",
                        "location error, code = " + aMapLocation.errorCode + ", info = " + aMapLocation.errorInfo
                    )
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (amapLocationClient != null) {
            amapLocationClient.startLocation()
        }
    }

    override fun onPause() {
        super.onPause()
        if (amapLocationClient != null) {
            amapLocationClient.startLocation()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (amapLocationClient != null) {
            amapLocationClient.onDestroy()
        }
    }

    //get current location
    private fun doLocation() {
        cityname = "shanghai"
        amapLocationClient = AMapLocationClient(this)
        mLocationOption = AMapLocationClientOption()
        mLocationOption.geoLanguage = AMapLocationClientOption.GeoLanguage.ZH
        mLocationOption.interval = 2000
        amapLocationClient.setLocationOption(mLocationOption)
        amapLocationClient.startLocation()
    }

    //click to go to the AddCityActivity
    override fun onClicktoActivity(view: View) {
        val context: Context = view.context
        val intent: Intent = Intent(context, CityManagementActivity::class.java)
        intent.putExtra("cityname", cityname)
        context.startActivity(intent)
    }

    override fun onFinish(view: View) {
    }

    override fun onRestart() {
        super.onRestart()
        refreshData()
    }

    override fun onClickFloatingActionButton(view: View) {
    }

    //Pop up the bottomSheetDialog of the air condition
    override fun showCheckBox(view: View) {
        val binding: BottomSheetDialogAirBinding = DataBindingUtil.inflate(
            LayoutInflater.from(view.context),
            R.layout.bottom_sheet_dialog_air,
            null,
            false
        )
        binding.viewModel = myViewModel
        val bottomSheetDialog = BottomSheetDialog(view.context)
        bottomSheetDialog.setContentView(binding.root)
        bottomSheetDialog.show()
    }

    private fun refreshData() {
        myViewModel!!.updateNowInfo(cityname)
        myViewModel!!.updateDailyInfo(cityname)
        myViewModel!!.updateHourInfo(cityname)
        myViewModel!!.updateAirInfo(cityname)
        myViewModel!!.updateSuggestionInfo(cityname)
    }

}