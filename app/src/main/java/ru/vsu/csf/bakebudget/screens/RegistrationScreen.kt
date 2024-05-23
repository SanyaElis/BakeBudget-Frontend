package ru.vsu.csf.bakebudget.screens

import android.content.Context
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.gson.GsonBuilder
import io.appmetrica.analytics.AppMetrica
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.vsu.csf.bakebudget.R
import ru.vsu.csf.bakebudget.api.RetrofitAPI
import ru.vsu.csf.bakebudget.components.PasswordTextForm
import ru.vsu.csf.bakebudget.components.TextForm
import ru.vsu.csf.bakebudget.models.request.UserSignUpRequestModel
import ru.vsu.csf.bakebudget.ui.theme.PrimaryBack


@Composable
fun RegistrationScreen(navController: NavHostController, isLogged: MutableState<Boolean>, retrofitAPI: RetrofitAPI) {
    val ctx = LocalContext.current

    val userName = remember {
        mutableStateOf("")
    }
    val userEmail = remember {
        mutableStateOf("")
    }
    val userPassword = remember {
        mutableStateOf("")
    }

    Surface(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .background(PrimaryBack)
        .padding(top = 26.dp, bottom = 10.dp)
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(PrimaryBack)
            .padding(top = 250.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {
            TextForm(label = "Имя пользователя", userName)
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
                    postDataUsingRetrofit(
                        ctx, userName, userEmail, userPassword, retrofitAPI = retrofitAPI
                    )
                    navController.navigate("login")}
            ) {
                Image(
                    painter = painterResource(id = R.drawable.register_white_button),
                    contentDescription = "register"
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
                onClick = {navController.navigate("home")},
            ) {
                Image(
                    painter = painterResource(id = R.drawable.have_account),
                    contentDescription = "enter",
                )
            }
        }
    }
}

private fun postDataUsingRetrofit(
    ctx: Context,
    userName: MutableState<String>,
    userEmail: MutableState<String>,
    userPassword: MutableState<String>,
    retrofitAPI: RetrofitAPI
) {
    val dataModel = UserSignUpRequestModel(userName.value, userEmail.value, userPassword.value)
    val call: Call<String?>? = retrofitAPI.register(dataModel)
    call!!.enqueue(object : Callback<String?> {
        override fun onResponse(call: Call<String?>, response: Response<String?>) {
            if (response.isSuccessful) {
                Toast.makeText(
                    ctx,
                    "Response Code : " + response.code() + "\n" + "Пользователь успешно зарегистрирован",
                    Toast.LENGTH_SHORT
                ).show()
                val eventParameters1 = "{\"button_clicked\":\"register\"}"
                AppMetrica.reportEvent(
                    "User registered",
                    eventParameters1
                )
            } else {
                Toast.makeText(
                    ctx,
                    "Response Code : " + response.code() +  "\n" + "Регистрация невозможна, некорректные данные",
                    Toast.LENGTH_SHORT
                ).show()
                val eventParameters1 = "{\"button_clicked\":\"register\"}"
                AppMetrica.reportEvent(
                    "User registration failed",
                    eventParameters1
                )
            }
        }

        override fun onFailure(call: Call<String?>, t: Throwable) {
        }
    })
}