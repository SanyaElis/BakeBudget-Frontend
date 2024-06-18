package ru.vsu.csf.bakebudget.models

data class IngredientInProductModel(
    val ingredientId : Int,
    var productId : Int,
    var name: String,
    var weight: Int
)
