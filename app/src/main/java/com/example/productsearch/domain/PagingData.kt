package com.example.productsearch.domain

data class PagingData<T>(
    val data: List<T>,
    val total: Int,
    val skip: Int,
    val error: Exception? = null
)
