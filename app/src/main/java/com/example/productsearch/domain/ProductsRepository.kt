package com.example.productsearch.domain

import com.example.productsearch.domain.entity.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface ProductsRepository {

    fun getProducts(): Flow<PagingData<Product>>

    suspend fun loadNextData()
}