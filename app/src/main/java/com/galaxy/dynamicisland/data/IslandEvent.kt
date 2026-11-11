package com.galaxy.dynamicisland.data

import android.graphics.Bitmap
import android.graphics.drawable.Icon

sealed class IslandEvent {
    // Music / Media Player
    data class Media(
        val title: String,
        val artist: String,
        val albumArt: Bitmap? = null,
        val isPlaying: Boolean = true,
        val durationMs: Long = 0,
        val currentPositionMs: Long = 0,
        val packageName: String = ""
    ) : IslandEvent()

    // VoIP & Phone Calls
    data class Call(
        val callerName: String,
        val appName: String,
        val durationSeconds: Long = 0,
        val isMuted: Boolean = false,
        val isVideo: Boolean = false,
        val avatar: Bitmap? = null
    ) : IslandEvent()

    // Notifications & Messages
    data class Notification(
        val key: String,
        val packageName: String,
        val appName: String,
        val title: String,
        val text: String,
        val timestamp: Long = System.currentTimeMillis(),
        val largeIcon: Bitmap? = null,
        val appIcon: Icon? = null,
        val isOTP: Boolean = false,
        val otpCode: String? = null,
        val groupedCount: Int = 1,
        val hasReplyAction: Boolean = false
    ) : IslandEvent()

    // Samsung Super Fast Charging 2.0 (45W) & Battery
    data class Charging(
        val batteryPercentage: Int,
        val isPlugged: Boolean,
        val isSuperFast2_0: Boolean = false, // 45W Samsung protocol
        val isFastCharging: Boolean = false,
        val wattageEstimate: Int = 45,
        val minutesToFull: Int = 35
    ) : IslandEvent()

    // Clock App Timers & Stopwatches
    data class Timer(
        val label: String,
        val remainingSeconds: Long,
        val totalSeconds: Long,
        val isRunning: Boolean = true
    ) : IslandEvent()

    // Turn-by-Turn Navigation (Google Maps, Waze)
    data class Navigation(
        val nextTurnManeuver: String, // e.g. "Turn right on Jl. Sudirman"
        val distanceLeft: String,     // e.g. "400 m"
        val eta: String               // e.g. "12 min"
    ) : IslandEvent()

    // Food Delivery & Ride Hailing (Grab, Gojek, Uber)
    data class RideDelivery(
        val providerName: String,
        val statusText: String,
        val progressPercent: Float,
        val driverName: String? = null,
        val vehiclePlate: String? = null
    ) : IslandEvent()

    // File Downloads & Transfers
    data class FileProgress(
        val fileName: String,
        val progressPercent: Int,
        val speedText: String,
        val timeLeftText: String
    ) : IslandEvent()

    // Generic Quick System Flash (e.g. Ringer mode, Flashlight, Bluetooth connected)
    data class SystemToast(
        val title: String,
        val subtitle: String,
        val iconType: String
    ) : IslandEvent()
}

// Model for the secondary "Companion Bubble" (when 2 activities are active simultaneously)
data class CompanionBubbleData(
    val title: String,
    val iconType: String,
    val badgeNumber: Int = 0,
    val onClickAction: () -> Unit = {}
)
