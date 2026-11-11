package com.galaxy.dynamicisland.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.os.BatteryManager
import com.galaxy.dynamicisland.data.IslandEvent
import com.galaxy.dynamicisland.data.IslandPreferences
import com.galaxy.dynamicisland.manager.IslandEventManager

class SystemEventReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        val action = intent?.action ?: return
        val preferences = IslandPreferences(context)

        when (action) {
            Intent.ACTION_BOOT_COMPLETED -> {
                if (preferences.isIslandEnabled.value) {
                    IslandOverlayService.start(context)
                }
            }

            Intent.ACTION_POWER_CONNECTED, Intent.ACTION_BATTERY_CHANGED -> {
                if (!preferences.isCharging45WVisualizerEnabled.value) return

                val status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
                val isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                        status == BatteryManager.BATTERY_STATUS_FULL

                if (isCharging) {
                    val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
                    val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
                    val batteryPct = if (level != -1 && scale > 0) ((level / scale.toFloat()) * 100).toInt() else 80

                    val plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1)
                    val isAcCharging = plugged == BatteryManager.BATTERY_PLUGGED_AC

                    // S24 Ultra Super Fast Charging 2.0 (45W) visualizer
                    val event = IslandEvent.Charging(
                        batteryPercentage = batteryPct,
                        isPlugged = true,
                        isSuperFast2_0 = isAcCharging, // Samsung 45W protocol
                        isFastCharging = true,
                        wattageEstimate = if (isAcCharging) 45 else 15,
                        minutesToFull = (100 - batteryPct) * 40 / 100
                    )
                    IslandEventManager.setPrimaryEvent(event, autoCollapseSeconds = 5)
                }
            }

            AudioManager.RINGER_MODE_CHANGED_ACTION -> {
                val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
                val (modeTitle, icon) = when (audioManager.ringerMode) {
                    AudioManager.RINGER_MODE_SILENT -> "Silent Mode" to "silent"
                    AudioManager.RINGER_MODE_VIBRATE -> "Vibrate Mode" to "vibrate"
                    else -> "Ring Mode" to "ring"
                }

                val event = IslandEvent.SystemToast(
                    title = modeTitle,
                    subtitle = "Profile updated",
                    iconType = icon
                )
                IslandEventManager.setPrimaryEvent(event, autoCollapseSeconds = 3)
            }

            Intent.ACTION_HEADSET_PLUG -> {
                val state = intent.getIntExtra("state", -1)
                if (state == 1) {
                    val event = IslandEvent.SystemToast(
                        title = "Headphones Connected",
                        subtitle = "Audio routed to headset",
                        iconType = "headphones"
                    )
                    IslandEventManager.setPrimaryEvent(event, autoCollapseSeconds = 3)
                }
            }
        }
    }
}
