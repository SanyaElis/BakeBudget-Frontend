package ru.vsu.csf.bakebudget.models.request

import ru.vsu.csf.bakebudget.models.IngredientInProductModel
import ru.vsu.csf.bakebudget.models.OutgoingModel

data class ProductRequestModel(
    var name : String,
    var weight: Int
)
