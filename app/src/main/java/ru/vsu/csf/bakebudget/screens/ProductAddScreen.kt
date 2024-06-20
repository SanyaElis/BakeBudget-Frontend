package ru.vsu.csf.bakebudget.screens

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
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
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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
import ru.vsu.csf.bakebudget.components.EstimatedWeightName
import ru.vsu.csf.bakebudget.components.ImagePicker
import ru.vsu.csf.bakebudget.components.IngredientInRecipe
import ru.vsu.csf.bakebudget.components.InputTextField
import ru.vsu.csf.bakebudget.components.InputTextFieldWeight
import ru.vsu.csf.bakebudget.models.IngredientInProductModel
import ru.vsu.csf.bakebudget.models.IngredientModel
import ru.vsu.csf.bakebudget.models.MenuItemModel
import ru.vsu.csf.bakebudget.models.OutgoingModel
import ru.vsu.csf.bakebudget.models.ProductModel
import ru.vsu.csf.bakebudget.models.request.ProductRequestModel
import ru.vsu.csf.bakebudget.models.response.IngredientResponseModel
import ru.vsu.csf.bakebudget.models.response.ProductResponseModel
import ru.vsu.csf.bakebudget.services.createProduct
import ru.vsu.csf.bakebudget.ui.theme.Back2
import ru.vsu.csf.bakebudget.ui.theme.PrimaryBack
import ru.vsu.csf.bakebudget.ui.theme.SideBack
import ru.vsu.csf.bakebudget.ui.theme.border
import ru.vsu.csf.bakebudget.ui.theme.borderH
import ru.vsu.csf.bakebudget.ui.theme.sizeForSmallDevices
import ru.vsu.csf.bakebudget.utils.dataIncorrectToast
import ru.vsu.csf.bakebudget.utils.dataIncorrectToastProduct
import ru.vsu.csf.bakebudget.utils.isCostValid
import ru.vsu.csf.bakebudget.utils.isNameValid
import ru.vsu.csf.bakebudget.utils.isWeightValid
import ru.vsu.csf.bakebudget.utils.sameName
import ru.vsu.csf.bakebudget.utils.sameNameProduct
import ru.vsu.csf.bakebudget.utils.successfulProduct

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProductAddScreen(
    navController: NavHostController,
    ingredients: MutableList<IngredientInProductModel>,
    ingredientsAll: MutableList<IngredientModel>,
    isLogged: MutableState<Boolean>,
    products: MutableList<ProductModel>,
    outgoings: MutableList<OutgoingModel>,
    retrofitAPI: RetrofitAPI,
    productsResponse: MutableList<ProductResponseModel>,
    ingredientsResponse: MutableList<IngredientResponseModel>
) {
    if (!isLogged.value) {
        navController.navigate("home")
    }

    val configuration = LocalConfiguration.current
    val height = configuration.screenHeightDp.dp

    val productId = remember {
        mutableIntStateOf(-1)
    }
    val mContext = LocalContext.current
    val item = listOf(MenuItemModel(R.drawable.products, "Готовые изделия"))
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val selectedItem = remember {
        mutableStateOf(item[0])
    }
    val estimatedWeight = remember {
        mutableStateOf("")
    }

    val retryHash = remember {
        mutableLongStateOf(0)
    }

    val name = remember {
        mutableStateOf("")
    }
    val selectedItemIndex = remember { mutableIntStateOf(0) }

    val selectedImageUri = remember {
        mutableStateOf<Uri?>(null)
    }

    val currentSet = remember {
        mutableSetOf<String>()
    }

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
                ingredients,
                context = mContext,
                ingredientsResponse,
                productId.intValue,
                currentSet,
                false
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
                                    painter = painterResource(id = if (height > borderH) R.drawable.button_add else R.drawable.add_button_small),
                                    contentDescription = "add"
                                )
                            }
                            TextButton(
                                onClick = {
                                    if (!(isNameValid(name.value) && isWeightValid(estimatedWeight.value))) {
                                        dataIncorrectToastProduct(context = mContext)
                                        val eventParameters2 =
                                            "{\"button_clicked\":\"create product\"}"
                                        AppMetrica.reportEvent(
                                            "Product creation failed",
                                            eventParameters2
                                        )
                                    } else {
                                        var nameUnique: Boolean = true
                                        for (prod in products) {
                                            if (prod.name == name.value) {
                                                nameUnique = false
                                            }
                                        }
                                        if (nameUnique) {
                                            val ings =
                                                mutableStateListOf<IngredientInProductModel>()
                                            val outs = mutableStateListOf<OutgoingModel>()
                                            for (ing in ingredients) {
                                                ings.add(ing)
                                            }
                                            for (out in outgoings) {
                                                outs.add(out)
                                            }
                                            createProduct(
                                                mContext,
                                                retrofitAPI,
                                                ProductRequestModel(
                                                    name.value,
                                                    estimatedWeight.value.toInt()
                                                ),
                                                productId,
                                                products,
                                                ProductModel(
                                                    0,
//                                                selectedImageUri.value,
                                                    selectedImageUri.value,
                                                    R.drawable.cake,
                                                    name.value,
                                                    ings,
                                                    outs,
                                                    estimatedWeight.value.toInt(),
                                                    null
                                                ),
                                                selectedImageUri,
                                                retryHash
                                            )
                                            val eventParameters1 =
                                                "{\"button_clicked\":\"create product\"}"
                                            AppMetrica.reportEvent(
                                                "Product created",
                                                eventParameters1
                                            )
                                            navController.navigate("products")
//                                            navController.navigate("home")
                                            ingredients.clear()
//                                        runBlocking {
//                                            delay(300)
//                                            navController.navigate("home")
//                                            runBlocking {
//                                                delay(1000)
//                                                navController.navigate("products")
//                                                ingredients.clear()
//                                            }
//                                        }
                                            successfulProduct(mContext)
                                            //TODO:удаляет со 2 раза
                                        } else {
                                            sameNameProduct(mContext)
                                        }
                                    }
                                }
                            ) {
                                Image(
                                    painter = painterResource(id = if (height > borderH) R.drawable.button_save else R.drawable.save_button_small),
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
                        Header(scope = scope, drawerState = drawerState)
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(0.8f)
                                .background(SideBack)
                                .padding(top = 20.dp)
                        ) {
                            if (ingredients.isEmpty()) {
                                item {
                                    Box(modifier = Modifier.padding(16.dp)) {
                                        Text(
                                            text = "Это страница добавления готового изделия. \nЗдесь вы можете добавлять, редактировать и удалять ингредиенты, которые будут использоваться в этом изделии! \nЧтобы добаить ингредиент, нажмите кнопку «ДОБАВИТЬ» в нижней панели. \nДля сохранения изделия введите вес, на который рассчитан рецепт, его название и нажмите кнопку «СОХРАНИТЬ».",
                                            fontSize = 18.sp,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }
                            itemsIndexed(ingredients) { num, ingredient ->
                                IngredientInRecipe(
                                    ingredient = ingredient,
                                    if (num % 2 == 0) SideBack else Back2,
                                    ingredients,
                                    ingredientsAll,
                                    selectedItemIndex,
                                    ingredientsResponse,
                                    retrofitAPI,
                                    currentSet
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
                                ImagePicker(selectedImageUri, null, null)
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
                        .padding(top = 8.dp, end = 50.dp),
                    contentAlignment = Alignment.TopCenter
                ) {
                    val configuration = LocalConfiguration.current
                    val width = configuration.screenWidthDp.dp
                    if (width < border) {
                        Text(text = "ИЗДЕЛИЕ", fontSize = sizeForSmallDevices, color = Color.White)
                    } else {
                        Text(text = "ИЗДЕЛИЕ", fontSize = 24.sp, color = Color.White)
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
fun AlertDialog2(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    ingredientsAll: MutableList<IngredientModel>,
    selectedItemIndex: MutableIntState,
    ingredients: MutableList<IngredientInProductModel>,
    context: Context,
    ingredientsResponse: MutableList<IngredientResponseModel>,
    productId: Int,
    currentSet: MutableSet<String>,
    isView: Boolean
) {
    val weight = remember {
        mutableStateOf("")
    }
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
            Column {
                Text(text = dialogText)
                if (ingredientsAll.isNotEmpty()) {
                    DropdownMenuBox(
                        ingredientsAll = ingredientsAll,
                        selectedItemIndex = selectedItemIndex
                    )
                    InputTextFieldWeight(placeholder = "Вес", weight, 8, true)
                }
            }
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (isWeightValid(weight.value)) {
                        if (isView) {
                            for (ingredient in ingredients) {
                                currentSet.add(ingredient.name)
                            }
                        }
                        if (currentSet.contains(ingredientsAll[selectedItemIndex.intValue].name)) {
                            sameName(context)
                        } else {
                            currentSet.add(ingredientsAll[selectedItemIndex.intValue].name)
                            ingredients.add(
                                IngredientInProductModel(
                                    ingredientsResponse[selectedItemIndex.intValue].id,
                                    productId,
                                    ingredientsAll[selectedItemIndex.intValue].name,
                                    weight = weight.value.toInt()
                                )
                            )
                        }
                        onConfirmation()
                    } else {
                        dataIncorrectToast(context)
                    }
                }
            ) {
                Text("ОК")
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenuBox(
    ingredientsAll: MutableList<IngredientModel>,
    selectedItemIndex: MutableIntState
) {
    var expanded by remember { mutableStateOf(false) }
    val list = mutableListOf<String>()
    for (ingredient in ingredientsAll) {
        list.add(ingredient.name)
    }


    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.padding(5.dp),
    ) {
        TextField(
            colors = ExposedDropdownMenuDefaults.textFieldColors(
                unfocusedContainerColor = SideBack,
                focusedContainerColor = Back2
            ),
            value = list[selectedItemIndex.intValue],
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor()
        )

        ExposedDropdownMenu(
            modifier = Modifier.background(SideBack),
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            list.forEachIndexed { index, item ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = item,
                            fontWeight = if (index == selectedItemIndex.intValue) FontWeight.Bold else null
                        )
                    },
                    onClick = {
                        selectedItemIndex.intValue = index
                        expanded = false
                    }
                )
            }
        }
    }
}
