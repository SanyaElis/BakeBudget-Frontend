package ru.vsu.csf.bakebudget.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import io.appmetrica.analytics.AppMetrica
import ru.vsu.csf.bakebudget.R
import ru.vsu.csf.bakebudget.models.IngredientModel
import ru.vsu.csf.bakebudget.ui.theme.SideBack

@Composable
fun Ingredient(ingredient: IngredientModel, color: Color, ingredients: MutableList<IngredientModel>) {
    val openAlertDialog = remember { mutableStateOf(false) }
    when {
        openAlertDialog.value -> {
            AlertDialog(
                onDismissRequest = {
                    openAlertDialog.value = false },
                onConfirmation = {
                    openAlertDialog.value = false
                },
                dialogTitle = ingredient.name,
                dialogText = "Можете редактировать или удалить ингредиент",
                ingredient,
                ingredients
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
            Box(modifier = Modifier.fillMaxWidth(0.33f),
                contentAlignment = Alignment.CenterStart) {
                Text(text = ingredient.name, maxLines = 3)
            }
            Box(modifier = Modifier.fillMaxWidth(0.5f),
                contentAlignment = Alignment.Center) {
                Text(text = ingredient.weight.toString(), maxLines = 3)
            }
            Box(modifier = Modifier.fillMaxWidth(0.75f),
                contentAlignment = Alignment.CenterEnd) {
                Text(text = ingredient.cost.toString(), maxLines = 3)
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
fun AlertDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    ingredient: IngredientModel,
    ingredients: MutableList<IngredientModel>
) {
    val name = remember {
        mutableStateOf(ingredient.name)
    }
    val weight = remember {
        mutableStateOf(ingredient.weight.toString())
    }
    val cost = remember {
        mutableStateOf(ingredient.cost.toString())
    }
    AlertDialog(
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
                InputTextField(text = "Вес", weight, 30, true)
                InputTextField(text = "Цена", cost, 30, true)
            }
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val eventParameters1 = "{\"button_clicked\":\"ingredient_edit\"}"
                    AppMetrica.reportEvent("Ingredient edited", eventParameters1)
                    ingredients.remove(ingredient)
                    ingredients.add(IngredientModel(
                        name.value,
                        weight.value.toInt(),
                        cost.value.toInt()
                    ))
                    onConfirmation()
                }
            ) {
                Text("Редактировать")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    val eventParameters1 = "{\"button_clicked\":\"ingredient_delete\"}"
                    AppMetrica.reportEvent("Ingredient deleted", eventParameters1)
                    ingredients.remove(ingredient)
                    onDismissRequest()
                }
            ) {
                Text("Удалить")
            }
        }
    )
}
