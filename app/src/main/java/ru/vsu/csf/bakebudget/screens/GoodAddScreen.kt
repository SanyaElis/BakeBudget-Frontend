package ru.vsu.csf.bakebudget.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ru.vsu.csf.bakebudget.R
import ru.vsu.csf.bakebudget.components.IngredientInRecipe
import ru.vsu.csf.bakebudget.components.InputTextField
import ru.vsu.csf.bakebudget.models.GoodModel
import ru.vsu.csf.bakebudget.models.IngredientInRecipeModel
import ru.vsu.csf.bakebudget.models.IngredientModel
import ru.vsu.csf.bakebudget.models.MenuItemModel
import ru.vsu.csf.bakebudget.ui.theme.Back2
import ru.vsu.csf.bakebudget.ui.theme.PrimaryBack
import ru.vsu.csf.bakebudget.ui.theme.SideBack
import ru.vsu.csf.bakebudget.ui.theme.UnfocusedField

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun GoodAddScreen(
    navController: NavHostController,
    ingredients: MutableList<IngredientInRecipeModel>,
    ingredientsAll: MutableList<IngredientModel>,
    isLogged: MutableState<Boolean>,
    goods: MutableList<GoodModel>
) {
    val item = listOf(MenuItemModel(R.drawable.goods, "Готовые изделия"))
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val selectedItem = remember {
        mutableStateOf(item[0])
    }
    val selectedItemIndex = remember { mutableIntStateOf(0) }

    val openAlertDialog = remember { mutableStateOf(false) }
    when {
        openAlertDialog.value -> {
            AlertDialog2(
                onDismissRequest = {
                    openAlertDialog.value = false },
                onConfirmation = {
                    openAlertDialog.value = false
                },
                dialogTitle = "Найдите ингредиент",
                dialogText = "",
                ingredientsAll = ingredientsAll,
                selectedItemIndex,
                ingredients
            )
        }
    }
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            SideMenu(
                navController = navController,
                drawerState = drawerState,
                scope = scope,
                selectedItem = selectedItem,
                isLogged = isLogged
            )
        },
        content = {
            Scaffold(bottomBar = {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.2f)
                        .background(SideBack)
                        .padding(start = 8.dp, end = 8.dp),
                    shape = RoundedCornerShape(10.dp, 10.dp, 0.dp, 0.dp),
                ) {
                    Box(
                        modifier = Modifier.background(PrimaryBack),
                        contentAlignment = Alignment.Center
                    ) {
                        Column {
                            TextButton(
                                onClick = {
                                    openAlertDialog.value = true
                                }
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.button_add),
                                    contentDescription = "add"
                                )
                            }
                            TextButton(
                                onClick = {
                                    goods.add(GoodModel(R.drawable.cake, "Изделие", ingredients))
                                    navController.navigate("goods")
                                }
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.button_save),
                                    contentDescription = "save"
                                )
                            }
                        }
                    }
                }
            }) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .background(SideBack)
                        .padding(bottom = 10.dp)
                ) {
                    Column {
                        Header(scope = scope, drawerState = drawerState)
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(0.8f)
                                .background(SideBack)
                                .padding(top = 20.dp)
                        ) {
                            itemsIndexed(ingredients) { num, ingredient ->
                                IngredientInRecipe(ingredient = ingredient, if (num % 2 == 0) SideBack else Back2, ingredients)
                            }
                        }
                    }
                }
            }
        })
}

@Composable
private fun Header(scope: CoroutineScope, drawerState: DrawerState) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.1f)
            .background(SideBack)
            .padding(start = 8.dp, end = 8.dp),
        shape = RoundedCornerShape(0.dp, 0.dp, 10.dp, 10.dp),
    ) {
        Column(modifier = Modifier.background(PrimaryBack)) {
            Row {
                Box(
                    modifier = Modifier
                        .background(PrimaryBack)
                ) {
                    TextButton(onClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    }) {
                        Image(
                            painter = painterResource(id = R.drawable.menu_open),
                            contentDescription = "menu",
                            modifier = Modifier.padding(5.dp)
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(PrimaryBack)
                        .padding(top = 12.dp, end = 50.dp),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Text(text = "ИЗДЕЛИЕ", fontSize = 24.sp, color = Color.White)
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(40.dp)
                    .background(PrimaryBack)
                    .padding(start = 16.dp, top = 10.dp, bottom = 9.dp, end = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "НАЗВАНИЕ", color = Color.White, fontSize = 12.sp)
                Text(text = "КОЛИЧЕСТВО В РЕЦЕПТЕ", color = Color.White, fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun AlertDialog2(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    ingredientsAll: MutableList<IngredientModel>,
    selectedItemIndex: MutableIntState,
    ingredients: MutableList<IngredientInRecipeModel>
) {
    val name = remember {
        mutableStateOf("")
    }
    val weight = remember {
        mutableStateOf("")
    }
    androidx.compose.material3.AlertDialog(
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
                DropdownMenuBox(ingredientsAll = ingredientsAll, selectedItemIndex = selectedItemIndex)
                InputTextField(text = "Вес", weight, 30)
            }
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    ingredients.add(IngredientInRecipeModel(ingredientsAll[selectedItemIndex.intValue].name, weight = weight.value.toInt()))
                    onConfirmation()
                }
            ) {
                Text("ОК")
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenuBox(ingredientsAll: MutableList<IngredientModel>, selectedItemIndex: MutableIntState) {
    var expanded by remember { mutableStateOf(false) }
    val list = mutableListOf<String>()
    for (ingredient in ingredientsAll) {
        list.add(ingredient.name)
    }


    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.padding(5.dp),
    ) {
        TextField(
            value = list[selectedItemIndex.intValue],
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            list.forEachIndexed { index, item ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = item,
                            fontWeight = if (index == selectedItemIndex.intValue) FontWeight.Bold else null
                        )
                    },
                    onClick = {
                        selectedItemIndex.intValue = index
                        expanded = false
                    }
                )
            }
        }
    }
}