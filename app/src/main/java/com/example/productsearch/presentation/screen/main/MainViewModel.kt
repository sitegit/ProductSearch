package com.example.productsearch.presentation.screen.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.productsearch.data.ProductResponseDto
import com.example.productsearch.domain.GetProductsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase
) : ViewModel() {

    private val _products = MutableStateFlow(ProductResponseDto(listOf(), 0, 0, 0))
    val products = _products.asStateFlow()

    init {
        viewModelScope.launch {
            _products.value = getProductsUseCase()
        }
    }
}