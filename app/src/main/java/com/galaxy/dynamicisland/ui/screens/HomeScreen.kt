package com.galaxy.dynamicisland.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.galaxy.dynamicisland.data.IslandPreferences
import com.galaxy.dynamicisland.manager.IslandEventManager
import com.galaxy.dynamicisland.service.IslandOverlayService
import com.galaxy.dynamicisland.ui.theme.DarkBorder
import com.galaxy.dynamicisland.ui.theme.DarkSurface
import com.galaxy.dynamicisland.ui.theme.DarkSurfaceVariant
import com.galaxy.dynamicisland.ui.theme.PureBlack
import com.galaxy.dynamicisland.ui.theme.SamsungChargingGreen
import com.galaxy.dynamicisland.ui.theme.SamsungSuperFastCyan
import com.galaxy.dynamicisland.ui.theme.TextMuted
import com.galaxy.dynamicisland.ui.theme.TextWhite
import com.galaxy.dynamicisland.ui.theme.WarningRed

@Composable
fun HomeScreen(preferences: IslandPreferences) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val isEnabled by preferences.isIslandEnabled.collectAsState()

    var hasOverlayPermission by remember {
        mutableStateOf(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) Settings.canDrawOverlays(context) else true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkSurface)
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        // App Header
        Text(
            text = "Galaxy Island",
            color = TextWhite,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Dynamic Island khusus Samsung Galaxy S24 Ultra",
            color = SamsungSuperFastCyan,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Master Island Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = PureBlack),
            shape = RoundedCornerShape(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = if (isEnabled) "Island Aktif" else "Island Nonaktif",
                        color = if (isEnabled) SamsungChargingGreen else TextMuted,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = if (isEnabled) "Memeluk lubang kamera S24 Ultra" else "Ketuk untuk menyalakan",
                        color = TextMuted,
                        fontSize = 12.sp
                    )
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
                        checkedTrackColor = SamsungSuperFastCyan.copy(alpha = 0.3f)
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Permissions Section
        Text("Izin Diperlukan", color = TextWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(10.dp))

        PermissionItem(
            title = "1. Tampilkan di Atas Aplikasi Lain",
            subtitle = "Wajib agar island bisa melayang di atas layar",
            isGranted = hasOverlayPermission,
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

        PermissionItem(
            title = "2. Akses Notifikasi & Media",
            subtitle = "Untuk membaca pesan masuk, OTP, dan Spotify",
            isGranted = true,
            onGrantClick = {
                val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
                context.startActivity(intent)
            }
        )

        PermissionItem(
            title = "3. Pengecualian Hemat Baterai (One UI)",
            subtitle = "Agar tidak dimatikan paksa oleh sistem Samsung",
            isGranted = true,
            onGrantClick = {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).apply {
                        data = Uri.parse("package:${context.packageName}")
                    }
                    context.startActivity(intent)
                }
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Live Simulation Playground
        Text("Uji Coba & Simulasi Fitur", color = TextWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Text("Klik tombol di bawah untuk langsung mencoba animasi di S24 Ultra Anda:", color = TextMuted, fontSize = 12.sp)
        Spacer(modifier = Modifier.height(12.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = { IslandEventManager.simulateS24UltraSuperFastCharging() },
                colors = ButtonDefaults.buttonColors(containerColor = DarkSurfaceVariant),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text("⚡ 45W Charge", color = SamsungSuperFastCyan, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = { IslandEventManager.simulateMusicPlayback() },
                colors = ButtonDefaults.buttonColors(containerColor = DarkSurfaceVariant),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text("🎵 Musik + Dual", color = SamsungChargingGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = { IslandEventManager.simulateWhatsAppOTP() },
                colors = ButtonDefaults.buttonColors(containerColor = DarkSurfaceVariant),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text("💬 Salin OTP", color = TextWhite, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = { IslandEventManager.simulateVoIPCall() },
                colors = ButtonDefaults.buttonColors(containerColor = DarkSurfaceVariant),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text("📞 Telepon WA", color = TextWhite, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = { IslandEventManager.clearAll() },
            colors = ButtonDefaults.outlinedButtonColors(contentColor = WarningRed),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("🧹 Bersihkan Island", fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
fun PermissionItem(
    title: String,
    subtitle: String,
    isGranted: Boolean,
    onGrantClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant),
        shape = RoundedCornerShape(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(title, color = TextWhite, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                Text(subtitle, color = TextMuted, fontSize = 11.sp)
            }
            Button(
                onClick = onGrantClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isGranted) DarkBorder else SamsungSuperFastCyan
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = if (isGranted) "Aktif" else "Izinkan",
                    color = if (isGranted) SamsungChargingGreen else PureBlack,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
