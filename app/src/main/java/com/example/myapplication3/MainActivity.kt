package com.example.myapplication3

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MainScreen(
                        onRequestOverlay = { requestOverlayPermission() },
                        onOpenAccessibility = { openAccessibilitySettings() },
                        onShowFloating = { startService(Intent(this, FloatingControlService::class.java)) },
                        onHideFloating = { stopService(Intent(this, FloatingControlService::class.java)) }
                    )
                }
            }
        }
    }

    private fun requestOverlayPermission() {
        val intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:$packageName")
        )
        startActivity(intent)
    }

    private fun openAccessibilitySettings() {
        startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
    }
}

@Composable
private fun MainScreen(
    onRequestOverlay: () -> Unit,
    onOpenAccessibility: () -> Unit,
    onShowFloating: () -> Unit,
    onHideFloating: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "请先开启无障碍服务和悬浮窗权限，再显示悬浮控制框。")
        Button(onClick = onRequestOverlay) {
            Text(text = "开启悬浮窗权限")
        }
        Button(onClick = onOpenAccessibility) {
            Text(text = "开启无障碍服务")
        }
        Button(onClick = onShowFloating) {
            Text(text = "显示悬浮控制框")
        }
        Button(onClick = onHideFloating) {
            Text(text = "隐藏悬浮控制框")
        }
    }
}
