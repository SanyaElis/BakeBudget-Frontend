package ru.vsu.csf.bakebudget.models.request

data class UserSignUpRequestModel(
    var username : String,
    var email : String,
    var password : String
)
