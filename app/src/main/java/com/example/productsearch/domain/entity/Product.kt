package com.example.productsearch.domain.entity

import androidx.compose.runtime.Immutable
import kotlin.math.round

@Immutable
data class Product(
    val id: Int,
    val title: String,
    val description: String,
    val price: Float,
    val discountPercentage: Float,
    val rating: Float,
    val stock: Int,
    val brand: String,
    val category: String,
    val thumbnail: String,
    val images: List<String>
) {
    fun getPriceWithDiscount(): Int {
        val discountAmount = price * (discountPercentage / 100)
        return (price - discountAmount).toInt()
    }

    fun getRoundedRating(): Float {
        return round(rating * 10) / 10
    }
}