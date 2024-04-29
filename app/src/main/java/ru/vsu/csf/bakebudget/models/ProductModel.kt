package ru.vsu.csf.bakebudget.models

data class ProductModel(
    val iconId : Int,
    var name : String,
    val ingredients: MutableList<IngredientInProductModel>,
    val outgoings: MutableList<OutgoingModel>,
    var estWeight: Int
)