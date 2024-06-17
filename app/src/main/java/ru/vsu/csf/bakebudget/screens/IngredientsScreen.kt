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
import androidx.navigation.NavHostController
import io.appmetrica.analytics.AppMetrica
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import retrofit2.Response
import ru.vsu.csf.bakebudget.R
import ru.vsu.csf.bakebudget.api.RetrofitAPI
import ru.vsu.csf.bakebudget.components.Ingredient
import ru.vsu.csf.bakebudget.components.IngredientAdd
import ru.vsu.csf.bakebudget.models.IngredientModel
import ru.vsu.csf.bakebudget.models.MenuItemModel
import ru.vsu.csf.bakebudget.models.response.IngredientResponseModel
import ru.vsu.csf.bakebudget.ui.theme.Back2
import ru.vsu.csf.bakebudget.ui.theme.PrimaryBack
import ru.vsu.csf.bakebudget.ui.theme.SideBack
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import ru.vsu.csf.bakebudget.getToken
import ru.vsu.csf.bakebudget.models.request.IngredientRequestModel
import ru.vsu.csf.bakebudget.services.createIngredient
import ru.vsu.csf.bakebudget.services.findAllIngredients
import ru.vsu.csf.bakebudget.ui.theme.border
import ru.vsu.csf.bakebudget.ui.theme.borderH
import ru.vsu.csf.bakebudget.ui.theme.sizeForSmallDevices
import ru.vsu.csf.bakebudget.utils.dataIncorrectToast
import ru.vsu.csf.bakebudget.utils.isCostValid
import ru.vsu.csf.bakebudget.utils.isNameValid
import ru.vsu.csf.bakebudget.utils.isWeightValid
import ru.vsu.csf.bakebudget.utils.sameName

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun IngredientsScreen(
    navController: NavHostController,
    ingredients: MutableList<IngredientModel>,
    isLogged: MutableState<Boolean>,
    retrofitAPI: RetrofitAPI,
    isDataReceivedIngredients: MutableState<Boolean>,
    ingredientsResponse: MutableList<IngredientResponseModel>,
    ingredientsSet: MutableSet<String>
) {
    val configuration = LocalConfiguration.current
    val height = configuration.screenHeightDp.dp
    val mContext = LocalContext.current
    val item = listOf(MenuItemModel(R.drawable.ingredients, "Ингредиенты"))
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val eventParameters1 = "{\"button_click(failed)\":\"ingredients_add failed\"}"
    val eventParameters2 = "{\"button_clicked\":\"ingredients_add\"}"

    val selectedItem = remember {
        mutableStateOf(item[0])
    }
    val name = remember {
        mutableStateOf("")
    }
    val weight = remember {
        mutableStateOf("")
    }
    val cost = remember {
        mutableStateOf("")
    }

    if (getToken(mContext) != null && !isDataReceivedIngredients.value) {
        findAllIngredients(mContext, retrofitAPI, ingredientsResponse)
        isDataReceivedIngredients.value = true
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
            ingredientsSet.add(ingredient.name)
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
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            IngredientAdd(name, weight, cost)
                            TextButton(
                                onClick = {
                                    if (!(isNameValid(name.value) && isWeightValid(weight.value) && isCostValid(cost.value))
                                    )  {
                                        AppMetrica.reportEvent(
                                            "Ingredient add failed",
                                            eventParameters1
                                        )
                                        dataIncorrectToast(context = mContext)
                                    } else if(ingredientsSet.contains(name.value)) {
                                        sameName(mContext)
                                    } else {
                                        AppMetrica.reportEvent("Ingredient added", eventParameters2)
                                        ingredients.add(
                                            IngredientModel(
                                                name.value,
                                                weight.value.toInt(),
                                                cost.value.toInt()
                                            )
                                        )
                                        ingredientsSet.add(
                                                name.value
                                        )
                                        createIngredient(
                                            mContext, retrofitAPI, IngredientRequestModel(
                                                name.value,
                                                weight.value.toInt(),
                                                cost.value.toInt()
                                            ),
                                            ingredientsResponse
                                        )
                                    }
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
                                .fillMaxHeight(0.8f)
                                .background(SideBack)
                                .padding(top = 20.dp)
                        ) {
                            if (ingredientsResponse.isEmpty()) {
                                item {
                                    Box(modifier = Modifier.padding(16.dp)) {
                                        Text(
                                            text = "Это страница ингредиентов. \nЗдесь вы можете добавлять, редактировать и удалять ингредиенты, которые вы сможете использоватьв своих изделиях! \nЧтобы добаить ингредиент, введите его название, вес в упаковке и цену, а затем нажмите кнопку «ДОБАВИТЬ» в нижней панели. \nДля редактирования и удаления ингредиентов, нажмите на 3 точки справа от нужного ингредиента.",
                                            fontSize = 18.sp,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }
                            itemsIndexed(ingredientsResponse) { num, ingredient ->
                                if (num % 2 == 0) {
                                    Ingredient(
                                        ingredient = ingredient,
                                        SideBack,
                                        ingredients,
                                        ingredientsSet,
                                        retrofitAPI,
                                        ingredientsResponse
                                    )
                                } else {
                                    Ingredient(
                                        ingredient = ingredient,
                                        Back2,
                                        ingredients,
                                        ingredientsSet,
                                        retrofitAPI,
                                        ingredientsResponse
                                    )
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
                        .padding(top = 8.dp, end = 50.dp),
                    contentAlignment = Alignment.TopCenter
                ) {
                    val configuration = LocalConfiguration.current
                    val width = configuration.screenWidthDp.dp
                    if (width < border) {
                        Text(text = "ИНГРЕДИЕНТЫ", fontSize = sizeForSmallDevices, color = Color.White)
                    } else {
                        Text(text = "ИНГРЕДИЕНТЫ", fontSize = 24.sp, color = Color.White)
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
                    Text(text = "КОЛИЧЕСТВО (гр.)", color = Color.White, fontSize = 12.sp)
                    Text(text = "СТОИМОСТЬ (руб.)", color = Color.White, fontSize = 12.sp)
                }
            }
        }
    }
}