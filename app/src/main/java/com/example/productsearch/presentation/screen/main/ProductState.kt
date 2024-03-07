package com.example.productsearch.presentation.screen.main

sealed class ProductState {

    data object Initial : ProductState()
    data object Loading : ProductState()
    data object Paginating : ProductState()
    data object Error : ProductState()
    data object ContentLoaded : ProductState()
}