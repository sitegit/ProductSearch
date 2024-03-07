package com.example.productsearch.data.mapper

import com.example.productsearch.data.network.dto.ProductDto
import com.example.productsearch.domain.entity.Product

fun ProductDto.toEntity(): Product = Product(
    id = id,
    title = title,
    description = description,
    price = price,
    discountPercentage = discountPercentage,
    rating = rating,
    stock = stock,
    brand = brand,
    category = category,
    thumbnail = thumbnail,
    images = images
)

fun List<ProductDto>.toEntities(): List<Product> = map {
    it.toEntity()
}