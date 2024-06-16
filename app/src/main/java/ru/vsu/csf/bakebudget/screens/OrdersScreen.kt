package ru.vsu.csf.bakebudget.screens

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
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
import ru.vsu.csf.bakebudget.components.Order
import ru.vsu.csf.bakebudget.components.OrderStateRow
import ru.vsu.csf.bakebudget.components.Product
import ru.vsu.csf.bakebudget.getToken
import ru.vsu.csf.bakebudget.models.IngredientInProductModel
import ru.vsu.csf.bakebudget.models.IngredientModel
import ru.vsu.csf.bakebudget.models.MenuItemModel
import ru.vsu.csf.bakebudget.models.OrderModel
import ru.vsu.csf.bakebudget.models.OutgoingModel
import ru.vsu.csf.bakebudget.models.ProductModel
import ru.vsu.csf.bakebudget.models.response.IngredientResponseModel
import ru.vsu.csf.bakebudget.models.response.OrderResponseModel
import ru.vsu.csf.bakebudget.models.response.ProductResponseModel
import ru.vsu.csf.bakebudget.services.findAllIngredients
import ru.vsu.csf.bakebudget.services.findAllOrders
import ru.vsu.csf.bakebudget.services.findAllProducts
import ru.vsu.csf.bakebudget.services.onResultFindAllOrders
import ru.vsu.csf.bakebudget.ui.theme.PrimaryBack
import ru.vsu.csf.bakebudget.ui.theme.SideBack
import ru.vsu.csf.bakebudget.ui.theme.border
import ru.vsu.csf.bakebudget.ui.theme.sizeForSmallDevices

@OptIn(ExperimentalLayoutApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun OrdersScreen(
    navController: NavHostController,
    isLogged: MutableState<Boolean>,
    orders: MutableList<OrderModel>,
    retrofitAPI: RetrofitAPI,
    isDataReceivedOrders : MutableState<Boolean>,
    productsAll: MutableList<ProductModel>,
    orders0: MutableList<OrderModel>, orders1: MutableList<OrderModel>, orders2: MutableList<OrderModel>, orders3: MutableList<OrderModel>,
    isDataReceivedProducts: MutableState<Boolean>,
    productsResponse: MutableList<ProductResponseModel>,
    ingredientsResponse: MutableList<IngredientResponseModel>,
    isDataReceivedIngredients: MutableState<Boolean>,
    ingredients: MutableList<IngredientModel>,
    ingredientsSet: MutableSet<String>,
    products: MutableList<ProductModel>,
    firstTimePr: MutableState<Boolean>
) {
    val mContext = LocalContext.current

    val item = listOf(MenuItemModel(R.drawable.orders, "Заказы"))
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val selectedItem = remember {
        mutableStateOf(item[0])
    }

    val state1 = remember { mutableStateOf(true) }
    val state2 = remember { mutableStateOf(true) }
    val state3 = remember { mutableStateOf(true) }
    val state4 = remember { mutableStateOf(true) }

    if (getToken(mContext) != null && !isDataReceivedIngredients.value) {
        findAllIngredients(mContext, retrofitAPI, ingredientsResponse)
        isDataReceivedIngredients.value = true
    }
    if (getToken(mContext) != null && !isDataReceivedProducts.value) {
        findAllProducts(mContext, retrofitAPI, productsResponse, orders, isDataReceivedOrders, productsAll, orders0, orders1, orders2, orders3)
        isDataReceivedProducts.value = true
    }
    if (products.isEmpty() && productsResponse.isNotEmpty() && firstTimePr.value) {
        firstTimePr.value = false
        for (product in productsResponse) {
            products.add(
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
                    product.weight,
                    null
                )
            )
        }
//        for (product in products) {
//            getPicture(mContext, retrofitAPI, product)
//        }
    }
    if (ingredients.isEmpty() && ingredientsResponse.isNotEmpty()) {
        for (ingredient in ingredientsResponse) {
            ingredients.add(
                IngredientModel(
                    ingredient.name,
                    ingredient.weight,
                    ingredient.cost
                )
            )
            ingredientsSet.add(
                ingredient.name
            )
        }
    }

    if (getToken(mContext) != null && !isDataReceivedOrders.value) {
        findAllOrders(mContext, retrofitAPI, orders, productsAll, orders0, orders1, orders2, orders3, isDataReceivedOrders)
        isDataReceivedOrders.value = true
    }

    sortByState(orders, orders0, orders1, orders2, orders3)

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
            Scaffold() {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .background(SideBack)
                        .padding(bottom = 10.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .background(SideBack)
                    ) {
                        Header(scope = scope, drawerState = drawerState)
                        LazyVerticalGrid(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight()
                                .background(SideBack)
                                .padding(top = 10.dp),
                            columns = GridCells.Fixed(2)
                        ) {
                            item(span = { GridItemSpan(2) }) { OrderStateRow("НЕ НАЧАТЫ", state1) }
                            if (state1.value) {
                                itemsIndexed(orders0) { _, order ->
                                    Order(order = order, orders, orders0, orders1, orders2, orders3, retrofitAPI)
                                }
                            }
                            item(span = { GridItemSpan(2) }) { OrderStateRow("В ПРОЦЕССЕ", state2) }
                            if (state2.value) {
                                itemsIndexed(orders1) { _, order ->
                                    Order(order = order, orders, orders0, orders1, orders2, orders3, retrofitAPI)
                                }
                            }
                            item(span = { GridItemSpan(2) }) { OrderStateRow("ЗАВЕРШЕНЫ", state3) }
                            if (state3.value) {
                                itemsIndexed(orders2) { _, order ->
                                    Order(order = order, orders, orders0, orders1, orders2, orders3, retrofitAPI)
                                }
                            }
                            item(span = { GridItemSpan(2) }) { OrderStateRow("ОТМЕНЕНЫ", state4) }
                            if (state4.value) {
                                itemsIndexed(orders3) { _, order ->
                                    Order(order = order, orders, orders0, orders1, orders2, orders3, retrofitAPI)
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
                        .padding(top = 8.dp, end = 60.dp),
                    contentAlignment = Alignment.TopCenter
                ) {
                    val configuration = LocalConfiguration.current
                    val width = configuration.screenWidthDp.dp
                    if (width < border) {
                        Text(text = "ЗАКАЗЫ", fontSize = sizeForSmallDevices, color = Color.White)
                    } else {
                        Text(text = "ЗАКАЗЫ", fontSize = 24.sp, color = Color.White)
                    }
                }
            }
        }
    }
}

public fun sortByState(orders: MutableList<OrderModel>, orders0: MutableList<OrderModel>, orders1: MutableList<OrderModel>, orders2: MutableList<OrderModel>, orders3: MutableList<OrderModel>) {
    orders0.clear()
    orders1.clear()
    orders2.clear()
    orders3.clear()
    for (order in orders) {
        if (order.status == 0) {
            orders0.add(order)
        }
        if (order.status == 1) {
            orders1.add(order)
        }
        if (order.status == 2) {
            orders2.add(order)
        }
        if (order.status == 3) {
            orders3.add(order)
        }
    }
}

//TODO:подгружать все в начале