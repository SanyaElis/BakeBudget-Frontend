package ru.vsu.csf.bakebudget.models

data class IngredientInProductModel(
    val ingredientId : Int,
    var productId : Int,
    val name: String,
    val weight: Int
)
