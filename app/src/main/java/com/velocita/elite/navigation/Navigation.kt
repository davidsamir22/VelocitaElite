package com.velocita.elite.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.velocita.elite.screens.*
import com.velocita.elite.viewmodel.MainViewModel

sealed class Routes(val path: String) {
    object Splash       : Routes("splash")
    object Enrollment   : Routes("enrollment")
    object Login        : Routes("login")
    object Showroom     : Routes("showroom")
    object BikeConfig   : Routes("bike_config/{bikeId}") {
        fun withId(bikeId: Int) = "bike_config/$bikeId"
    }
    object Concierge    : Routes("concierge")
    object Profile      : Routes("profile")
}

@Composable
fun VelocitaNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val viewModel: MainViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Routes.Splash.path,
        modifier = modifier
    ) {
        composable(Routes.Splash.path) {
            SplashScreen(onAnimationFinished = {
                navController.navigate(Routes.Enrollment.path) {
                    popUpTo(Routes.Splash.path) { inclusive = true }
                }
            })
        }

        composable(Routes.Enrollment.path) {
            EnrollmentScreen(
                viewModel = viewModel,
                onEnrolled = {
                    navController.navigate(Routes.Login.path)
                },
                onNavigateToLogin = {
                    navController.navigate(Routes.Login.path)
                }
            )
        }

        composable(Routes.Login.path) {
            LoginScreen(
                viewModel = viewModel,
                onLoggedIn = {
                    navController.navigate(Routes.Showroom.path) {
                        popUpTo(Routes.Enrollment.path) { inclusive = true }
                    }
                },
                onNavigateToEnrollment = {
                    navController.navigate(Routes.Enrollment.path)
                }
            )
        }

        composable(Routes.Showroom.path) {
            ShowroomScreen(
                viewModel = viewModel,
                onBikeSelected = { bikeId ->
                    navController.navigate(Routes.BikeConfig.withId(bikeId))
                },
                onConciergeRequested = {
                    navController.navigate(Routes.Concierge.path)
                },
                onAccountRequested = {
                    navController.navigate(Routes.Profile.path)
                }
            )
        }

        composable(
            route = Routes.BikeConfig.path,
            arguments = listOf(navArgument("bikeId") { type = NavType.IntType })
        ) { backStackEntry ->
            val bikeId = backStackEntry.arguments?.getInt("bikeId") ?: 0
            BikeConfigScreen(
                bikeId = bikeId,
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onBookConcierge = {
                    navController.navigate(Routes.Concierge.path)
                }
            )
        }

        composable(Routes.Concierge.path) {
            ConciergeScreen(
                viewModel = viewModel,
                onBack    = { navController.popBackStack() }
            )
        }

        composable(Routes.Profile.path) {
            ProfileScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
