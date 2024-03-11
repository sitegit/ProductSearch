package com.example.productsearch.presentation.screen.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.productsearch.domain.Result
import com.example.productsearch.domain.entity.Product
import com.example.productsearch.domain.entity.ProductList
import com.example.productsearch.domain.usecase.GetProductsUseCase
import com.example.productsearch.domain.usecase.LoadCategoryDataUseCase
import com.example.productsearch.domain.usecase.LoadNextDataUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    private val loadNextDataUseCase: LoadNextDataUseCase,
    private val loadCategoryDataUseCase: LoadCategoryDataUseCase
) : ViewModel() {

    private var totalItems = 0
    private var currentItems = 0
    private val loadNextDataFlow = MutableSharedFlow<ProductState>()
    private val list = mutableListOf<Product>()

    private val _errorState = MutableStateFlow(false)
    val errorState: StateFlow<Boolean> = _errorState

    private val _toastEvents = MutableSharedFlow<String>()
    val toastEventsFlow = _toastEvents.asSharedFlow()

    private val productsFlow: StateFlow<Result> = getProductsUseCase()

    val screenState = productsFlow
        .map {
            when (it) {
                is Result.Initial -> ProductState.Initial
                is Result.Loading -> {
                    ProductState.Loading
                }
                is Result.Error -> {
                    _toastEvents.emit(it.exception.message.toString())
                    _errorState.value = true
                    if (list.isNotEmpty()) {
                        ProductState.Products(list)
                    } else {
                        ProductState.Error
                    }
                }
                is Result.Success<*> -> {
                    val data = it.data as ProductList
                    val products = data.products
                    totalItems = data.total
                    currentItems = products.size
                    list.addAll(products)
                    _errorState.value = false
                    ProductState.Products(products)
                }
            }
        }
        .mergeWith(loadNextDataFlow)

    fun loadNextProducts() {
        if (currentItems == totalItems && list.isNotEmpty()) return

        viewModelScope.launch {
            loadNextDataFlow.emit(
                ProductState.Products(
                    products = list,
                    nextDataIsLoading = true
                )
            )
            _errorState.value = false
            loadNextDataUseCase()
        }
    }

    fun loadProductsByCategory(category: String) {
        viewModelScope.launch {
            list.clear()
            loadCategoryDataUseCase(category)
        }
    }

    private fun <T> Flow<T>.mergeWith(another: Flow<T>): Flow<T> {
        return merge(this, another)
    }
}
