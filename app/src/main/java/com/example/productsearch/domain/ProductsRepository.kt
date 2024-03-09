package com.example.productsearch.domain

import com.example.productsearch.domain.entity.Product
import kotlinx.coroutines.flow.StateFlow

interface ProductsRepository {

    fun getProducts(): StateFlow<Result>

    suspend fun loadNextData()

    suspend fun getDetailProductInfo(itemId: Int): Product
}