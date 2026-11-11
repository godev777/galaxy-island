package com.galaxy.dynamicisland.ui.island

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.galaxy.dynamicisland.R
import com.galaxy.dynamicisland.data.IslandEvent
import com.galaxy.dynamicisland.manager.IslandEventManager
import com.galaxy.dynamicisland.manager.QuickLaunchManager
import com.galaxy.dynamicisland.ui.theme.DarkBorder
import com.galaxy.dynamicisland.ui.theme.DarkSurfaceVariant
import com.galaxy.dynamicisland.ui.theme.ElectricBlue
import com.galaxy.dynamicisland.ui.theme.NeonPink
import com.galaxy.dynamicisland.ui.theme.PureBlack
import com.galaxy.dynamicisland.ui.theme.SamsungChargingGreen
import com.galaxy.dynamicisland.ui.theme.SamsungSuperFastCyan
import com.galaxy.dynamicisland.ui.theme.TextMuted
import com.galaxy.dynamicisland.ui.theme.TextWhite
import com.galaxy.dynamicisland.ui.theme.WarningRed

@Composable
fun ExpandedIslandView(
    event: IslandEvent?,
    onFocusChange: (Boolean) -> Unit = {}
) {
    val context = LocalContext.current
    val quickLaunchManager = remember { QuickLaunchManager(context) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(PureBlack)
            .padding(18.dp)
    ) {
        when (event) {
            // 1. Music Player Card
            is IslandEvent.Media -> {
                MediaExpandedCard(media = event)
            }

            // 2. 45W Samsung Super Fast Charging 2.0 Card
            is IslandEvent.Charging -> {
                ChargingExpandedCard(charging = event)
            }

            // 3. Notification & Inline Reply & OTP
            is IslandEvent.Notification -> {
                NotificationExpandedCard(
                    notif = event,
                    onFocusChange = onFocusChange
                )
            }

            // 4. VoIP / Phone Call Card
            is IslandEvent.Call -> {
                CallExpandedCard(call = event)
            }

            // 5. Default Quick Launch & Utilities Grid
            else -> {
                QuickLaunchExpandedCard(quickLaunchManager = quickLaunchManager)
            }
        }
    }
}

@Composable
fun MediaExpandedCard(media: IslandEvent.Media) {
    var isPlaying by remember { mutableStateOf(media.isPlaying) }
    var progress by remember { mutableFloatStateOf(0.35f) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Album Art
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(NeonPink),
                contentAlignment = Alignment.Center
            ) {
                Text("🎵", fontSize = 24.sp)
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = media.title,
                    color = TextWhite,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                Text(
                    text = media.artist,
                    color = TextMuted,
                    fontSize = 13.sp,
                    maxLines = 1
                )
            }

            // Mini visualizer
            AnimatedEqualizerWaveform()
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Wavy Seek Bar Slider
        Slider(
            value = progress,
            onValueChange = { progress = it },
            colors = SliderDefaults.colors(
                thumbColor = SamsungSuperFastCyan,
                activeTrackColor = SamsungSuperFastCyan,
                inactiveTrackColor = DarkBorder
            ),
            modifier = Modifier.fillMaxWidth().height(20.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("01:15", color = TextMuted, fontSize = 11.sp)
            Text("03:45", color = TextMuted, fontSize = 11.sp)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Playback Controls
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {}) {
                Icon(painter = painterResource(id = R.drawable.ic_skip_previous), contentDescription = "Prev", tint = TextWhite)
            }

            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(DarkSurfaceVariant)
                    .clickable { isPlaying = !isPlaying },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play),
                    contentDescription = "Play/Pause",
                    tint = SamsungSuperFastCyan,
                    modifier = Modifier.size(24.dp)
                )
            }

            IconButton(onClick = {}) {
                Icon(painter = painterResource(id = R.drawable.ic_skip_next), contentDescription = "Next", tint = TextWhite)
            }
        }
    }
}

@Composable
fun ChargingExpandedCard(charging: IslandEvent.Charging) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(SamsungSuperFastCyan.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Text("⚡", color = SamsungSuperFastCyan, fontSize = 32.sp)
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = if (charging.isSuperFast2_0) "Super Fast Charging 2.0 (45W)" else "Fast Charging",
            color = SamsungSuperFastCyan,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "${charging.batteryPercentage}% • ${charging.minutesToFull} min until full",
            color = TextMuted,
            fontSize = 13.sp
        )
    }
}

@Composable
fun NotificationExpandedCard(
    notif: IslandEvent.Notification,
    onFocusChange: (Boolean) -> Unit
) {
    val context = LocalContext.current
    var replyText by remember { mutableStateOf(IslandEventManager.getDraft(notif.key)) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(DarkSurfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Text("💬", fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = notif.title,
                    color = TextWhite,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = notif.appName,
                    color = TextMuted,
                    fontSize = 11.sp
                )
            }

            if (notif.isOTP && notif.otpCode != null) {
                OutlinedButton(
                    onClick = {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        clipboard.setPrimaryClip(ClipData.newPlainText("OTP", notif.otpCode))
                        Toast.makeText(context, "OTP ${notif.otpCode} Disalin!", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = SamsungSuperFastCyan),
                    border = ButtonDefaults.outlinedButtonBorder.copy(brush = androidx.compose.ui.graphics.SolidColor(SamsungSuperFastCyan))
                ) {
                    Text("Salin OTP: ${notif.otpCode}", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = notif.text,
            color = TextWhite,
            fontSize = 13.sp,
            lineHeight = 18.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Inline Reply Field with Draft Preservation
        OutlinedTextField(
            value = replyText,
            onValueChange = {
                replyText = it
                IslandEventManager.saveDraft(notif.key, it)
            },
            placeholder = { Text("Balas langsung...", color = TextMuted, fontSize = 12.sp) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = DarkSurfaceVariant,
                unfocusedContainerColor = DarkSurfaceVariant,
                focusedTextColor = TextWhite,
                unfocusedTextColor = TextWhite,
                focusedBorderColor = SamsungSuperFastCyan,
                unfocusedBorderColor = DarkBorder
            )
        )
    }
}

@Composable
fun CallExpandedCard(call: IslandEvent.Call) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = call.callerName,
            color = TextWhite,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "${call.appName} • 02:45",
            color = SamsungChargingGreen,
            fontSize = 13.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconButton(
                onClick = {},
                modifier = Modifier.size(50.dp).clip(CircleShape).background(DarkSurfaceVariant)
            ) {
                Text("🎤", fontSize = 18.sp)
            }

            IconButton(
                onClick = { IslandEventManager.collapse(); IslandEventManager.setPrimaryEvent(null) },
                modifier = Modifier.size(50.dp).clip(CircleShape).background(WarningRed)
            ) {
                Text("📞", fontSize = 18.sp)
            }
        }
    }
}

@Composable
fun QuickLaunchExpandedCard(quickLaunchManager: QuickLaunchManager) {
    val context = LocalContext.current
    var isTorchOn by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Galaxy Quick Launcher",
            color = TextWhite,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            QuickLaunchItem(
                icon = "🔦",
                label = if (isTorchOn) "Senter ON" else "Senter",
                isActive = isTorchOn
            ) {
                isTorchOn = quickLaunchManager.toggleTorch()
            }

            QuickLaunchItem(
                icon = "🧮",
                label = "Kalkulator",
                isActive = false
            ) {
                quickLaunchManager.openCalculator()
                IslandEventManager.collapse()
            }

            QuickLaunchItem(
                icon = "📶",
                label = "Quick Set",
                isActive = false
            ) {
                quickLaunchManager.triggerQuickSettings()
                IslandEventManager.collapse()
            }

            QuickLaunchItem(
                icon = "🔔",
                label = "Tirai Notif",
                isActive = false
            ) {
                quickLaunchManager.triggerNotificationShade()
                IslandEventManager.collapse()
            }
        }
    }
}

@Composable
fun QuickLaunchItem(
    icon: String,
    label: String,
    isActive: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(46.dp)
                .clip(CircleShape)
                .background(if (isActive) SamsungSuperFastCyan else DarkSurfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Text(icon, fontSize = 20.sp)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(label, color = TextMuted, fontSize = 10.sp)
    }
}
