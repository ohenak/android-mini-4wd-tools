package com.mini4wd.lab.feature.profile.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mini4wd.lab.core.common.model.GearRatioInfo

@Composable
fun GearRatioIndicator(
    gearRatio: GearRatioInfo,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Gear 1 (Spur) color circle
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(gearRatio.gear1Color.toGearColor())
            )
            Text(
                text = "Spur",
                style = MaterialTheme.typography.labelSmall
            )
        }

        // Ratio display
        Text(
            text = gearRatio.ratio,
            style = MaterialTheme.typography.headlineMedium
        )

        // Gear 2 (Pinion/Counter) color circle
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(gearRatio.gear2Color.toGearColor())
            )
            Text(
                text = if (gearRatio.shaftType == "double") "Counter" else "Pinion",
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

private fun String.toGearColor(): Color {
    return when (this.lowercase()) {
        "yellow" -> Color(0xFFFFEB3B)
        "light blue" -> Color(0xFF03A9F4)
        "green" -> Color(0xFF4CAF50)
        "light green" -> Color(0xFF8BC34A)
        "light brown" -> Color(0xFFBCAAA4)
        "black" -> Color(0xFF212121)
        "red" -> Color(0xFFF44336)
        "blue" -> Color(0xFF2196F3)
        "pink" -> Color(0xFFE91E63)
        "orange" -> Color(0xFFFF9800)
        else -> Color.Gray
    }
}
