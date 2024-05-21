package ru.vsu.csf.bakebudget.models

import android.net.Uri
import androidx.compose.runtime.snapshots.SnapshotStateList

data class ProductModel(
    var id : Int,
    var uri: Uri?,
    val iconId : Int,
    var name : String,
    val ingredients: SnapshotStateList<IngredientInProductModel>,
    val outgoings: MutableList<OutgoingModel>,
    var estWeight: Int
)