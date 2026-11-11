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
import androidx.compose.material3.MaterialTheme
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
import com.galaxy.dynamicisland.data.AppLanguage
import com.galaxy.dynamicisland.data.IslandPreferences
import com.galaxy.dynamicisland.ui.components.bounceClick
import com.galaxy.dynamicisland.ui.theme.SamsungSuperFastCyan

@Composable
fun LiveActivitiesScreen(preferences: IslandPreferences) {
    val scrollState = rememberScrollState()

    val isSPenEnabled by preferences.isSPenHoverEnabled.collectAsState()
    val is45WEnabled by preferences.isCharging45WVisualizerEnabled.collectAsState()
    val isOTPEnabled by preferences.isAutoOTPDetectionEnabled.collectAsState()
    val isAntiBurnInEnabled by preferences.isAntiBurnInEnabled.collectAsState()
    val isAutoHideGames by preferences.isGameCinemaAutoHideEnabled.collectAsState()
    val appLang by preferences.appLanguage.collectAsState()

    val isIndo = appLang == AppLanguage.INDONESIAN

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 18.dp, vertical = 12.dp)
            .verticalScroll(scrollState)
    ) {
        Text(
            text = if (isIndo) "Fitur & Live Activities" else "Features & Live Activities",
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = (-0.5).sp
        )
        Text(
            text = if (isIndo) "Konfigurasi modul pintar dan interaksi cerdas untuk S24 Ultra" else "Configure smart island triggers and live activities for S24 Ultra",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 13.sp
        )

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = if (isIndo) "Eksklusif S24 Ultra" else "S24 Ultra Exclusives",
            color = MaterialTheme.colorScheme.primary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))

        ModernFeatureCard(
            title = if (isIndo) "🖊️ S-Pen Smart Hover & Actions" else "🖊️ S-Pen Smart Hover & Actions",
            subtitle = if (isIndo) "Dekatkan S-Pen ke lubang kamera untuk membesarkan preview mini" else "Hover S-Pen tip over cutout to trigger dynamic peek preview",
            checked = isSPenEnabled,
            onCheckedChange = { preferences.setSPenHoverEnabled(it) }
        )

        ModernFeatureCard(
            title = if (isIndo) "⚡ Super Fast Charging 2.0 (45W)" else "⚡ Super Fast Charging 2.0 (45W)",
            subtitle = if (isIndo) "Animasi energi neon cyan khas Samsung One UI saat dicas" else "Neon cyan energy ring animation when 45W charger is plugged",
            checked = is45WEnabled,
            onCheckedChange = { preferences.setCharging45WEnabled(it) }
        )

        ModernFeatureCard(
            title = if (isIndo) "📋 Deteksi OTP & Salin 1-Tap" else "📋 Auto-OTP Detection & 1-Tap Copy",
            subtitle = if (isIndo) "Munculkan tombol salin instan saat SMS/WA OTP masuk" else "Instant one-tap copy button when 2FA/OTP messages arrive",
            checked = isOTPEnabled,
            onCheckedChange = { preferences.setAutoOTPDetectionEnabled(it) }
        )

        ModernFeatureCard(
            title = if (isIndo) "🛡️ Pixel Shift (Anti-Burn-In)" else "🛡️ Pixel Shift (Anti-Burn-In Protection)",
            subtitle = if (isIndo) "Geser posisi 1-2 piksel berkala untuk melindungi layar AMOLED" else "Subtly shifts idle widgets by 1-2px periodically on AMOLED screen",
            checked = isAntiBurnInEnabled,
            onCheckedChange = {}
        )

        ModernFeatureCard(
            title = if (isIndo) "🎮 Sembunyi Saat Game & Film" else "🎮 Auto-Hide in Fullscreen Games & Movies",
            subtitle = if (isIndo) "Menyusut otomatis di mode layar penuh agar tidak menghalangi kontrol" else "Minimizes transparently during landscape gaming or video playback",
            checked = isAutoHideGames,
            onCheckedChange = {}
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = if (isIndo) "Notifikasi & Media" else "Notifications & Media",
            color = MaterialTheme.colorScheme.primary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))

        ModernFeatureCard(
            title = if (isIndo) "🎵 Equalizer Waveform & Cover Musik" else "🎵 Equalizer Waveform & Album Art",
            subtitle = if (isIndo) "Visualizer audio menari mengikuti irama lagu Spotify / YouTube" else "Dancing 3-band audio waveform with rotating artwork",
            checked = true,
            onCheckedChange = {}
        )

        ModernFeatureCard(
            title = if (isIndo) "💬 Inline Reply & Draf Otomatis" else "💬 Inline Reply & Auto-Draft Retention",
            subtitle = if (isIndo) "Balas chat langsung dari island tanpa kehilangan ketikan" else "Reply inline without losing unfinished drafts if interrupted",
            checked = true,
            onCheckedChange = {}
        )

        ModernFeatureCard(
            title = if (isIndo) "📞 Timer Panggilan VoIP & Telepon" else "📞 VoIP & Phone Call Timer",
            subtitle = if (isIndo) "Lacak durasi panggilan WhatsApp & telepon secara real-time" else "Live active call counter for WhatsApp, Telegram & Phone calls",
            checked = true,
            onCheckedChange = {}
        )

        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
fun ModernFeatureCard(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .bounceClick { onCheckedChange(!checked) },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
                Text(
                    title,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    subtitle,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 11.sp,
                    lineHeight = 16.sp
                )
            }
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(checkedThumbColor = SamsungSuperFastCyan)
            )
        }
    }
}
