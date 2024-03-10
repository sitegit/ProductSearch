package com.example.productsearch.domain.usecase

import com.example.productsearch.domain.ProductsRepository
import com.example.productsearch.domain.Result
import com.example.productsearch.domain.entity.ProductList
import javax.inject.Inject

class SearchProductsByNameUseCase @Inject constructor(
    private val repository: ProductsRepository
) {

    suspend operator fun invoke(name: String): Result {
        if (name.isEmpty()) return Result.Success(ProductList(listOf(), 0, 0))
        return try {
            Result.Success(repository.searchProducts(name))
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}