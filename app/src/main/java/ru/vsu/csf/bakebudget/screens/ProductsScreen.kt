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
import androidx.compose.foundation.lazy.grid.items
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
import ru.vsu.csf.bakebudget.models.IngredientInProductModel
import ru.vsu.csf.bakebudget.models.IngredientModel
import ru.vsu.csf.bakebudget.models.ProductModel
import ru.vsu.csf.bakebudget.models.MenuItemModel
import ru.vsu.csf.bakebudget.models.OutgoingModel
import ru.vsu.csf.bakebudget.models.response.IngredientResponseModel
import ru.vsu.csf.bakebudget.models.response.ProductResponseModel
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
    jwtToken: MutableState<String>,
    isDataReceivedProducts: MutableState<Boolean>,
    productsResponse: MutableList<ProductResponseModel>
) {
    val mContext = LocalContext.current
    val item = listOf(MenuItemModel(R.drawable.products, "Готовые изделия"))
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val selectedItem = remember {
        mutableStateOf(item[0])
    }
    val eventParameters1 = "{\"button_clicked\":\"product_add\"}"

    if (jwtToken.value != "" && !isDataReceivedProducts.value) {
        findAll(mContext, retrofitAPI, jwtToken, productsResponse)
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
                                AppMetrica.reportEvent("Product add button clicked", eventParameters1)
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
                    Column {
                        Header(scope = scope, drawerState = drawerState)
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

@OptIn(DelicateCoroutinesApi::class)
private fun findAll(
    ctx: Context,
    retrofitAPI: RetrofitAPI,
    jwtToken: MutableState<String>,
    productsResponse: MutableList<ProductResponseModel>
) {
    GlobalScope.launch(Dispatchers.Main) {
        val res = retrofitAPI.findAllProducts("Bearer ".plus(jwtToken.value))
        onResultFindAll(res, productsResponse)
    }
}

private fun onResultFindAll(
    result: Response<List<ProductResponseModel>?>?,
    productsResponse: MutableList<ProductResponseModel>
) {
    if (result!!.body() != null) {
        if (result.body()!!.isNotEmpty()) {
            for (prod in result.body()!!) {
                productsResponse.add(prod)
            }
        }
    }
}