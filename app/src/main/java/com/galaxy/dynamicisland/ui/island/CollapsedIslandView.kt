package com.galaxy.dynamicisland.ui.island

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.galaxy.dynamicisland.data.IslandEvent
import com.galaxy.dynamicisland.service.NetworkSpeedMonitor
import com.galaxy.dynamicisland.service.WeatherManager
import com.galaxy.dynamicisland.ui.theme.ElectricBlue
import com.galaxy.dynamicisland.ui.theme.NeonPink
import com.galaxy.dynamicisland.ui.theme.PureBlack
import com.galaxy.dynamicisland.ui.theme.SamsungChargingGreen
import com.galaxy.dynamicisland.ui.theme.SamsungSuperFastCyan
import com.galaxy.dynamicisland.ui.theme.TextWhite
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CollapsedIslandView(
    event: IslandEvent?,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(PureBlack),
        contentAlignment = Alignment.Center
    ) {
        when (event) {
            // 1. Music / Media Player Active
            is IslandEvent.Media -> {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left side: Mini Album Art / Note
                    Box(
                        modifier = Modifier
                            .size(18.dp)
                            .clip(CircleShape)
                            .background(NeonPink),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("♪", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }

                    // Right side: Live Equalizer Waveform Bars (dancing to music)
                    AnimatedEqualizerWaveform()
                }
            }

            // 2. Samsung Super Fast Charging 2.0 (45W)
            is IslandEvent.Charging -> {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "⚡",
                        color = if (event.isSuperFast2_0) SamsungSuperFastCyan else SamsungChargingGreen,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${event.batteryPercentage}%",
                        color = if (event.isSuperFast2_0) SamsungSuperFastCyan else SamsungChargingGreen,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // 3. VoIP & Phone Call
            is IslandEvent.Call -> {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("📞", fontSize = 11.sp)
                    Text(
                        text = formatCallDuration(event.durationSeconds),
                        color = SamsungChargingGreen,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // 4. WhatsApp / SMS OTP Alert
            is IslandEvent.Notification -> {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("💬", fontSize = 11.sp)
                    Text(
                        text = if (event.isOTP) "OTP: ${event.otpCode}" else event.title.take(8),
                        color = TextWhite,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // 5. Default Idle Widgets: Clock, Weather, or Net Speed
            else -> {
                IdleCutoutContent()
            }
        }
    }
}

@Composable
fun AnimatedEqualizerWaveform() {
    val infiniteTransition = rememberInfiniteTransition(label = "equalizer")
    val bar1 by infiniteTransition.animateFloat(
        initialValue = 4f,
        targetValue = 14f,
        animationSpec = infiniteRepeatable(
            animation = tween(400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bar1"
    )
    val bar2 by infiniteTransition.animateFloat(
        initialValue = 12f,
        targetValue = 5f,
        animationSpec = infiniteRepeatable(
            animation = tween(350, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bar2"
    )
    val bar3 by infiniteTransition.animateFloat(
        initialValue = 6f,
        targetValue = 15f,
        animationSpec = infiniteRepeatable(
            animation = tween(450, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bar3"
    )

    Row(
        modifier = Modifier.height(16.dp),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.width(2.5.dp).height(bar1.dp).clip(RoundedCornerShape(1.dp)).background(SamsungChargingGreen))
        Box(modifier = Modifier.width(2.5.dp).height(bar2.dp).clip(RoundedCornerShape(1.dp)).background(SamsungSuperFastCyan))
        Box(modifier = Modifier.width(2.5.dp).height(bar3.dp).clip(RoundedCornerShape(1.dp)).background(ElectricBlue))
    }
}

@Composable
fun IdleCutoutContent() {
    var currentTime by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
        while (true) {
            currentTime = formatter.format(Date())
            delay(1000)
        }
    }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = currentTime,
            color = TextWhite,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = "28°C",
            color = SamsungSuperFastCyan,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

private fun formatCallDuration(seconds: Long): String {
    val m = seconds / 60
    val s = seconds % 60
    return String.format("%02d:%02d", m, s)
}
