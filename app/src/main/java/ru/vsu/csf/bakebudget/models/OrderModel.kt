package ru.vsu.csf.bakebudget.models

import java.time.LocalDate

data class OrderModel(
    val status: Int,
    val product: ProductModel,
    val costPrice: Int,
    val finalPrice: Int,
    val markup: Int,
    val extra: Int,
    val weight: Int,
    val completionDate: LocalDate
)
