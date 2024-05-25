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
import ru.vsu.csf.bakebudget.models.request.UserSignInRequestModel
import ru.vsu.csf.bakebudget.models.request.UserSignUpRequestModel
import ru.vsu.csf.bakebudget.models.response.UserSignInResponseModel

@OptIn(DelicateCoroutinesApi::class)
fun register(
    ctx: Context,
    userName: MutableState<String>,
    userEmail: MutableState<String>,
    userPassword: MutableState<String>,
    retrofitAPI: RetrofitAPI
) {
    GlobalScope.launch(Dispatchers.Main) {
        val res = retrofitAPI.register(UserSignUpRequestModel(userName.value, userEmail.value, userPassword.value))
        onResultRegister(res, ctx)
    }
}

fun onResultRegister(
    result: Response<Void>?,
    ctx: Context
) {
    if (result != null) {
        if (result.isSuccessful) {
            Toast.makeText(
                ctx,
                "Response Code : " + result.code() + "\n" + "Пользователь успешно зарегистрирован",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(
                ctx,
                "Response Code : " + result.code() + "\n" + "Регистрация невозможна, некорректные данные",
                Toast.LENGTH_SHORT
            ).show()
            val eventParameters2 = "{\"button_clicked\":\"register failed\"}"
            AppMetrica.reportEvent(
                "User registration failed",
                eventParameters2
            )
        }
    }
}

fun login(
    ctx: Context,
    userEmail: MutableState<String>,
    userPassword: MutableState<String>,
    retrofitAPI: RetrofitAPI,
    isLogged: MutableState<Boolean>,
    jwtToken: MutableState<String>,
    userRole: MutableState<String>,
    isPro: MutableState<Boolean>
) {
    GlobalScope.launch(Dispatchers.Main) {
        val res = retrofitAPI.login(UserSignInRequestModel(userEmail.value, userPassword.value))
        onResultLogin(res, ctx, isLogged, jwtToken, userRole, isPro)
    }
}

fun onResultLogin(
    result: Response<UserSignInResponseModel?>?,
    ctx: Context,
    isLogged: MutableState<Boolean>,
    jwtToken: MutableState<String>,
    userRole: MutableState<String>,
    isPro: MutableState<Boolean>
) {
    if (result != null) {
        if (result.isSuccessful) {
            isLogged.value = true
            val model: UserSignInResponseModel? = result.body()
            jwtToken.value = model!!.token
            Toast.makeText(
                ctx,
                "Response Code : " + result.code() + "\n" + "User Name : " + model.username + "\n" + "Email : " + model.email + "\n" + "Token : " + jwtToken.value + "\n" + "ROLE : " + model.role,
                Toast.LENGTH_SHORT
            ).show()
            userRole.value = model.role
            isPro.value = userRole.value == "ROLE_ADVANCED_USER"
            val eventParameters1 = "{\"button_clicked\":\"enter to account\"}"
            AppMetrica.reportEvent(
                "User enter to account",
                eventParameters1
            )
        } else {
            Toast.makeText(
                ctx,
                "Response Code : " + result.code() + "\n" + "Такого пользователя не существует или пароль неверный",
                Toast.LENGTH_SHORT
            ).show()
            val eventParameters2 = "{\"button_clicked\":\"enter to account failed\"}"
            AppMetrica.reportEvent(
                "User enter wrong name or password",
                eventParameters2
            )
        }
    }
}

