package ru.vsu.csf.bakebudget.components

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import ru.vsu.csf.bakebudget.R
import ru.vsu.csf.bakebudget.api.RetrofitAPI
import ru.vsu.csf.bakebudget.models.OrderModel
import ru.vsu.csf.bakebudget.screens.sortByState
import ru.vsu.csf.bakebudget.services.setStatusOrder
import ru.vsu.csf.bakebudget.ui.theme.SideBack
import ru.vsu.csf.bakebudget.ui.theme.TextPrimary

@Composable
fun Order(order: OrderModel,
          orders: MutableList<OrderModel>,
          orders0: MutableList<OrderModel>,
          orders1: MutableList<OrderModel>,
          orders2: MutableList<OrderModel>,
          orders3: MutableList<OrderModel>,
          retrofitAPI: RetrofitAPI) {
    val mContext = LocalContext.current

    val openAlertDialog = remember { mutableStateOf(false) }
    val selectedValue = remember { mutableIntStateOf(order.status) }

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    when {
        openAlertDialog.value -> {
            AlertDialogOrder(
                onDismissRequest = {
                    openAlertDialog.value = false },
                onConfirmation = {
                    openAlertDialog.value = false
                },
                dialogTitle = order.name,
                dialogText = "Можете изменить состояние заказа",
                order,
                selectedValue,
                orders,
                orders0,
                orders1,
                orders2,
                orders3,
                mContext,
                retrofitAPI
            )
        }
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(SideBack)
            .padding(8.dp)
    ) {
        if (order.product.url != null) {
            AsyncImage(
                modifier = Modifier
                    .height((screenWidth-32.dp)/2)
                    .clip(RoundedCornerShape(14.dp))
                    .clickable(onClick = { openAlertDialog.value = true }),
                model = order.product.url,
                error = painterResource(R.drawable.error),
                placeholder = painterResource(id = R.drawable.loading2),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        } else {
            if (order.product.uri != null) {
                AsyncImage(
                    modifier = Modifier
                        .height((screenWidth-32.dp)/2)
                        .clip(RoundedCornerShape(14.dp))
                        .clickable(onClick = { openAlertDialog.value = true }),
                    model = order.product.uri,
//                        placeholder = painterResource(id = R.drawable.loading),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
            } else {
                Image(
                    modifier = Modifier
                        .height((screenWidth-32.dp)/2)
                        .clip(RoundedCornerShape(14.dp))
                        .clickable(onClick = { openAlertDialog.value = true }),
                    contentScale = ContentScale.Fit,
                    painter = painterResource(id = order.product.iconId),
                    contentDescription = order.product.name
                )
            }
        }
        Column {
            Text(modifier = Modifier.padding(start = 8.dp, bottom = 6.dp), text = order.name, color = TextPrimary, fontSize = 16.sp)
            Text(modifier = Modifier.padding(start = 8.dp), text = order.finalPrice.toLong().toString() + " р.", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Text(modifier = Modifier.padding(start = 10.dp), text = "за " + order.weight.toString() + " г.", fontSize = 12.sp, color = TextPrimary)
        }
    }
}

@Composable
fun AlertDialogOrder(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    order: OrderModel,
    selectedValue: MutableState<Int>,
    orders: MutableList<OrderModel>,
    orders0: MutableList<OrderModel>,
    orders1: MutableList<OrderModel>,
    orders2: MutableList<OrderModel>,
    orders3: MutableList<OrderModel>,
    context: Context,
    retrofitAPI: RetrofitAPI,
) {
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
                OrderStateRadio(selectedValue = selectedValue)
            }
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    order.status = selectedValue.value
                    setStatusOrder(context, retrofitAPI, order, selectedValue.value)
                    sortByState(orders, orders0, orders1, orders2, orders3)
                    onConfirmation()
                }
            ) {
                Text("Сохранить")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Отмена")
            }
        }
    )
}

//TODO:удалить заказ