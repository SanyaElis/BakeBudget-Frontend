package ru.vsu.csf.bakebudget.models

data class GoodModel(
    val iconId : Int,
    val name : String,
    val ingredients: MutableList<IngredientInRecipeModel>,
    val estWeight: Int
)