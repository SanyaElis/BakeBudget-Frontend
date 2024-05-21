package ru.vsu.csf.bakebudget.models

data class OrderModel(
    val id : Int,
    var status: Int,
    val product: ProductModel,
    val finalPrice: Double,
    val weight: Int,
)
