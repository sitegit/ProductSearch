package com.example.productsearch.domain.usecase

import com.example.productsearch.domain.ProductsRepository
import com.example.productsearch.domain.Result
import javax.inject.Inject

class GetDetailInfoUseCase @Inject constructor(
    private val repository: ProductsRepository
) {

    suspend operator fun invoke(itemId: Int): Result {
        return try {
            Result.Success(repository.getDetailProductInfo(itemId))
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}