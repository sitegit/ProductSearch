package com.example.productsearch.domain

import kotlinx.coroutines.flow.StateFlow

interface ProductsRepository {

    fun getProducts(): StateFlow<Result>
    suspend fun loadNextData()
}