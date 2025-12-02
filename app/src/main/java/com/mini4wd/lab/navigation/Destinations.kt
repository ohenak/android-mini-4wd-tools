package com.mini4wd.lab.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Timer
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    data object Garage : Screen("garage", "Garage", Icons.Default.DirectionsCar)
    data object Timer : Screen("timer", "Timer", Icons.Default.Timer)
    data object Rpm : Screen("rpm", "RPM", Icons.Default.Speed)
    data object Stats : Screen("stats", "Stats", Icons.Default.Analytics)
}

object ProfileRoutes {
    const val CREATE = "profile/create"
    const val EDIT = "profile/edit/{profileId}"
    const val DETAIL = "profile/detail/{profileId}"

    fun editRoute(profileId: Long) = "profile/edit/$profileId"
    fun detailRoute(profileId: Long) = "profile/detail/$profileId"
}

val bottomNavScreens = listOf(
    Screen.Garage,
    Screen.Timer,
    Screen.Rpm,
    Screen.Stats
)
