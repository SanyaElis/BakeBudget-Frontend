package ru.vsu.csf.bakebudget.services

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.MutableState
import io.appmetrica.analytics.AppMetrica
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Response
import ru.vsu.csf.bakebudget.api.RetrofitAPI
import ru.vsu.csf.bakebudget.getToken
import ru.vsu.csf.bakebudget.models.OrderModel
import ru.vsu.csf.bakebudget.models.ProductModel
import ru.vsu.csf.bakebudget.models.request.CalculationRequestModel
import ru.vsu.csf.bakebudget.models.request.OrderRequestModel
import ru.vsu.csf.bakebudget.models.response.CalculationResponseModel
import ru.vsu.csf.bakebudget.models.response.OrderResponseModel
import ru.vsu.csf.bakebudget.screens.sortByState
import ru.vsu.csf.bakebudget.utils.orderCreated
import ru.vsu.csf.bakebudget.utils.sameName


private val orderState = mapOf(0 to "NOT_STARTED", 1 to "IN_PROCESS", 2 to "DONE", 3 to "CANCELLED")
private val orderStateRev = mapOf("NOT_STARTED" to 0, "IN_PROCESS" to 1, "DONE" to 2, "CANCELLED" to 3)
private var toast: Toast? = null

@OptIn(DelicateCoroutinesApi::class)
fun calculate(
    ctx: Context,
    retrofitAPI: RetrofitAPI,
    calculationRequestModel: CalculationRequestModel,
    costPrice: MutableState<Long>,
    resultPrice: MutableState<Long>
) {
    GlobalScope.launch(Dispatchers.Main) {
        val res =
            retrofitAPI.calculate(
                calculationRequestModel,
                "Bearer ".plus(getToken(ctx))
            )
        onResultCalculate(res, ctx, costPrice, resultPrice)
    }
}

private fun onResultCalculate(
    result: Response<CalculationResponseModel?>?,
    ctx: Context,
    costPrice: MutableState<Long>,
    resultPrice: MutableState<Long>
) {
//    Toast.makeText(
//        ctx,
//        "Response Code : " + result!!.code() + "\n" + result.body(),
//        Toast.LENGTH_SHORT
//    ).show()
    costPrice.value = result!!.body()!!.costPrice.toLong()
    resultPrice.value = result.body()!!.finalCost.toLong()
}

@OptIn(DelicateCoroutinesApi::class)
fun createOrder(
    ctx: Context,
    retrofitAPI: RetrofitAPI,
    orderRequestModel: OrderRequestModel,
    orders : MutableList<OrderModel>,
    productsAll: MutableList<ProductModel>,
    selectedItemIndex : MutableState<Int>,
    costPrice: MutableState<Long>,
    resultPrice: MutableState<Long>,
    name: MutableState<String>
) {
    GlobalScope.launch(Dispatchers.Main) {
        val res =
            retrofitAPI.createOrder(orderRequestModel, "Bearer ".plus(getToken(ctx)))
        onResultCreateOrder(res, ctx, orders, productsAll, selectedItemIndex, costPrice, resultPrice, name)
    }
}

private fun onResultCreateOrder(
    result: Response<OrderResponseModel?>?,
    ctx: Context,
    orders : MutableList<OrderModel>,
    productsAll: MutableList<ProductModel>,
    selectedItemIndex : MutableState<Int>,
    costPrice: MutableState<Long>,
    resultPrice: MutableState<Long>,
    name: MutableState<String>
) {
//    Toast.makeText(
//        ctx,
//        "Response Code : " + result!!.code() + "\n" + result.body(),
//        Toast.LENGTH_SHORT
//    ).show()
    if (result!!.code() == 409) {
        sameName(ctx)
    }
    if (result.body()!=null) {
        costPrice.value = result.body()!!.costPrice.toLong()
        resultPrice.value = result.body()!!.finalCost.toLong()
        orders.add(
            OrderModel(
                result.body()!!.id,
                name.value,
                0,
                productsAll[selectedItemIndex.value],
                resultPrice.value.toDouble(),
                result.body()!!.finalWeight
            )
        )
        orderCreated(ctx)
    }
}

@OptIn(DelicateCoroutinesApi::class)
fun setStatusOrder(
    ctx: Context,
    retrofitAPI: RetrofitAPI,
    order: OrderModel,
    newStatus : Int
) {
    GlobalScope.launch(Dispatchers.Main) {
        val res = retrofitAPI.setStatus(order.id, orderState[newStatus]!!, "Bearer ".plus(getToken(ctx)))
        onResultSetStatus(res, order, newStatus)
    }
}

private fun onResultSetStatus(
    result: Response<Void>?,
    order: OrderModel,
    newStatus : Int
) {
    val eventParameters1 = "{\"button_clicked\":\"change order status\"}"
    AppMetrica.reportEvent(
        "Order status changed",
        eventParameters1
    )
}

@OptIn(DelicateCoroutinesApi::class)
fun findAllOrders(
    ctx: Context,
    retrofitAPI: RetrofitAPI,
    orders: MutableList<OrderModel>,
    productsAll: MutableList<ProductModel>,
    orders0: MutableList<OrderModel>, orders1: MutableList<OrderModel>, orders2: MutableList<OrderModel>, orders3: MutableList<OrderModel>, isDataReceivedOrders : MutableState<Boolean>
) {
    GlobalScope.launch(Dispatchers.Main) {
        val res = retrofitAPI.findAllOrders("Bearer ".plus(getToken(ctx)))
        onResultFindAllOrders(res, orders, productsAll, orders0, orders1, orders2, orders3, isDataReceivedOrders)
    }
}

fun onResultFindAllOrders(
    result: Response<List<OrderResponseModel>?>?,
    orders: MutableList<OrderModel>,
    productsAll: MutableList<ProductModel>,
    orders0: MutableList<OrderModel>, orders1: MutableList<OrderModel>, orders2: MutableList<OrderModel>, orders3: MutableList<OrderModel>, isDataReceivedOrders: MutableState<Boolean>
) {
    if (result!!.body() != null) {
        if (result.body()!!.isNotEmpty()) {
            for (order in result.body()!!) {
                for (product in productsAll) {
                    if (product.id == order.productId) {
                        orders.add(OrderModel(order.id, order.name,
                            orderStateRev[order.status]!!, product, order.finalCost, order.finalWeight))
                        break
                    }
                }
            }
        }
        sortByState(orders, orders0, orders1, orders2, orders3)
        isDataReceivedOrders.value = true
    }
}

@OptIn(DelicateCoroutinesApi::class)
fun deleteOrder(
    ctx: Context,
    retrofitAPI: RetrofitAPI,
    orderId: Int
) {
    GlobalScope.launch(Dispatchers.Main) {
        val res =
            retrofitAPI.deleteProduct(
                orderId,
                "Bearer ".plus(getToken(ctx))
            )
        onResultDeleteOrder(res, ctx)
    }
}

private fun onResultDeleteOrder(
    result: Response<Void>?,
    ctx: Context,
) {
    if (toast!= null) {
        toast!!.cancel();
    }
    toast = Toast.makeText(
        ctx,
        "Заказ успешно удален",
        Toast.LENGTH_SHORT
    )
    toast!!.show()
    val eventParameters1 = "{\"button_clicked\":\"delete order\"}"
    AppMetrica.reportEvent(
        "order deleted",
        eventParameters1
    )
}