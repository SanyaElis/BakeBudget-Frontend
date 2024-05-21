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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
import ru.vsu.csf.bakebudget.models.request.IngredientRequestModel
import ru.vsu.csf.bakebudget.utils.dataIncorrectToast
import ru.vsu.csf.bakebudget.utils.isCostValid
import ru.vsu.csf.bakebudget.utils.isNameValid
import ru.vsu.csf.bakebudget.utils.isWeightValid

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun IngredientsScreen(
    navController: NavHostController,
    ingredients: MutableList<IngredientModel>,
    isLogged: MutableState<Boolean>,
    retrofitAPI: RetrofitAPI,
    jwtToken: MutableState<String>,
    isDataReceivedIngredients: MutableState<Boolean>,
    ingredientsResponse: MutableList<IngredientResponseModel>,
    ingredientsSet: MutableSet<IngredientModel>
) {
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

    if (jwtToken.value != "" && !isDataReceivedIngredients.value) {
        findAllIngredients(mContext, retrofitAPI, jwtToken, ingredientsResponse)
        isDataReceivedIngredients.value = true
    }
        //TODO: подсказки пользователям, когда нет ингредиентов
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
                IngredientModel(
                    ingredient.name,
                    ingredient.weight,
                    ingredient.cost
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
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            IngredientAdd(name, weight, cost)
                            TextButton(
                                onClick = {
                                    if (!(isNameValid(name.value) && isWeightValid(weight.value) && isCostValid(cost.value) && !ingredientsSet.contains(
                                            IngredientModel(
                                                name.value,
                                                weight.value.toInt(),
                                                cost.value.toInt()
                                            )
                                        )
                                                )
                                    ) {
                                        AppMetrica.reportEvent(
                                            "Ingredient add failed",
                                            eventParameters1
                                        )
                                        dataIncorrectToast(context = mContext)
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
                                            IngredientModel(
                                                name.value,
                                                weight.value.toInt(),
                                                cost.value.toInt()
                                            )
                                        )
                                        create(
                                            mContext, retrofitAPI, jwtToken, IngredientRequestModel(
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
                                    painter = painterResource(id = R.drawable.button_add),
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
                            itemsIndexed(ingredientsResponse) { num, ingredient ->
                                if (num % 2 == 0) {
                                    Ingredient(
                                        ingredient = ingredient,
                                        SideBack,
                                        ingredients,
                                        ingredientsSet,
                                        retrofitAPI,
                                        jwtToken,
                                        ingredientsResponse
                                    )
                                } else {
                                    Ingredient(
                                        ingredient = ingredient,
                                        Back2,
                                        ingredients,
                                        ingredientsSet,
                                        retrofitAPI,
                                        jwtToken,
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
                    Text(text = "ИНГРЕДИЕНТЫ", fontSize = 24.sp, color = Color.White)
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
                Text(text = "КОЛИЧЕСТВО", color = Color.White, fontSize = 12.sp)
                Text(text = "СТОИМОСТЬ", color = Color.White, fontSize = 12.sp)
            }
        }
    }
}
//TODO:делать все имена уникальными
@OptIn(DelicateCoroutinesApi::class)
fun findAllIngredients(
    ctx: Context,
    retrofitAPI: RetrofitAPI,
    jwtToken: MutableState<String>,
    ingredientsResponse: MutableList<IngredientResponseModel>
) {
    GlobalScope.launch(Dispatchers.Main) {
        val res = retrofitAPI.findAllIngredients("Bearer ".plus(jwtToken.value))
        onResultFindAll(res, ingredientsResponse)
    }
//    var model: List<IngredientResponseModel>? = null
//    call!!.enqueue(object : Callback<List<IngredientResponseModel>?> {
//        override fun onResponse(
//            call: Call<List<IngredientResponseModel>?>,
//            response: Response<List<IngredientResponseModel>?>
//        ) {
//            if (response.isSuccessful) {
//                model = response.body()
//                for (ing in model!!) {
//                    ingredientsResponse.add(ing)
//                }
//                Toast.makeText(ctx, "Response Code : " + response.code() + "\n", Toast.LENGTH_SHORT)
//                    .show()
//            } else {
//                Toast.makeText(
//                    ctx,
//                    "Response Code : " + response.code() + "\n" + "Ошибка получения ингредиентов",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//        }
//
//        override fun onFailure(call: Call<List<IngredientResponseModel>?>, t: Throwable) {
//        }
//    })
}

private fun onResultFindAll(
    result: Response<List<IngredientResponseModel>?>?,
    ingredientsResponse: MutableList<IngredientResponseModel>
) {
    if (result!!.body() != null) {
        if (result.body()!!.isNotEmpty()) {
            for (ing in result.body()!!) {
                ingredientsResponse.add(ing)
            }
        }
    }
}

@OptIn(DelicateCoroutinesApi::class)
private fun create(
    ctx: Context,
    retrofitAPI: RetrofitAPI,
    jwtToken: MutableState<String>,
    ingredientRequestModel: IngredientRequestModel,
    ingredientsResponse: MutableList<IngredientResponseModel>
) {
//    CoroutineScope(Dispatchers.IO).launch {
//        val response = retrofitAPI.createIngredient(ingredientRequestModel, "Bearer ".plus(jwtToken.value))
//
//        withContext(Dispatchers.Main) {
//            if (response!!.isSuccessful) {
//                onResultCreate(response, ctx)
//
//            } else {
//
//                Toast.makeText(
//                    ctx,
//                    "Response Code : " + response.code(),
//                    Toast.LENGTH_SHORT
//                ).show()
//
//            }
//        }
//    }
    GlobalScope.launch(Dispatchers.Main) {
        val res =
            retrofitAPI.createIngredient(ingredientRequestModel, "Bearer ".plus(jwtToken.value))
        onResultCreate(res, ctx, ingredientsResponse)
    }
}

fun onResultCreate(
    result: Response<IngredientResponseModel?>?,
    ctx: Context,
    ingredientsResponse: MutableList<IngredientResponseModel>
) {
    Toast.makeText(
        ctx,
        "Response Code : " + result!!.code() + "\n" + result.body(),
        Toast.LENGTH_SHORT
    ).show()
    if (result.isSuccessful) {
        if (result.body() != null) {
            ingredientsResponse.add(result.body()!!)
        }
    }
}