package com.example.myapplication3

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Path
import kotlin.random.Random
import android.view.accessibility.AccessibilityEvent

class AutoClickAccessibilityService : AccessibilityService() {
    private var isRunning = false

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
        val targetX = Random.nextFloat() * (CLICK_BOUNDS_RIGHT - CLICK_BOUNDS_LEFT) + CLICK_BOUNDS_LEFT
        val targetY = Random.nextFloat() * (CLICK_BOUNDS_BOTTOM - CLICK_BOUNDS_TOP) + CLICK_BOUNDS_TOP
        val path = Path().apply {
            moveTo(targetX, targetY)
        }
        val gesture = GestureDescription.Builder()
            .addStroke(GestureDescription.StrokeDescription(path, 0, CLICK_DURATION_MS))
            .build()
        dispatchGesture(gesture, null, null)
    }

    private fun startClicking() {
        if (isRunning) return
        isRunning = true
        performClick()
        isRunning = false
    }

    private fun stopClicking() {
        isRunning = false
    }

    companion object {
        private const val CLICK_BOUNDS_LEFT = 423f
        private const val CLICK_BOUNDS_TOP = 167f
        private const val CLICK_BOUNDS_RIGHT = 802f
        private const val CLICK_BOUNDS_BOTTOM = 247f
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
