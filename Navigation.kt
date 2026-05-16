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

// ─────────────────────────────────────────────────────────────
//  VELOCITÀ ELITE — NAVIGATION ROUTES
//
//  All screen destinations are declared as constants inside the
//  sealed Routes class so they are referenced from a single
//  source of truth.  No magic strings elsewhere.
// ─────────────────────────────────────────────────────────────

/** Sealed class holding every named navigation route in the app. */
sealed class Routes(val path: String) {

    /** Screen 1 — Animated ignition splash */
    object Splash       : Routes("splash")

    /** Screen 2 — VIP Enrollment / Request Access */
    object Enrollment   : Routes("enrollment")

    /** Screen 3 — Owner Login */
    object Login        : Routes("login")

    /** Screen 4 — The Showroom (bike list) */
    object Showroom     : Routes("showroom")

    /**
     * Screen 5 — Bike Configuration (detail view).
     * Accepts a [bikeId] path argument so the correct model data
     * is loaded.
     *
     * Full path example: "bike_config/3"
     */
    object BikeConfig   : Routes("bike_config/{bikeId}") {
        /** Helper to build the navigation path with a concrete id. */
        fun withId(bikeId: Int) = "bike_config/$bikeId"
    }

    /** Screen 6 — Concierge Service (booking) */
    object Concierge    : Routes("concierge")
}

// ─────────────────────────────────────────────────────────────
//  NAV HOST
//
//  Single function that wires every composable destination into
//  the navigation graph.  Called once from MainActivity.
// ─────────────────────────────────────────────────────────────

/**
 * Sets up the full [NavHost] for Velocità Elite.
 *
 * @param navController The app-scoped controller provided by MainActivity.
 * @param modifier      Optional [Modifier] passed down to the NavHost container.
 */
@Composable
fun VelocitaNavHost(
    navController : NavHostController,
    modifier      : Modifier = Modifier
) {
    // Shared ViewModel scoped to the NavHost so all screens read
    // from the same data and state.
    val viewModel: MainViewModel = viewModel()

    NavHost(
        navController    = navController,
        startDestination = Routes.Splash.path,
        modifier         = modifier
    ) {

        // ── Screen 1 : Splash ────────────────────────────────
        composable(route = Routes.Splash.path) {
            SplashScreen(
                onAnimationFinished = {
                    // Replace the splash on the back-stack so the user
                    // cannot navigate back to it with the hardware button.
                    navController.navigate(Routes.Showroom.path) {
                        popUpTo(Routes.Splash.path) { inclusive = true }
                    }
                }
            )
        }

        // ── Screen 2 : VIP Enrollment ────────────────────────
        composable(route = Routes.Enrollment.path) {
            EnrollmentScreen(
                viewModel  = viewModel,
                onEnrolled = {
                    // After successful form submission route to Showroom
                    navController.navigate(Routes.Showroom.path) {
                        popUpTo(Routes.Enrollment.path) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.navigate(Routes.Login.path)
                }
            )
        }

        // ── Screen 3 : Owner Login ───────────────────────────
        composable(route = Routes.Login.path) {
            LoginScreen(
                viewModel   = viewModel,
                onLoggedIn  = {
                    navController.navigate(Routes.Showroom.path) {
                        popUpTo(Routes.Login.path) { inclusive = true }
                    }
                },
                onNavigateToEnrollment = {
                    navController.navigate(Routes.Enrollment.path)
                }
            )
        }

        // ── Screen 4 : The Showroom ──────────────────────────
        composable(route = Routes.Showroom.path) {
            ShowroomScreen(
                viewModel = viewModel,
                onBikeSelected = { bikeId ->
                    // Navigate to the detail screen, injecting the bike id
                    navController.navigate(Routes.BikeConfig.withId(bikeId))
                },
                onConciergeRequested = {
                    navController.navigate(Routes.Concierge.path)
                },
                onAccountRequested = {
                    navController.navigate(Routes.Login.path)
                }
            )
        }

        // ── Screen 5 : Bike Configuration ───────────────────
        composable(
            route     = Routes.BikeConfig.path,
            arguments = listOf(
                navArgument("bikeId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            // Extract the bikeId from the navigation arguments
            val bikeId = backStackEntry.arguments?.getInt("bikeId") ?: 0

            BikeConfigScreen(
                bikeId    = bikeId,
                viewModel = viewModel,
                onBack    = { navController.popBackStack() },
                onBookConcierge = {
                    navController.navigate(Routes.Concierge.path)
                }
            )
        }

        // ── Screen 6 : Concierge Service ────────────────────
        composable(route = Routes.Concierge.path) {
            ConciergeScreen(
                viewModel = viewModel,
                onBack    = { navController.popBackStack() }
            )
        }
    }
}
