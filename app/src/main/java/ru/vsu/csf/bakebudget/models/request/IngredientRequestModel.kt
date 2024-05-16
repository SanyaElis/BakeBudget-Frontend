package ru.vsu.csf.bakebudget.models.request

data class IngredientRequestModel(
    val name : String,
    val weight : Int,
    val cost : Int
)
