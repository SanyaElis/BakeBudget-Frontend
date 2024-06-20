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
import io.appmetrica.analytics.AppMetrica
import ru.vsu.csf.bakebudget.R
import ru.vsu.csf.bakebudget.api.RetrofitAPI
import ru.vsu.csf.bakebudget.components.PasswordTextForm
import ru.vsu.csf.bakebudget.components.TextForm
import ru.vsu.csf.bakebudget.components.TextFormEmail
import ru.vsu.csf.bakebudget.services.register
import ru.vsu.csf.bakebudget.ui.theme.PrimaryBack
import ru.vsu.csf.bakebudget.ui.theme.borderH


@Composable
fun RegistrationScreen(navController: NavHostController, isLogged: MutableState<Boolean>, retrofitAPI: RetrofitAPI) {
    val ctx = LocalContext.current
    val configuration = LocalConfiguration.current
    val height = configuration.screenHeightDp.dp

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
            .padding(top = height/4),
            horizontalAlignment = Alignment.CenterHorizontally) {
            TextForm(label = "Имя пользователя", userName, 50)
            TextFormEmail(label = "Email", userEmail, 255)
            PasswordTextForm(label = "Пароль", userPassword, 255)
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 100.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            TextButton(
                onClick = {
                    register(
                        ctx, userName, userEmail, userPassword, retrofitAPI = retrofitAPI
                    )
                    val eventParameters1 = "{\"button_clicked\":\"register\"}"
                    AppMetrica.reportEvent(
                        "User registered",
                        eventParameters1
                    )
                    navController.navigate("login")}
            ) {
                Image(
                    painter = painterResource(id = if (height > borderH) R.drawable.register_white_button else R.drawable.register_button_small),
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