package com.mini4wd.lab.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mini4wd.lab.ui.screens.GaragePlaceholder
import com.mini4wd.lab.ui.screens.RpmPlaceholder
import com.mini4wd.lab.ui.screens.StatsPlaceholder
import com.mini4wd.lab.ui.screens.TimerPlaceholder

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Garage.route,
        modifier = modifier
    ) {
        composable(Screen.Garage.route) {
            GaragePlaceholder()
        }
        composable(Screen.Timer.route) {
            TimerPlaceholder()
        }
        composable(Screen.Rpm.route) {
            RpmPlaceholder()
        }
        composable(Screen.Stats.route) {
            StatsPlaceholder()
        }
    }
}
