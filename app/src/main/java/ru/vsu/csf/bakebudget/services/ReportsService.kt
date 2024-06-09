package ru.vsu.csf.bakebudget.services

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.MutableState
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Response
import ru.vsu.csf.bakebudget.api.RetrofitAPI
import ru.vsu.csf.bakebudget.getToken
import ru.vsu.csf.bakebudget.models.request.ReportRequestModel
import ru.vsu.csf.bakebudget.models.response.ReportIncomeResponseModel
import ru.vsu.csf.bakebudget.models.response.ReportOrdersResponseModel
import java.util.Timer
import kotlin.concurrent.schedule

@OptIn(DelicateCoroutinesApi::class)
fun createReportOrders(
    ctx: Context,
    retrofitAPI: RetrofitAPI,
    reportRequestModel: ReportRequestModel,
    dataListOrders : MutableList<Long>,
    reportState: MutableState<Boolean>,
    isGroup: Boolean
) {
    GlobalScope.launch(Dispatchers.Main) {
        val res =
            if (!isGroup) retrofitAPI.reportOrdersSelf(reportRequestModel, "Bearer ".plus(getToken(ctx))) else retrofitAPI.reportOrdersGroup(reportRequestModel, "Bearer ".plus(getToken(ctx)))
        onResultCreateReportOrders(res, ctx, dataListOrders, reportState)
    }
}

private fun onResultCreateReportOrders(
    result: Response<ReportOrdersResponseModel?>?,
    ctx: Context,
    dataListOrders : MutableList<Long>,
    reportState: MutableState<Boolean>
) {
//    Toast.makeText(
//        ctx,
//        "Response Code : " + result!!.code() + "\n" + result.body(),
//        Toast.LENGTH_SHORT
//    ).show()
    dataListOrders.clear()
    val cancelled = result!!.body()!!.CANCELLED
    val done = result.body()!!.DONE
    val inProcess = result.body()!!.IN_PROCESS
    val notStarted = result.body()!!.NOT_STARTED
    dataListOrders.add(cancelled+done+inProcess+notStarted)
    dataListOrders.add(done)
    dataListOrders.add(cancelled)
    reportState.value = true
}

@OptIn(DelicateCoroutinesApi::class)
fun createReportIncome(
    ctx: Context,
    retrofitAPI: RetrofitAPI,
    reportRequestModel: ReportRequestModel,
    dataListIncome : MutableList<Long>,
    reportState: MutableState<Boolean>,
    isGroup: Boolean
) {
    GlobalScope.launch(Dispatchers.Main) {
        val res = if (!isGroup) retrofitAPI.reportIncomeSelf(reportRequestModel, "Bearer ".plus(getToken(ctx))) else retrofitAPI.reportIncomeGroup(reportRequestModel, "Bearer ".plus(getToken(ctx)))
        onResultCreateReportIncome(res, ctx, dataListIncome, reportState)
    }
}

private fun onResultCreateReportIncome(
    result: Response<ReportIncomeResponseModel?>?,
    ctx: Context,
    dataListIncome : MutableList<Long>,
    reportState: MutableState<Boolean>
) {
//    Toast.makeText(
//        ctx,
//        "Response Code : " + result!!.code() + "\n" + result.body(),
//        Toast.LENGTH_SHORT
//    ).show()
    dataListIncome.clear()
    dataListIncome.add(result!!.body()!!.cost.toLong())
    dataListIncome.add(result.body()!!.income.toLong())
    Timer().schedule(1000) {
        reportState.value = true
    }
}