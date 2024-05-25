package ru.vsu.csf.bakebudget.models.response

data class ReportOrdersResponseModel(
    var CANCELLED : Long,
    var DONE : Long,
    var NOT_STARTED : Long,
    var IN_PROCESS : Long
)
