package ru.vsu.csf.bakebudget.screens

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
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
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Response
import ru.vsu.csf.bakebudget.R
import ru.vsu.csf.bakebudget.api.RetrofitAPI
import ru.vsu.csf.bakebudget.components.DropdownMenuProducts
import ru.vsu.csf.bakebudget.components.InputTextField
import ru.vsu.csf.bakebudget.models.IngredientInProductModel
import ru.vsu.csf.bakebudget.models.MenuItemModel
import ru.vsu.csf.bakebudget.models.OrderModel
import ru.vsu.csf.bakebudget.models.OutgoingModel
import ru.vsu.csf.bakebudget.models.ProductModel
import ru.vsu.csf.bakebudget.models.request.CalculationRequestModel
import ru.vsu.csf.bakebudget.models.request.OrderRequestModel
import ru.vsu.csf.bakebudget.models.request.OutgoingRequestModel
import ru.vsu.csf.bakebudget.models.request.ProductRequestModel
import ru.vsu.csf.bakebudget.models.response.CalculationResponseModel
import ru.vsu.csf.bakebudget.models.response.OrderResponseModel
import ru.vsu.csf.bakebudget.models.response.ProductResponseModel
import ru.vsu.csf.bakebudget.ui.theme.PrimaryBack
import ru.vsu.csf.bakebudget.ui.theme.SideBack
import ru.vsu.csf.bakebudget.utils.dataIncorrectToast
import ru.vsu.csf.bakebudget.utils.isCostValid
import ru.vsu.csf.bakebudget.utils.isWeightValid
import ru.vsu.csf.bakebudget.utils.sameOrder
import java.util.Random

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CalculationScreen(
    navController: NavHostController,
    isLogged: MutableState<Boolean>,
    productsAll: MutableList<ProductModel>,
    orders: MutableList<OrderModel>,
    retrofitAPI: RetrofitAPI,
    jwtToken: MutableState<String>,
    isDataReceivedProducts: MutableState<Boolean>,
    productsResponse: MutableList<ProductResponseModel>,

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

    val resultPriceLast = remember {
        mutableIntStateOf(0)
    }
    val same = remember {
        mutableStateOf(false)
    }

    val eventParameters1 = "{\"button_clicked\":\"order_created\"}"

    if (jwtToken.value != "" && !isDataReceivedProducts.value) {
        findAllProducts(mContext, retrofitAPI, jwtToken, productsResponse)
        isDataReceivedProducts.value = true
    }
    if (productsAll.isEmpty() && productsResponse.isNotEmpty()) {
        for (product in productsResponse) {
            productsAll.add(
                ProductModel(
                    product.id,
                    null,
                    R.drawable.cake,
                    product.name,
                    remember {
                        mutableStateListOf<IngredientInProductModel>()
                    },
                    remember {
                        mutableStateListOf<OutgoingModel>()
                    },
                    product.weight
                )
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
                                    if (!(isWeightValid(weight.value) && isCostValid(markup.value) && isCostValid(
                                            extraCost.value
                                        ))
                                    ) {
                                        dataIncorrectToast(mContext)
                                    } else {
                                        calculate(
                                            mContext,
                                            retrofitAPI,
                                            jwtToken,
                                            CalculationRequestModel(
                                                extraCost.value.toInt(),
                                                (100 + markup.value.toInt()) / 100.0,
                                                weight.value.toInt(),
                                                productsAll[selectedItemIndex.intValue].id
                                            ),
                                            costPrice,
                                            resultPrice
                                        )
//                                        costPrice.intValue = (1000..5000).random()
//                                        resultPrice.intValue = costPrice.intValue*(100+markup.value.toInt())/100 + extraCost.value.toInt()
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
                                    if (!(isWeightValid(weight.value) && isCostValid(markup.value) && isCostValid(
                                            extraCost.value
                                        ))
                                    ) {
                                        dataIncorrectToast(mContext)
                                    } else {
                                        //TODO:заблокировать повторное создание
                                        AppMetrica.reportEvent("Order created", eventParameters1)
//                                        if (resultPriceLast.intValue == resultPrice.intValue || same.value) {
//                                            sameOrder(mContext)
//                                            same.value = true
//                                        } else {
                                            create(
                                                mContext,
                                                retrofitAPI,
                                                jwtToken,
                                                OrderRequestModel(
                                                    //TODO:ввод названия, чтобы одинаковые нельзя было создавать
                                                    productsAll[selectedItemIndex.intValue].name.plus(productsAll[selectedItemIndex.intValue].id.toString()),
                                                    "",
                                                    extraCost.value.toInt(),
                                                    weight.value.toInt(),
                                                    (100 + markup.value.toInt()) / 100.0,
                                                    productsAll[selectedItemIndex.intValue].id
                                                ),
                                                orders, productsAll, selectedItemIndex, resultPrice
                                            )
                                            resultPriceLast.intValue = resultPrice.intValue
                                            same.value = false
                                        }
                                    }
//                                }
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
                                if (productsAll.isNotEmpty()) {
                                    DropdownMenuProducts(
                                        productsAll,
                                        selectedItemIndex = selectedItemIndex
                                    )
                                }//TODO:make mock if there is no products
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
                            //TODO:сначала нажал создать заказ до кнопки рассчитать
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

@OptIn(DelicateCoroutinesApi::class)
private fun calculate(
    ctx: Context,
    retrofitAPI: RetrofitAPI,
    jwtToken: MutableState<String>,
    calculationRequestModel: CalculationRequestModel,
    costPrice: MutableState<Int>,
    resultPrice: MutableState<Int>
) {
    GlobalScope.launch(Dispatchers.Main) {
        val res =
            retrofitAPI.calculate(
                calculationRequestModel,
                "Bearer ".plus(jwtToken.value)
            )
        onResultCalculate(res, ctx, costPrice, resultPrice)
    }
}

private fun onResultCalculate(
    result: Response<CalculationResponseModel?>?,
    ctx: Context,
    costPrice: MutableState<Int>,
    resultPrice: MutableState<Int>
) {
    Toast.makeText(
        ctx,
        "Response Code : " + result!!.code() + "\n" + result.body(),
        Toast.LENGTH_SHORT
    ).show()
    costPrice.value = result.body()!!.costPrice.toInt()
    resultPrice.value = result.body()!!.finalCost.toInt()
}

//TODO:разделить на классы
@OptIn(DelicateCoroutinesApi::class)
private fun create(
    ctx: Context,
    retrofitAPI: RetrofitAPI,
    jwtToken: MutableState<String>,
    orderRequestModel: OrderRequestModel,
    orders : MutableList<OrderModel>,
    productsAll: MutableList<ProductModel>,
    selectedItemIndex : MutableState<Int>,
    resultPrice: MutableState<Int>
) {
    GlobalScope.launch(Dispatchers.Main) {
        val res =
            retrofitAPI.createOrder(orderRequestModel, "Bearer ".plus(jwtToken.value))
        onResultCreateOrder(res, ctx, orders, productsAll, selectedItemIndex, resultPrice)
    }
}

private fun onResultCreateOrder(
    result: Response<OrderResponseModel?>?,
    ctx: Context,
    orders : MutableList<OrderModel>,
    productsAll: MutableList<ProductModel>,
    selectedItemIndex : MutableState<Int>,
    resultPrice: MutableState<Int>
) {
    Toast.makeText(
        ctx,
        "Response Code : " + result!!.code() + "\n" + result.body(),
        Toast.LENGTH_SHORT
    ).show()
    if (result.body()!=null) {
        orders.add(
            OrderModel(
                result.body()!!.id,
                0,
                productsAll[selectedItemIndex.value],
                resultPrice.value.toDouble(),
                result.body()!!.finalWeight
            )
        )
        mToast(ctx)
    }
}