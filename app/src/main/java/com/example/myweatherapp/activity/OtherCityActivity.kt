package com.example.myweatherapp.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myweatherapp.R
import com.example.myweatherapp.adapter.HourWeatherAdapter
import com.example.myweatherapp.datamodel.BasicModel
import com.example.myweatherapp.adapter.WeekWeatherAdapter
import com.example.myweatherapp.database.DataModel
import com.example.myweatherapp.databinding.ActivityOthercityBinding
import com.example.myweatherapp.databinding.BottomSheetDialogAirBinding
import com.example.myweatherapp.datamodel.HourDataModel
import com.example.myweatherapp.viewmodel.MyViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.jaeger.library.StatusBarUtil

class OtherCityActivity : OnClickHandlerInterface, AppCompatActivity() {

    private var myViewModel: MyViewModel? = null
    private var binding: ActivityOthercityBinding? = null
    private var cityname: String? = null
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        StatusBarUtil.setTransparent(this)

        val window = this.window
        when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_VISIBLE
            Configuration.UI_MODE_NIGHT_NO -> window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_othercity
        )

        binding?.lifecycleOwner = this

        myViewModel = ViewModelProviders.of(this).get(MyViewModel::class.java)

        //onLocationChanged 就是如果服务器给客户端返回数据，调用的回调函数
        // aMapLocation 就是服务器给客户端返回的定位数据
        //服务器是有响应的
        //定位成功，aMapLocation获取数据
        val intent = intent

        cityname = intent.getStringExtra("cityname")
        if (cityname != null) {
            myViewModel!!.init(cityname!!)
        }
        binding?.viewModel = myViewModel
        binding?.clickHandler = this
        myViewModel!!.repositoryforDaily?.observe(this, Observer<ArrayList<BasicModel>> { t ->

            val temBasicModel = t[0]
            t.removeAt(0)
            val textView = binding?.root?.findViewById<TextView>(R.id.highandlow)
            textView?.text = "${temBasicModel.high}℃/${temBasicModel.low}℃"

            binding?.recyclerview?.layoutManager = LinearLayoutManager(this)
            binding?.recyclerview?.adapter = WeekWeatherAdapter(t)
            binding?.recyclerview?.setHasFixedSize(false)
            val initProgressBar = binding?.root?.findViewById<ProgressBar>(R.id.progressbar)
            if (initProgressBar != null) {
                initProgressBar.visibility = View.GONE
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

        val temperature = myViewModel!!.repositoryforNow?.value?.get(0)?.toInt()
        val type = myViewModel!!.repositoryforNow?.value?.get(2)
        val cityCN = myViewModel!!.repositoryforNow?.value?.get(1)
            if (cityname != null&&temperature != null&&type != null&& cityCN != null) {
                val dataModel = DataModel(cityname!!, temperature, type)
                myViewModel!!.updateItemsforDatabase(cityname!!, dataModel)
            }
    }

    override fun onClicktoActivity(view: View) {
        val context: Context = view.context
        val intent = Intent(context, AddCityActivity::class.java)
        intent.putExtra("temperature",myViewModel!!.repositoryforNow?.value?.get(0))
        intent.putExtra("weathertype",myViewModel!!.repositoryforNow?.value?.get(2))
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
}