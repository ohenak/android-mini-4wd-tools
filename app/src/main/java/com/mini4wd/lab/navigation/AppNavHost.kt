package com.mini4wd.lab.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.mini4wd.lab.feature.profile.ui.detail.ProfileDetailScreen
import com.mini4wd.lab.feature.profile.ui.edit.ProfileEditScreen
import com.mini4wd.lab.feature.profile.ui.garage.GarageScreen
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
        // Bottom nav destinations
        composable(Screen.Garage.route) {
            GarageScreen(
                onNavigateToProfile = { profileId ->
                    navController.navigate(ProfileRoutes.detailRoute(profileId))
                },
                onNavigateToCreateProfile = {
                    navController.navigate(ProfileRoutes.CREATE)
                }
            )
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

        // Profile Detail
        composable(
            route = ProfileRoutes.DETAIL,
            arguments = listOf(navArgument("profileId") { type = NavType.LongType })
        ) {
            ProfileDetailScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToEdit = { profileId ->
                    navController.navigate(ProfileRoutes.editRoute(profileId))
                },
                onNavigateToTimer = {
                    // TODO: Navigate to timer with profile selected
                    navController.navigate(Screen.Timer.route)
                },
                onNavigateToRpm = {
                    // TODO: Navigate to RPM with profile selected
                    navController.navigate(Screen.Rpm.route)
                }
            )
        }

        // Profile Create
        composable(ProfileRoutes.CREATE) {
            ProfileEditScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // Profile Edit
        composable(
            route = ProfileRoutes.EDIT,
            arguments = listOf(navArgument("profileId") { type = NavType.LongType })
        ) {
            ProfileEditScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
