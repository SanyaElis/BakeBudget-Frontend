package ru.vsu.csf.bakebudget.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import ru.vsu.csf.bakebudget.R
import ru.vsu.csf.bakebudget.ui.theme.PrimaryBack

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(navController: NavHostController) {
    Scaffold(topBar = {SideMenu(navController = navController)}) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(PrimaryBack)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.8f),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "logo"
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                TextButton(
                    onClick = { navController.navigate("login") }
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.button_enter),
                        contentDescription = "enter"
                    )
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
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
        }
    }
}