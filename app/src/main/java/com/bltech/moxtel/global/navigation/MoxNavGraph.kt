package com.bltech.moxtel.global.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.bltech.moxtel.features.ui.details.DetailsScreen
import com.bltech.moxtel.features.ui.home.HomeScreen
import com.bltech.moxtel.features.ui.player.VideoView
import com.bltech.moxtel.global.TitleSetter

@Composable
fun MoxNavGraph(navController: NavHostController, titleSetter: TitleSetter) {
    NavHost(navController = navController, startDestination = MoxRoutes.HOME) {
        composable(MoxRoutes.HOME) {
            HomeScreen(navController = navController, titleSetter = titleSetter)
        }
        composable(
            "${MoxRoutes.DETAILS}/{${MoxNavArgKey.MOVIE_ID}}",
            arguments = listOf(navArgument(MoxNavArgKey.MOVIE_ID) { type = NavType.IntType })
        ) { backStackEntry ->
            backStackEntry.arguments?.getInt(MoxNavArgKey.MOVIE_ID)?.let {
                DetailsScreen(it, navController, titleSetter)
            }
        }
        composable(
            "${MoxRoutes.PLAYER}/{${MoxNavArgKey.MOVIE_ID}}",
            arguments = listOf(navArgument(MoxNavArgKey.MOVIE_ID) { type = NavType.IntType })
        ) { backStackEntry ->
            backStackEntry.arguments?.getInt(MoxNavArgKey.MOVIE_ID)?.let {
                VideoView(it) {
                    navController.popBackStack()
                }
            }
        }
    }
}
