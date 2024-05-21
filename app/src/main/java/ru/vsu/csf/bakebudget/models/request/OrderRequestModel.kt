package ru.vsu.csf.bakebudget.models.request

data class OrderRequestModel(
    val name : String,
    val description : String,
    val extraExpenses : Int,
    val finalWeight : Int,
    val marginFactor : Double,
    val creationDate : String,
    val finishDate : String,
    val productId : Int
)
