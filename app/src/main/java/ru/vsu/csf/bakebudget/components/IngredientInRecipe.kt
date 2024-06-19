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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import ru.vsu.csf.bakebudget.R
import ru.vsu.csf.bakebudget.api.RetrofitAPI
import ru.vsu.csf.bakebudget.models.IngredientInProductModel
import ru.vsu.csf.bakebudget.models.IngredientModel
import ru.vsu.csf.bakebudget.models.response.IngredientResponseModel
import ru.vsu.csf.bakebudget.services.deleteIngredientInProduct
import ru.vsu.csf.bakebudget.services.updateIngredientInProduct
import ru.vsu.csf.bakebudget.ui.theme.SideBack
import ru.vsu.csf.bakebudget.utils.dataIncorrectToast
import ru.vsu.csf.bakebudget.utils.isWeightValid

@Composable
fun IngredientInRecipe(
    ingredient: IngredientInProductModel,
    color: Color,
    ingredients: MutableList<IngredientInProductModel>,
    ingredientsAll: MutableList<IngredientModel>,
    selectedItemIndex: MutableIntState,
    ingredientsResponse: MutableList<IngredientResponseModel>,
    retrofitAPI: RetrofitAPI,
    currentSet: MutableSet<String>
) {
    val mContext = LocalContext.current
    val openAlertDialog = remember { mutableStateOf(false) }
    when {
        openAlertDialog.value -> {
            AlertDialog1(
                onDismissRequest = {
                    openAlertDialog.value = false
                },
                onConfirmation = {
                    openAlertDialog.value = false
                },
                dialogTitle = ingredient.name,
                dialogText = "Можете редактировать или удалить ингредиент",
                icon = Icons.Default.Info,
                ingredient,
                ingredients,
                ingredientsAll,
                selectedItemIndex,
                mContext,
                ingredientsResponse,
                retrofitAPI,
                currentSet
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
                Text(text = ingredient.name, maxLines = 3)
            }
            Box(
                modifier = Modifier.fillMaxWidth(0.8f),
                contentAlignment = Alignment.Center
            ) {
                Text(text = ingredient.weight.toString() + " гр.", maxLines = 3)
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
fun AlertDialog1(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
    ingredient: IngredientInProductModel,
    ingredients: MutableList<IngredientInProductModel>,
    ingredientsAll: MutableList<IngredientModel>,
    selectedItemIndex: MutableIntState,
    context: Context,
    ingredientsResponse: MutableList<IngredientResponseModel>,
    retrofitAPI: RetrofitAPI,
    currentSet: MutableSet<String>
) {
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
                if (ingredientsAll.isNotEmpty()) {
//                    DropdownMenuBox(
//                        ingredientsAll = ingredientsAll,
//                        selectedItemIndex = selectedItemIndex
//                    )
                    InputTextField(placeholder = "Вес", weight, 8, true)
                }
            }
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (isWeightValid(weight.value)) {
                        if (ingredient.productId != -1) {
                            updateIngredientInProduct(context, retrofitAPI, IngredientInProductModel(
                                ingredient.ingredientId,
                                ingredient.productId,
                                ingredientsAll[selectedItemIndex.intValue].name,
                                weight.value.toInt()
                            ))
                        }

                        ingredient.weight = weight.value.toInt()
                        onConfirmation()
                    } else {
                        dataIncorrectToast(context = context)
                    }
                }
            ) {
                Text("Сохранить")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    if (ingredient.productId != -1) {
                        deleteIngredientInProduct(
                            context, retrofitAPI, IngredientInProductModel(
                                ingredient.ingredientId,
                                ingredient.productId,
                                ingredientsAll[selectedItemIndex.intValue].name,
                                ingredient.weight
                            )
                        )
                    }
                    currentSet.remove(ingredient.name)
                    ingredients.remove(ingredient)
                    onDismissRequest()
                }
            ) {
                Text("Удалить")
            }
        }
    )
}
