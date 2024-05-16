package ru.vsu.csf.bakebudget.models.response

data class IngredientResponseModel(
    val id : Int,
    val name : String,
    val weight : Int,
    val cost : Int
)
