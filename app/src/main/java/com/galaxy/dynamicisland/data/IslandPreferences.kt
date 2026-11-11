package com.galaxy.dynamicisland.data

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class CompanionSide {
    RIGHT,
    LEFT
}

class IslandPreferences(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("galaxy_island_settings", Context.MODE_PRIVATE)

    // Observable states for Compose UI
    private val _isIslandEnabled = MutableStateFlow(prefs.getBoolean(KEY_ISLAND_ENABLED, true))
    val isIslandEnabled: StateFlow<Boolean> = _isIslandEnabled.asStateFlow()

    private val _offsetX = MutableStateFlow(prefs.getInt(KEY_OFFSET_X, CutoutPreset.S24_ULTRA.offsetX))
    val offsetX: StateFlow<Int> = _offsetX.asStateFlow()

    private val _offsetY = MutableStateFlow(prefs.getInt(KEY_OFFSET_Y, CutoutPreset.S24_ULTRA.offsetY))
    val offsetY: StateFlow<Int> = _offsetY.asStateFlow()

    private val _pillWidth = MutableStateFlow(prefs.getInt(KEY_PILL_WIDTH, CutoutPreset.S24_ULTRA.collapsedWidth))
    val pillWidth: StateFlow<Int> = _pillWidth.asStateFlow()

    private val _pillHeight = MutableStateFlow(prefs.getInt(KEY_PILL_HEIGHT, CutoutPreset.S24_ULTRA.collapsedHeight))
    val pillHeight: StateFlow<Int> = _pillHeight.asStateFlow()

    private val _cornerRadius = MutableStateFlow(prefs.getInt(KEY_CORNER_RADIUS, CutoutPreset.S24_ULTRA.cornerRadius))
    val cornerRadius: StateFlow<Int> = _cornerRadius.asStateFlow()

    // Companion Bubble (The small side bubble)
    private val _isCompanionBubbleEnabled = MutableStateFlow(prefs.getBoolean(KEY_COMPANION_ENABLED, true))
    val isCompanionBubbleEnabled: StateFlow<Boolean> = _isCompanionBubbleEnabled.asStateFlow()

    private val _companionSide = MutableStateFlow(
        CompanionSide.valueOf(prefs.getString(KEY_COMPANION_SIDE, CompanionSide.RIGHT.name) ?: CompanionSide.RIGHT.name)
    )
    val companionSide: StateFlow<CompanionSide> = _companionSide.asStateFlow()

    private val _companionGap = MutableStateFlow(prefs.getInt(KEY_COMPANION_GAP, 8)) // dp
    val companionGap: StateFlow<Int> = _companionGap.asStateFlow()

    // S24 Ultra Exclusive Features
    private val _isSPenHoverEnabled = MutableStateFlow(prefs.getBoolean(KEY_SPEN_HOVER, true))
    val isSPenHoverEnabled: StateFlow<Boolean> = _isSPenHoverEnabled.asStateFlow()

    private val _isCharging45WVisualizerEnabled = MutableStateFlow(prefs.getBoolean(KEY_CHARGING_45W, true))
    val isCharging45WVisualizerEnabled: StateFlow<Boolean> = _isCharging45WVisualizerEnabled.asStateFlow()

    private val _isAutoOTPDetectionEnabled = MutableStateFlow(prefs.getBoolean(KEY_AUTO_OTP, true))
    val isAutoOTPDetectionEnabled: StateFlow<Boolean> = _isAutoOTPDetectionEnabled.asStateFlow()

    private val _isAntiBurnInEnabled = MutableStateFlow(prefs.getBoolean(KEY_ANTI_BURN_IN, true))
    val isAntiBurnInEnabled: StateFlow<Boolean> = _isAntiBurnInEnabled.asStateFlow()

    private val _isGameCinemaAutoHideEnabled = MutableStateFlow(prefs.getBoolean(KEY_AUTO_HIDE_GAMES, true))
    val isGameCinemaAutoHideEnabled: StateFlow<Boolean> = _isGameCinemaAutoHideEnabled.asStateFlow()

    // Setters
    fun setIslandEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_ISLAND_ENABLED, enabled).apply()
        _isIslandEnabled.value = enabled
    }

    fun setCalibration(x: Int, y: Int, width: Int, height: Int, radius: Int) {
        prefs.edit()
            .putInt(KEY_OFFSET_X, x)
            .putInt(KEY_OFFSET_Y, y)
            .putInt(KEY_PILL_WIDTH, width)
            .putInt(KEY_PILL_HEIGHT, height)
            .putInt(KEY_CORNER_RADIUS, radius)
            .apply()

        _offsetX.value = x
        _offsetY.value = y
        _pillWidth.value = width
        _pillHeight.value = height
        _cornerRadius.value = radius
    }

    fun applyPreset(preset: CutoutPreset) {
        setCalibration(
            x = preset.offsetX,
            y = preset.offsetY,
            width = preset.collapsedWidth,
            height = preset.collapsedHeight,
            radius = preset.cornerRadius
        )
    }

    fun setCompanionBubbleConfig(enabled: Boolean, side: CompanionSide, gap: Int) {
        prefs.edit()
            .putBoolean(KEY_COMPANION_ENABLED, enabled)
            .putString(KEY_COMPANION_SIDE, side.name)
            .putInt(KEY_COMPANION_GAP, gap)
            .apply()

        _isCompanionBubbleEnabled.value = enabled
        _companionSide.value = side
        _companionGap.value = gap
    }

    fun setSPenHoverEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_SPEN_HOVER, enabled).apply()
        _isSPenHoverEnabled.value = enabled
    }

    fun setCharging45WEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_CHARGING_45W, enabled).apply()
        _isCharging45WVisualizerEnabled.value = enabled
    }

    fun setAutoOTPDetectionEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_AUTO_OTP, enabled).apply()
        _isAutoOTPDetectionEnabled.value = enabled
    }

    companion object {
        private const val KEY_ISLAND_ENABLED = "island_enabled"
        private const val KEY_OFFSET_X = "offset_x"
        private const val KEY_OFFSET_Y = "offset_y"
        private const val KEY_PILL_WIDTH = "pill_width"
        private const val KEY_PILL_HEIGHT = "pill_height"
        private const val KEY_CORNER_RADIUS = "corner_radius"
        private const val KEY_COMPANION_ENABLED = "companion_enabled"
        private const val KEY_COMPANION_SIDE = "companion_side"
        private const val KEY_COMPANION_GAP = "companion_gap"
        private const val KEY_SPEN_HOVER = "spen_hover"
        private const val KEY_CHARGING_45W = "charging_45w"
        private const val KEY_AUTO_OTP = "auto_otp"
        private const val KEY_ANTI_BURN_IN = "anti_burn_in"
        private const val KEY_AUTO_HIDE_GAMES = "auto_hide_games"
    }
}
