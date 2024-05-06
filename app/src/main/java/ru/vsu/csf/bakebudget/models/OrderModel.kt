package ru.vsu.csf.bakebudget.models

data class OrderModel(
    val status: Int,
    val product: ProductModel,
    val finalPrice: Int,
    val weight: Int,
)
