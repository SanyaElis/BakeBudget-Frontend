package ru.vsu.csf.bakebudget.services

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.MutableState
import io.appmetrica.analytics.AppMetrica
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Response
import ru.vsu.csf.bakebudget.api.RetrofitAPI
import ru.vsu.csf.bakebudget.models.IngredientInProductModel
import ru.vsu.csf.bakebudget.models.IngredientModel
import ru.vsu.csf.bakebudget.models.OrderModel
import ru.vsu.csf.bakebudget.models.OutgoingModel
import ru.vsu.csf.bakebudget.models.ProductModel
import ru.vsu.csf.bakebudget.models.request.UserSignInRequestModel
import ru.vsu.csf.bakebudget.models.request.UserSignUpRequestModel
import ru.vsu.csf.bakebudget.models.response.IngredientResponseModel
import ru.vsu.csf.bakebudget.models.response.ProductResponseModel
import ru.vsu.csf.bakebudget.models.response.UserSignInResponseModel
import ru.vsu.csf.bakebudget.saveIsProUser
import ru.vsu.csf.bakebudget.saveToken

private var toast: Toast? = null

@OptIn(DelicateCoroutinesApi::class)
fun register(
    ctx: Context,
    userName: MutableState<String>,
    userEmail: MutableState<String>,
    userPassword: MutableState<String>,
    retrofitAPI: RetrofitAPI
) {
    GlobalScope.launch(Dispatchers.Main) {
        val res = retrofitAPI.register(
            UserSignUpRequestModel(
                userName.value,
                userEmail.value,
                userPassword.value
            )
        )
        onResultRegister(res, ctx)
    }
}

fun onResultRegister(
    result: Response<Void>?,
    ctx: Context
) {
    if (result != null) {
        if (result.isSuccessful) {
            if (toast!= null) {
                toast!!.cancel();
            }
            toast = Toast.makeText(
                ctx,
                "Пользователь успешно зарегистрирован",
                Toast.LENGTH_SHORT
            )
            toast!!.show()
        } else if (result.code() == 409) {
            if (toast!= null) {
                toast!!.cancel();
            }
            toast = Toast.makeText(
                ctx,
                "Пользователь с такой почтой уже существует!",
                Toast.LENGTH_SHORT
            )
            toast!!.show()
        } else {
            if (toast!= null) {
                toast!!.cancel();
            }
            toast = Toast.makeText(
                ctx,
                "Регистрация невозможна, некорректные данные. Имя должно быть от 3 до 50, а пароль от 8 до 255 символов",
                Toast.LENGTH_SHORT
            )
            toast!!.show()
            val eventParameters2 = "{\"button_clicked\":\"register failed\"}"
            AppMetrica.reportEvent(
                "User registration failed",
                eventParameters2
            )
        }
    }
}

@OptIn(DelicateCoroutinesApi::class)
fun login(
    ctx: Context,
    userEmail: MutableState<String>,
    userPassword: MutableState<String>,
    retrofitAPI: RetrofitAPI,
    isLogged: MutableState<Boolean>,
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
    isDataReceivedOutgoings: MutableState<Boolean>,
    isDataReceivedOrders: MutableState<Boolean>,
    orders0: MutableList<OrderModel>,
    orders1: MutableList<OrderModel>,
    orders2: MutableList<OrderModel>,
    orders3: MutableList<OrderModel>,
    firstTryPr: MutableState<Boolean>
) {
    GlobalScope.launch(Dispatchers.Main) {
        val res = retrofitAPI.login(UserSignInRequestModel(userEmail.value, userPassword.value))
        onResultLogin(
            res,
            ctx,
            isLogged,
            userRole,
            ingredients,
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
            orders0,
            orders1,
            orders2,
            orders3,
            firstTryPr
        )
    }
}

fun onResultLogin(
    result: Response<UserSignInResponseModel?>?,
    ctx: Context,
    isLogged: MutableState<Boolean>,
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
    isDataReceivedOutgoings: MutableState<Boolean>,
    isDataReceivedOrders: MutableState<Boolean>,
    orders0: MutableList<OrderModel>,
    orders1: MutableList<OrderModel>,
    orders2: MutableList<OrderModel>,
    orders3: MutableList<OrderModel>,
    firstTryPr: MutableState<Boolean>
) {
    if (result != null) {
        if (result.isSuccessful) {
            isLogged.value = true
            val model: UserSignInResponseModel? = result.body()
            saveToken(model!!.token, ctx)
//            Toast.makeText(
//                ctx,
//                "Response Code : " + result.code() + "\n" + "User Name : " + model.username + "\n" + "Email : " + model.email + "\n" + "Token : " + getToken(ctx) + "\n" + "ROLE : " + model.role,
//                Toast.LENGTH_SHORT
//            ).show()
            userRole.value = model.role
            if (userRole.value == "ROLE_ADVANCED_USER") {
                saveIsProUser("y", ctx)
            } else {
                saveIsProUser("n", ctx)
            }
            val eventParameters1 = "{\"button_clicked\":\"enter to account\"}"
            AppMetrica.reportEvent(
                "User enter to account",
                eventParameters1
            )
            ingredients.clear()
            products.clear()
            ingredientsInRecipe.clear()
            outgoings.clear()
            orders.clear()
            isDataReceivedIngredients.value = false
            ingredientsResponse.clear()
            ingredientsSet.clear()
            isDataReceivedProducts.value = false
            productsResponse.clear()
            isDataReceivedOutgoings.value = false
            isDataReceivedOrders.value = false
            firstTryPr.value = true
            orders0.clear()
            orders1.clear()
            orders2.clear()
            orders3.clear()
        } else {
            if (toast!= null) {
                toast!!.cancel();
            }
            toast = Toast.makeText(
                ctx,
                "Такого пользователя не существует или пароль неверный",
                Toast.LENGTH_SHORT
            )
            toast!!.show()
            val eventParameters2 = "{\"button_clicked\":\"enter to account failed\"}"
            AppMetrica.reportEvent(
                "User enter wrong name or password",
                eventParameters2
            )
        }
    }
}

