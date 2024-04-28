package ru.vsu.csf.bakebudget.models

data class ProductModel(
    val iconId : Int,
    val name : String,
    val ingredients: MutableList<IngredientInProductModel>,
    val estWeight: Int
)