package com.velocita.elite

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.velocita.elite.navigation.VelocitaNavHost
import com.velocita.elite.ui.theme.VelocitaEliteTheme

// ─────────────────────────────────────────────────────────────
//  VELOCITÀ ELITE — MAIN ACTIVITY
//
//  Single Activity Architecture.
//  Responsibilities:
//    1. Enable edge-to-edge rendering (status bar draws over content)
//    2. Apply the VelocitaEliteTheme to the entire composition tree
//    3. Instantiate a NavController and hand it to VelocitaNavHost
//
//  Everything else (screens, state, navigation logic) lives in
//  dedicated files — this class is intentionally thin.
// ─────────────────────────────────────────────────────────────

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Draw content behind status bar and navigation bar cutouts
        enableEdgeToEdge()

        setContent {
            VelocitaEliteTheme {
                // NavController: created here and passed down through
                // VelocitaNavHost; never recreated on recomposition.
                val navController = rememberNavController()

                VelocitaNavHost(
                    navController = navController,
                    modifier      = Modifier.fillMaxSize()
                )
            }
        }
    }
}
