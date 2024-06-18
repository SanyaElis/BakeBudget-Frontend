package ru.vsu.csf.bakebudget.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.DismissibleDrawerSheet
import androidx.compose.material3.DismissibleNavigationDrawer
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import io.appmetrica.analytics.AppMetrica
import kotlinx.coroutines.launch
import ru.vsu.csf.bakebudget.R
import ru.vsu.csf.bakebudget.api.RetrofitAPI
import ru.vsu.csf.bakebudget.components.AlertDialog
import ru.vsu.csf.bakebudget.components.InputTextField
import ru.vsu.csf.bakebudget.getToken
import ru.vsu.csf.bakebudget.models.IngredientInProductModel
import ru.vsu.csf.bakebudget.models.IngredientModel
import ru.vsu.csf.bakebudget.models.MenuItemModel
import ru.vsu.csf.bakebudget.models.OrderModel
import ru.vsu.csf.bakebudget.models.OutgoingModel
import ru.vsu.csf.bakebudget.models.ProductModel
import ru.vsu.csf.bakebudget.models.response.IngredientResponseModel
import ru.vsu.csf.bakebudget.models.response.ProductResponseModel
import ru.vsu.csf.bakebudget.services.findAllIngredients
import ru.vsu.csf.bakebudget.services.findAllProducts
import ru.vsu.csf.bakebudget.services.getPicture
import ru.vsu.csf.bakebudget.ui.theme.PrimaryBack
import ru.vsu.csf.bakebudget.ui.theme.SecondaryBack
import ru.vsu.csf.bakebudget.ui.theme.SideBack
import ru.vsu.csf.bakebudget.ui.theme.TextPrimary

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(navController: NavHostController, isLogged: MutableState<Boolean>,
               products: MutableList<ProductModel>,
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
               orders0: MutableList<OrderModel>, orders1: MutableList<OrderModel>, orders2: MutableList<OrderModel>, orders3: MutableList<OrderModel>,
               firstTimePr: MutableState<Boolean>) {
    val item = listOf(MenuItemModel(R.drawable.home, "Главная"))
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val selectedItem = remember {
        mutableStateOf(item[0])
    }

    val retryHash = remember {
        mutableLongStateOf(0)
    }
    val mContext = LocalContext.current


    //TODO: добавить кнопку типа начать, чтобы открылось выпадающее меню или придумать другой вариант


    val openAlertDialog = remember { mutableStateOf(false) }
    when {
        openAlertDialog.value -> {
            AlertDialogHome(
                onDismissRequest = {
                    openAlertDialog.value = false },
                onConfirmation = {
                    openAlertDialog.value = false
                }
            )
        }
    }
    if (isLogged.value) {
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
            for (product in products) {
                getPicture(mContext, retrofitAPI, product, retryHash)
            }
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
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(PrimaryBack)
                    .padding(top = 13.dp, bottom = 10.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .background(PrimaryBack)
                ) {
                    Row(modifier = Modifier
                        .fillMaxWidth(),
                        ) {
                        Box {
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
                        Spacer(Modifier.weight(1f))
                        Box(modifier = Modifier.padding(5.dp)) {
                            IconButton(onClick = {
                                val eventParameters1 = "{\"button_clicked\":\"information\"}"
                                AppMetrica.reportEvent(
                                    "View of Info about app",
                                    eventParameters1
                                )
                                openAlertDialog.value = true
                            }) {
                                Icon(imageVector = Icons.Default.Info, contentDescription = "Info", tint = Color.White)
                            }
                        }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.8f),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.logo),
                            contentDescription = "logo"
                        )
                    }
                }
                if (isLogged.value.not()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 100.dp),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        TextButton(
                            onClick = { navController.navigate("login") }
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.button_enter),
                                contentDescription = "enter"
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 60.dp),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        TextButton(
                            onClick = { navController.navigate("register") },
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.button_register),
                                contentDescription = "registration",
                            )
                        }
                    }
                }
            }
        })

}

@Composable
fun AlertDialogHome(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
) {
    val scroll = rememberScrollState(0)
    androidx.compose.material3.AlertDialog(
        containerColor = SideBack,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        ),
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .fillMaxHeight(0.9f),
        title = {
            Text(text = "Добро пожаловать в BAKEBUDGET", textAlign = TextAlign.Center)
        },
        text = {
            Column {
                Text(text = "Наше приложение для домашних кондитеров - это твой лучший друг в мире кондитерства! Мы создали удобный инструмент, который поможет тебе легко управлять своим кондитерским бизнесом. Вот что ты сможешь делать в нашим приложении:\n" +
                        "\n" +
                        "1. Ингредиенты как на ладони:\n" +
                        "   - Легко добавляй, удаляй и редактируй разнообразные ингредиенты для твоих кондитерских шедевров.\n" +
                        "\n" +
                        "2. Будь в курсе издержек:\n" +
                        "   - Добавляй издержки, связанные с производством кондитерских изделий. Это помогает в расчетах при определении стоимости продукции!\n" +
                        "\n" +
                        "3. Заказы на подходе:\n" +
                        "   - Принимай заказы на твои волшебные кулинарные шедевры.\n" +
                        "\n" +
                        "4. Отчёты для успеха:\n" +
                        "   - Получай отчёты о доходах и заказах. Смотри, как процветает твой кондитерский бизнес и принимай грамотные решения для его развития.\n" +
                        "\n" +
                        "5. Рассчитывай себестоимость:\n" +
                        "   - Делай расчеты себестоимости и конечной стоимости своих изделий!\n" +
                        "\n" +
                        "6. Создавай свои команды:\n" +
                        "   - Приглашай помощников в группу, чтобы следить за бизнесом, если вы не одни!\n" +
                        "\n" +
                        "С нашим приложением, заботливо созданным для тебя, ты сможешь создавать, управлять и развивать свой кондитерский бизнес с лёгкостью и улыбкой!",
                    modifier = Modifier.verticalScroll(scroll))
            }
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text("ОК")
            }
        }
    )
}