package com.galaxy.dynamicisland.service

import android.app.Notification
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.galaxy.dynamicisland.IslandApp
import com.galaxy.dynamicisland.R
import com.galaxy.dynamicisland.data.IslandPreferences
import com.galaxy.dynamicisland.manager.IslandEventManager
import com.galaxy.dynamicisland.ui.island.DynamicIslandRootView
import com.galaxy.dynamicisland.ui.theme.GalaxyIslandTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class IslandOverlayService : LifecycleService(), SavedStateRegistryOwner, ViewModelStoreOwner {

    private val savedStateRegistryController = SavedStateRegistryController.create(this)
    private val vStore = ViewModelStore()
    private val serviceScope = CoroutineScope(Dispatchers.Main + Job())

    override val savedStateRegistry: SavedStateRegistry
        get() = savedStateRegistryController.savedStateRegistry

    override val viewModelStore: ViewModelStore
        get() = vStore

    private lateinit var windowManager: WindowManager
    private var overlayView: View? = null
    private lateinit var layoutParams: WindowManager.LayoutParams
    private lateinit var preferences: IslandPreferences

    override fun onCreate() {
        super.onCreate()
        savedStateRegistryController.performRestore(null)
        preferences = IslandPreferences(this)
        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager

        startForeground(NOTIFICATION_ID, createForegroundNotification())
        setupOverlay()
        observeSettings()
    }

    private fun createForegroundNotification(): Notification {
        return NotificationCompat.Builder(this, IslandApp.CHANNEL_ID)
            .setContentTitle(getString(R.string.notification_active_title))
            .setContentText(getString(R.string.notification_active_text))
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)
            .build()
    }

    private fun setupOverlay() {
        if (overlayView != null) return

        val overlayType = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            @Suppress("DEPRECATION")
            WindowManager.LayoutParams.TYPE_PHONE
        }

        layoutParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            overlayType,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
            x = preferences.offsetX.value
            y = dpToPx(preferences.offsetY.value)
        }

        val composeView = ComposeView(this).apply {
            setViewTreeLifecycleOwner(this@IslandOverlayService)
            setViewTreeSavedStateRegistryOwner(this@IslandOverlayService)
            setViewTreeViewModelStoreOwner(this@IslandOverlayService)
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnDetachedFromWindow)

            setContent {
                GalaxyIslandTheme {
                    DynamicIslandRootView(
                        preferences = preferences,
                        onUpdateWindowFocus = { canFocus ->
                            updateWindowFocusable(canFocus)
                        }
                    )
                }
            }

            // S-Pen Hover Listener for Samsung Galaxy S24 Ultra
            setOnGenericMotionListener { _, event ->
                if (preferences.isSPenHoverEnabled.value && event.isFromSource(MotionEvent.TOOL_TYPE_STYLUS)) {
                    when (event.action) {
                        MotionEvent.ACTION_HOVER_ENTER -> {
                            // Expand preview slightly when S-Pen hovers over the island
                            if (!IslandEventManager.isExpanded.value) {
                                IslandEventManager.expand()
                            }
                            true
                        }
                        MotionEvent.ACTION_HOVER_EXIT -> {
                            true
                        }
                        else -> false
                    }
                } else {
                    false
                }
            }
        }

        overlayView = composeView
        try {
            windowManager.addView(overlayView, layoutParams)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun observeSettings() {
        serviceScope.launch {
            preferences.offsetY.collectLatest { yDp ->
                layoutParams.y = dpToPx(yDp)
                updateOverlayLayout()
            }
        }

        serviceScope.launch {
            preferences.offsetX.collectLatest { xDp ->
                layoutParams.x = dpToPx(xDp)
                updateOverlayLayout()
            }
        }

        serviceScope.launch {
            preferences.isIslandEnabled.collectLatest { enabled ->
                overlayView?.visibility = if (enabled) View.VISIBLE else View.GONE
            }
        }
    }

    private fun updateWindowFocusable(canFocus: Boolean) {
        if (overlayView == null) return
        if (canFocus) {
            layoutParams.flags = layoutParams.flags and WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE.inv()
        } else {
            layoutParams.flags = layoutParams.flags or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        }
        try {
            windowManager.updateViewLayout(overlayView, layoutParams)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun updateOverlayLayout() {
        if (overlayView != null && overlayView?.isAttachedToWindow == true) {
            try {
                windowManager.updateViewLayout(overlayView, layoutParams)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun dpToPx(dp: Int): Int {
        val density = resources.displayMetrics.density
        return (dp * density).toInt()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (overlayView != null) {
            try {
                windowManager.removeView(overlayView)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            overlayView = null
        }
        vStore.clear()
    }

    companion object {
        private const val NOTIFICATION_ID = 1001

        fun start(context: Context) {
            val intent = Intent(context, IslandOverlayService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }

        fun stop(context: Context) {
            val intent = Intent(context, IslandOverlayService::class.java)
            context.stopService(intent)
        }
    }
}
