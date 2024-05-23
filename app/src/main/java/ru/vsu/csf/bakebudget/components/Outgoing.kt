package ru.vsu.csf.bakebudget.components

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import io.appmetrica.analytics.AppMetrica
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Response
import ru.vsu.csf.bakebudget.R
import ru.vsu.csf.bakebudget.api.RetrofitAPI
import ru.vsu.csf.bakebudget.models.OutgoingModel
import ru.vsu.csf.bakebudget.models.request.OutgoingRequestModel
import ru.vsu.csf.bakebudget.ui.theme.SideBack
import ru.vsu.csf.bakebudget.utils.dataIncorrectToast
import ru.vsu.csf.bakebudget.utils.isCostValid
import ru.vsu.csf.bakebudget.utils.isNameValid

@Composable
fun Outgoing(
    outgoing: OutgoingModel,
    color: Color,
    outgoings: MutableList<OutgoingModel>,
    productId: Int,
    retrofitAPI: RetrofitAPI,
    jwtToken: MutableState<String>
) {
    val mContext = LocalContext.current

    val openAlertDialog = remember { mutableStateOf(false) }
    when {
        openAlertDialog.value -> {
            AlertDialog3(
                onDismissRequest = {
                    openAlertDialog.value = false
                },
                onConfirmation = {
                    openAlertDialog.value = false
                },
                dialogTitle = outgoing.name,
                dialogText = "Можете редактировать или удалить издержку",
                outgoing,
                outgoings,
                mContext,
                productId,
                retrofitAPI, jwtToken
            )
        }
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .background(color)
            .padding(5.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 50.dp)
                .background(color)
                .padding(start = 21.dp, end = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(0.5f),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(text = outgoing.name, maxLines = 3)
            }
            Box(
                modifier = Modifier.fillMaxWidth(0.8f),
                contentAlignment = Alignment.CenterEnd
            ) {
                Text(text = (outgoing.cost.toString() + " руб."), maxLines = 3)
            }
            TextButton(
                onClick = { openAlertDialog.value = true }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.options),
                    contentDescription = "options"
                )
            }
        }
    }
}

@Composable
fun AlertDialog3(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    outgoing: OutgoingModel,
    outgoings: MutableList<OutgoingModel>,
    context: Context,
    productId: Int,
    retrofitAPI: RetrofitAPI,
    jwtToken: MutableState<String>
) {
    val name = remember {
        mutableStateOf(outgoing.name)
    }
    val value = remember {
        mutableStateOf(outgoing.cost.toString())
    }
    androidx.compose.material3.AlertDialog(
        containerColor = SideBack,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        ),
        modifier = Modifier.fillMaxWidth(0.9f),
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Column {
                Text(text = dialogText)
                InputTextField(placeholder = "Название", name, 30, true)
                InputTextField(placeholder = "Значение", value, 30, true)
            }
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (!isNameValid(name.value) || !isCostValid(value.value)) {
                        dataIncorrectToast(context = context)
                    } else {
                        updateOutgoing(
                            context, retrofitAPI, jwtToken, OutgoingModel(
                                outgoing.id,
                                name.value,
                                value.value.toInt()
                            ), productId
                        )
                        outgoings.remove(outgoing)
                        outgoings.add(
                            OutgoingModel(
                                outgoing.id,
                                name.value,
                                value.value.toInt()
                            )
                        )
                        onConfirmation()
                    }
                }
            ) {
                Text("Редактировать")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    deleteOutgoing(context, retrofitAPI, jwtToken, outgoing.id)
                    outgoings.remove(outgoing)
                    onDismissRequest()
                }
            ) {
                Text("Удалить")
            }
        }
    )
}

@OptIn(DelicateCoroutinesApi::class)
private fun updateOutgoing(
    ctx: Context,
    retrofitAPI: RetrofitAPI,
    jwtToken: MutableState<String>,
    outgoing: OutgoingModel,
    productId: Int
) {
    GlobalScope.launch(Dispatchers.Main) {
        val res =
            retrofitAPI.updateOutgoing(
                outgoing.id,
                OutgoingRequestModel(outgoing.name, outgoing.cost),
                "Bearer ".plus(jwtToken.value)
            )
        onResultUpdateOutgoing(res, ctx)
    }
}

private fun onResultUpdateOutgoing(
    result: Response<Void>?,
    ctx: Context
) {
    Toast.makeText(
        ctx,
        "Response Code : " + result!!.code() + "\n" + result.body(),
        Toast.LENGTH_SHORT
    ).show()
    val eventParameters1 = "{\"button_clicked\":\"update outgoing\"}"
    AppMetrica.reportEvent(
        "Outgoing updated",
        eventParameters1
    )
}

@OptIn(DelicateCoroutinesApi::class)
private fun deleteOutgoing(
    ctx: Context,
    retrofitAPI: RetrofitAPI,
    jwtToken: MutableState<String>,
    outgoingId: Int
) {
    GlobalScope.launch(Dispatchers.Main) {
        val res =
            retrofitAPI.deleteOutgoing(outgoingId, "Bearer ".plus(jwtToken.value))
        onResultDeleteOutgoing(outgoingId, ctx, res)
    }
}

//TODO: в отдельные классы положить бы это все..

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
