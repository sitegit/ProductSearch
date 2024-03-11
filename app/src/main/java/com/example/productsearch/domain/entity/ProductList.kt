package com.example.productsearch.domain.entity

data class ProductList(
    val products: List<Product>,
    val total: Int
)