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
import io.appmetrica.analytics.AppMetrica
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ru.vsu.csf.bakebudget.R
import ru.vsu.csf.bakebudget.models.MenuItemModel
import ru.vsu.csf.bakebudget.ui.theme.SecondaryBack
import ru.vsu.csf.bakebudget.ui.theme.SideBack
import ru.vsu.csf.bakebudget.ui.theme.TextPrimary

@Composable
fun SideMenu(
    navController: NavHostController,
    drawerState: DrawerState,
    scope: CoroutineScope,
    selectedItem: MutableState<MenuItemModel>,
    isLogged: MutableState<Boolean>
) {
    val items = listOf(
        MenuItemModel(R.drawable.home, "Главная"),
        MenuItemModel(R.drawable.ingredients, "Ингредиенты"),
        MenuItemModel(R.drawable.products, "Готовые изделия"),
        MenuItemModel(R.drawable.outgoings, "Издержки"),
        MenuItemModel(R.drawable.calculation, "Расчет стоимости"),
        MenuItemModel(R.drawable.orders, "Заказы"),
        MenuItemModel(R.drawable.reports, "Отчеты"),
        MenuItemModel(R.drawable.groups, "Группы"),
    )
    val eventParameters1 = "{\"button_clicked\":\"ingredients\"}"
    val eventParameters2 = "{\"button_clicked\":\"products\"}"
    val eventParameters3 = "{\"button_clicked\":\"orders\"}"
    val eventParameters4 = "{\"button_clicked\":\"groups\"}"
    val eventParameters5 = "{\"button_clicked\":\"outgoings\"}"
    val eventParameters6 = "{\"button_clicked\":\"calculation\"}"
    val eventParameters7 = "{\"button_clicked\":\"reports\"}"
    val mContext = LocalContext.current

    DismissibleDrawerSheet(
        modifier = Modifier.clip(RoundedCornerShape(0.dp, 8.dp, 8.dp, 0.dp)),
        drawerContainerColor = SideBack
    ) {
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
                                items[5] -> {
                                    AppMetrica.reportEvent(
                                        "Orders slide menu click",
                                        eventParameters3
                                    )
                                    navController.navigate("orders")
                                }

                                items[1] -> {
                                    AppMetrica.reportEvent(
                                        "Ingredients slide menu click",
                                        eventParameters1
                                    )
                                    navController.navigate("ingredients")
                                }

                                items[2] -> {
                                    AppMetrica.reportEvent(
                                        "Products slide menu click",
                                        eventParameters2
                                    )
                                    navController.navigate("products")
                                }
                                //TODO:расположить в праивильном порядке
                                items[4] -> {
                                    AppMetrica.reportEvent(
                                        "Calculation slide menu click",
                                        eventParameters6
                                    )
                                    navController.navigate("calculation")
                                }

                                items[3] -> {
                                    AppMetrica.reportEvent(
                                        "Outgoings slide menu click",
                                        eventParameters5
                                    )
                                    navController.navigate("outgoings")
                                }

                                items[6] -> {
                                    AppMetrica.reportEvent(
                                        "Reports slide menu click",
                                        eventParameters7
                                    )
                                    navController.navigate("reports")
                                }

                                items[7] -> {
                                    AppMetrica.reportEvent(
                                        "Groups slide menu click",
                                        eventParameters4
                                    )
                                    navController.navigate("groups")
                                }

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
                        navController.navigate("login")
                    }
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