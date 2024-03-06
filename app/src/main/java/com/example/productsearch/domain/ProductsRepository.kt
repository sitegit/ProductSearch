package com.example.productsearch.domain

import com.example.productsearch.data.ProductResponseDto

interface ProductsRepository {

    suspend fun getProducts(): ProductResponseDto
}