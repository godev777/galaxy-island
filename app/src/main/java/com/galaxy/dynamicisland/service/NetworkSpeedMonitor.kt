package com.galaxy.dynamicisland.service

import android.net.TrafficStats
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

object NetworkSpeedMonitor {

    private val scope = CoroutineScope(Dispatchers.IO + Job())
    private var job: Job? = null

    private val _currentSpeedText = MutableStateFlow("")
    val currentSpeedText: StateFlow<String> = _currentSpeedText.asStateFlow()

    private val _isTrafficActive = MutableStateFlow(false)
    val isTrafficActive: StateFlow<Boolean> = _isTrafficActive.asStateFlow()

    private var lastRxBytes: Long = 0
    private var lastTxBytes: Long = 0
    private var lastTimestamp: Long = 0

    fun startMonitoring(thresholdKbps: Int = 50) {
        if (job?.isActive == true) return

        lastRxBytes = TrafficStats.getTotalRxBytes()
        lastTxBytes = TrafficStats.getTotalTxBytes()
        lastTimestamp = System.currentTimeMillis()

        job = scope.launch {
            while (isActive) {
                delay(1000)
                val currentRx = TrafficStats.getTotalRxBytes()
                val currentTx = TrafficStats.getTotalTxBytes()
                val now = System.currentTimeMillis()

                val timeDiffSec = (now - lastTimestamp) / 1000.0
                if (timeDiffSec <= 0) continue

                val rxSpeedBytesPerSec = ((currentRx - lastRxBytes) / timeDiffSec).toLong()
                val txSpeedBytesPerSec = ((currentTx - lastTxBytes) / timeDiffSec).toLong()
                val totalSpeedBytes = rxSpeedBytesPerSec + txSpeedBytesPerSec
                val totalSpeedKbps = totalSpeedBytes / 1024

                lastRxBytes = currentRx
                lastTxBytes = currentTx
                lastTimestamp = now

                if (totalSpeedKbps >= thresholdKbps) {
                    _isTrafficActive.value = true
                    _currentSpeedText.value = formatSpeed(totalSpeedBytes)
                } else {
                    _isTrafficActive.value = false
                    _currentSpeedText.value = ""
                }
            }
        }
    }

    fun stopMonitoring() {
        job?.cancel()
        job = null
        _isTrafficActive.value = false
        _currentSpeedText.value = ""
    }

    private fun formatSpeed(bytesPerSec: Long): String {
        val kb = bytesPerSec / 1024.0
        val mb = kb / 1024.0
        return when {
            mb >= 1.0 -> String.format("%.1f MB/s", mb)
            else -> String.format("%d KB/s", kb.toInt())
        }
    }
}
