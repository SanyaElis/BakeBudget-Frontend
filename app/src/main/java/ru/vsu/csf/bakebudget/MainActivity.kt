package ru.vsu.csf.bakebudget

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.gson.GsonBuilder
import io.appmetrica.analytics.AppMetrica
import io.appmetrica.analytics.AppMetricaConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.vsu.csf.bakebudget.api.RetrofitAPI
import ru.vsu.csf.bakebudget.models.OutgoingModel
import ru.vsu.csf.bakebudget.models.ProductModel
import ru.vsu.csf.bakebudget.models.IngredientInProductModel
import ru.vsu.csf.bakebudget.models.IngredientModel
import ru.vsu.csf.bakebudget.models.OrderModel
import ru.vsu.csf.bakebudget.models.response.IngredientResponseModel
import ru.vsu.csf.bakebudget.models.response.ProductResponseModel
import ru.vsu.csf.bakebudget.screens.CalculationScreen
import ru.vsu.csf.bakebudget.screens.OutgoingsScreen
import ru.vsu.csf.bakebudget.screens.ProductAddScreen
import ru.vsu.csf.bakebudget.screens.ProductsScreen
import ru.vsu.csf.bakebudget.screens.GroupsScreen
import ru.vsu.csf.bakebudget.screens.HomeScreen
import ru.vsu.csf.bakebudget.screens.IngredientsScreen
import ru.vsu.csf.bakebudget.screens.LoginScreen
import ru.vsu.csf.bakebudget.screens.OrdersScreen
import ru.vsu.csf.bakebudget.screens.PasswordResetScreen
import ru.vsu.csf.bakebudget.screens.ProductView
import ru.vsu.csf.bakebudget.screens.RegistrationScreen
import ru.vsu.csf.bakebudget.screens.ReportsScreen
import ru.vsu.csf.bakebudget.ui.theme.BakeBudgetTheme

class MainActivity : ComponentActivity() {
    //TODO:больше метрик и воронок
    private val API_KEY = "a6d5ee67-5fc9-4adf-bab5-17730828b9b5"
    private val url = "http://185.251.89.195:8080/api/"
    private val gson = GsonBuilder()
        .setLenient()
        .create()
    private val retrofit = Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
    private val retrofitAPI: RetrofitAPI = retrofit.create(RetrofitAPI::class.java)

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("UnrememberedMutableState")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val config = AppMetricaConfig.newConfigBuilder(API_KEY).build()
        AppMetrica.activate(this, config)
        setContent {
            BakeBudgetTheme {
                val ctx = LocalContext.current
                val jwtToken = remember {
                    mutableStateOf("")
                }
                val navController = rememberNavController()
                val isLoggedIn = remember { mutableStateOf(false) }

                val ingredients = remember { mutableStateListOf<IngredientModel>() }
                val ingredientsSet = remember { mutableSetOf<IngredientModel>() }
                val ingredientsResponse = remember {
                    mutableStateListOf<IngredientResponseModel>()
                }
                val isDataReceivedIngredients = remember {
                    mutableStateOf(false)
                }

                val outgoings = remember {
                    mutableStateListOf(
                        OutgoingModel(0, "Вода", 100),
                        OutgoingModel(0, "Электроэнергия", 150)
                    )
                }

                val ingredientsInRecipe = remember {
                    mutableStateListOf<IngredientInProductModel>()
                }
                val products = remember {
                    mutableStateListOf<ProductModel>()
                }
                val productsResponse = remember {
                    mutableStateListOf<ProductResponseModel>()
                }
                val isDataReceivedProducts = remember {
                    mutableStateOf(false)
                }
                val isDataReceivedOutgoings = remember {
                    mutableStateOf(false)
                }
                val isDataReceivedOrders = remember {
                    mutableStateOf(false)
                }
                val orders = remember {
                    mutableStateListOf<OrderModel>()
                }
                NavGraph(
                    navController = navController,
                    ingredients,
                    isLoggedIn,
                    products,
                    ingredientsInRecipe,
                    outgoings,
                    orders,
                    jwtToken,
                    isDataReceivedIngredients,
                    ingredientsResponse,
                    ingredientsSet,
                    isDataReceivedProducts,
                    productsResponse,
                    isDataReceivedOutgoings
                )
            }
        }
    }

    //TODO:везде добавить подписи (гр.) (руб.)

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun NavGraph(
        navController: NavHostController,
        ingredients: MutableList<IngredientModel>,
        isLogged: MutableState<Boolean>,
        products: MutableList<ProductModel>,
        ingredientsInRecipe: MutableList<IngredientInProductModel>,
        outgoings: MutableList<OutgoingModel>,
        orders: MutableList<OrderModel>,
        jwtToken: MutableState<String>,
        isDataReceivedIngredients: MutableState<Boolean>,
        ingredientsResponse: MutableList<IngredientResponseModel>,
        ingredientsSet: MutableSet<IngredientModel>,
        isDataReceivedProducts: MutableState<Boolean>,
        productsResponse: MutableList<ProductResponseModel>,
        isDataReceivedOutgoings : MutableState<Boolean>
    ) {
        NavHost(
            navController = navController,
            startDestination = "home"
        ) {
            composable(route = "login") {
                LoginScreen(navController, isLogged, retrofitAPI, jwtToken = jwtToken)
            }

            composable(route = "home") {
                HomeScreen(navController, isLogged)
            }

            composable(route = "register") {
                RegistrationScreen(navController, isLogged, retrofitAPI)
            }

            composable(route = "ingredients") {
                IngredientsScreen(
                    navController,
                    ingredients,
                    isLogged,
                    retrofitAPI,
                    jwtToken,
                    isDataReceivedIngredients,
                    ingredientsResponse,
                    ingredientsSet
                )
            }

            composable(route = "passwordReset") {
                PasswordResetScreen(navController, isLogged)
            }

            composable(route = "products") {
                ProductsScreen(navController, products, ingredients, isLogged,
                    retrofitAPI,
                    jwtToken,
                    isDataReceivedProducts,
                    productsResponse,
                    ingredientsResponse,
                    isDataReceivedIngredients,
                    ingredients,
                    ingredientsSet
                )
            }

            composable(route = "productAdd") {
                ProductAddScreen(
                    navController,
                    ingredientsInRecipe,
                    ingredients,
                    isLogged,
                    products,
                    outgoings,
                    retrofitAPI,
                    jwtToken,
                    productsResponse,
                    ingredientsResponse
                )
            }

            composable(route = "products/{id}", arguments = listOf(navArgument(name = "id") {
                type = NavType.IntType
            })) { backstackEntry ->
                ProductView(
                    navController = navController,
                    ingredientsAll = ingredients,
                    isLogged = isLogged,
                    product = products[backstackEntry.arguments?.getInt("id")!!],
                    ingredientsResponse,
                    retrofitAPI,
                    jwtToken
                )
            }

            composable(route = "outgoings") {
                OutgoingsScreen(navController, outgoings, products, isLogged, retrofitAPI, jwtToken, isDataReceivedProducts, productsResponse, ingredientsResponse, isDataReceivedIngredients, isDataReceivedOutgoings)
            }

            composable(route = "reports") {
                ReportsScreen(navController, isLogged)
            }

            composable(route = "groups") {
                GroupsScreen(navController, isLogged, true)
            }

            composable(route = "calculation") {
                CalculationScreen(navController, isLogged, products, orders, retrofitAPI, jwtToken, isDataReceivedProducts, productsResponse)
            }

            composable(route = "orders") {
                OrdersScreen(navController, isLogged, orders)
            }
        }
    }
}