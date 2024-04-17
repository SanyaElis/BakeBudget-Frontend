package ru.vsu.csf.bakebudget.screens

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
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import ru.vsu.csf.bakebudget.R
import ru.vsu.csf.bakebudget.components.Ingredient
import ru.vsu.csf.bakebudget.models.IngredientModel
import ru.vsu.csf.bakebudget.models.MenuItemModel
import ru.vsu.csf.bakebudget.ui.theme.PrimaryBack

@Composable
fun IngredientsScreen(navController: NavHostController) {
//    val ingredients = remember {
//        mutableStateListOf(
//            IngredientModel("Milk", 100, 30),
//            IngredientModel("Flower", 1000, 100),
//            IngredientModel("Butter", 150, 250),
//        )
//    }
//    val item = listOf(MenuItemModel(R.drawable.ingredients, "Ингредиенты"))
//    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
//    val scope = rememberCoroutineScope()
//    val selectedItem = remember {
//        mutableStateOf(item[0])
//    }
//
//    ModalNavigationDrawer(
//        drawerState = drawerState,
//        drawerContent = {
//            SideMenu(
//                navController = navController,
//                drawerState = drawerState,
//                scope = scope,
//                selectedItem = selectedItem
//            )
//        },
//        content = {
//            Column {
//                Header()
//                LazyColumn(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                ) {
//                    itemsIndexed(ingredients) { _, ingredient ->
//                        Ingredient(ingredient = ingredient)
//                    }
//                    item {
//                        Box(
//                            modifier = Modifier.fillMaxWidth(),
//                            contentAlignment = Alignment.Center
//                        ) {
//                            Button(
//                                onClick = { ingredients.add(IngredientModel("Butter", 150, 250)) },
//                                modifier = Modifier.padding(5.dp)
//                            ) {
//                                Text(text = "Добавить")
//                            }
//                        }
//                    }
//                }
//            }
//        })
}

@Composable
private fun Header() {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .defaultMinSize(40.dp)
//            .padding(top = 10.dp, bottom = 5.dp), horizontalArrangement = Arrangement.SpaceEvenly,
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        Text(text = "Название")
//        Text(text = "Вес")
//        Text(text = "Цена")
//    }
}