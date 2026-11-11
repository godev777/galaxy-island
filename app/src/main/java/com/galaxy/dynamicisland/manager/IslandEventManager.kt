package com.galaxy.dynamicisland.manager

import com.galaxy.dynamicisland.data.CompanionBubbleData
import com.galaxy.dynamicisland.data.IslandEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

object IslandEventManager {

    private val scope = CoroutineScope(Dispatchers.Main)
    private var autoCollapseJob: Job? = null

    // Current primary activity displayed on the main pill
    private val _primaryEvent = MutableStateFlow<IslandEvent?>(null)
    val primaryEvent: StateFlow<IslandEvent?> = _primaryEvent.asStateFlow()

    // Secondary activity displayed on the Companion Bubble
    private val _companionBubble = MutableStateFlow<CompanionBubbleData?>(null)
    val companionBubble: StateFlow<CompanionBubbleData?> = _companionBubble.asStateFlow()

    // Inline reply draft cache: Key = notificationKey, Value = text
    private val replyDrafts = mutableMapOf<String, String>()

    // Whether Island is expanded into a rich card
    private val _isExpanded = MutableStateFlow(false)
    val isExpanded: StateFlow<Boolean> = _isExpanded.asStateFlow()

    // Whether Island is currently doing a Heads-Up pop/stretch animation
    private val _isHeadsUpPulse = MutableStateFlow(false)
    val isHeadsUpPulse: StateFlow<Boolean> = _isHeadsUpPulse.asStateFlow()

    // Face Unlock / Screen unlock pulse animation trigger
    private val _isUnlockAnimationActive = MutableStateFlow(false)
    val isUnlockAnimationActive: StateFlow<Boolean> = _isUnlockAnimationActive.asStateFlow()

    fun setPrimaryEvent(event: IslandEvent?, autoCollapseSeconds: Long? = null) {
        autoCollapseJob?.cancel()
        _primaryEvent.value = event

        if (event != null) {
            // Trigger dynamic bouncy heads-up expansion on arrival!
            _isHeadsUpPulse.value = true

            if (autoCollapseSeconds != null) {
                autoCollapseJob = scope.launch {
                    delay(autoCollapseSeconds * 1000)
                    _isHeadsUpPulse.value = false
                    if (!_isExpanded.value) {
                        // After heads up duration, if it wasn't media or call, clear event
                        if (event is IslandEvent.Notification || event is IslandEvent.Charging || event is IslandEvent.SystemToast) {
                            _primaryEvent.value = null
                        }
                    }
                }
            }
        } else {
            _isHeadsUpPulse.value = false
            _isExpanded.value = false
        }
    }

    fun setCompanionBubble(bubble: CompanionBubbleData?) {
        _companionBubble.value = bubble
    }

    fun expand() {
        _isExpanded.value = true
        _isHeadsUpPulse.value = false
    }

    fun collapse() {
        _isExpanded.value = false
        _isHeadsUpPulse.value = false
    }

    fun toggleExpand() {
        if (_isExpanded.value) {
            collapse()
        } else {
            expand()
        }
    }

    fun triggerUnlockAnimation() {
        scope.launch {
            _isUnlockAnimationActive.value = true
            delay(1200)
            _isUnlockAnimationActive.value = false
        }
    }

    // Draft persistence for inline reply
    fun saveDraft(key: String, draft: String) {
        replyDrafts[key] = draft
    }

    fun getDraft(key: String): String {
        return replyDrafts[key] ?: ""
    }

    fun clearDraft(key: String) {
        replyDrafts.remove(key)
    }

    // Interactive Test Triggers (for instant testing inside app)
    fun simulateS24UltraSuperFastCharging() {
        val event = IslandEvent.Charging(
            batteryPercentage = 78,
            isPlugged = true,
            isSuperFast2_0 = true, // 45W Samsung Protocol
            isFastCharging = true,
            wattageEstimate = 45,
            minutesToFull = 22
        )
        setPrimaryEvent(event, autoCollapseSeconds = 5)
    }

    fun simulateMusicPlayback() {
        val event = IslandEvent.Media(
            title = "Starboy",
            artist = "The Weeknd ft. Daft Punk",
            isPlaying = true,
            durationMs = 230000,
            currentPositionMs = 85000,
            packageName = "com.spotify.music"
        )
        setPrimaryEvent(event)
        // Also add a companion bubble for testing dual-island mode!
        setCompanionBubble(
            CompanionBubbleData(
                title = "03:45",
                iconType = "timer",
                badgeNumber = 1
            )
        )
    }

    fun simulateWhatsAppOTP() {
        val event = IslandEvent.Notification(
            key = "wa_otp_1",
            packageName = "com.whatsapp",
            appName = "WhatsApp",
            title = "Bank BNI",
            text = "JANGAN BERIKAN KEPADA SIAPAPUN. Kode OTP transfer Anda adalah 839210. Berlaku 5 menit.",
            isOTP = true,
            otpCode = "839210",
            groupedCount = 1,
            hasReplyAction = false
        )
        setPrimaryEvent(event, autoCollapseSeconds = 8)
    }

    fun simulateVoIPCall() {
        val event = IslandEvent.Call(
            callerName = "Sarah Jenkins",
            appName = "WhatsApp Call",
            durationSeconds = 142,
            isMuted = false,
            isVideo = false
        )
        setPrimaryEvent(event)
    }

    fun clearAll() {
        autoCollapseJob?.cancel()
        _primaryEvent.value = null
        _companionBubble.value = null
        _isExpanded.value = false
        _isHeadsUpPulse.value = false
    }
}
