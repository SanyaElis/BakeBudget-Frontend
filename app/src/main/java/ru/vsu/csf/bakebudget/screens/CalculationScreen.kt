package ru.vsu.csf.bakebudget.screens

import android.annotation.SuppressLint
import android.content.Context
import android.provider.ContactsContract.Data
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import io.appmetrica.analytics.AppMetrica
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ru.vsu.csf.bakebudget.DataIncorrectToast
import ru.vsu.csf.bakebudget.R
import ru.vsu.csf.bakebudget.components.DatePeriodField
import ru.vsu.csf.bakebudget.components.DropdownMenuProducts
import ru.vsu.csf.bakebudget.components.InputTextField
import ru.vsu.csf.bakebudget.components.SwitchForm
import ru.vsu.csf.bakebudget.models.IngredientModel
import ru.vsu.csf.bakebudget.models.MenuItemModel
import ru.vsu.csf.bakebudget.models.OrderModel
import ru.vsu.csf.bakebudget.models.ProductModel
import ru.vsu.csf.bakebudget.ui.theme.PrimaryBack
import ru.vsu.csf.bakebudget.ui.theme.SideBack
import ru.vsu.csf.bakebudget.ui.theme.TextPrimary
import java.time.format.DateTimeFormatter
import java.util.Random

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CalculationScreen(
    navController: NavHostController,
    isLogged: MutableState<Boolean>,
    productsAll: MutableList<ProductModel>,
    orders: MutableList<OrderModel>
) {
    val mContext = LocalContext.current
    val item = listOf(MenuItemModel(R.drawable.calculation, "Расчет стоимости"))
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val selectedItem = remember {
        mutableStateOf(item[0])
    }
    val selectedItemIndex = remember { mutableIntStateOf(0) }
    val weight = remember {
        mutableStateOf("")
    }
    val extraCost = remember {
        mutableStateOf("")
    }
    val markup = remember {
        mutableStateOf("")
    }

    val costPrice = remember {
        mutableIntStateOf(0)
    }
    val resultPrice = remember {
        mutableIntStateOf(0)
    }

    val eventParameters1 = "{\"button_clicked\":\"order_created\"}"

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
                                    if (weight.value.toIntOrNull() == null || markup.value.toIntOrNull() == null || extraCost.value.toIntOrNull() == null) {
                                        DataIncorrectToast(mContext)
                                    } else {
                                        costPrice.intValue = (1000..5000).random()
                                        resultPrice.intValue = costPrice.intValue*(100+markup.value.toInt())/100 + extraCost.value.toInt()
                                    }
                                }
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.button_calculate),
                                    contentDescription = "calculate"
                                )
                            }
                            TextButton(
                                onClick = {
                                    if (weight.value.toIntOrNull() == null) {
                                        DataIncorrectToast(mContext)
                                    } else {
                                        AppMetrica.reportEvent("Order created", eventParameters1)
                                        orders.add(
                                            OrderModel(
                                                0,
                                                productsAll[selectedItemIndex.intValue],
                                                resultPrice.intValue,
                                                weight.value.toInt()
                                            )
                                        )
                                        mToast(mContext)
                                    }
                                }
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.button_make_order),
                                    contentDescription = "make order"
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
                                .padding(start = 8.dp, top = 20.dp, bottom = 20.dp)
                        ) {
                            item {
                                Box(modifier = Modifier.padding(start = 8.dp)) {
                                    Text(
                                        text = "Выберите изделие",
                                        fontSize = 24.sp
                                    )
                                }
                                DropdownMenuProducts(
                                    productsAll,
                                    selectedItemIndex = selectedItemIndex
                                )
                                Spacer(modifier = Modifier.padding(12.dp))
                                Box(modifier = Modifier.padding(start = 8.dp)) {
                                    Text(
                                        text = "Вес изделия (граммы):",
                                        fontSize = 24.sp
                                    )
                                }
                                Row(modifier = Modifier.padding(start = 3.dp)) {
                                    InputTextField(
                                        text = "Вес",
                                        value = weight,
                                        max = 10,
                                        true
                                    )
                                }
                                Spacer(modifier = Modifier.padding(12.dp))
                                Box(modifier = Modifier.padding(start = 8.dp)) {
                                    Text(
                                        text = "Доп. расходы (рубли):",
                                        fontSize = 24.sp
                                    )
                                }
                                Row(modifier = Modifier.padding(start = 3.dp)) {
                                    InputTextField(
                                        text = "Расходы",
                                        value = extraCost,
                                        max = 10,
                                        true
                                    )
                                }
                                Spacer(modifier = Modifier.padding(12.dp))
                                Box(modifier = Modifier.padding(start = 8.dp)) {
                                    Text(
                                        text = "Коэффициент наценки (%):",
                                        fontSize = 24.sp
                                    )
                                }
                                Row(modifier = Modifier.padding(start = 3.dp)) {
                                    InputTextField(
                                        text = "Коэффициент",
                                        value = markup,
                                        max = 10,
                                        true
                                    )
                                }
                            }
                            item {
                                Column {
                                    Spacer(modifier = Modifier.padding(12.dp))
                                    Box(modifier = Modifier.padding(start = 8.dp)) {
                                        Text(
                                            text = "Себестоимость : ${costPrice.intValue} руб.",
                                            fontSize = 24.sp
                                        )
                                    }
                                    Spacer(modifier = Modifier.padding(12.dp))
                                    Box(modifier = Modifier.padding(start = 8.dp)) {
                                        Text(
                                            text = "Конечная стоимость : ${resultPrice.intValue} руб.",
                                            fontSize = 24.sp
                                        )
                                    }
                                }
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
            .fillMaxHeight(0.08f)
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
                        .padding(top = 8.dp, end = 48.dp),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Text(text = "РАСЧЕТ СТОИМОСТИ", fontSize = 24.sp, color = Color.White)
                }
            }
        }
    }
}

private fun mToast(context: Context) {
    Toast.makeText(
        context,
        "Заказ создан",
        Toast.LENGTH_LONG
    ).show()
}

fun IntRange.random() = Random().nextInt((endInclusive + 1) - start) + start