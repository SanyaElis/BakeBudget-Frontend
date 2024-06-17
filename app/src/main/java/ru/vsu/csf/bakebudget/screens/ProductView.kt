package ru.vsu.csf.bakebudget.screens

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
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
import ru.vsu.csf.bakebudget.components.EstimatedWeightName
import ru.vsu.csf.bakebudget.components.ImagePicker
import ru.vsu.csf.bakebudget.components.IngredientInRecipe
import ru.vsu.csf.bakebudget.components.InputTextField
import ru.vsu.csf.bakebudget.models.IngredientInProductModel
import ru.vsu.csf.bakebudget.models.IngredientModel
import ru.vsu.csf.bakebudget.models.MenuItemModel
import ru.vsu.csf.bakebudget.models.OutgoingModel
import ru.vsu.csf.bakebudget.models.ProductModel
import ru.vsu.csf.bakebudget.models.request.IngredientInProductRequestModel
import ru.vsu.csf.bakebudget.models.request.ProductRequestModel
import ru.vsu.csf.bakebudget.models.response.IngredientResponseModel
import ru.vsu.csf.bakebudget.models.response.ProductResponseModel
import ru.vsu.csf.bakebudget.services.addIngredientToProduct
import ru.vsu.csf.bakebudget.services.deleteOutgoing
import ru.vsu.csf.bakebudget.services.deleteProduct
import ru.vsu.csf.bakebudget.services.findAllIngredientsInProduct
import ru.vsu.csf.bakebudget.services.updateOutgoing
import ru.vsu.csf.bakebudget.services.updateProduct
import ru.vsu.csf.bakebudget.ui.theme.Back2
import ru.vsu.csf.bakebudget.ui.theme.PrimaryBack
import ru.vsu.csf.bakebudget.ui.theme.SideBack
import ru.vsu.csf.bakebudget.ui.theme.border
import ru.vsu.csf.bakebudget.ui.theme.sizeForSmallDevices
import ru.vsu.csf.bakebudget.utils.dataIncorrectToast
import ru.vsu.csf.bakebudget.utils.isCostValid
import ru.vsu.csf.bakebudget.utils.isNameValid
import ru.vsu.csf.bakebudget.utils.isWeightValid
import ru.vsu.csf.bakebudget.utils.sameNameProduct
import ru.vsu.csf.bakebudget.utils.successfulProductEdit
import java.util.Locale

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProductView(
    navController: NavHostController,
    ingredientsAll: MutableList<IngredientModel>,
    isLogged: MutableState<Boolean>,
    product: ProductModel,
    ingredientsResponse: MutableList<IngredientResponseModel>,
    retrofitAPI: RetrofitAPI,
    load: MutableState<Boolean>,
    products: MutableList<ProductModel>,
    productsResponse: MutableList<ProductResponseModel>
) {
    val mContext = LocalContext.current

    val item = listOf(MenuItemModel(R.drawable.products, "Готовые изделия"))
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val selectedItem = remember {
        mutableStateOf(item[0])
    }
    val estimatedWeight = remember {
        mutableStateOf(product.estWeight.toString())
    }
    val name = remember {
        mutableStateOf(product.name)
    }
    val selectedItemIndex = remember { mutableIntStateOf(0) }

    val selectedImageUri = remember {
        mutableStateOf<Uri?>(null)
    }
    val productDataReceived = remember {
        mutableStateOf(false)
    }


//    if (product.ingredients.isEmpty() && !productDataReceived.value) {
//        findAllIngredientsInProduct(mContext, retrofitAPI, product, ingredientsResponse)
//        productDataReceived.value = true
//    }
    if (!load.value) {
        product.ingredients.clear()
        findAllIngredientsInProduct(mContext, retrofitAPI, product, ingredientsResponse)
        load.value = true
    }

    val openAlertDialogDelete = remember { mutableStateOf(false) }

    when {
        openAlertDialogDelete.value -> {
            DeleteAlert(
                onDismissRequest = {
                    openAlertDialogDelete.value = false
                },
                onConfirmation = {
                    openAlertDialogDelete.value = false
                    navController.navigate("products")
                },
                "Удалить изделие",
                "Вы уверены, что хотите удалить изделие?",
                mContext,
                products,
                productsResponse,
                product.id,
                retrofitAPI,
                product
            )
        }
    }

    val retryHash = remember {
        mutableLongStateOf(0)
    }

    val productId: Int = product.id


    val openAlertDialog = remember { mutableStateOf(false) }
    when {
        openAlertDialog.value -> {
            AlertDialog2(
                onDismissRequest = {
                    openAlertDialog.value = false
                },
                onConfirmation = {
                    openAlertDialog.value = false
                },
                dialogTitle = "Найдите ингредиент",
                dialogText = "",
                ingredientsAll = ingredientsAll,
                selectedItemIndex,
                product.ingredients,
                mContext,
                ingredientsResponse,
                productId,
                mutableSetOf<String>(),
                true
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
                                    openAlertDialog.value = true
                                }
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.button_add),
                                    contentDescription = "add"
                                )
                            }
                            TextButton(
                                onClick = {
                                    if (!(isWeightValid(estimatedWeight.value) && isNameValid(name.value))) {
                                        dataIncorrectToast(mContext)
                                    } else {
                                        var nameUnique: Boolean = true
                                        for (prod in products) {
                                            if (prod.name == name.value) {
                                                nameUnique = false
                                            }
                                        }
                                        if (nameUnique || (product.name == name.value)) {
                                            for (ingredient in product.ingredients) {
                                                ingredient.productId = product.id
                                                addIngredientToProduct(
                                                    mContext,
                                                    retrofitAPI,
                                                    ingredient
                                                )
                                            }
                                            updateProduct(
                                                mContext,
                                                retrofitAPI,
                                                ProductRequestModel(
                                                    name.value,
                                                    estimatedWeight.value.toInt()
                                                ),
                                                product.id,
                                                selectedImageUri,
                                                product,
                                                retryHash
                                            )
                                            product.estWeight = estimatedWeight.value.toInt()
                                            product.name = name.value
                                            if (selectedImageUri.value != null) {
                                                product.url = null
                                            }
                                            //TODO:Если что убрать
                                            if (selectedImageUri.value != null) {
                                                product.uri = selectedImageUri.value
                                            }
                                            val eventParameters1 =
                                                "{\"button_clicked\":\"save product\"}"
                                            AppMetrica.reportEvent(
                                                "Existing product saved",
                                                eventParameters1
                                            )
                                            navController.navigate("products")
//                                        navController.navigate("home")
                                            successfulProductEdit(mContext)
                                        } else {
                                            sameNameProduct(mContext)
                                        }
                                    }
                                }
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.button_save),
                                    contentDescription = "save"
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
                        var last = 0
                        Header(
                            scope = scope,
                            drawerState = drawerState,
                            product.name,
                            openAlertDialogDelete
                        )
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(0.8f)
                                .background(SideBack)
                                .padding(top = 20.dp)
                        ) {
                            itemsIndexed(product.ingredients) { num, ingredient ->
                                IngredientInRecipe(
                                    ingredient = ingredient,
                                    if (num % 2 == 0) SideBack else Back2,
                                    product.ingredients,
                                    ingredientsAll,
                                    selectedItemIndex,
                                    ingredientsResponse,
                                    retrofitAPI,
                                    mutableSetOf<String>()
                                )
                                last = num
                            }
                            item {
                                EstimatedWeightName(
                                    color = if (last % 2 != 0) SideBack else SideBack,
                                    estimatedWeight = estimatedWeight,
                                    name = name
                                )
                            }
                            item {
                                ImagePicker(selectedImageUri, product.uri, product.url)
                            }
                        }
                    }
                }
            }
        })
}

@Composable
private fun Header(
    scope: CoroutineScope,
    drawerState: DrawerState,
    productName: String,
    openAlertDialogDelete: MutableState<Boolean>
) {
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
                Row(horizontalArrangement = Arrangement.SpaceBetween) {
                    Box(
                        modifier = Modifier
                            .background(PrimaryBack)
                            .padding(top = 8.dp),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        val configuration = LocalConfiguration.current
                        val width = configuration.screenWidthDp.dp
                        if (width < border) {
                            Text(
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1,
                                text = productName.uppercase(Locale.ROOT),
                                fontSize = sizeForSmallDevices,
                                color = Color.White,
                                modifier = Modifier.fillMaxWidth(0.77f),
                                textAlign = TextAlign.Center
                            )
                        } else {
                            Text(
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1,
                                text = productName.uppercase(Locale.ROOT),
                                fontSize = 24.sp,
                                color = Color.White,
                                modifier = Modifier.fillMaxWidth(0.77f),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(PrimaryBack)
                            .padding(top = 8.dp, end = 13.dp),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Icon(
                            modifier = Modifier
                                .padding(top = 5.dp)
                                .clickable(onClick = { openAlertDialogDelete.value = true }),
                            imageVector = Icons.Default.Delete,
                            tint = Color.White,
                            contentDescription = "delete",
                        )
                    }
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
                Text(text = "КОЛИЧЕСТВО В РЕЦЕПТЕ (гр.)", color = Color.White, fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun DeleteAlert(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    context: Context,
    products: MutableList<ProductModel>,
    productsResponse: MutableList<ProductResponseModel>,
    productId: Int,
    retrofitAPI: RetrofitAPI,
    product: ProductModel
) {
    androidx.compose.material3.AlertDialog(
        containerColor = SideBack,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        ),
        modifier = Modifier.fillMaxWidth(0.9f),
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    deleteProduct(context, retrofitAPI, productId)
                    for (ingredient in product.ingredients) {
                        product.ingredients.remove(ingredient)
                    }
                    for (outgoing in product.outgoings) {
                        product.outgoings.remove(outgoing)
                    }
                    if (products.contains(product)) {
                        products.remove(product)
                    }
                    onConfirmation()
                }
            ) {
                Text("Удалить")
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
