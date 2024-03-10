package com.example.productsearch.presentation.screen.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.productsearch.domain.Result
import com.example.productsearch.domain.entity.ProductList
import com.example.productsearch.domain.usecase.SearchProductsByNameUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    private val searchProductsByNameUseCase: SearchProductsByNameUseCase
) : ViewModel() {

    private val _products = MutableStateFlow<SearchState>(SearchState.Initial)
    val products = _products.asStateFlow()

    fun searchProduct(name: String) {
        viewModelScope.launch {
            _products.value = SearchState.Loading
            when(val result = searchProductsByNameUseCase(name)) {
                is Result.Error -> {
                    _products.value = SearchState.Error
                }
                Result.Initial -> {}
                Result.Loading -> {}
                is Result.Success<*> -> {
                    val item = result.data as ProductList
                    if (item.products.isEmpty()) {
                        _products.value = SearchState.EmptyResult
                    } else {
                        _products.value = SearchState.SuccessLoaded(item)
                    }
                }
            }
        }
    }
}