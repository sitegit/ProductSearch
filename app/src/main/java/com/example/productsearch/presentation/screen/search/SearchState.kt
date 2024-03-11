package com.example.productsearch.presentation.screen.search

import com.example.productsearch.domain.entity.ProductList

sealed class SearchState {

    data object Initial : SearchState()

    data object Loading : SearchState()

    data object Error : SearchState()

    data object EmptyResult : SearchState()

    data class SuccessLoaded(val result: ProductList) : SearchState()
}