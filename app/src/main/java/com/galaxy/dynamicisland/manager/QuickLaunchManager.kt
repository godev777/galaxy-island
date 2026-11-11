package com.galaxy.dynamicisland.manager

import android.content.Context
import android.content.Intent
import android.hardware.camera2.CameraManager
import android.provider.Settings
import com.galaxy.dynamicisland.service.IslandAccessibilityService

class QuickLaunchManager(private val context: Context) {

    private var isTorchOn = false
    private val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as? CameraManager

    fun toggleTorch(): Boolean {
        try {
            val cameraId = cameraManager?.cameraIdList?.firstOrNull() ?: return false
            isTorchOn = !isTorchOn
            cameraManager.setTorchMode(cameraId, isTorchOn)
            return isTorchOn
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    fun openCalculator() {
        val intents = listOf(
            Intent().setClassName("com.sec.android.app.popupcalculator", "com.sec.android.app.popupcalculator.Calculator"), // Samsung Calculator
            Intent().setClassName("com.google.android.calculator", "com.android.calculator2.Calculator"),
            Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_APP_CALCULATOR)
        )
        for (intent in intents) {
            try {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
                return
            } catch (ignored: Exception) {}
        }
    }

    fun openClock() {
        try {
            val intent = Intent(Settings.ACTION_DATE_SETTINGS).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (ignored: Exception) {}
    }

    fun triggerNotificationShade() {
        IslandAccessibilityService.instance?.triggerNotificationShade()
    }

    fun triggerQuickSettings() {
        IslandAccessibilityService.instance?.triggerQuickSettings()
    }

    fun triggerGlobalBack() {
        IslandAccessibilityService.instance?.triggerGlobalBack()
    }

    fun triggerGlobalHome() {
        IslandAccessibilityService.instance?.triggerGlobalHome()
    }
}
