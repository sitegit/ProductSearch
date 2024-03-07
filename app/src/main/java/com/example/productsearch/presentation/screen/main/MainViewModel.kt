package com.example.productsearch.presentation.screen.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide.init
import com.example.productsearch.data.network.dto.ProductResponseDto
import com.example.productsearch.domain.GetProductsUseCase
import com.example.productsearch.domain.entity.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase
) : ViewModel() {

    private val products = mutableStateListOf<Product>()
    private var page by mutableIntStateOf(20)
    val canPaginate by mutableStateOf(false)

    var state by mutableStateOf<ProductState>(ProductState.Initial)

    init {
        getProducts()
    }

    fun getProducts() {
        viewModelScope.launch {
            if (page == 0 || (page != 0 && canPaginate) && state == ProductState.Initial) {
                state = if (page == 0) ProductState.Loading else ProductState.Paginating

                getProductsUseCase(page).collect {

                }
            }
        }
    }










}