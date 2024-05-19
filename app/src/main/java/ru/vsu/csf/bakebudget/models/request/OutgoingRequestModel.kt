package ru.vsu.csf.bakebudget.models.request

data class OutgoingRequestModel(
    val name: String,
    val cost: Int,
    var productId: Int
)
