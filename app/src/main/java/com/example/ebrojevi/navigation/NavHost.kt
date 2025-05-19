package com.example.ebrojevi.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ebrojevi.additives.presentation.FoodAdditivesScreenRoot
import com.example.ebrojevi.navigation.Routes.ADDITIVES_ROUTE
import com.example.ebrojevi.navigation.Routes.CAMERA_ROUTE
import com.example.ebrojevi.ui.camera.CameraScreenRoot
import com.example.ebrojevi.ui.loading.LoadingScreenRoot

@Composable
fun eNumbersNavHost() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = CAMERA_ROUTE) {
        composable(CAMERA_ROUTE) {
            CameraScreenRoot(navController = navController)
        }
        composable(ADDITIVES_ROUTE) {
            FoodAdditivesScreenRoot(navController = navController)
        }
        composable(
            route = "${Routes.LOADING_ROUTE}/{queryString}",
            arguments = listOf(navArgument("queryString") { type = NavType.StringType })
        ) { backStackEntry ->
            val queryString = backStackEntry.arguments?.getString("queryString") ?: ""
            LoadingScreenRoot(navController = navController, queryString = queryString)
        }
    }
}
