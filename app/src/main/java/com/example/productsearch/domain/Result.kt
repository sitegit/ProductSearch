package com.example.productsearch.domain

sealed class Result {

    data object Initial : Result()

    data object Loading : Result()

    data class Success<T>(val data: T) : Result()

    data class Error(val exception: Exception) : Result()
}