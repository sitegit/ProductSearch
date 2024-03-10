package com.example.productsearch.presentation.navigation

sealed class Screen(
    val route: String
) {

    data object Main : Screen(ROUTE_MAIN)

    data object Detail : Screen(ROUTE_DETAIL) {
        private const val ROUTE_FOR_ARGS = "detail"

        fun getRouteWithArgs(itemId: Int, isFlag: Boolean): String {
            return "$ROUTE_FOR_ARGS/$itemId/$isFlag"
        }
    }

    data object Search : Screen(ROUTE_SEARCH)

    companion object {
        const val KEY_DETAIL_ID = "key_product_id"
        const val KEY_DETAIL_FLAG = "key_product_flag"

        const val ROUTE_MAIN = "main"
        const val ROUTE_DETAIL = "detail/{$KEY_DETAIL_ID}/{$KEY_DETAIL_FLAG}"
        const val ROUTE_SEARCH = "search"
    }
}