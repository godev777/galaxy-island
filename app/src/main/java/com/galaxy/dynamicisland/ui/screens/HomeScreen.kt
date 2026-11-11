package com.galaxy.dynamicisland.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.galaxy.dynamicisland.data.AppLanguage
import com.galaxy.dynamicisland.data.IslandPreferences
import com.galaxy.dynamicisland.data.ThemeMode
import com.galaxy.dynamicisland.manager.IslandEventManager
import com.galaxy.dynamicisland.service.IslandOverlayService
import com.galaxy.dynamicisland.ui.components.bounceClick
import com.galaxy.dynamicisland.ui.theme.SamsungChargingGreen
import com.galaxy.dynamicisland.ui.theme.SamsungSuperFastCyan
import com.galaxy.dynamicisland.ui.theme.WarningRed

@Composable
fun HomeScreen(preferences: IslandPreferences) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val scrollState = rememberScrollState()
    val isEnabled by preferences.isIslandEnabled.collectAsState()
    val appLang by preferences.appLanguage.collectAsState()
    val themeMode by preferences.themeMode.collectAsState()

    val isIndo = appLang == AppLanguage.INDONESIAN
    val isDark = themeMode == ThemeMode.DARK

    var hasOverlayPermission by remember { mutableStateOf(checkOverlay(context)) }
    var hasNotificationAccess by remember { mutableStateOf(checkNotificationAccess(context)) }
    var hasBatteryExemption by remember { mutableStateOf(checkBatteryExemption(context)) }

    // Re-check permissions whenever user returns to the app from Settings!
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == LifecycleEvent.ON_RESUME) {
                hasOverlayPermission = checkOverlay(context)
                hasNotificationAccess = checkNotificationAccess(context)
                hasBatteryExemption = checkBatteryExemption(context)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 18.dp, vertical = 12.dp)
            .verticalScroll(scrollState)
    ) {
        // Top Bar: Title, Theme Mode Toggle (Day/Dark), and Language Chips
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Galaxy Island",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = (-0.5).sp
                )
                Text(
                    text = if (isIndo) "Samsung S24 Ultra Edition" else "Samsung S24 Ultra Edition",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Day / Dark Mode Switcher Icon Button
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .bounceClick {
                            preferences.setThemeMode(if (isDark) ThemeMode.LIGHT else ThemeMode.DARK)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isDark) "🌙" else "☀️",
                        fontSize = 17.sp
                    )
                }

                // Language Toggle Chip
                Box(
                    modifier = Modifier
                        .height(38.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .bounceClick {
                            preferences.setLanguage(if (isIndo) AppLanguage.ENGLISH else AppLanguage.INDONESIAN)
                        }
                        .padding(horizontal = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isIndo) "🇮🇩 ID" else "🇬🇧 EN",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Hero Master Switch Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .bounceClick {
                    val target = !isEnabled
                    preferences.setIslandEnabled(target)
                    if (target) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(context)) {
                            val intent = Intent(
                                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                Uri.parse("package:${context.packageName}")
                            )
                            context.startActivity(intent)
                        } else {
                            IslandOverlayService.start(context)
                        }
                    } else {
                        IslandOverlayService.stop(context)
                    }
                },
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(
                                if (isEnabled) SamsungSuperFastCyan.copy(alpha = 0.15f)
                                else MaterialTheme.colorScheme.surfaceVariant
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (isEnabled) "🏝️" else "💤",
                            fontSize = 24.sp
                        )
                    }

                    Column {
                        Text(
                            text = if (isEnabled) {
                                if (isIndo) "Island Berjalan" else "Island Active"
                            } else {
                                if (isIndo) "Island Istirahat" else "Island Paused"
                            },
                            color = if (isEnabled) SamsungChargingGreen else MaterialTheme.colorScheme.onSurface,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = if (isEnabled) {
                                if (isIndo) "Memeluk lubang kamera S24 Ultra" else "Hugging S24 Ultra punch-hole"
                            } else {
                                if (isIndo) "Ketuk kartu untuk menyalakan" else "Tap card to enable"
                            },
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 12.sp
                        )
                    }
                }

                Switch(
                    checked = isEnabled,
                    onCheckedChange = { active ->
                        preferences.setIslandEnabled(active)
                        if (active) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(context)) {
                                val intent = Intent(
                                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                    Uri.parse("package:${context.packageName}")
                                )
                                context.startActivity(intent)
                            } else {
                                IslandOverlayService.start(context)
                            }
                        } else {
                            IslandOverlayService.stop(context)
                        }
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = SamsungSuperFastCyan,
                        checkedTrackColor = SamsungSuperFastCyan.copy(alpha = 0.35f)
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(22.dp))

        // Permissions Section
        Text(
            text = if (isIndo) "Izin Akses One UI" else "One UI Permissions",
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(10.dp))

        ModernPermissionItem(
            icon = "🪟",
            title = if (isIndo) "1. Tampilkan di Atas Layar" else "1. Display Over Other Apps",
            subtitle = if (isIndo) "Wajib agar pill melayang di atas semua aplikasi" else "Required to float around the camera cutout",
            isGranted = hasOverlayPermission,
            actionLabel = if (isIndo) "Izinkan" else "Grant",
            onGrantClick = {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    val intent = Intent(
                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:${context.packageName}")
                    )
                    context.startActivity(intent)
                }
            }
        )

        ModernPermissionItem(
            icon = "🔔",
            title = if (isIndo) "2. Akses Notifikasi & Media" else "2. Notifications & Media",
            subtitle = if (isIndo) "Wajib agar notifikasi WhatsApp/SMS & Spotify muncul di Island" else "Required to display WhatsApp/SMS & Spotify on Island",
            isGranted = hasNotificationAccess,
            actionLabel = if (isIndo) "Izinkan" else "Grant",
            onGrantClick = {
                val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
                context.startActivity(intent)
            }
        )

        // Samsung S24 Ultra Android 14 Restricted Settings Warning Box (If not granted)
        if (!hasNotificationAccess) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth().padding(14.dp)) {
                    Text(
                        text = if (isIndo) "⚠️ Tips Samsung One UI 6.1 (S24 Ultra):" else "⚠️ Samsung One UI 6.1 Tips (S24 Ultra):",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (isIndo) {
                            "Jika tombol izin di Pengaturan berwarna abu-abu / tidak bisa diklik:\n1. Buka Pengaturan HP > Aplikasi > Galaxy Island\n2. Ketuk titik tiga (⋮) di pojok kanan atas\n3. Pilih 'Izinkan pengaturan terbatas' (Allow restricted settings)\n4. Kembali ke sini dan klik tombol Izinkan."
                        } else {
                            "If the toggle in Settings is greyed out (Restricted Setting):\n1. Go to Settings > Apps > Galaxy Island\n2. Tap the 3 dots (⋮) in the top-right corner\n3. Tap 'Allow restricted settings'\n4. Return here and tap Grant."
                        },
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 11.sp,
                        lineHeight = 16.sp
                    )
                }
            }
        }

        ModernPermissionItem(
            icon = "🔋",
            title = if (isIndo) "3. Pengecualian Hemat Daya" else "3. Unrestricted Battery",
            subtitle = if (isIndo) "Mencegah Samsung One UI mematikan service di background" else "Prevents Samsung One UI from killing the overlay",
            isGranted = hasBatteryExemption,
            actionLabel = if (isIndo) "Izinkan" else "Grant",
            onGrantClick = {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).apply {
                        data = Uri.parse("package:${context.packageName}")
                    }
                    context.startActivity(intent)
                }
            }
        )

        Spacer(modifier = Modifier.height(22.dp))

        // Live Simulation Playground (Interactive Test Lab)
        Column {
            Text(
                text = if (isIndo) "Simulasi & Pratinjau Live" else "Live Simulation Lab",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = if (isIndo) "Ketuk tombol untuk melihat animasi mekar di S24 Ultra:" else "Tap chips below to trigger real-time spring animations:",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 12.sp
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            BouncyActionChip(
                icon = "⚡",
                label = "45W Charging",
                color = SamsungSuperFastCyan,
                modifier = Modifier.weight(1f)
            ) {
                IslandEventManager.simulateS24UltraSuperFastCharging()
            }

            BouncyActionChip(
                icon = "🎵",
                label = if (isIndo) "Musik + Dual" else "Music + Dual",
                color = SamsungChargingGreen,
                modifier = Modifier.weight(1f)
            ) {
                IslandEventManager.simulateMusicPlayback()
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            BouncyActionChip(
                icon = "💬",
                label = if (isIndo) "WhatsApp OTP" else "WhatsApp OTP",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1f)
            ) {
                IslandEventManager.simulateWhatsAppOTP()
            }

            BouncyActionChip(
                icon = "📞",
                label = if (isIndo) "Telepon WA" else "VoIP Call",
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.weight(1f)
            ) {
                IslandEventManager.simulateVoIPCall()
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedButton(
            onClick = { IslandEventManager.clearAll() },
            colors = ButtonDefaults.outlinedButtonColors(contentColor = WarningRed),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .bounceClick { IslandEventManager.clearAll() }
        ) {
            Text(if (isIndo) "🧹 Bersihkan Layar" else "🧹 Clear Island View", fontSize = 13.sp)
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
fun BouncyActionChip(
    icon: String,
    label: String,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .bounceClick { onClick() },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Text(icon, fontSize = 18.sp)
            }
            Text(
                text = label,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun ModernPermissionItem(
    icon: String,
    title: String,
    subtitle: String,
    isGranted: Boolean,
    actionLabel: String,
    onGrantClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .bounceClick { onGrantClick() },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Text(icon, fontSize = 18.sp)
                }

                Column {
                    Text(
                        title,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        subtitle,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 11.sp
                    )
                }
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        if (isGranted) SamsungChargingGreen.copy(alpha = 0.15f)
                        else MaterialTheme.colorScheme.primary
                    )
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isGranted) "✓" else actionLabel,
                    color = if (isGranted) SamsungChargingGreen else MaterialTheme.colorScheme.onPrimary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

private fun checkOverlay(context: Context): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        Settings.canDrawOverlays(context)
    } else true
}

private fun checkNotificationAccess(context: Context): Boolean {
    val pkgName = context.packageName
    val flat = Settings.Secure.getString(context.contentResolver, "enabled_notification_listeners")
    return flat != null && flat.contains(pkgName)
}

private fun checkBatteryExemption(context: Context): Boolean {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        return pm.isIgnoringBatteryOptimizations(context.packageName)
    }
    return true
}
