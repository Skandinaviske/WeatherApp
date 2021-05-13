package com.example.myweatherapp.activity

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.DisplayMetrics
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.myweatherapp.R
import java.lang.ref.WeakReference
import java.util.*
import java.util.concurrent.ThreadLocalRandom
import kotlin.random.Random

class BallGameActivity : AppCompatActivity(){
    // 屏幕的宽度
    private var tableWidth = 0f
    // 屏幕的高度
    private var tableHeight = 0f
    // 球拍的垂直位置
    private var racketY = 0f
    // 下面定义球拍的高度和宽度
    private var racketHeight: Float = 0f
    private var racketWidth: Float = 0f
    // 小球的大小
    private var ballSize: Float = 0f
    // 小球纵向的运行速度
    private var ySpeed = 20
    private var rand = Random(1)
    // 返回一个-0.5~0.5的比率，用于控制小球的运行方向
    private val xyRate = rand.nextDouble() - 0.5
    // 小球横向的运行速度
    private var xSpeed = (ySpeed.toDouble() * xyRate * 2.0).toInt()
    // ballX和ballY代表小球的坐标
    private var ballX = rand.nextInt(200) + 20f
    private var ballY = rand.nextInt(10) + 40f
    // racketX代表球拍的水平位置
    private var racketX = rand.nextInt(200).toFloat()
    // 游戏是否结束的旗标
    private var isLose = false
    private lateinit var gameView: GameView
    class MyHandler(private var gameView: WeakReference<BallGameActivity>) : Handler()
    {
        override fun handleMessage(msg: Message)
        {
            if (msg.what == 0x123)
            {
                gameView.get()?.gameView?.invalidate()
            }
        }
    }
    private val handler = MyHandler(WeakReference(this))
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        // 去掉窗口标题
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        racketHeight = resources.getDimension(R.dimen.racket_height)
        racketWidth = resources.getDimension(R.dimen.racket_width)
        ballSize = resources.getDimension(R.dimen.ball_size)
        // 全屏显示
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        // 创建GameView组件
        gameView = GameView(this)
        setContentView(gameView)
        // 获取窗口管理器
        val windowManager = windowManager
        val display = windowManager.defaultDisplay
        val metrics = DisplayMetrics()
        display.getMetrics(metrics)
        // 获得屏幕宽和高
        tableWidth = metrics.widthPixels.toFloat()
        tableHeight = metrics.heightPixels.toFloat()
        racketY = tableHeight - 80
        gameView.setOnTouchListener { _, event ->  // ②
            if (event.x <= tableWidth / 10)
            {
                // 控制挡板左移
                if (racketX > 0) racketX -= 10
            }
            if (event.x >= tableWidth * 9 / 10)
            {
                // 控制挡板右移
                if (racketX < tableWidth - racketWidth) racketX += 10
            }
            true
        }

        val timer = Timer()
        timer.schedule(object : TimerTask()  // ①
        {
            override fun run()
            {
                // 如果小球碰到左边边框
                if (ballX < ballSize || ballX >= tableWidth - ballSize)
                {
                    xSpeed = -xSpeed
                }
                // 如果小球高度超出了球拍位置，且横向不在球拍范围之内，游戏结束
                if (ballY >= racketY - ballSize &&
                    (ballX < racketX || ballX > racketX + racketWidth))
                {
                    timer.cancel()
                    // 设置游戏是否结束的旗标为true
                    isLose = true
                }
                // 如果小球位于球拍之内，且到达球拍位置，小球反弹
                else if (ballY < ballSize || (ballY >= racketY - ballSize &&
                            ballX > racketX && ballX <= racketX + racketWidth))
                {
                    ySpeed = -ySpeed
                }
                // 小球坐标增加
                ballY += ySpeed
                ballX += xSpeed
                // 发送消息，通知系统重绘组件
                handler.sendEmptyMessage(0x123)
            }
        }, 0, 100)
    }
    internal inner class GameView(context: Context) : View(context)
    {
        private var paint = Paint()
        private var mShader = RadialGradient(- ballSize/ 2,
            - ballSize/ 2, ballSize, Color.WHITE, Color.RED, Shader.TileMode.CLAMP)
        init
        {
            isFocusable = true
            paint.style = Paint.Style.FILL
            // 设置去锯齿
            paint.isAntiAlias = true
        }
        // 重写View的onDraw方法，实现绘画
        override fun onDraw(canvas: Canvas)
        {
            // 如果游戏已经结束
            if (isLose)
            {
                paint.color = Color.RED
                paint.textSize = 40f
                canvas.drawText("游戏已结束", tableWidth / 2 - 100, 200f, paint)
            }
            // 如果游戏还未结束
            else
            {
                canvas.save() // 保存坐标系统
                // 将坐标系统平移到小球圆心处来绘制小球
                canvas.translate(ballX, ballY)
                // 设置渐变，并绘制小球
                paint.shader = mShader
                canvas.drawCircle(0f, 0f, ballSize, paint)
                canvas.restore() // 恢复原来的坐标系统
                paint.shader = null
                // 设置颜色，并绘制球拍
                paint.color = Color.rgb(80, 80, 200)
                canvas.drawRect(
                    racketX.toFloat(), racketY, (racketX + racketWidth).toFloat(),
                    racketY + racketHeight, paint)
            }
        }
    }

    fun rand(start: Double, end: Double): Double {
        return ThreadLocalRandom.current().nextDouble(start, end);
    }
}
