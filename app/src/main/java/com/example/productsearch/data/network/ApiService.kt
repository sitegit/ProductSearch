package com.example.productsearch.data.network

import com.example.productsearch.data.network.dto.ProductDto
import com.example.productsearch.data.network.dto.ProductResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("products")
    suspend fun loadProducts(
        @Query("skip") page: Int,
        @Query("limit") limit: Int
    ): ProductResponseDto

    @GET("products/{id}")
    suspend fun getDetailProductInfo(
        @Path("id") itemId: Int
    ): ProductDto
}