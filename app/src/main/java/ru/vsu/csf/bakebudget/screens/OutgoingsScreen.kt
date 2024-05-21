package ru.vsu.csf.bakebudget.screens

import android.annotation.SuppressLint
import android.content.Context
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Response
import ru.vsu.csf.bakebudget.R
import ru.vsu.csf.bakebudget.api.RetrofitAPI
import ru.vsu.csf.bakebudget.components.DropdownMenuProducts
import ru.vsu.csf.bakebudget.components.Outgoing
import ru.vsu.csf.bakebudget.components.OutgoingAdd
import ru.vsu.csf.bakebudget.models.IngredientInProductModel
import ru.vsu.csf.bakebudget.models.IngredientModel
import ru.vsu.csf.bakebudget.models.OutgoingModel
import ru.vsu.csf.bakebudget.models.MenuItemModel
import ru.vsu.csf.bakebudget.models.ProductModel
import ru.vsu.csf.bakebudget.models.request.IngredientInProductRequestModel
import ru.vsu.csf.bakebudget.models.request.IngredientRequestModel
import ru.vsu.csf.bakebudget.models.request.OutgoingRequestModel
import ru.vsu.csf.bakebudget.models.response.IngredientResponseModel
import ru.vsu.csf.bakebudget.models.response.ProductResponseModel
import ru.vsu.csf.bakebudget.ui.theme.Back2
import ru.vsu.csf.bakebudget.ui.theme.PrimaryBack
import ru.vsu.csf.bakebudget.ui.theme.SideBack
import ru.vsu.csf.bakebudget.utils.dataIncorrectToast
import ru.vsu.csf.bakebudget.utils.isCostValid
import ru.vsu.csf.bakebudget.utils.isNameValid
import java.util.Timer
import kotlin.concurrent.schedule

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun OutgoingsScreen(
    navController: NavHostController,
    outgoings: MutableList<OutgoingModel>,
    productsAll: MutableList<ProductModel>,
    isLogged: MutableState<Boolean>,
    retrofitAPI: RetrofitAPI,
    jwtToken: MutableState<String>,
    isDataReceivedProducts: MutableState<Boolean>,
    productsResponse: MutableList<ProductResponseModel>,
    ingredientsResponse: MutableList<IngredientResponseModel>,
    isDataReceivedIngredients: MutableState<Boolean>,
    isDataReceivedOutgoings : MutableState<Boolean>
) {
    val mContext = LocalContext.current
    val item = listOf(MenuItemModel(R.drawable.outgoings, "Издержки"))
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val selectedItemIndex = remember { mutableIntStateOf(0) }

    val selectedItem = remember {
        mutableStateOf(item[0])
    }
    //TODO: подсказки пользователям, когда нет ингредиентов



    if (jwtToken.value != "" && !isDataReceivedIngredients.value) {
        findAllIngredients(mContext, retrofitAPI, jwtToken, ingredientsResponse)
        isDataReceivedIngredients.value = true
    }
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

    Timer().schedule(2000) {
        if (jwtToken.value != "" && !isDataReceivedOutgoings.value) {
            for (prod in productsAll) {
                findAllOutgoingsInProduct(mContext, retrofitAPI, jwtToken, prod)
            }
            isDataReceivedOutgoings.value = true
        }
    }

    val name = remember {
        mutableStateOf("")
    }
    val value = remember {
        mutableStateOf("")
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
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            OutgoingAdd(name, value)
                            TextButton(
                                onClick = {
                                    if (!(isNameValid(name.value) && isCostValid(value.value))) {
                                        dataIncorrectToast(context = mContext)
                                    } else {
                                        createOutgoing(mContext, retrofitAPI, jwtToken, OutgoingRequestModel(name.value,
                                            value.value.toInt()), productsAll[selectedItemIndex.intValue], productsAll)
                                    }
                                }
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.button_add),
                                    contentDescription = "add"
                                )
                            }
                        }
                    }
                }
                    //TODO:выводить на какой вес
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
                            item {
                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (productsAll.isNotEmpty()) {
                                        DropdownMenuProducts(
                                            productsAll,
                                            selectedItemIndex = selectedItemIndex
                                        )
                                    }
                                }
                            }
                            if (productsAll.isNotEmpty()) {
                                itemsIndexed(productsAll[selectedItemIndex.intValue].outgoings) { num, outgoing ->
                                    if (num % 2 == 0) {
                                        Outgoing(
                                            outgoing,
                                            SideBack,
                                            productsAll[selectedItemIndex.intValue].outgoings,
                                            productsAll[selectedItemIndex.intValue].id,
                                            retrofitAPI, jwtToken
                                        )
                                    } else {
                                        Outgoing(
                                            outgoing,
                                            Back2,
                                            productsAll[selectedItemIndex.intValue].outgoings,
                                            productsAll[selectedItemIndex.intValue].id,
                                            retrofitAPI, jwtToken
                                        )
                                    }
                                }
                            } else{
                                item {
                                    Box(
                                        modifier = Modifier.padding(8.dp),
                                        contentAlignment = Alignment.BottomCenter
                                    ) {
                                        Text(text = "Сначала добавьте готовые изделия", fontSize = 20.sp,
                                            textAlign = TextAlign.Center)
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
                        .padding(top = 8.dp, end = 60.dp),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Text(text = "ИЗДЕРЖКИ", fontSize = 24.sp, color = Color.White)
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(40.dp)
                    .background(PrimaryBack)
                    .padding(start = 16.dp, top = 6.dp, bottom = 6.dp, end = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "НАЗВАНИЕ", color = Color.White, fontSize = 12.sp)
                Text(text = "СТОИМОСТЬ", color = Color.White, fontSize = 12.sp)
            }
        }
    }
}

@OptIn(DelicateCoroutinesApi::class)
private fun createOutgoing(
    ctx: Context,
    retrofitAPI: RetrofitAPI,
    jwtToken: MutableState<String>,
    outgoingModel: OutgoingRequestModel,
    product: ProductModel,
    productsAll: MutableList<ProductModel>
) {
    GlobalScope.launch(Dispatchers.Main) {
        val res =
            retrofitAPI.createOutgoing(product.id, outgoingModel, "Bearer ".plus(jwtToken.value))
        onResultCreateOutgoing(res, ctx, product, productsAll)
    }
}

fun onResultCreateOutgoing(
    result: Response<OutgoingModel?>?,
    ctx: Context,
    product: ProductModel,
    productsAll: MutableList<ProductModel>
) {
    Toast.makeText(
        ctx,
        "Response Code : " + result!!.code() + "\n" + result.body(),
        Toast.LENGTH_SHORT
    ).show()
    if (result.isSuccessful) {
        if (result.body() != null) {
            product.outgoings.add(result.body()!!)
        }
    }
}

@OptIn(DelicateCoroutinesApi::class)
private fun findAllOutgoingsInProduct(
    ctx: Context,
    retrofitAPI: RetrofitAPI,
    jwtToken: MutableState<String>,
    product: ProductModel
) {
    GlobalScope.launch(Dispatchers.Main) {
        val res =
            retrofitAPI.findAllOutgoingsInProduct(product.id, "Bearer ".plus(jwtToken.value)
            )
        onResultFindAllOutgoingsInProduct(res, ctx, product)
    }
}

private fun onResultFindAllOutgoingsInProduct(
    result: Response<List<OutgoingModel>?>?,
    ctx: Context,
    product: ProductModel
) {
    Toast.makeText(
        ctx,
        "Response Code : " + result!!.code() + "\n" + result.body(),
        Toast.LENGTH_SHORT
    ).show()
    if (result.body() != null) {
        if (result.body()!!.isNotEmpty()) {
            for (outgoing in result.body()!!) {
                product.outgoings.add(outgoing)
            }
        }
    }
}