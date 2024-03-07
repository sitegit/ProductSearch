package com.example.productsearch.presentation.screen.main

import com.example.productsearch.domain.entity.Product

sealed class ProductState {

    data object Initial : ProductState()
    data object Loading : ProductState()
    data object Error : ProductState()
    data class Products(
        val products: List<Product>,
        val nextDataIsLoading: Boolean = false
    ) : ProductState()
}