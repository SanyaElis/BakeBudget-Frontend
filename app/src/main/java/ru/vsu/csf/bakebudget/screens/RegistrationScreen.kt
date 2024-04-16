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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import ru.vsu.csf.bakebudget.R
import ru.vsu.csf.bakebudget.components.TextForm
import ru.vsu.csf.bakebudget.ui.theme.PrimaryBack

@Composable
fun RegistrationScreen(navController: NavHostController) {
    Surface(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .background(PrimaryBack)
        .padding(top = 26.dp, bottom = 10.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth().fillMaxHeight().background(PrimaryBack).padding(top = 250.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {
            TextForm(label = "Имя пользователя")
            TextForm(label = "Email")
            TextForm(label = "Пароль")
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 100.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            TextButton(
                onClick = {navController.navigate("login")}
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