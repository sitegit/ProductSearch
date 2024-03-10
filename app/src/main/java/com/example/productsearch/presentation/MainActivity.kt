package com.example.productsearch.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.productsearch.presentation.navigation.AppNavGraph
import com.example.productsearch.presentation.navigation.Screen
import com.example.productsearch.presentation.screen.detail.DetailScreen
import com.example.productsearch.presentation.screen.main.MainScreen
import com.example.productsearch.presentation.ui.theme.ProductSearchTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ProductSearchTheme {
                val navHostController = rememberNavController()

                AppNavGraph(
                    navHostController = navHostController,
                    mainScreenContent = {
                        MainScreen {
                            navHostController.navigate(Screen.Detail.getRouteWithArgs(it))
                        }
                    },
                    detailScreenContent = {
                        DetailScreen(it) {
                            navHostController.popBackStack()
                        }
                    }
                )
            }
        }
    }
}