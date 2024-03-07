package com.example.productsearch.presentation.screen.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.productsearch.domain.GetProductsUseCase
import com.example.productsearch.domain.LoadNextDataUseCase
import com.example.productsearch.domain.entity.Product
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    private val loadNextDataUseCase: LoadNextDataUseCase
) : ViewModel() {

    private var totalItems = 0
    private var currentPage = 0
    private val loadNextDataFlow = MutableSharedFlow<ProductState>()

    private val productsFlow: StateFlow<List<Product>> = getProductsUseCase().map {
        totalItems = it.total
        currentPage = it.skip
        it.data
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList()
    )

    val screenState = productsFlow
        .filter { it.isNotEmpty() }
        .map {
            ProductState.Products(it) as ProductState }
        .onStart {
            emit(ProductState.Loading)
        }.mergeWith(loadNextDataFlow)

    fun loadNextProducts() {
        if (currentPage == totalItems) return
        viewModelScope.launch {
            loadNextDataFlow.emit(
                ProductState.Products(
                    products = productsFlow.value,
                    nextDataIsLoading = true
                )
            )
            loadNextDataUseCase()
        }
    }

    private fun <T> Flow<T>.mergeWith(another: Flow<T>): Flow<T> {
        return merge(this, another)
    }
}
