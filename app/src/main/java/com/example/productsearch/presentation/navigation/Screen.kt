package com.example.productsearch.presentation.navigation

sealed class Screen(
    val route: String
) {

    data object Main : Screen(ROUTE_MAIN)

    data object Detail : Screen(ROUTE_DETAIL) {
        private const val ROUTE_FOR_ARGS = "detail"

        fun getRouteWithArgs(itemId: Int): String {
            return "$ROUTE_FOR_ARGS/$itemId"
        }
    }

    companion object {
        const val KEY_DETAIL = "key_to_do_list"

        const val ROUTE_MAIN = "main"
        const val ROUTE_DETAIL = "detail/{$KEY_DETAIL}"
    }
}