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
import ru.vsu.csf.bakebudget.models.OutgoingModel
import ru.vsu.csf.bakebudget.models.ProductModel
import ru.vsu.csf.bakebudget.models.request.OutgoingRequestModel
import ru.vsu.csf.bakebudget.utils.sameName

@OptIn(DelicateCoroutinesApi::class)
fun createOutgoing(
    ctx: Context,
    retrofitAPI: RetrofitAPI,
    outgoingModel: OutgoingRequestModel,
    product: ProductModel,
    productsAll: MutableList<ProductModel>
) {
    GlobalScope.launch(Dispatchers.Main) {
        val res =
            retrofitAPI.createOutgoing(product.id, outgoingModel, "Bearer ".plus(getToken(ctx)))
        onResultCreateOutgoing(res, ctx, product, productsAll)
    }
}

fun onResultCreateOutgoing(
    result: Response<OutgoingModel?>?,
    ctx: Context,
    product: ProductModel,
    productsAll: MutableList<ProductModel>
) {
    Toast.makeText(
        ctx,
        "Response Code : " + result!!.code() + "\n" + result.body(),
        Toast.LENGTH_SHORT
    ).show()
    if (result.code() == 409) {
        sameName(ctx)
    }
    if (result.isSuccessful && result.body() != null) {
            product.outgoings.add(result.body()!!)
    }
}

@OptIn(DelicateCoroutinesApi::class)
fun findAllOutgoingsInProduct(
    ctx: Context,
    retrofitAPI: RetrofitAPI,
    product: ProductModel
) {
    GlobalScope.launch(Dispatchers.Main) {
        val res =
            retrofitAPI.findAllOutgoingsInProduct(product.id, "Bearer ".plus(getToken(ctx))
            )
        onResultFindAllOutgoingsInProduct(res, ctx, product)
    }
}

private fun onResultFindAllOutgoingsInProduct(
    result: Response<List<OutgoingModel>?>?,
    ctx: Context,
    product: ProductModel
) {
    Toast.makeText(
        ctx,
        "Response Code : " + result!!.code() + "\n" + result.body(),
        Toast.LENGTH_SHORT
    ).show()
    if (result.body() != null) {
        if (result.body()!!.isNotEmpty()) {
            for (outgoing in result.body()!!) {
                product.outgoings.add(outgoing)
            }
        }
    }
}

@OptIn(DelicateCoroutinesApi::class)
fun updateOutgoing(
    ctx: Context,
    retrofitAPI: RetrofitAPI,
    outgoing: OutgoingModel,
    outgoingBefore: OutgoingModel,
    outgoings: MutableList<OutgoingModel>,
    productId: Int
) {
    GlobalScope.launch(Dispatchers.Main) {
        val res =
            retrofitAPI.updateOutgoing(
                outgoing.id,
                OutgoingRequestModel(outgoing.name, outgoing.cost),
                "Bearer ".plus(getToken(ctx))
            )
        onResultUpdateOutgoing(res, ctx, outgoingBefore, outgoing, outgoings)
    }
}

private fun onResultUpdateOutgoing(
    result: Response<Void>?,
    ctx: Context,
    outgoingBefore: OutgoingModel,
    outgoing: OutgoingModel,
    outgoings: MutableList<OutgoingModel>
    ) {
    Toast.makeText(
        ctx,
        "Response Code : " + result!!.code() + "\n" + result.body(),
        Toast.LENGTH_SHORT
    ).show()
    if (result.code() == 409) {
        sameName(ctx)
    } else {
        val eventParameters1 = "{\"button_clicked\":\"update outgoing\"}"
        AppMetrica.reportEvent(
            "Outgoing updated",
            eventParameters1
        )
        outgoings.remove(outgoingBefore)
        outgoings.add(
            outgoing
        )
    }
}

@OptIn(DelicateCoroutinesApi::class)
fun deleteOutgoing(
    ctx: Context,
    retrofitAPI: RetrofitAPI,
    outgoingId: Int
) {
    GlobalScope.launch(Dispatchers.Main) {
        val res =
            retrofitAPI.deleteOutgoing(outgoingId, "Bearer ".plus(getToken(ctx)))
        onResultDeleteOutgoing(outgoingId, ctx, res)
    }
}

private fun onResultDeleteOutgoing(
    outgoingId: Int,
    ctx: Context,
    result: Response<Void>?
) {
    Toast.makeText(
        ctx,
        "Response Code : " + result!!.code() + "\n" + "Deleted outgoing id: " + "\n" + outgoingId,
        Toast.LENGTH_SHORT
    ).show()
    val eventParameters2 = "{\"button_clicked\":\"delete outgoing\"}"
    AppMetrica.reportEvent(
        "Outgoing deleted",
        eventParameters2
    )
}