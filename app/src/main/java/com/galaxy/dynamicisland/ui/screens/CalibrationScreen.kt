package com.galaxy.dynamicisland.ui.screens

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.galaxy.dynamicisland.data.CompanionSide
import com.galaxy.dynamicisland.data.CutoutPreset
import com.galaxy.dynamicisland.data.IslandPreferences
import com.galaxy.dynamicisland.ui.theme.DarkBorder
import com.galaxy.dynamicisland.ui.theme.DarkSurface
import com.galaxy.dynamicisland.ui.theme.DarkSurfaceVariant
import com.galaxy.dynamicisland.ui.theme.PureBlack
import com.galaxy.dynamicisland.ui.theme.SamsungSuperFastCyan
import com.galaxy.dynamicisland.ui.theme.TextMuted
import com.galaxy.dynamicisland.ui.theme.TextWhite

@Composable
fun CalibrationScreen(preferences: IslandPreferences) {
    val scrollState = rememberScrollState()

    val currentX by preferences.offsetX.collectAsState()
    val currentY by preferences.offsetY.collectAsState()
    val currentW by preferences.pillWidth.collectAsState()
    val currentH by preferences.pillHeight.collectAsState()
    val currentRadius by preferences.cornerRadius.collectAsState()

    val isCompanionEnabled by preferences.isCompanionBubbleEnabled.collectAsState()
    val companionSide by preferences.companionSide.collectAsState()
    val companionGap by preferences.companionGap.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkSurface)
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        Text(
            text = "Kalibrasi Layar & Kamera",
            color = TextWhite,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Sesuaikan posisi dan ukuran pill agar pas memeluk lubang kamera S24 Ultra Anda.",
            color = TextMuted,
            fontSize = 13.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Preset Button
        Button(
            onClick = {
                preferences.applyPreset(CutoutPreset.S24_ULTRA)
            },
            colors = ButtonDefaults.buttonColors(containerColor = SamsungSuperFastCyan),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth().height(48.dp)
        ) {
            Text("★ Terapkan Preset Galaxy S24 Ultra (Bawaan)", color = PureBlack, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Live Preview Box
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = PureBlack),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Pratinjau Posisi Pill", color = TextMuted, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(companionGap.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (isCompanionEnabled && companionSide == CompanionSide.LEFT) {
                        Box(
                            modifier = Modifier
                                .size(currentH.dp)
                                .clip(CircleShape)
                                .background(DarkSurfaceVariant),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("●", color = SamsungSuperFastCyan, fontSize = 10.sp)
                        }
                    }

                    Box(
                        modifier = Modifier
                            .width(currentW.dp)
                            .height(currentH.dp)
                            .clip(RoundedCornerShape(currentRadius.dp))
                            .background(DarkSurfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Galaxy Island", color = TextWhite, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }

                    if (isCompanionEnabled && companionSide == CompanionSide.RIGHT) {
                        Box(
                            modifier = Modifier
                                .size(currentH.dp)
                                .clip(CircleShape)
                                .background(DarkSurfaceVariant),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("●", color = SamsungSuperFastCyan, fontSize = 10.sp)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Sliders
        CalibrationSlider(
            label = "Offset X (Geser Kiri / Kanan)",
            value = currentX.toFloat(),
            range = -60f..60f,
            unit = "dp",
            onValueChange = { preferences.setCalibration(it.toInt(), currentY, currentW, currentH, currentRadius) }
        )

        CalibrationSlider(
            label = "Offset Y (Jarak dari Atas Layar)",
            value = currentY.toFloat(),
            range = 0f..50f,
            unit = "dp",
            onValueChange = { preferences.setCalibration(currentX, it.toInt(), currentW, currentH, currentRadius) }
        )

        CalibrationSlider(
            label = "Lebar Pill Utama",
            value = currentW.toFloat(),
            range = 70f..200f,
            unit = "dp",
            onValueChange = { preferences.setCalibration(currentX, currentY, it.toInt(), currentH, currentRadius) }
        )

        CalibrationSlider(
            label = "Tinggi Pill Utama",
            value = currentH.toFloat(),
            range = 24f..50f,
            unit = "dp",
            onValueChange = { preferences.setCalibration(currentX, currentY, currentW, it.toInt(), currentRadius) }
        )

        CalibrationSlider(
            label = "Radius Sudut (Corner Radius)",
            value = currentRadius.toFloat(),
            range = 8f..30f,
            unit = "dp",
            onValueChange = { preferences.setCalibration(currentX, currentY, currentW, currentH, it.toInt()) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Companion Bubble Settings
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Companion Bubble (Bola Kecil)", color = TextWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Text("Muncul saat 2 aktivitas berjalan bersamaan", color = TextMuted, fontSize = 11.sp)
                    }
                    Switch(
                        checked = isCompanionEnabled,
                        onCheckedChange = { preferences.setCompanionBubbleConfig(it, companionSide, companionGap) },
                        colors = SwitchDefaults.colors(checkedThumbColor = SamsungSuperFastCyan)
                    )
                }

                if (isCompanionEnabled) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Posisi Bola Kecil:", color = TextWhite, fontSize = 12.sp)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FilterChip(
                            selected = companionSide == CompanionSide.RIGHT,
                            onClick = { preferences.setCompanionBubbleConfig(true, CompanionSide.RIGHT, companionGap) },
                            label = { Text("Sebelah Kanan (Rekomendasi)") }
                        )
                        FilterChip(
                            selected = companionSide == CompanionSide.LEFT,
                            onClick = { preferences.setCompanionBubbleConfig(true, CompanionSide.LEFT, companionGap) },
                            label = { Text("Sebelah Kiri") }
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    CalibrationSlider(
                        label = "Jarak Renggang (Gap)",
                        value = companionGap.toFloat(),
                        range = 4f..20f,
                        unit = "dp",
                        onValueChange = { preferences.setCompanionBubbleConfig(true, companionSide, it.toInt()) }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
fun CalibrationSlider(
    label: String,
    value: Float,
    range: ClosedFloatingPointRange<Float>,
    unit: String,
    onValueChange: (Float) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label, color = TextWhite, fontSize = 13.sp)
            Text("${value.toInt()} $unit", color = SamsungSuperFastCyan, fontSize = 13.sp, fontWeight = FontWeight.Bold)
        }
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = range,
            colors = SliderDefaults.colors(
                thumbColor = SamsungSuperFastCyan,
                activeTrackColor = SamsungSuperFastCyan,
                inactiveTrackColor = DarkBorder
            )
        )
    }
}
