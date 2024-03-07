package com.example.productsearch.data

import android.util.Log
import com.example.productsearch.data.mapper.toEntities
import com.example.productsearch.data.network.ApiService
import com.example.productsearch.domain.PagingData
import com.example.productsearch.domain.ProductsRepository
import com.example.productsearch.domain.entity.Product
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.retry
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

    override fun getProducts(): Flow<PagingData<Product>> = flow {
        nextDataNeededEvents.emit(Unit)
        nextDataNeededEvents.collect {

            try {
                val response = apiService.loadProducts(currentPage * pageSize, pageSize)
                _products.addAll(response.products.toEntities())
                emit(PagingData(products, response.total, response.skip + pageSize))
                currentPage++
            } catch (e: Exception) {
                Log.i("MyTag", e.toString())
                emit(PagingData(listOf(), 0, 0, e))
            }
        }
    }

    override suspend fun loadNextData() {
        nextDataNeededEvents.emit(Unit)
    }
}