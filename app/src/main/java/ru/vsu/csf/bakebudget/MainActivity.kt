package ru.vsu.csf.bakebudget

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
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
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
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
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    private val API_KEY = "a6d5ee67-5fc9-4adf-bab5-17730828b9b5"
    private val url = "https://bakebudget.ru/api/"
    private val gson = GsonBuilder()
        .setLenient()
        .create()

    private val certificatePinner = CertificatePinner.Builder()
        .add("bakebudget.ru", "sha256/"  + "AKTn7mPMXBbfMM+QiM5ck9vT71KNeH7gQS9HH77u1Qg=")
        .build()


    private val okHttpClient = OkHttpClient.Builder()
        .certificatePinner(certificatePinner)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()
    private val retrofit = Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(okHttpClient)
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

                val navController = rememberNavController()
                val isLoggedIn = remember { mutableStateOf(false) }

                val ingredients = remember { mutableStateListOf<IngredientModel>() }
                val ingredientsSet = remember { mutableSetOf<String>() }
                val ingredientsResponse = remember {
                    mutableStateListOf<IngredientResponseModel>()
                }
                val isDataReceivedIngredients = remember {
                    mutableStateOf(false)
                }

                val outgoings = remember {
                        mutableStateListOf<OutgoingModel>()
                }

                val isPro = remember {
                    mutableStateOf(false)
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
                val userRole = remember {
                    mutableStateOf("")
                }
                val orders0 = remember {
                    mutableStateListOf<OrderModel>()
                }

                val orders1 = remember {
                    mutableStateListOf<OrderModel>()
                }
                val orders2 = remember {
                    mutableStateListOf<OrderModel>()
                }
                val orders3 = remember {
                    mutableStateListOf<OrderModel>()
                }

                if (getToken(ctx) != null) {
                    isLoggedIn.value = true
                }

                NavGraph(
                    navController = navController,
                    ingredients,
                    isLoggedIn,
                    products,
                    ingredientsInRecipe,
                    outgoings,
                    orders,
                    isDataReceivedIngredients,
                    ingredientsResponse,
                    ingredientsSet,
                    isDataReceivedProducts,
                    productsResponse,
                    isDataReceivedOutgoings,
                    isDataReceivedOrders,
                    isPro,
                    userRole,
                    orders0,
                    orders1,
                    orders2,
                    orders3
                )
            }
        }
    }

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
        isDataReceivedIngredients: MutableState<Boolean>,
        ingredientsResponse: MutableList<IngredientResponseModel>,
        ingredientsSet: MutableSet<String>,
        isDataReceivedProducts: MutableState<Boolean>,
        productsResponse: MutableList<ProductResponseModel>,
        isDataReceivedOutgoings : MutableState<Boolean>,
        isDataReceivedOrders : MutableState<Boolean>,
        isPro : MutableState<Boolean>,
        userRole : MutableState<String>,
        orders0: MutableList<OrderModel>, orders1: MutableList<OrderModel>, orders2: MutableList<OrderModel>, orders3: MutableList<OrderModel>
    ) {
        NavHost(
            navController = navController,
            startDestination = "home"
        ) {
            composable(route = "login") {
                LoginScreen(navController, isLogged, retrofitAPI, userRole, isPro, ingredients, products, ingredientsInRecipe, outgoings, orders, isDataReceivedIngredients, ingredientsResponse, ingredientsSet, isDataReceivedProducts, productsResponse, isDataReceivedOutgoings, isDataReceivedOrders, orders0, orders1, orders2, orders3)
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
                    productsResponse,
                    ingredientsResponse
                )
            }

            composable(route = "products/{id}", arguments = listOf(navArgument(name = "id") {
                type = NavType.IntType
            })) {
                backstackEntry ->
                val load = remember {
                    mutableStateOf(false)
                }
                ProductView(
                    navController = navController,
                    ingredientsAll = ingredients,
                    isLogged = isLogged,
                    product = products[backstackEntry.arguments?.getInt("id")!!],
                    ingredientsResponse,
                    retrofitAPI,
                    load
                )
            }

            composable(route = "outgoings") {
                OutgoingsScreen(navController, outgoings, products, isLogged, retrofitAPI, isDataReceivedProducts, productsResponse, ingredientsResponse, isDataReceivedIngredients, isDataReceivedOutgoings)
            }

            composable(route = "reports") {
                ReportsScreen(navController, isLogged, isPro, retrofitAPI, userRole)
            }

            composable(route = "groups") {
                GroupsScreen(navController, isLogged, isPro, retrofitAPI, userRole)
            }

            composable(route = "calculation") {
                CalculationScreen(navController, isLogged, products, orders, retrofitAPI, isDataReceivedProducts, productsResponse, isDataReceivedOrders, orders0, orders1, orders2, orders3)
            }

            composable(route = "orders") {
                OrdersScreen(navController, isLogged, orders, retrofitAPI, isDataReceivedOrders, products, orders0, orders1, orders2, orders3)
            }
        }
    }
}

fun saveToken(token: String, ctx: Context) {
    val prefs: SharedPreferences = ctx.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    val editor = prefs.edit()
    editor.putString("auth_token", token)
    editor.apply()
}

fun getToken(ctx: Context): String? {
    val prefs: SharedPreferences = ctx.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    return prefs.getString("auth_token", null)
}

fun clearToken(ctx: Context) {
    val prefs: SharedPreferences = ctx.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    val editor = prefs.edit()
    editor.remove("auth_token")
    editor.apply()
}