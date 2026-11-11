package com.galaxy.dynamicisland.service

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.view.accessibility.AccessibilityEvent

class IslandAccessibilityService : AccessibilityService() {

    override fun onServiceConnected() {
        super.onServiceConnected()
        instance = this
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // Listening for window state changes if needed
    }

    override fun onInterrupt() {
        instance = null
    }

    override fun onDestroy() {
        super.onDestroy()
        instance = null
    }

    fun triggerGlobalBack() {
        performGlobalAction(GLOBAL_ACTION_BACK)
    }

    fun triggerGlobalHome() {
        performGlobalAction(GLOBAL_ACTION_HOME)
    }

    fun triggerNotificationShade() {
        performGlobalAction(GLOBAL_ACTION_NOTIFICATIONS)
    }

    fun triggerQuickSettings() {
        performGlobalAction(GLOBAL_ACTION_QUICK_SETTINGS)
    }

    fun triggerRecentApps() {
        performGlobalAction(GLOBAL_ACTION_RECENTS)
    }

    companion object {
        var instance: IslandAccessibilityService? = null
            private set

        val isRunning: Boolean
            get() = instance != null
    }
}
