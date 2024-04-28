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
import ru.vsu.csf.bakebudget.R
import ru.vsu.csf.bakebudget.models.IngredientInProductModel
import ru.vsu.csf.bakebudget.ui.theme.SideBack

@Composable
fun IngredientInRecipe(ingredient: IngredientInProductModel, color: Color, ingredients: MutableList<IngredientInProductModel>) {
    val openAlertDialog = remember { mutableStateOf(false) }
    when {
        openAlertDialog.value -> {
            AlertDialog1(
                onDismissRequest = {
                    openAlertDialog.value = false },
                onConfirmation = {
                    openAlertDialog.value = false
                    println("Confirmation registered") // Add logic here to handle confirmation.
                },
                dialogTitle = ingredient.name,
                dialogText = "Можете редактировать или удалить ингредиент",
                icon = Icons.Default.Info,
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
            Box(modifier = Modifier.fillMaxWidth(0.5f),
                contentAlignment = Alignment.CenterStart) {
                Text(text = ingredient.name, maxLines = 3)
            }
            Box(modifier = Modifier.fillMaxWidth(0.8f),
                contentAlignment = Alignment.Center) {
                Text(text = ingredient.weight.toString(), maxLines = 3)
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
fun AlertDialog1(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
    ingredient: IngredientInProductModel,
    ingredients: MutableList<IngredientInProductModel>
) {
    val name = remember {
        mutableStateOf(ingredient.name)
    }
    val weight = remember {
        mutableStateOf(ingredient.weight.toString())
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
                InputTextField(text = "Название", name, 30)
                InputTextField(text = "Вес", weight, 30)
            }
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    ingredients.remove(ingredient)
                    ingredients.add(
                        IngredientInProductModel(
                            name.value,
                            weight.value.toInt()
                        )
                    )
                    onConfirmation()
                }
            ) {
                Text("Редактировать")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    ingredients.remove(ingredient)
                    onDismissRequest()
                }
            ) {
                Text("Удалить")
            }
        }
    )
}
