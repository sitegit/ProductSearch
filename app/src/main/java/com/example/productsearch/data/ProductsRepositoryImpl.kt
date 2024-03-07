package com.example.productsearch.data

import com.example.productsearch.data.mapper.toEntities
import com.example.productsearch.data.network.ApiService
import com.example.productsearch.domain.ProductsRepository
import com.example.productsearch.domain.entity.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class ProductsRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : ProductsRepository {

    override fun getProducts(page: Int): Flow<List<Product>> = flow {
        try {
            emit(apiService.loadProducts(page).products.toEntities())
        } catch(e: Exception) {
            emit(listOf())
        }
    }
}