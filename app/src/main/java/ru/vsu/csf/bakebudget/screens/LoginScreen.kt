package ru.vsu.csf.bakebudget.screens

import android.content.Context
import android.content.SharedPreferences
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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.vsu.csf.bakebudget.R
import ru.vsu.csf.bakebudget.api.RetrofitAPI
import ru.vsu.csf.bakebudget.components.PasswordTextForm
import ru.vsu.csf.bakebudget.components.TextForm
import ru.vsu.csf.bakebudget.models.IngredientModel
import ru.vsu.csf.bakebudget.models.response.UserSignInResponseModel
import ru.vsu.csf.bakebudget.models.request.UserSignInRequestModel
import ru.vsu.csf.bakebudget.models.response.IngredientResponseModel
import ru.vsu.csf.bakebudget.ui.theme.PrimaryBack

@Composable
fun LoginScreen(
    navController: NavHostController,
    isLogged: MutableState<Boolean>,
    retrofitAPI: RetrofitAPI,
    jwtToken: MutableState<String>,
    userRole: MutableState<String>
) {
    val ctx = LocalContext.current
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
                .padding(top = 250.dp),
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
                    postDataUsingRetrofitLogin(
                        ctx, userEmail, userPassword, retrofitAPI = retrofitAPI, isLogged, jwtToken, userRole
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

private fun postDataUsingRetrofitLogin(
    ctx: Context,
    userEmail: MutableState<String>,
    userPassword: MutableState<String>,
    retrofitAPI: RetrofitAPI,
    isLogged: MutableState<Boolean>,
    jwtToken: MutableState<String>,
    userRole: MutableState<String>
) {
    val dataModel = UserSignInRequestModel(userEmail.value, userPassword.value)
    val call: Call<UserSignInResponseModel?>? = retrofitAPI.login(dataModel)
    call!!.enqueue(object : Callback<UserSignInResponseModel?> {
        override fun onResponse(
            call: Call<UserSignInResponseModel?>,
            response: Response<UserSignInResponseModel?>
        ) {
            if (response.isSuccessful) {
                isLogged.value = true
                val model: UserSignInResponseModel? = response.body()
                jwtToken.value = model!!.token
                Toast.makeText(
                    ctx,
                    "Response Code : " + response.code() + "\n" + "User Name : " + model.username + "\n" + "Email : " + model.email + "\n" + "Token : " + jwtToken.value + "\n" + "ROLE : " + model.role,
                    Toast.LENGTH_SHORT
                ).show()
                userRole.value = model.role
            } else {
                Toast.makeText(
                    ctx,
                    "Response Code : " + response.code() + "\n" + "Такого пользователя не существует или пароль неверный",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        override fun onFailure(call: Call<UserSignInResponseModel?>, t: Throwable) {
        }
    })
}