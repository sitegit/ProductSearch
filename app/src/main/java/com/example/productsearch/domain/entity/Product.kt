package com.example.productsearch.domain.entity

data class Product(
    val id: Int,
    val title: String,
    val description: String,
    val price: Double,
    val discountPercentage: Double,
    val rating: Double,
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
}