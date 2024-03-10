package com.example.productsearch.data

import android.util.Log
import com.example.productsearch.data.mapper.toEntities
import com.example.productsearch.data.mapper.toEntity
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
    private var currentCategory: String? = null

    private val _products = mutableListOf<Product>()
    private val products: List<Product>
        get() = _products.toList()

    private val nextDataNeededEvents = MutableSharedFlow<Unit>(replay = 1)

    private val productsFlow = flow {
        nextDataNeededEvents.emit(Unit)
        nextDataNeededEvents.collect {
            if (products.isEmpty()) emit(Result.Loading)
            try {
                val response = if (currentCategory == null) {
                    apiService.loadProducts(currentPage * pageSize, pageSize)
                } else {
                    apiService.loadCategory(currentCategory!!, currentPage * pageSize, pageSize)
                }
                _products.addAll(response.products.toEntities())
                emit(Result.Success(ProductList(products, response.total, response.skip + pageSize)))
                currentPage++
            } catch (e: Exception) {
                Log.i("MyTag", e.message.toString())
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

    override suspend fun loadProductsByCategory(category: String) {
        currentCategory = category
        currentPage = 0
        _products.clear()
        loadNextData()
    }

    override suspend fun loadNextData() {
        nextDataNeededEvents.emit(Unit)
    }

    override suspend fun getDetailProductInfo(itemId: Int): Product {
        return apiService.getDetailProductInfo(itemId).toEntity()
    }

    override suspend fun searchProducts(name: String): ProductList {
        val result = apiService.searchProducts(name).products.toEntities()
        val filteredResult = result.filter { it.title.contains(name, ignoreCase = true) }
        return ProductList(filteredResult, 0, 0)
    }
}
