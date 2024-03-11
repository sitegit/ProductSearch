package com.example.productsearch.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.productsearch.presentation.navigation.Screen.Companion.KEY_DETAIL_ID

@Composable
fun AppNavGraph(
    navHostController: NavHostController,
    mainScreenContent: @Composable () -> Unit,
    detailScreenContent: @Composable (Int) -> Unit,
    searchScreenContent: @Composable () -> Unit
) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.Main.route,
    ) {

        composable(Screen.Main.route) {
            mainScreenContent()
        }

        composable(
            route = Screen.Detail.route,
            arguments = listOf(
                navArgument(KEY_DETAIL_ID) {
                    type = NavType.IntType
                }
            )
        ) {
            val itemId = it.arguments?.getInt(KEY_DETAIL_ID) ?: 0
            detailScreenContent(itemId)
        }

        composable(Screen.Search.route) {
            searchScreenContent()
        }
    }
}