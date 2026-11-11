package com.galaxy.dynamicisland.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.galaxy.dynamicisland.data.ThemeMode

private val DarkColorScheme = darkColorScheme(
    primary = SamsungSuperFastCyan,
    secondary = ElectricBlue,
    tertiary = SamsungChargingGreen,
    background = DarkBackground,
    surface = DarkSurface,
    surfaceVariant = DarkSurfaceVariant,
    outline = DarkBorder,
    onPrimary = PureBlack,
    onSecondary = DarkTextPrimary,
    onBackground = DarkTextPrimary,
    onSurface = DarkTextPrimary,
    onSurfaceVariant = DarkTextMuted
)

private val LightColorScheme = lightColorScheme(
    primary = ElectricBlue,
    secondary = SamsungSuperFastCyan,
    tertiary = SamsungChargingGreen,
    background = LightBackground,
    surface = LightSurface,
    surfaceVariant = LightSurfaceVariant,
    outline = LightBorder,
    onPrimary = LightSurface,
    onSecondary = LightTextPrimary,
    onBackground = LightTextPrimary,
    onSurface = LightTextPrimary,
    onSurfaceVariant = LightTextMuted
)

@Composable
fun GalaxyIslandTheme(
    themeMode: ThemeMode = ThemeMode.DARK,
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val isSystemDark = isSystemInDarkTheme()

    val isDark = when (themeMode) {
        ThemeMode.SYSTEM -> isSystemDark
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
    }

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (isDark) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        isDark -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
