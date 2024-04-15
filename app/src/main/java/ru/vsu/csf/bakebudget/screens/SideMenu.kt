package ru.vsu.csf.bakebudget.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import ru.vsu.csf.bakebudget.MenuItemModel
import ru.vsu.csf.bakebudget.R
import ru.vsu.csf.bakebudget.ui.theme.SecondaryBack
import ru.vsu.csf.bakebudget.ui.theme.SideBack
import ru.vsu.csf.bakebudget.ui.theme.TextPrimary

@Composable
fun SideMenu() {
    val items = listOf(
        MenuItemModel(R.drawable.home, "Главная"),
        MenuItemModel(R.drawable.orders, "Заказы"),
        MenuItemModel(R.drawable.ingredients, "Ингредиенты"),
        MenuItemModel(R.drawable.goods, "Готовые изделия"),
        MenuItemModel(R.drawable.calculation, "Расчет стоимости"),
        MenuItemModel(R.drawable.costs, "Издержки"),
        MenuItemModel(R.drawable.reports, "Отчеты"),
        MenuItemModel(R.drawable.groups, "Группы"),
    )
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val selectedItem = remember {
        mutableStateOf(items[0])
    }
    val mContext = LocalContext.current

    ModalNavigationDrawer(drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(drawerContainerColor= SideBack) {
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
                        label = { Text(text = item.title, color = TextPrimary, fontSize = 20.sp)},
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
                                mToast(mContext)
                            }
                        }
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(bottom = 30.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    TextButton(
                        onClick = {}
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.button_exit),
                            contentDescription = "exit"
                        )
                    }
                }
            }
        },
        content = {

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
        })
}

private fun mToast(context: Context){
    Toast.makeText(context, "Данный раздел находится на стадии разработки и станет доступным в ближайщее время", Toast.LENGTH_LONG).show()
}