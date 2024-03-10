package com.example.productsearch.domain.usecase

import com.example.productsearch.domain.ProductsRepository
import javax.inject.Inject

class LoadCategoryDataUseCase @Inject constructor(
    private val repository: ProductsRepository
) {

    suspend operator fun invoke(category: String) {
        repository.loadProductsByCategory(category)
    }
}