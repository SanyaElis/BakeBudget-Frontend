package ru.vsu.csf.bakebudget.models.request

data class CalculationRequestModel(
    val extraExpenses : Int,
    val marginFactor : Double,
    val finalWeight : Int,
    val productId : Int
)
