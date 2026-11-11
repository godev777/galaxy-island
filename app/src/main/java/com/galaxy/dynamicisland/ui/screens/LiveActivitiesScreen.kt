package com.galaxy.dynamicisland.ui.screens

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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.galaxy.dynamicisland.data.IslandPreferences
import com.galaxy.dynamicisland.ui.theme.DarkSurface
import com.galaxy.dynamicisland.ui.theme.DarkSurfaceVariant
import com.galaxy.dynamicisland.ui.theme.SamsungSuperFastCyan
import com.galaxy.dynamicisland.ui.theme.TextMuted
import com.galaxy.dynamicisland.ui.theme.TextWhite

@Composable
fun LiveActivitiesScreen(preferences: IslandPreferences) {
    val scrollState = rememberScrollState()

    val isSPenEnabled by preferences.isSPenHoverEnabled.collectAsState()
    val is45WEnabled by preferences.isCharging45WVisualizerEnabled.collectAsState()
    val isOTPEnabled by preferences.isAutoOTPDetectionEnabled.collectAsState()
    val isAntiBurnInEnabled by preferences.isAntiBurnInEnabled.collectAsState()
    val isAutoHideGames by preferences.isGameCinemaAutoHideEnabled.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkSurface)
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        Text("Fitur & Live Activities", color = TextWhite, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text("Aktifkan atau sesuaikan modul cerdas untuk S24 Ultra", color = TextMuted, fontSize = 13.sp)

        Spacer(modifier = Modifier.height(18.dp))

        Text("Eksklusif S24 Ultra", color = SamsungSuperFastCyan, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))

        FeatureSwitchCard(
            title = "🖊️ S-Pen Smart Hover & Actions",
            subtitle = "Dekatkan S-Pen ke lubang kamera untuk membesarkan preview mini",
            checked = isSPenEnabled,
            onCheckedChange = { preferences.setSPenHoverEnabled(it) }
        )

        FeatureSwitchCard(
            title = "⚡ Super Fast Charging 2.0 (45W)",
            subtitle = "Animasi energi neon cyan khas Samsung One UI saat dicas",
            checked = is45WEnabled,
            onCheckedChange = { preferences.setCharging45WEnabled(it) }
        )

        FeatureSwitchCard(
            title = "📋 Deteksi OTP Otomatis & Salin 1-Tap",
            subtitle = "Munculkan tombol salin instan saat SMS/WA OTP masuk",
            checked = isOTPEnabled,
            onCheckedChange = { preferences.setAutoOTPDetectionEnabled(it) }
        )

        FeatureSwitchCard(
            title = "🛡️ Pixel Shift (Anti-Burn-In Protection)",
            subtitle = "Geser posisi 1-2 piksel berkala untuk melindungi layar AMOLED",
            checked = isAntiBurnInEnabled,
            onCheckedChange = {}
        )

        FeatureSwitchCard(
            title = "🎮 Sembunyi Otomatis Saat Game & Film",
            subtitle = "Menyusut otomatis di mode layar penuh agar tidak mengganggu kontrol",
            checked = isAutoHideGames,
            onCheckedChange = {}
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text("Notifikasi & Media", color = SamsungSuperFastCyan, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))

        FeatureSwitchCard(
            title = "🎵 Equalizer Waveform & Cover Musik",
            subtitle = "Visualizer audio menari mengikuti irama lagu Spotify / YouTube",
            checked = true,
            onCheckedChange = {}
        )

        FeatureSwitchCard(
            title = "💬 Inline Reply & Draf Otomatis",
            subtitle = "Balas pesan langsung dari card tanpa kehilangan ketikan",
            checked = true,
            onCheckedChange = {}
        )

        FeatureSwitchCard(
            title = "📞 Timer Panggilan VoIP & Telepon",
            subtitle = "Lacak durasi panggilan WhatsApp & telepon secara real-time",
            checked = true,
            onCheckedChange = {}
        )

        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
fun FeatureSwitchCard(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
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
            Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
                Text(title, color = TextWhite, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                Text(subtitle, color = TextMuted, fontSize = 11.sp)
            }
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(checkedThumbColor = SamsungSuperFastCyan)
            )
        }
    }
}
