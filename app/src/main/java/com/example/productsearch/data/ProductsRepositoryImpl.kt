package com.example.productsearch.data

import com.example.productsearch.data.mapper.toEntities
import com.example.productsearch.data.network.ApiService
import com.example.productsearch.domain.ProductsRepository
import com.example.productsearch.domain.Result
import com.example.productsearch.domain.entity.Product
import com.example.productsearch.domain.entity.ProductList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class ProductsRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : ProductsRepository {
    private var currentPage = 0
    private val pageSize = 20

    private val _products = mutableListOf<Product>()
    private val products: List<Product>
        get() = _products.toList()

    private val nextDataNeededEvents = MutableSharedFlow<Unit>(replay = 1)

    private val productsFlow = flow {
        nextDataNeededEvents.emit(Unit)
        nextDataNeededEvents.collect {
            if (products.isEmpty()) emit(Result.Loading)
            try {
                val response = apiService.loadProducts(currentPage * pageSize, pageSize)
                _products.addAll(response.products.toEntities())
                emit(Result.Success(ProductList(products, response.total, response.skip + pageSize)))
                currentPage++
            } catch (e: Exception) {
                emit(Result.Error(e))
            }
        }
    }

    override fun getProducts(): StateFlow<Result> = productsFlow
        .stateIn(
            scope = CoroutineScope(Dispatchers.Default),
            started = SharingStarted.Lazily,
            initialValue = Result.Initial
        )

    override suspend fun loadNextData() {
        nextDataNeededEvents.emit(Unit)
    }
}
