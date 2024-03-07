package com.example.productsearch.domain

import javax.inject.Inject

class LoadNextDataUseCase @Inject constructor(
    private val repository: ProductsRepository
) {

    suspend operator fun invoke() {
        repository.loadNextData()
    }
}