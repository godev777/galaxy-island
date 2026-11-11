package com.galaxy.dynamicisland.ui.island

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.galaxy.dynamicisland.data.CompanionSide
import com.galaxy.dynamicisland.data.IslandEvent
import com.galaxy.dynamicisland.data.IslandPreferences
import com.galaxy.dynamicisland.manager.IslandEventManager
import kotlin.math.abs

@Composable
fun DynamicIslandRootView(
    preferences: IslandPreferences,
    onUpdateWindowFocus: (Boolean) -> Unit = {}
) {
    val primaryEvent by IslandEventManager.primaryEvent.collectAsState()
    val companionBubble by IslandEventManager.companionBubble.collectAsState()
    val isExpanded by IslandEventManager.isExpanded.collectAsState()
    val isHeadsUp by IslandEventManager.isHeadsUpPulse.collectAsState()

    val pillWidthPref by preferences.pillWidth.collectAsState()
    val pillHeightPref by preferences.pillHeight.collectAsState()
    val cornerRadiusPref by preferences.cornerRadius.collectAsState()
    val companionSide by preferences.companionSide.collectAsState()
    val companionGap by preferences.companionGap.collectAsState()
    val isCompanionEnabled by preferences.isCompanionBubbleEnabled.collectAsState()

    // Smooth Spring physics animations for S24 Ultra 120Hz display
    val targetWidth = when {
        isExpanded -> 360.dp
        isHeadsUp && primaryEvent is IslandEvent.Notification -> 270.dp
        isHeadsUp && primaryEvent is IslandEvent.Charging -> 230.dp
        isHeadsUp && primaryEvent is IslandEvent.SystemToast -> 210.dp
        else -> pillWidthPref.dp
    }

    val targetHeight = when {
        isExpanded -> 190.dp
        isHeadsUp && primaryEvent is IslandEvent.Notification -> 44.dp
        isHeadsUp && primaryEvent is IslandEvent.Charging -> 42.dp
        else -> pillHeightPref.dp
    }

    val targetRadius = when {
        isExpanded -> 28.dp
        isHeadsUp -> 22.dp
        else -> cornerRadiusPref.dp
    }

    val animatedWidth by animateDpAsState(
        targetValue = targetWidth,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessMediumLow
        ),
        label = "width"
    )

    val animatedHeight by animateDpAsState(
        targetValue = targetHeight,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessMediumLow
        ),
        label = "height"
    )

    val animatedRadius by animateDpAsState(
        targetValue = targetRadius,
        animationSpec = spring(stiffness = Spring.StiffnessMedium),
        label = "radius"
    )

    // Gesture tracking variables
    var totalDragX by remember { mutableFloatStateOf(0f) }
    var totalDragY by remember { mutableFloatStateOf(0f) }

    Row(
        horizontalArrangement = Arrangement.spacedBy(companionGap.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Companion Bubble on LEFT side (if chosen)
        if (isCompanionEnabled && companionSide == CompanionSide.LEFT) {
            CompanionBubbleView(
                bubbleData = companionBubble,
                sizeDp = pillHeightPref,
                onClick = {
                    IslandEventManager.toggleExpand()
                }
            )
        }

        // Main Island Pill
        Box(
            modifier = Modifier
                .width(animatedWidth)
                .height(animatedHeight)
                .clip(RoundedCornerShape(animatedRadius))
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            IslandEventManager.toggleExpand()
                        }
                    )
                }
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = {
                            totalDragX = 0f
                            totalDragY = 0f
                        },
                        onDrag = { change, dragAmount ->
                            change.consume()
                            totalDragX += dragAmount.x
                            totalDragY += dragAmount.y
                        },
                        onDragEnd = {
                            val threshold = 40f
                            if (abs(totalDragY) > abs(totalDragX)) {
                                if (totalDragY > threshold) {
                                    // Swipe Down -> Expand
                                    IslandEventManager.expand()
                                } else if (totalDragY < -threshold) {
                                    // Swipe Up -> Collapse / Dismiss
                                    IslandEventManager.collapse()
                                }
                            } else {
                                if (totalDragX > threshold || totalDragX < -threshold) {
                                    // Swipe Left / Right -> Dismiss current notification or next
                                    IslandEventManager.setPrimaryEvent(null)
                                }
                            }
                        }
                    )
                }
        ) {
            if (isExpanded) {
                ExpandedIslandView(
                    event = primaryEvent,
                    onFocusChange = onUpdateWindowFocus
                )
            } else {
                CollapsedIslandView(
                    event = primaryEvent,
                    isHeadsUp = isHeadsUp
                )
            }
        }

        // Companion Bubble on RIGHT side (default for Dual Island)
        if (isCompanionEnabled && companionSide == CompanionSide.RIGHT) {
            CompanionBubbleView(
                bubbleData = companionBubble,
                sizeDp = pillHeightPref,
                onClick = {
                    IslandEventManager.toggleExpand()
                }
            )
        }
    }
}
