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
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import io.appmetrica.analytics.AppMetrica
import io.appmetrica.analytics.AppMetricaConfig
import ru.vsu.csf.bakebudget.models.OutgoingModel
import ru.vsu.csf.bakebudget.models.ProductModel
import ru.vsu.csf.bakebudget.models.IngredientInProductModel
import ru.vsu.csf.bakebudget.models.IngredientModel
import ru.vsu.csf.bakebudget.models.OrderModel
import ru.vsu.csf.bakebudget.screens.CalculationScreen
import ru.vsu.csf.bakebudget.screens.OutgoingsScreen
import ru.vsu.csf.bakebudget.screens.ProductAddScreen
import ru.vsu.csf.bakebudget.screens.ProductsScreen
import ru.vsu.csf.bakebudget.screens.GroupsScreen
import ru.vsu.csf.bakebudget.screens.HomeScreen
import ru.vsu.csf.bakebudget.screens.IngredientsScreen
import ru.vsu.csf.bakebudget.screens.LoginScreen
import ru.vsu.csf.bakebudget.screens.OrdersScreen
import ru.vsu.csf.bakebudget.screens.ProductView
import ru.vsu.csf.bakebudget.screens.RegistrationScreen
import ru.vsu.csf.bakebudget.screens.ReportsScreen
import ru.vsu.csf.bakebudget.ui.theme.BakeBudgetTheme
import java.time.LocalDate

class MainActivity : ComponentActivity() {
    private val API_KEY = "a6d5ee67-5fc9-4adf-bab5-17730828b9b5"
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("UnrememberedMutableState")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val config = AppMetricaConfig.newConfigBuilder(API_KEY).build()
        AppMetrica.activate(this, config)
        setContent {
            BakeBudgetTheme {
                val navController = rememberNavController()
                val isLoggedIn = remember { mutableStateOf(false) }
                val ingredients = mutableStateListOf(
                    IngredientModel("Молоко", 100, 30),
                    IngredientModel("Мука", 1000, 100),
                    IngredientModel("Масло", 150, 250),
                )
                val outgoings = mutableStateListOf(
                    OutgoingModel("Вода", 100),
                    OutgoingModel("Электроэнергия", 150)
                )
                val outgoings1 = mutableStateListOf(
                    OutgoingModel("Вода", 100),
                    OutgoingModel("Электроэнергия", 150)
                )
                val outgoings2 = mutableStateListOf(
                    OutgoingModel("Вода", 100),
                    OutgoingModel("Электроэнергия", 150)
                )
                val outgoings3 = mutableStateListOf(
                    OutgoingModel("Вода", 100),
                    OutgoingModel("Электроэнергия", 150)
                )
                val ingredientsInRecipe = mutableStateListOf(
                    IngredientInProductModel("Молоко", 100),
                )
                val ingredientsInRecipe1 = mutableStateListOf(
                    IngredientInProductModel("Молоко", 100),
                )
                val ingredientsInRecipe2 = mutableStateListOf(
                    IngredientInProductModel("Молоко", 100),
                )
                val ingredientsInRecipe3 = mutableStateListOf(
                    IngredientInProductModel("Молоко", 100),
                )
                val products = remember {
                    mutableStateListOf(
                        ProductModel(R.drawable.cake, "Тортик 1", ingredientsInRecipe1, outgoings1, 1000),
                        ProductModel(R.drawable.cake, "Тортик 2", ingredientsInRecipe2, outgoings2,1000),
                        ProductModel(R.drawable.cake, "Тортик 3", ingredientsInRecipe3, outgoings3,1000)
                    )
                }
                val orders = mutableStateListOf(
                    OrderModel(0, products[0], 1000, 2000),
                    OrderModel(0, products[1], 100, 200),
                    OrderModel(0, products[2], 3000, 1000)
                )
                NavGraph(navController = navController, ingredients, isLoggedIn, products, ingredientsInRecipe, outgoings, orders)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun NavGraph(navController: NavHostController, ingredients : MutableList<IngredientModel>, isLogged : MutableState<Boolean>
                 , products: MutableList<ProductModel>, ingredientsInRecipe : MutableList<IngredientInProductModel>, outgoings: MutableList<OutgoingModel>,
                 orders: MutableList<OrderModel>) {
        NavHost(
            navController = navController,
            startDestination = "home"
        ) {
            composable(route = "login") {
                LoginScreen(navController, isLogged)
            }

            composable(route = "home") {
                HomeScreen(navController, isLogged)
            }

            composable(route = "register") {
                RegistrationScreen(navController, isLogged)
            }

            composable(route = "ingredients") {
                IngredientsScreen(navController, ingredients, isLogged)
            }

            composable(route = "products") {
                ProductsScreen(navController, products, ingredients, isLogged)
            }

            composable(route = "productAdd") {
                ProductAddScreen(navController, ingredientsInRecipe, ingredients, isLogged, products, outgoings)
            }

            composable(route = "products/{id}", arguments = listOf(navArgument(name = "id") {
                type = NavType.IntType
            })) { backstackEntry ->
                ProductView(navController = navController, ingredientsAll = ingredients, isLogged = isLogged, product = products[backstackEntry.arguments?.getInt("id")!!])
            }

            composable(route = "outgoings") {
                OutgoingsScreen(navController, outgoings, products, isLogged)
            }

            composable(route = "reports") {
                ReportsScreen(navController, isLogged)
            }

            composable(route = "groups") {
                GroupsScreen(navController, isLogged, true)
            }

            composable(route = "calculation") {
                CalculationScreen(navController, isLogged, products, orders)
            }

            composable(route = "orders") {
                OrdersScreen(navController, isLogged, orders)
            }
        }
    }
}