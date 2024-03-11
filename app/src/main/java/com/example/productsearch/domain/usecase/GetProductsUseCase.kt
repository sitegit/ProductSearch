package com.example.productsearch.domain.usecase

import com.example.productsearch.domain.ProductsRepository
import com.example.productsearch.domain.Result
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(
    private val repository: ProductsRepository
) {

    operator fun invoke(): StateFlow<Result> = repository.getProducts()
}