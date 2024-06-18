package ru.vsu.csf.bakebudget.models.response

data class UserSignInResponseModel(
    var token : String,
    var type : String,
    var id : String,
    var username : String,
    var email : String,
    var role : String
)
