package ru.vsu.csf.bakebudget.screens

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
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
import ru.vsu.csf.bakebudget.components.Product
import ru.vsu.csf.bakebudget.getToken
import ru.vsu.csf.bakebudget.models.IngredientInProductModel
import ru.vsu.csf.bakebudget.models.IngredientModel
import ru.vsu.csf.bakebudget.models.ProductModel
import ru.vsu.csf.bakebudget.models.MenuItemModel
import ru.vsu.csf.bakebudget.models.OrderModel
import ru.vsu.csf.bakebudget.models.OutgoingModel
import ru.vsu.csf.bakebudget.models.response.IngredientResponseModel
import ru.vsu.csf.bakebudget.models.response.ProductResponseModel
import ru.vsu.csf.bakebudget.services.findAllIngredients
import ru.vsu.csf.bakebudget.services.findAllProducts
import ru.vsu.csf.bakebudget.services.getPicture
import ru.vsu.csf.bakebudget.ui.theme.PrimaryBack
import ru.vsu.csf.bakebudget.ui.theme.SideBack

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProductsScreen(
    navController: NavHostController,
    products: MutableList<ProductModel>,
    ingredientsAll: MutableList<IngredientModel>,
    isLogged: MutableState<Boolean>,
    retrofitAPI: RetrofitAPI,
    isDataReceivedProducts: MutableState<Boolean>,
    productsResponse: MutableList<ProductResponseModel>,
    ingredientsResponse: MutableList<IngredientResponseModel>,
    isDataReceivedIngredients: MutableState<Boolean>,
    ingredients: MutableList<IngredientModel>,
    ingredientsSet: MutableSet<String>,
    orders: MutableList<OrderModel>,
    isDataReceivedOrders : MutableState<Boolean>,
    productsAll: MutableList<ProductModel>,
    orders0: MutableList<OrderModel>, orders1: MutableList<OrderModel>, orders2: MutableList<OrderModel>, orders3: MutableList<OrderModel>
) {
    val mContext = LocalContext.current
    val item = listOf(MenuItemModel(R.drawable.products, "Готовые изделия"))
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val selectedItem = remember {
        mutableStateOf(item[0])
    }
    val eventParameters1 = "{\"button_clicked\":\"product_add\"}"

    if (getToken(mContext) != null && !isDataReceivedIngredients.value) {
        findAllIngredients(mContext, retrofitAPI, ingredientsResponse)
        isDataReceivedIngredients.value = true
    }
    if (getToken(mContext) != null && !isDataReceivedProducts.value) {
        findAllProducts(mContext, retrofitAPI, productsResponse, orders, isDataReceivedOrders, productsAll, orders0, orders1, orders2, orders3)
        isDataReceivedProducts.value = true
    }
    if (products.isEmpty() && productsResponse.isNotEmpty()) {
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
        for (product in products) {
            getPicture(mContext, retrofitAPI, product)
        }
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
                        .fillMaxHeight(0.1f)
                        .background(SideBack)
                        .padding(start = 8.dp, end = 8.dp),
                    shape = RoundedCornerShape(10.dp, 10.dp, 0.dp, 0.dp),
                ) {
                    Box(
                        modifier = Modifier.background(PrimaryBack),
                        contentAlignment = Alignment.Center
                    ) {
                        TextButton(
                            onClick = {
                                AppMetrica.reportEvent(
                                    "Product add button clicked",
                                    eventParameters1
                                )
                                navController.navigate("productAdd")
                            }
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.button_add),
                                contentDescription = "add"
                            )
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
                    Column(modifier = Modifier.background(SideBack)) {
                        Header(scope = scope, drawerState = drawerState)
                        if (products.isEmpty()) {
                            Box(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Это страница готовых изделий. \nЗдесь отображаются созданные вами кондитерские изделия! \nЧтобы добаить новое изделие, нажмите кнопку «ДОБАВИТЬ» в нижней панели. \nДля редактирования существующего изделия нажмите на изображение над названием изделия.",
                                    fontSize = 18.sp,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                        LazyVerticalGrid(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(0.91f)
                                .background(SideBack)
                                .padding(top = 20.dp),
                            columns = GridCells.Fixed(2)
                        ) {
                            itemsIndexed(products) { num, product ->
                                Product(navController, product = product, num)
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
                        .padding(top = 8.dp, end = 50.dp),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Text(text = "ГОТОВЫЕ ИЗДЕЛИЯ", fontSize = 24.sp, color = Color.White)
                }
            }
        }
    }
}