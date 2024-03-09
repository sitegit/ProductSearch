package com.example.productsearch.domain.usecase

import com.example.productsearch.domain.ProductsRepository
import javax.inject.Inject

class LoadNextDataUseCase @Inject constructor(
    private val repository: ProductsRepository
) {

    suspend operator fun invoke() {
        repository.loadNextData()
    }
}