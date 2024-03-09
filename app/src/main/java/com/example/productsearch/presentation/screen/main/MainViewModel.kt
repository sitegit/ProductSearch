package com.example.productsearch.presentation.screen.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.productsearch.domain.GetProductsUseCase
import com.example.productsearch.domain.LoadNextDataUseCase
import com.example.productsearch.domain.Result
import com.example.productsearch.domain.entity.Product
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
    private val loadNextDataUseCase: LoadNextDataUseCase
) : ViewModel() {

    private var totalItems = 0
    private var currentPage = 0
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
                is Result.Initial -> {}
                is Result.Loading -> {
                    ProductState.Loading
                }
                is Result.Error -> {
                    Log.i("MyTag", "error")
                    _toastEvents.emit(it.exception.message.toString())
                    _errorState.value = true
                    if (list.isNotEmpty()) {
                        ProductState.Products(list)
                    } else {
                        ProductState.Error
                    }
                }
                is Result.Success -> {
                    Log.i("MyTag", "success")
                    totalItems = it.productList.total
                    currentPage = it.productList.skip
                    val products = it.productList.products
                    list.addAll(products)
                    _errorState.value = false
                    ProductState.Products(products)
                }
            }
        }
        .mergeWith(loadNextDataFlow)

    fun loadNextProducts() {

        if (currentPage == totalItems && list.isNotEmpty()) return
        Log.i("MyTag", "loadNextProducts()")

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

    private fun <T> Flow<T>.mergeWith(another: Flow<T>): Flow<T> {
        return merge(this, another)
    }
}
