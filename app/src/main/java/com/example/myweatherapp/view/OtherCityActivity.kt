package com.example.myweatherapp.view

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myweatherapp.R
import com.example.myweatherapp.datamodel.BasicModel
import com.example.myweatherapp.adapter.WeekWeatherAdapter
import com.example.myweatherapp.databinding.ActivityOthercityBinding
import com.example.myweatherapp.viewmodel.MyViewModel
import com.jaeger.library.StatusBarUtil

class OtherCityActivity : OnClickHandlerInterface, AppCompatActivity() {

    private var myViewModel: MyViewModel? = null
    private var binding: ActivityOthercityBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_othercity
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

        //onLocationChanged 就是如果服务器给客户端返回数据，调用的回调函数
        // aMapLocation 就是服务器给客户端返回的定位数据
        //服务器是有响应的
        //定位成功，aMapLocation获取数据
        val intent = intent

        val cityname = intent.getStringExtra("cityname")
        if (cityname != null) {
            myViewModel!!.init(cityname)
        }
        binding?.viewModel = myViewModel
        binding?.clickHandler = this
        myViewModel!!.repositoryforDaily?.observe(this, Observer<ArrayList<BasicModel>> { t ->
            binding?.recyclerview?.layoutManager = LinearLayoutManager(this)
            binding?.recyclerview?.adapter = WeekWeatherAdapter(t)
        })
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
    }

}