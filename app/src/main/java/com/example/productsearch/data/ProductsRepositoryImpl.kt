package com.example.productsearch.data

import com.example.productsearch.domain.ProductsRepository
import javax.inject.Inject

class ProductsRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : ProductsRepository {

    override suspend fun getProducts(): ProductResponseDto {
        return apiService.loadProducts()
    }
}