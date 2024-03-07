package com.example.productsearch.domain

import com.example.productsearch.domain.entity.Product
import kotlinx.coroutines.flow.Flow

interface ProductsRepository {

    fun getProducts(page: Int): Flow<List<Product>>
}