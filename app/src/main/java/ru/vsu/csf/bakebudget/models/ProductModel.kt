package ru.vsu.csf.bakebudget.models

import android.net.Uri

data class ProductModel(
    var id : Int,
    var uri: Uri?,
    val iconId : Int,
    var name : String,
    val ingredients: MutableList<IngredientInProductModel>,
    val outgoings: MutableList<OutgoingModel>,
    var estWeight: Int,
    var url: String?
)