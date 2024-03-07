package com.example.productsearch.domain

import com.example.productsearch.domain.entity.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(
    private val repository: ProductsRepository
) {

    operator fun invoke(): Flow<PagingData<Product>> {
        return repository.getProducts()
    }
}