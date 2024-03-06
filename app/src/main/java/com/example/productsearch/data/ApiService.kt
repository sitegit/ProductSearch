package com.example.productsearch.data

import retrofit2.http.GET

interface ApiService {

    @GET("products")
    suspend fun loadProducts(): ProductResponseDto
}