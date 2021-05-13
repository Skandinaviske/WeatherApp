package com.example.myweatherapp.activity

import android.graphics.Point
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.animation.TranslateAnimation
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.myweatherapp.R
import java.lang.ref.WeakReference
import java.util.*

class ButterflyActivity : AppCompatActivity() {
    // 记录蝴蝶ImageView当前的位置
    private var curX = 0f
    private var curY = 0f

    // 记录蝴蝶ImageView下一个位置的坐标
    private var nextX = 0f
    private var nextY = 0f
    private lateinit var imageView: ImageView
    private lateinit var startbtn: Button
    private var screenWidth = 0f
    private var screenHeight = 0f
    private var isFlying = false

    class MyHandler(private var activity: WeakReference<ButterflyActivity>) : Handler() {
        override fun handleMessage(msg: Message) {
            val act = activity.get()!!
            if (activity.get() != null && msg.what == 0x123) {
                // 横向上一直向右飞
                if (act.nextX > act.screenWidth) {
                    act.nextX = 0f
                    act.curX = act.nextX
                } else {
                    act.nextX += (0..15).random().toFloat()
                }
                // 纵向上可以随机上下
                if (act.nextY > act.screenHeight) {
                    act.nextY = 0f
                    act.curY = act.nextY
                } else {
                    act.nextY += (0..15).random().toFloat()  //10f
                }
                // 设置显示蝴蝶的ImageView发生位移改变
                val anim = TranslateAnimation(act.curX, act.nextX, act.curY, act.nextY)
                act.curX = act.nextX
                act.curY = act.nextY
                anim.duration = 200
                // 开始位移动画
                act.imageView.startAnimation(anim)  // ①
            }
//            else if (activity.get() != null && msg.what == 0x1234){
//                act.imageView.animation.cancel()
//                act.imageView.clearAnimation()
//            }
        }
    }

    private val handler = MyHandler(WeakReference(this))
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_butterfly)
        val p = Point()
        // 获取屏幕宽度
        windowManager.defaultDisplay.getSize(p)
        screenWidth = p.x.toFloat()
        screenHeight = p.y.toFloat()
        // 获取显示蝴蝶的ImageView组件
        imageView = findViewById(R.id.butterfly)
        val butterfly = imageView.background as AnimationDrawable
        var timer = Timer()
        startbtn = findViewById(R.id.startAnimation)
        startbtn.setOnClickListener {
            // 开始播放蝴蝶振翅的逐帧动画
            if (!isFlying) {
                butterfly.start()  // ②
                // 通过定时器控制每0.2秒运行一次TranslateAnimation动画
                timer.schedule(object : TimerTask() {
                    override fun run() {
                        handler.sendEmptyMessage(0x123)
                    }
                }, 0, 20)
                startbtn.text = "结束"
            } else {
                butterfly.stop()
                timer.cancel()
                timer.purge()
                timer = Timer()
                startbtn.text = "开始"
            }
            isFlying = !isFlying
        }
    }
}