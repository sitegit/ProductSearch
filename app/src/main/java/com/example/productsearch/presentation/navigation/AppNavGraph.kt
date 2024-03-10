package com.example.productsearch.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.productsearch.presentation.navigation.Screen.Companion.KEY_DETAIL_FLAG
import com.example.productsearch.presentation.navigation.Screen.Companion.KEY_DETAIL_ID

@Composable
fun AppNavGraph(
    navHostController: NavHostController,
    mainScreenContent: @Composable () -> Unit,
    detailScreenContent: @Composable (Int, Boolean) -> Unit,
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
                },
                navArgument(KEY_DETAIL_FLAG) { type = NavType.BoolType }
            )
        ) {
            val itemId = it.arguments?.getInt(KEY_DETAIL_ID) ?: 0
            val isFlag = it.arguments?.getBoolean(KEY_DETAIL_FLAG) ?: false
            detailScreenContent(itemId, isFlag)
        }

        composable(Screen.Search.route) {
            searchScreenContent()
        }
    }
}