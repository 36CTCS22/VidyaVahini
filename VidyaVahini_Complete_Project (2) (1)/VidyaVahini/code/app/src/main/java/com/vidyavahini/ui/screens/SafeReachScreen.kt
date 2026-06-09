package com.vidyavahini.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vidyavahini.ui.theme.SuccessGreen

@Composable
fun SafeReachScreen(
    routeName: String,
    alreadyReached: Boolean,
    onConfirm: () -> Unit,
    onBack: () -> Unit
) {
    var confirmed by remember { mutableStateOf(alreadyReached) }

    val scale = remember { Animatable(1f) }

    LaunchedEffect(confirmed) {
        if (confirmed) {
            scale.animateTo(
                1.2f,
                spring(dampingRatio = Spring.DampingRatioMediumBouncy)
            )
            scale.animateTo(1f, spring())
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(if (confirmed) SuccessGreen.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant)
                .scale(scale.value),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.CheckCircle,
                contentDescription = null,
                tint = if (confirmed) SuccessGreen else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                modifier = Modifier.size(72.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = if (confirmed) "You've Reached Safely!" else "Confirm Safe Arrival",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = if (confirmed) SuccessGreen else MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = if (confirmed)
                "Great! Your safe arrival has been recorded for $routeName."
            else
                "Tap the button below once you have safely reached your destination using $routeName.",
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            lineHeight = 22.sp
        )

        Spacer(modifier = Modifier.height(40.dp))

        if (!confirmed) {
            Button(
                onClick = {
                    confirmed = true
                    onConfirm()
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen)
            ) {
                Icon(Icons.Default.CheckCircle, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("I Reached Safely", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))
        }

        OutlinedButton(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth().height(52.dp)
        ) {
            Text(if (confirmed) "Back to Routes" else "Not Yet")
        }
    }
}
