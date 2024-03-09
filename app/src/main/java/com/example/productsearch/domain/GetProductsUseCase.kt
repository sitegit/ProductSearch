package com.example.productsearch.domain

import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(
    private val repository: ProductsRepository
) {

    operator fun invoke(): StateFlow<Result> = repository.getProducts()
}