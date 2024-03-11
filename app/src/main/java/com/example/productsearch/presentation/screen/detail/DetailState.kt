package com.example.productsearch.presentation.screen.detail

import com.example.productsearch.domain.entity.Product

sealed class DetailState {
    data object Initial : DetailState()

    data object Error : DetailState()

    data object Loading : DetailState()

    data class Products(val product: Product) : DetailState()
}