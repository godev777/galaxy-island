package com.galaxy.dynamicisland.ui.island

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.galaxy.dynamicisland.data.CompanionBubbleData
import com.galaxy.dynamicisland.ui.theme.PureBlack
import com.galaxy.dynamicisland.ui.theme.SamsungSuperFastCyan

@Composable
fun CompanionBubbleView(
    bubbleData: CompanionBubbleData?,
    sizeDp: Int,
    onClick: () -> Unit
) {
    AnimatedVisibility(
        visible = bubbleData != null,
        enter = scaleIn() + fadeIn(),
        exit = scaleOut() + fadeOut()
    ) {
        if (bubbleData != null) {
            Box(
                modifier = Modifier
                    .size(sizeDp.dp)
                    .clip(CircleShape)
                    .background(PureBlack)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        onClick()
                    },
                contentAlignment = Alignment.Center
            ) {
                when (bubbleData.iconType) {
                    "timer" -> {
                        Text(
                            text = bubbleData.title,
                            color = SamsungSuperFastCyan,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    else -> {
                        Text(
                            text = "●",
                            color = SamsungSuperFastCyan,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}
