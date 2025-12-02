package com.mini4wd.lab.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun TimerPlaceholder() {
    PlaceholderContent("Timer - Coming in M3")
}

@Composable
fun RpmPlaceholder() {
    PlaceholderContent("RPM - Coming in M4")
}

@Composable
fun StatsPlaceholder() {
    PlaceholderContent("Stats - Coming in M5")
}

@Composable
private fun PlaceholderContent(text: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.headlineMedium
        )
    }
}
