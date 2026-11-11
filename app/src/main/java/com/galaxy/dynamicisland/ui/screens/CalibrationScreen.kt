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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.galaxy.dynamicisland.data.AppLanguage
import com.galaxy.dynamicisland.data.CompanionSide
import com.galaxy.dynamicisland.data.CutoutPreset
import com.galaxy.dynamicisland.data.IslandPreferences
import com.galaxy.dynamicisland.ui.components.bounceClick
import com.galaxy.dynamicisland.ui.theme.PureBlack
import com.galaxy.dynamicisland.ui.theme.SamsungSuperFastCyan

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
            text = if (isIndo) "Kalibrasi Layar & Kamera" else "Display & Cutout Calibration",
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = (-0.5).sp
        )
        Text(
            text = if (isIndo) "Sesuaikan posisi pill secara presisi memeluk lubang kamera S24 Ultra" else "Fine-tune pill geometry to fit your S24 Ultra camera punch-hole",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 13.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Preset Button
        Button(
            onClick = {
                preferences.applyPreset(CutoutPreset.S24_ULTRA)
            },
            colors = ButtonDefaults.buttonColors(containerColor = SamsungSuperFastCyan),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .bounceClick { preferences.applyPreset(CutoutPreset.S24_ULTRA) }
        ) {
            Text(
                if (isIndo) "★ Terapkan Preset Galaxy S24 Ultra" else "★ Apply Galaxy S24 Ultra Preset",
                color = PureBlack,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Live Interactive Preview Box
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(22.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (isIndo) "Pratinjau Geometri Island" else "Island Geometry Preview",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(companionGap.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (isCompanionEnabled && companionSide == CompanionSide.LEFT) {
                        Box(
                            modifier = Modifier
                                .size(currentH.dp)
                                .clip(CircleShape)
                                .background(PureBlack),
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
                            .background(PureBlack),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Galaxy Island",
                            color = androidx.compose.ui.graphics.Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    if (isCompanionEnabled && companionSide == CompanionSide.RIGHT) {
                        Box(
                            modifier = Modifier
                                .size(currentH.dp)
                                .clip(CircleShape)
                                .background(PureBlack),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("●", color = SamsungSuperFastCyan, fontSize = 10.sp)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Sliders Card Container
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(22.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                ModernCalibrationSlider(
                    label = if (isIndo) "Offset X (Geser Horisontal)" else "Offset X (Horizontal Position)",
                    value = currentX.toFloat(),
                    range = -60f..60f,
                    unit = "dp",
                    onValueChange = { preferences.setCalibration(it.toInt(), currentY, currentW, currentH, currentRadius) }
                )

                ModernCalibrationSlider(
                    label = if (isIndo) "Offset Y (Jarak dari Atas Layar)" else "Offset Y (Top Screen Margin)",
                    value = currentY.toFloat(),
                    range = 0f..50f,
                    unit = "dp",
                    onValueChange = { preferences.setCalibration(currentX, it.toInt(), currentW, currentH, currentRadius) }
                )

                ModernCalibrationSlider(
                    label = if (isIndo) "Lebar Pill Utama" else "Pill Width",
                    value = currentW.toFloat(),
                    range = 70f..200f,
                    unit = "dp",
                    onValueChange = { preferences.setCalibration(currentX, currentY, it.toInt(), currentH, currentRadius) }
                )

                ModernCalibrationSlider(
                    label = if (isIndo) "Tinggi Pill Utama" else "Pill Height",
                    value = currentH.toFloat(),
                    range = 24f..50f,
                    unit = "dp",
                    onValueChange = { preferences.setCalibration(currentX, currentY, currentW, it.toInt(), currentRadius) }
                )

                ModernCalibrationSlider(
                    label = if (isIndo) "Radius Sudut Kapsul" else "Corner Radius",
                    value = currentRadius.toFloat(),
                    range = 8f..30f,
                    unit = "dp",
                    onValueChange = { preferences.setCalibration(currentX, currentY, currentW, currentH, it.toInt()) }
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Companion Bubble Settings Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(22.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth().padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f).padding(end = 10.dp)) {
                        Text(
                            if (isIndo) "Companion Bubble (Bola Kecil)" else "Companion Bubble (Dual Island)",
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            if (isIndo) "Muncul saat 2 aktivitas berjalan bersamaan" else "Displays secondary live activity beside the punch-hole",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 11.sp
                        )
                    }
                    Switch(
                        checked = isCompanionEnabled,
                        onCheckedChange = { preferences.setCompanionBubbleConfig(it, companionSide, companionGap) },
                        colors = SwitchDefaults.colors(checkedThumbColor = SamsungSuperFastCyan)
                    )
                }

                if (isCompanionEnabled) {
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        if (isIndo) "Posisi Bola Terpisah:" else "Bubble Position:",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        FilterChip(
                            selected = companionSide == CompanionSide.RIGHT,
                            onClick = { preferences.setCompanionBubbleConfig(true, CompanionSide.RIGHT, companionGap) },
                            label = { Text(if (isIndo) "Kanan Kamera" else "Right of Cutout", fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = SamsungSuperFastCyan,
                                selectedLabelColor = PureBlack
                            )
                        )
                        FilterChip(
                            selected = companionSide == CompanionSide.LEFT,
                            onClick = { preferences.setCompanionBubbleConfig(true, CompanionSide.LEFT, companionGap) },
                            label = { Text(if (isIndo) "Kiri Kamera" else "Left of Cutout", fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = SamsungSuperFastCyan,
                                selectedLabelColor = PureBlack
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    ModernCalibrationSlider(
                        label = if (isIndo) "Jarak Renggang (Gap)" else "Separation Gap",
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
fun ModernCalibrationSlider(
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
            Text(label, color = MaterialTheme.colorScheme.onSurface, fontSize = 13.sp, fontWeight = FontWeight.Medium)
            Text("${value.toInt()} $unit", color = MaterialTheme.colorScheme.primary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
        }
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = range,
            colors = SliderDefaults.colors(
                thumbColor = SamsungSuperFastCyan,
                activeTrackColor = SamsungSuperFastCyan,
                inactiveTrackColor = MaterialTheme.colorScheme.surfaceVariant
            )
        )
    }
}
