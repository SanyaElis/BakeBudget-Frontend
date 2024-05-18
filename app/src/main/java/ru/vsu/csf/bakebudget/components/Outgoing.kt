package ru.vsu.csf.bakebudget.components

import android.content.Context
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import ru.vsu.csf.bakebudget.R
import ru.vsu.csf.bakebudget.models.OutgoingModel
import ru.vsu.csf.bakebudget.ui.theme.SideBack
import ru.vsu.csf.bakebudget.utils.dataIncorrectToast
import ru.vsu.csf.bakebudget.utils.isCostValid
import ru.vsu.csf.bakebudget.utils.isNameValid

@Composable
fun Outgoing(outgoing: OutgoingModel, color: Color, outgoings: MutableList<OutgoingModel>) {
    val mContext = LocalContext.current

    val openAlertDialog = remember { mutableStateOf(false) }
    when {
        openAlertDialog.value -> {
            AlertDialog3(
                onDismissRequest = {
                    openAlertDialog.value = false },
                onConfirmation = {
                    openAlertDialog.value = false
                },
                dialogTitle = outgoing.name,
                dialogText = "Можете редактировать или удалить издержку",
                outgoing,
                outgoings,
                mContext
            )
        }
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .background(color)
            .padding(5.dp)) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 50.dp)
            .background(color)
            .padding(start = 21.dp, end = 5.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.fillMaxWidth(0.5f),
                contentAlignment = Alignment.CenterStart) {
                Text(text = outgoing.name, maxLines = 3)
            }
            Box(modifier = Modifier.fillMaxWidth(0.8f),
                contentAlignment = Alignment.CenterEnd) {
                Text(text = (outgoing.cost.toString() + " руб."), maxLines = 3)
            }
            TextButton(
                onClick = { openAlertDialog.value = true }
            ){
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
    context : Context
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
                InputTextField(text = "Название", name, 30, true)
                InputTextField(text = "Значение", value, 30, true)
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
                        outgoings.remove(outgoing)
                        outgoings.add(
                            OutgoingModel(
                                0,
                                name.value,
                                value.value.toInt(),
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
                    outgoings.remove(outgoing)
                    onDismissRequest()
                }
            ) {
                Text("Удалить")
            }
        }
    )
}
