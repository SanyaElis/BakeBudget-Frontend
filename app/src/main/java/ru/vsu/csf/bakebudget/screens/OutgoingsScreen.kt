package ru.vsu.csf.bakebudget.screens

import android.annotation.SuppressLint
import android.content.Context
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import io.appmetrica.analytics.AppMetrica
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ru.vsu.csf.bakebudget.R
import ru.vsu.csf.bakebudget.api.RetrofitAPI
import ru.vsu.csf.bakebudget.components.DropdownMenuProducts
import ru.vsu.csf.bakebudget.components.InputTextField
import ru.vsu.csf.bakebudget.components.InputTextFieldCost
import ru.vsu.csf.bakebudget.components.Outgoing
import ru.vsu.csf.bakebudget.getToken
import ru.vsu.csf.bakebudget.models.IngredientInProductModel
import ru.vsu.csf.bakebudget.models.MenuItemModel
import ru.vsu.csf.bakebudget.models.OrderModel
import ru.vsu.csf.bakebudget.models.OutgoingModel
import ru.vsu.csf.bakebudget.models.ProductModel
import ru.vsu.csf.bakebudget.models.request.OutgoingRequestModel
import ru.vsu.csf.bakebudget.models.response.IngredientResponseModel
import ru.vsu.csf.bakebudget.models.response.ProductResponseModel
import ru.vsu.csf.bakebudget.services.createOutgoing
import ru.vsu.csf.bakebudget.services.findAllIngredients
import ru.vsu.csf.bakebudget.services.findAllOutgoingsInProduct
import ru.vsu.csf.bakebudget.services.findAllProducts
import ru.vsu.csf.bakebudget.ui.theme.Back2
import ru.vsu.csf.bakebudget.ui.theme.PrimaryBack
import ru.vsu.csf.bakebudget.ui.theme.SideBack
import ru.vsu.csf.bakebudget.ui.theme.border
import ru.vsu.csf.bakebudget.ui.theme.borderH
import ru.vsu.csf.bakebudget.ui.theme.sizeForSmallDevices
import ru.vsu.csf.bakebudget.utils.dataIncorrectToast
import ru.vsu.csf.bakebudget.utils.isCostValid
import ru.vsu.csf.bakebudget.utils.isNameValid

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun OutgoingsScreen(
    navController: NavHostController,
    outgoings: MutableList<OutgoingModel>,
    productsAll: MutableList<ProductModel>,
    isLogged: MutableState<Boolean>,
    retrofitAPI: RetrofitAPI,
    isDataReceivedProducts: MutableState<Boolean>,
    productsResponse: MutableList<ProductResponseModel>,
    ingredientsResponse: MutableList<IngredientResponseModel>,
    isDataReceivedIngredients: MutableState<Boolean>,
    isDataReceivedOutgoings: MutableState<Boolean>,
    orders: MutableList<OrderModel>,
    isDataReceivedOrders: MutableState<Boolean>,
    orders0: MutableList<OrderModel>,
    orders1: MutableList<OrderModel>,
    orders2: MutableList<OrderModel>,
    orders3: MutableList<OrderModel>,
) {
    if (!isLogged.value) {
        navController.navigate("home")
    }

    val configuration = LocalConfiguration.current
    val height = configuration.screenHeightDp.dp
    val mContext = LocalContext.current
    val item = listOf(MenuItemModel(R.drawable.outgoings, "Издержки"))
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val selectedItemIndex = remember { mutableIntStateOf(0) }

    val selectedItem = remember {
        mutableStateOf(item[0])
    }


    if (getToken(mContext) != null && !isDataReceivedIngredients.value) {
        findAllIngredients(mContext, retrofitAPI, ingredientsResponse)
        isDataReceivedIngredients.value = true
    }
    if (getToken(mContext) != null && !isDataReceivedProducts.value) {
        findAllProducts(
            mContext,
            retrofitAPI,
            productsResponse,
            orders,
            isDataReceivedOrders,
            productsAll,
            orders0,
            orders1,
            orders2,
            orders3
        )
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
                    product.weight,
                    null
                )
            )
        }
    }


    if (getToken(mContext) != null && !isDataReceivedOutgoings.value && productsAll.isNotEmpty()) {
        for (prod in productsAll) {
            findAllOutgoingsInProduct(mContext, retrofitAPI, prod)
        }
        isDataReceivedOutgoings.value = true
    }

    val name = remember {
        mutableStateOf("")
    }
    val value = remember {
        mutableStateOf("")
    }

    val openAlertDialog = remember { mutableStateOf(false) }
    when {
        openAlertDialog.value -> {
            AlertOutgoingAdd(
                onDismissRequest = {
                    openAlertDialog.value = false
                },
                onConfirmation = {
                    openAlertDialog.value = false
                },
                dialogTitle = "Создать издержку",
                dialogText = "Введите название и цену.",
                mContext,
                retrofitAPI,
                name,
                value, productsAll, selectedItemIndex
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
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            TextButton(
                                onClick = {
                                    openAlertDialog.value = true
                                }
                            ) {
                                Image(
                                    painter = painterResource(id = if (height > borderH) R.drawable.button_add else R.drawable.add_button_small),
                                    contentDescription = "add"
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
                                .fillMaxHeight(0.91f)
                                .background(SideBack)
                                .padding(top = 20.dp)
                        ) {
                            item {
                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (productsAll.isNotEmpty()) {
                                        Column {
                                            DropdownMenuProducts(
                                                productsAll,
                                                selectedItemIndex = selectedItemIndex
                                            )
                                            Text(
                                                text = "(" + productsAll[selectedItemIndex.intValue].estWeight + " гр.)",
                                                fontSize = 14.sp,
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                    } else {
                                        Box(modifier = Modifier.padding(16.dp)) {
                                            Text(
                                                text = "Это страница добавления издержек. \nЗдесь вы можете добавить свои затраты на приготовление изделий!.\nСейчас у вас нет ни одного готового изделия, создайте его на соответствующей странице!",
                                                fontSize = 18.sp,
                                                textAlign = TextAlign.Center
                                            )
                                        }
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
                                            retrofitAPI
                                        )
                                    } else {
                                        Outgoing(
                                            outgoing,
                                            Back2,
                                            productsAll[selectedItemIndex.intValue].outgoings,
                                            productsAll[selectedItemIndex.intValue].id,
                                            retrofitAPI
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
                    val configuration = LocalConfiguration.current
                    val width = configuration.screenWidthDp.dp
                    if (width < border) {
                        Text(text = "ИЗДЕРЖКИ", fontSize = sizeForSmallDevices, color = Color.White)
                    } else {
                        Text(text = "ИЗДЕРЖКИ", fontSize = 24.sp, color = Color.White)
                    }
                }
            }
            val configuration = LocalConfiguration.current
            val height = configuration.screenHeightDp.dp
            val width = configuration.screenWidthDp.dp
            if ((height > borderH + 50.dp) && (width > border)) {
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
                    Text(text = "СТОИМОСТЬ (руб.)", color = Color.White, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun AlertOutgoingAdd(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    context: Context,
    retrofitAPI: RetrofitAPI,
    name: MutableState<String>,
    value: MutableState<String>,
    productsAll: MutableList<ProductModel>,
    selectedItemIndex: MutableIntState

) {
    AlertDialog(
        containerColor = SideBack,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        ),
        modifier = Modifier.fillMaxWidth(0.9f),
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Column {
                Text(text = dialogText)
                InputTextField(placeholder = "Название", name, 30, true)
                InputTextFieldCost(placeholder = "Стоимость", value, 8, true)
            }
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (!(isNameValid(name.value) && isCostValid(value.value))) {
                        dataIncorrectToast(context = context)
                        val eventParameters2 =
                            "{\"button_clicked\":\"create outgoing\"}"
                        AppMetrica.reportEvent(
                            "Outgoing add failed",
                            eventParameters2
                        )
                    } else {
                        createOutgoing(
                            context, retrofitAPI, OutgoingRequestModel(
                                name.value,
                                value.value.toInt()
                            ), productsAll[selectedItemIndex.intValue], productsAll
                        )
                        val eventParameters1 =
                            "{\"button_clicked\":\"create outgoing\"}"
                        AppMetrica.reportEvent(
                            "Outgoing created",
                            eventParameters1
                        )
                        name.value = ""
                        value.value = ""
                        onConfirmation()
                    }
                }
            ) {
                Text("Сохранить")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Отмена")
            }
        }
    )
}