package com.example.myweatherapp.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.example.myweatherapp.R
import com.example.myweatherapp.adapter.HourWeatherAdapter
import com.example.myweatherapp.datamodel.BasicModel
import com.example.myweatherapp.adapter.WeekWeatherAdapter
import com.example.myweatherapp.application.MyApplication
import com.example.myweatherapp.databinding.ActivityMainBinding
import com.example.myweatherapp.databinding.BottomSheetDialogAirBinding
import com.example.myweatherapp.datamodel.HourDataModel
import com.example.myweatherapp.viewmodel.MyViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.jaeger.library.StatusBarUtil


class MainActivity : OnClickHandlerInterface, AppCompatActivity() {
    private var myViewModel: MyViewModel? = null
    private var binding: ActivityMainBinding? = null
    lateinit var amapLocationClient: AMapLocationClient
    lateinit var mLocationOption: AMapLocationClientOption
    private var cityname: String = "上海"
    private var lastcityname: String = ""

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        requestPermissions(
//            arrayOf(
//                Manifest.permission.ACCESS_COARSE_LOCATION,
//                Manifest.permission.ACCESS_BACKGROUND_LOCATION
//            ), 100
//        )

        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_main
        )

        binding?.lifecycleOwner = this

        StatusBarUtil.setTransparent(this)

        val window = this.window
        when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_VISIBLE
            Configuration.UI_MODE_NIGHT_NO -> window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        //val appDatabase = AppDatabase.getDatabase(this)

        myViewModel = ViewModelProviders.of(this).get(MyViewModel::class.java)

        doLocation()

        amapLocationClient.setLocationListener { aMapLocation ->
            //onLocationChanged 就是如果服务器给客户端返回数据，调用的回调函数
            // aMapLocation 就是服务器给客户端返回的定位数据
            if (aMapLocation != null) {
                //服务器是有响应的
                if (aMapLocation.errorCode == 0) {
                    //定位成功，aMapLocation获取数据
                    cityname = aMapLocation.city

                    //Log.d("CurrentLiang",cityname)
                    cityname = "成都"
                    //var location = MyApplication
                    MyApplication.currentLocation = cityname

                    if (cityname != lastcityname) {
                        myViewModel!!.init(cityname)
                        binding?.viewModel = myViewModel
                        binding?.clickHandler = this
                        val city = binding?.root?.findViewById<TextView>(R.id.city)

                        city?.text = cityname
                        myViewModel!!.repositoryforDaily?.observe(
                            this,
                            Observer<ArrayList<BasicModel>> { t ->

                                val temBasicModel = t[0]
                                t.removeAt(0)
                                val textView = binding?.root?.findViewById<TextView>(R.id.highandlow)
                                textView?.text = "${temBasicModel.high}℃/${temBasicModel.low}℃"

                                binding?.recyclerview?.layoutManager = LinearLayoutManager(this)
                                binding?.recyclerview?.adapter = WeekWeatherAdapter(t)
                                binding?.recyclerview?.setHasFixedSize(false)
                                val initText = binding?.root?.findViewById<TextView>(R.id.initText)
                                if (initText != null) {
                                    initText.visibility = View.GONE
                                }
                            })

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
                    //定位失败，
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

    private fun doLocation() {
        //1 创建一个客户端定位句柄
        cityname = "shanghai"
        amapLocationClient = AMapLocationClient(this)
        //2 给客户端句柄设置一个listenner来处理服务器返回的定位数据

        mLocationOption = AMapLocationClientOption()
        mLocationOption.geoLanguage = AMapLocationClientOption.GeoLanguage.EN
        mLocationOption.interval = 1000
        amapLocationClient.setLocationOption(mLocationOption)
        amapLocationClient.startLocation()
    }

    override fun onClicktoActivity(view: View) {
        val context: Context = view.context
        //Log.d("TestLiang", "Cityname is $cityname")
        val intent: Intent = Intent(context, AddCityActivity::class.java)
        intent.putExtra("cityname", cityname)
        context.startActivity(intent)
    }

    override fun onFinish(view: View) {
    }

    override fun onClickFloatingActionButton(view: View) {
    }

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

//    @TargetApi(30)
//    private fun Context.checkBackgroundLocationPermissionAPI30(backgroundLocationRequestCode: Int) {
//        if (checkSinglePermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION)) return
//        AlertDialog.Builder(this)
//            .setTitle(R.string.background_location_permission_title)
//            .setMessage(R.string.background_location_permission_message)
//            .setPositiveButton(R.string.yes) { _,_ ->
//                // this request will take user to Application's Setting page
//                requestPermissions(arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION), backgroundLocationRequestCode)
//            }
//            .setNegativeButton(R.string.no) { dialog,_ ->
//                dialog.dismiss()
//            }
//            .create()
//            .show()
//
//    }
}