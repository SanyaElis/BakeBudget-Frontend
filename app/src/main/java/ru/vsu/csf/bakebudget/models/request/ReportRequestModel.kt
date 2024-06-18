package ru.vsu.csf.bakebudget.models.request

data class ReportRequestModel(
    var startCreatedAt : String,
    var endCreatedAt : String,
    var startFinishedAt : String,
    var endFinishedAt : String
)
