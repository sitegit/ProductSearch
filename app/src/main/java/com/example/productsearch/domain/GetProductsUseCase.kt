package com.example.productsearch.domain

import com.example.productsearch.domain.entity.Product
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(
    private val repository: ProductsRepository
) {

    operator fun invoke(page: Int): Flow<List<Product>> {
        return repository.getProducts(page)
    }
}