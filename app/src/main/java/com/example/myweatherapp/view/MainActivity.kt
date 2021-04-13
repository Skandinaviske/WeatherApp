package com.example.myweatherapp.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.example.myweatherapp.R
import com.example.myweatherapp.datamodel.BasicModel
import com.example.myweatherapp.adapter.WeekWeatherAdapter
import com.example.myweatherapp.application.MyApplication
import com.example.myweatherapp.databinding.ActivityMainBinding
import com.example.myweatherapp.viewmodel.MyViewModel
import com.jaeger.library.StatusBarUtil


class MainActivity : OnClickHandlerInterface, AppCompatActivity() {
    private var myViewModel: MyViewModel? = null
    private var binding: ActivityMainBinding? = null
    lateinit var amapLocationClient: AMapLocationClient
    lateinit var mLocationOption: AMapLocationClientOption
    private var cityname: String = "shanghai"
    private var lastcityname: String = ""

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_BACKGROUND_LOCATION), 100)

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
                    cityname = aMapLocation.city.toLowerCase()

                    //var location = MyApplication
                    MyApplication.currentLocation = cityname

                    if (cityname != lastcityname) {
                        myViewModel!!.init(cityname)
                        binding?.viewModel = myViewModel
                        binding?.clickHandler = this
                        myViewModel!!.repositoryforDaily?.observe(this, Observer<ArrayList<BasicModel>>{
                                t ->
                            binding?.recyclerview?.layoutManager = LinearLayoutManager(this)
                            binding?.recyclerview?.adapter = WeekWeatherAdapter(t)
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
        mLocationOption.interval = 10000
        amapLocationClient.setLocationOption(mLocationOption)
        amapLocationClient.startLocation()
    }

    override fun onClicktoActivity(view: View, cityname: String) {
        val context: Context = view.context
        Log.d("TestLiang", "Cityname is $cityname")
        val intent: Intent = Intent(context, AddCityActivity::class.java)
        intent.putExtra("cityname", cityname)
        context.startActivity(intent)
    }

    override fun onFinish(view: View) {
    }

    override fun onClickFloatingActionButton(view: View) {
    }

    override fun showCheckBox(view: View) {
        TODO("Not yet implemented")
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