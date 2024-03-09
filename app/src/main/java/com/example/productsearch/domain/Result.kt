package com.example.productsearch.domain

import com.example.productsearch.domain.entity.ProductList

sealed class Result {

    data object Initial : Result()

    data object Loading : Result()

    data class Success(val productList: ProductList) : Result()

    data class Error(val exception: Exception) : Result()
}