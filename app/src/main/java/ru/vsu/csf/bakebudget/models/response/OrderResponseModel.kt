package ru.vsu.csf.bakebudget.models.response

data class OrderResponseModel(
    val id : Int,
    val name : String,
    val description : String,
    val status : String,
    val extraExpenses : Int,
    val finalWeight : Int,
    val marginFactor : Double,
    val creationDate : String,
    val finishDate : String
)
