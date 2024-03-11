package com.example.productsearch.presentation.screen.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.productsearch.domain.Result
import com.example.productsearch.domain.entity.Product
import com.example.productsearch.domain.usecase.GetDetailInfoUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class DetailViewModel @Inject constructor(
    private val getDetailInfoUseCase: GetDetailInfoUseCase
) : ViewModel() {

    private val _product = MutableStateFlow<DetailState>(DetailState.Initial)
    val product = _product.asStateFlow()

    fun getInfo(itemId: Int) {
        viewModelScope.launch {
            _product.value = DetailState.Loading
            when (val result = getDetailInfoUseCase(itemId)) {
                is Result.Error -> {
                    _product.value = DetailState.Error
                }
                Result.Initial -> {}
                Result.Loading -> {}
                is Result.Success<*> -> {
                    val item = result.data as Product
                    delay(1000)
                    _product.value = DetailState.Products(item)
                }
            }
        }
    }
}