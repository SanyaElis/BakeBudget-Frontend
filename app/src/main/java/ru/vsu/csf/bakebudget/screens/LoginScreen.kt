package ru.vsu.csf.bakebudget.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import ru.vsu.csf.bakebudget.R
import ru.vsu.csf.bakebudget.api.RetrofitAPI
import ru.vsu.csf.bakebudget.components.PasswordTextForm
import ru.vsu.csf.bakebudget.components.TextForm
import ru.vsu.csf.bakebudget.models.IngredientInProductModel
import ru.vsu.csf.bakebudget.models.IngredientModel
import ru.vsu.csf.bakebudget.models.OrderModel
import ru.vsu.csf.bakebudget.models.OutgoingModel
import ru.vsu.csf.bakebudget.models.ProductModel
import ru.vsu.csf.bakebudget.models.response.IngredientResponseModel
import ru.vsu.csf.bakebudget.models.response.ProductResponseModel
import ru.vsu.csf.bakebudget.services.login
import ru.vsu.csf.bakebudget.ui.theme.PrimaryBack

@Composable
fun LoginScreen(
    navController: NavHostController,
    isLogged: MutableState<Boolean>,
    retrofitAPI: RetrofitAPI,
    userRole: MutableState<String>,
    ingredients: MutableList<IngredientModel>,
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
    orders0: MutableList<OrderModel>, orders1: MutableList<OrderModel>, orders2: MutableList<OrderModel>, orders3: MutableList<OrderModel>,
    firstTryPr: MutableState<Boolean>
) {
    val ctx = LocalContext.current
    val configuration = LocalConfiguration.current
    val height = configuration.screenHeightDp.dp

    val userEmail = remember {
        mutableStateOf("")
    }
    val userPassword = remember {
        mutableStateOf("")
    }
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
                .padding(top = height/3),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextForm(label = "Email", userEmail)
            PasswordTextForm(label = "Пароль", userPassword)
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 100.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            TextButton(
                onClick = {
                    login(
                        ctx, userEmail, userPassword, retrofitAPI = retrofitAPI, isLogged, userRole, ingredients, products, ingredientsInRecipe, outgoings, orders, isDataReceivedIngredients, ingredientsResponse, ingredientsSet, isDataReceivedProducts, productsResponse, isDataReceivedOutgoings, isDataReceivedOrders, orders0, orders1, orders2, orders3, firstTryPr
                    )
                    navController.navigate("home")
                }
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
                .padding(60.dp),
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
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(30.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            TextButton(
                onClick = { navController.navigate("passwordReset") },
            ) {
                Image(
                    painter = painterResource(id = R.drawable.reset_password_button),
                    contentDescription = "reset password",
                )
            }
        }
    }
}