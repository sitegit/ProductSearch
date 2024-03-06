package com.example.productsearch.domain

import com.example.productsearch.data.ProductResponseDto
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(
    private val repository: ProductsRepository
) {

    suspend operator fun invoke(): ProductResponseDto {
        return repository.getProducts()
    }
}