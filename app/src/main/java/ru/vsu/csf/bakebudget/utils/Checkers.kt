package ru.vsu.csf.bakebudget.utils

fun isWeightValid(weight: String) : Boolean {
    val value = weight.toIntOrNull()
    return !(value == null || value <= 0 || value > 100000)
}

fun isCostValid(cost: String) : Boolean {
    val value = cost.toIntOrNull()
    return !(value == null || value < 0 || value > 1000000)
}

fun isNameValid(name: String) : Boolean {
    return !(name.isEmpty() || name.length < 2)
}
