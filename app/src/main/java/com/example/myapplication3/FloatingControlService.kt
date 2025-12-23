package com.example.myapplication3

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.IBinder
import android.view.Gravity
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast

class FloatingControlService : Service() {
    private var windowManager: WindowManager? = null
    private var controlView: LinearLayout? = null

    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        controlView = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(16, 16, 16, 16)
            setBackgroundColor(0xAA000000.toInt())

            val startButton = Button(context).apply {
                text = "开始"
                setOnClickListener {
                    AutoClickAccessibilityService.startAutoClicking()
                    Toast.makeText(context, "已开始自动点击", Toast.LENGTH_SHORT).show()
                }
            }
            val stopButton = Button(context).apply {
                text = "停止"
                setOnClickListener {
                    AutoClickAccessibilityService.stopAutoClicking()
                    Toast.makeText(context, "已停止自动点击", Toast.LENGTH_SHORT).show()
                }
            }
            addView(startButton)
            addView(stopButton)
        }

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
            x = 50
            y = 200
        }
        windowManager?.addView(controlView, params)
    }

    override fun onDestroy() {
        super.onDestroy()
        controlView?.let { windowManager?.removeView(it) }
        controlView = null
        windowManager = null
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
