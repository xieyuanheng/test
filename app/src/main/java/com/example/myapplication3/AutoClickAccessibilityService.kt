package com.example.myapplication3

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.os.Handler
import android.os.Looper
import android.view.accessibility.AccessibilityEvent

class AutoClickAccessibilityService : AccessibilityService() {
    private val handler = Handler(Looper.getMainLooper())
    private var isRunning = false
    private val clickRunnable = object : Runnable {
        override fun run() {
            if (!isRunning) return
            performClick()
            handler.postDelayed(this, CLICK_INTERVAL_MS)
        }
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        instance = this
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // No-op
    }

    override fun onInterrupt() {
        stopClicking()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopClicking()
        if (instance === this) {
            instance = null
        }
    }

    private fun performClick() {
        val centerX = (CLICK_BOUNDS_LEFT + CLICK_BOUNDS_RIGHT) / 2f
        val centerY = (CLICK_BOUNDS_TOP + CLICK_BOUNDS_BOTTOM) / 2f
        val path = Path().apply {
            moveTo(centerX, centerY)
        }
        val gesture = GestureDescription.Builder()
            .addStroke(GestureDescription.StrokeDescription(path, 0, CLICK_DURATION_MS))
            .build()
        dispatchGesture(gesture, null, null)
    }

    private fun startClicking() {
        if (isRunning) return
        isRunning = true
        handler.post(clickRunnable)
    }

    private fun stopClicking() {
        isRunning = false
        handler.removeCallbacks(clickRunnable)
    }

    companion object {
        private const val CLICK_BOUNDS_LEFT = 423f
        private const val CLICK_BOUNDS_TOP = 167f
        private const val CLICK_BOUNDS_RIGHT = 802f
        private const val CLICK_BOUNDS_BOTTOM = 247f
        private const val CLICK_INTERVAL_MS = 1000L
        private const val CLICK_DURATION_MS = 50L

        @Volatile
        private var instance: AutoClickAccessibilityService? = null

        fun startAutoClicking() {
            instance?.startClicking()
        }

        fun stopAutoClicking() {
            instance?.stopClicking()
        }
    }
}
