package ru.vsu.csf.bakebudget.models.request

data class PasswordResetRequestModel(
    var email : String,
    var newPassword : String
)
