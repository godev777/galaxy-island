package com.galaxy.dynamicisland.service

import android.app.Notification
import android.content.ComponentName
import android.content.Context
import android.media.MediaMetadata
import android.media.session.MediaController
import android.media.session.MediaSessionManager
import android.media.session.PlaybackState
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import com.galaxy.dynamicisland.data.IslandEvent
import com.galaxy.dynamicisland.manager.IslandEventManager
import java.util.regex.Pattern

class IslandNotificationListener : NotificationListenerService() {

    private var mediaSessionManager: MediaSessionManager? = null
    private var activeMediaController: MediaController? = null

    private val otpPattern = Pattern.compile("(?i)(?:otp|kode|code|pin|verifikasi)[^0-9]*([0-9]{4,8})")

    override fun onListenerConnected() {
        super.onListenerConnected()
        initMediaListener()
    }

    override fun onCreate() {
        super.onCreate()
        initMediaListener()
    }

    private fun initMediaListener() {
        try {
            if (mediaSessionManager == null) {
                mediaSessionManager = getSystemService(Context.MEDIA_SESSION_SERVICE) as? MediaSessionManager
                val component = ComponentName(this, IslandNotificationListener::class.java)
                mediaSessionManager?.addOnActiveSessionsChangedListener({ controllers ->
                    handleActiveMediaSessions(controllers)
                }, component)
                handleActiveMediaSessions(mediaSessionManager?.getActiveSessions(component))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)
        if (sbn == null || sbn.packageName == packageName) return

        val notification = sbn.notification ?: return
        val extras = notification.extras ?: return

        // Extract title & text with fallbacks for WhatsApp/Telegram/MessagingStyle
        val title = extras.getCharSequence(Notification.EXTRA_TITLE)?.toString()
            ?: extras.getCharSequence(Notification.EXTRA_TITLE_BIG)?.toString() ?: ""
        val text = extras.getCharSequence(Notification.EXTRA_TEXT)?.toString()
            ?: extras.getCharSequence(Notification.EXTRA_BIG_TEXT)?.toString()
            ?: extras.getCharSequence(Notification.EXTRA_SUB_TEXT)?.toString() ?: ""

        if (title.isBlank() && text.isBlank()) return

        // Ignore non-alerting low priority system background pings
        if (notification.flags and Notification.FLAG_FOREGROUND_SERVICE != 0 &&
            notification.category != Notification.CATEGORY_CALL &&
            notification.category != Notification.CATEGORY_TRANSPORT) {
            // Background service pings
            return
        }

        // 1. Check for Active VoIP or Phone Call
        val isCall = (notification.category == Notification.CATEGORY_CALL) ||
                (notification.flags and Notification.FLAG_ONGOING_EVENT != 0 &&
                        (title.contains("Calling", true) || text.contains("Call", true) || title.contains("Panggilan", true)))

        if (isCall) {
            val callEvent = IslandEvent.Call(
                callerName = if (title.isNotBlank()) title else "Incoming Call",
                appName = getAppLabel(sbn.packageName),
                durationSeconds = 0,
                isMuted = false,
                isVideo = false
            )
            IslandEventManager.setPrimaryEvent(callEvent)
            return
        }

        // 2. Check for OTP (One-Time Password) detection for S24 Ultra
        var isOTP = false
        var otpCode: String? = null
        val fullContent = "$title $text"
        val matcher = otpPattern.matcher(fullContent)
        if (matcher.find()) {
            isOTP = true
            otpCode = matcher.group(1)
        }

        // 3. Normal Notification or OTP Alert (Triggers real-time bouncy heads-up expansion!)
        val notifEvent = IslandEvent.Notification(
            key = sbn.key,
            packageName = sbn.packageName,
            appName = getAppLabel(sbn.packageName),
            title = title,
            text = text,
            isOTP = isOTP,
            otpCode = otpCode,
            groupedCount = 1,
            hasReplyAction = notification.actions?.any { it.remoteInputs?.isNotEmpty() == true } ?: false
        )

        IslandEventManager.setPrimaryEvent(notifEvent, autoCollapseSeconds = if (isOTP) 8 else 5)
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        super.onNotificationRemoved(sbn)
        val current = IslandEventManager.primaryEvent.value
        if (current is IslandEvent.Notification && current.key == sbn?.key) {
            IslandEventManager.collapse()
            IslandEventManager.setPrimaryEvent(null)
        }
    }

    private fun handleActiveMediaSessions(controllers: List<MediaController>?) {
        if (controllers.isNullOrEmpty()) return
        val controller = controllers.firstOrNull() ?: return
        activeMediaController = controller

        val playbackState = controller.playbackState
        val isPlaying = playbackState?.state == PlaybackState.STATE_PLAYING

        val metadata = controller.metadata
        val songTitle = metadata?.getString(MediaMetadata.METADATA_KEY_TITLE) ?: "Now Playing"
        val artist = metadata?.getString(MediaMetadata.METADATA_KEY_ARTIST) ?: "Unknown Artist"
        val artBitmap = metadata?.getBitmap(MediaMetadata.METADATA_KEY_ALBUM_ART)
            ?: metadata?.getBitmap(MediaMetadata.METADATA_KEY_ART)

        if (isPlaying) {
            val mediaEvent = IslandEvent.Media(
                title = songTitle,
                artist = artist,
                albumArt = artBitmap,
                isPlaying = true,
                durationMs = metadata?.getLong(MediaMetadata.METADATA_KEY_DURATION) ?: 0L,
                currentPositionMs = playbackState?.position ?: 0L,
                packageName = controller.packageName
            )
            IslandEventManager.setPrimaryEvent(mediaEvent)
        }
    }

    private fun getAppLabel(pkgName: String): String {
        return try {
            val pm = packageManager
            val appInfo = pm.getApplicationInfo(pkgName, 0)
            pm.getApplicationLabel(appInfo).toString()
        } catch (e: Exception) {
            pkgName
        }
    }
}
