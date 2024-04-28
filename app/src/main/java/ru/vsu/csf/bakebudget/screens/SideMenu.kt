package ru.vsu.csf.bakebudget.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DismissibleDrawerSheet
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ru.vsu.csf.bakebudget.models.MenuItemModel
import ru.vsu.csf.bakebudget.R
import ru.vsu.csf.bakebudget.ui.theme.SecondaryBack
import ru.vsu.csf.bakebudget.ui.theme.SideBack
import ru.vsu.csf.bakebudget.ui.theme.TextPrimary

@Composable
fun SideMenu(navController: NavHostController,
             drawerState: DrawerState,
             scope : CoroutineScope,
             selectedItem : MutableState<MenuItemModel>,
             isLogged: MutableState<Boolean>) {
    val items = listOf(
        MenuItemModel(R.drawable.home, "Главная"),
        MenuItemModel(R.drawable.orders, "Заказы"),
        MenuItemModel(R.drawable.ingredients, "Ингредиенты"),
        MenuItemModel(R.drawable.products, "Готовые изделия"),
        MenuItemModel(R.drawable.calculation, "Расчет стоимости"),
        MenuItemModel(R.drawable.outgoings, "Издержки"),
        MenuItemModel(R.drawable.reports, "Отчеты"),
        MenuItemModel(R.drawable.groups, "Группы"),
    )
    val mContext = LocalContext.current

    DismissibleDrawerSheet(modifier = Modifier.clip(RoundedCornerShape(0.dp, 8.dp, 8.dp, 0.dp)),
        drawerContainerColor = SideBack) {
        TextButton(onClick = {
            scope.launch {
                drawerState.close()
            }
        }) {
            Icon(
                painter = painterResource(R.drawable.close_menu),
                contentDescription = "close",
                tint = TextPrimary,
                modifier = Modifier.padding(top = 15.dp, bottom = 30.dp)
            )
        }
        items.forEach { item ->
            NavigationDrawerItem(
                shape = RectangleShape,
                colors = NavigationDrawerItemDefaults.colors(
                    selectedContainerColor = SecondaryBack,
                    unselectedContainerColor = SideBack
                ),
                label = { Text(text = item.title, color = TextPrimary, fontSize = 20.sp) },
                icon = {
                    Icon(
                        painter = painterResource(item.iconId),
                        contentDescription = item.title,
                        tint = TextPrimary
                    )
                },
                selected = selectedItem.value == item,
                onClick = {
                    scope.launch {
                        selectedItem.value = item
                        if (isLogged.value.not()) {
                            mToast(mContext, isLogged)
                        } else {
                            when (selectedItem.value) {
                                items[0] -> navController.navigate("home")
                                items[2] -> navController.navigate("ingredients")
                                items[3] -> navController.navigate("products")
                                items[4] -> navController.navigate("calculation")
                                items[5] -> navController.navigate("outgoings")
                                items[6] -> navController.navigate("reports")
                                items[7] -> navController.navigate("groups")
                                else -> {
                                    mToast(mContext, isLogged)
                                }
                            }
                        }
                    }
                }
            )
        }
        if (isLogged.value) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(bottom = 110.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                TextButton(
                    onClick = {
                        isLogged.value = false
                        navController.navigate("login") }
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.button_exit),
                        contentDescription = "exit"
                    )
                }
            }
        }
    }
}

private fun mToast(context: Context, isLogged: MutableState<Boolean>) {
    Toast.makeText(
        context,
        if (isLogged.value) "Данный раздел находится на стадии разработки и станет доступным в ближайщее время" else "Сначала необходимо авторизоваться",
        Toast.LENGTH_LONG
    ).show()
}