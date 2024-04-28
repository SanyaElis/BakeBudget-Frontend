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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.vsu.csf.bakebudget.models.OutgoingModel
import ru.vsu.csf.bakebudget.models.ProductModel
import ru.vsu.csf.bakebudget.models.IngredientInProductModel
import ru.vsu.csf.bakebudget.models.IngredientModel
import ru.vsu.csf.bakebudget.screens.OutgoingsScreen
import ru.vsu.csf.bakebudget.screens.ProductAddScreen
import ru.vsu.csf.bakebudget.screens.ProductsScreen
import ru.vsu.csf.bakebudget.screens.GroupsScreen
import ru.vsu.csf.bakebudget.screens.HomeScreen
import ru.vsu.csf.bakebudget.screens.IngredientsScreen
import ru.vsu.csf.bakebudget.screens.LoginScreen
import ru.vsu.csf.bakebudget.screens.RegistrationScreen
import ru.vsu.csf.bakebudget.screens.ReportsScreen
import ru.vsu.csf.bakebudget.ui.theme.BakeBudgetTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("UnrememberedMutableState")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BakeBudgetTheme {
                val navController = rememberNavController()
                val isLoggedIn = remember { mutableStateOf(false) }
                //    val ingredients = remember {
                val ingredients = mutableStateListOf(
                    IngredientModel("Milk", 100, 30),
                    IngredientModel("Flower", 1000, 100),
                    IngredientModel("Butter", 150, 250),
                )
                val outgoings = mutableStateListOf(
                    OutgoingModel("Вода", 100),
                    OutgoingModel("Электроэнергия", 150)
                )
                val ingredientsInRecipe = mutableStateListOf(
                    IngredientInProductModel("Milk", 100),
                )
                val products = mutableStateListOf(
                    ProductModel(R.drawable.cake, "100", ingredientsInRecipe, 1000),
                    ProductModel(R.drawable.cake, "1000", ingredientsInRecipe, 1000),
                    ProductModel(R.drawable.cake, "150", ingredientsInRecipe, 1000),
                    ProductModel(R.drawable.cake, "152340", ingredientsInRecipe, 1000),
                    ProductModel(R.drawable.cake, "152340", ingredientsInRecipe, 1000),
                    ProductModel(R.drawable.cake, "1530", ingredientsInRecipe, 1000),
                    ProductModel(R.drawable.cake, "15320", ingredientsInRecipe, 1000),
                    ProductModel(R.drawable.cake, "12250", ingredientsInRecipe, 1000),
                    ProductModel(R.drawable.cake, "1dg", ingredientsInRecipe, 1000)
                )
                NavGraph(navController = navController, ingredients, isLoggedIn, products, ingredientsInRecipe, outgoings)
//                HomeScreen()
//                LoginScreen()
//                RegistrationScreen()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun NavGraph(navController: NavHostController, ingredients : MutableList<IngredientModel>, isLogged : MutableState<Boolean>
                 , products: MutableList<ProductModel>, ingredientsInRecipe : MutableList<IngredientInProductModel>, outgoings: MutableList<OutgoingModel>) {
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
                ProductsScreen(navController, products, isLogged)
            }

            composable(route = "productAdd") {
                ProductAddScreen(navController, ingredientsInRecipe, ingredients, isLogged, products)
            }

            composable(route = "outgoings") {
                OutgoingsScreen(navController, outgoings, isLogged)
            }

            composable(route = "reports") {
                ReportsScreen(navController, isLogged)
            }

            composable(route = "groups") {
                GroupsScreen(navController, isLogged, true)
            }
        }
    }
}