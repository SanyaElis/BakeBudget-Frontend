package ru.vsu.csf.bakebudget.models.request

data class IngredientInProductRequestModel(
    val ingredientId: Int,
    val productId: Int,
    var weight : Int
)
