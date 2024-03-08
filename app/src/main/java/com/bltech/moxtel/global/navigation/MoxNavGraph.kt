package com.bltech.moxtel.global.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bltech.moxtel.gallery.ui.Gallery

@Composable
fun MoxNavGraph() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = MoxRoutes.HOME ) {
        composable(MoxRoutes.HOME) {
            Gallery()
        }
    }
}
