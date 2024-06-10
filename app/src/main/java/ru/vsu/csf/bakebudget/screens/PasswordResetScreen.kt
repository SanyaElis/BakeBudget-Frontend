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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import io.appmetrica.analytics.AppMetrica
import ru.vsu.csf.bakebudget.R
import ru.vsu.csf.bakebudget.api.RetrofitAPI
import ru.vsu.csf.bakebudget.components.AlertDialog1
import ru.vsu.csf.bakebudget.components.InputTextField
import ru.vsu.csf.bakebudget.components.PasswordTextForm
import ru.vsu.csf.bakebudget.components.TextForm
import ru.vsu.csf.bakebudget.models.IngredientInProductModel
import ru.vsu.csf.bakebudget.models.IngredientModel
import ru.vsu.csf.bakebudget.models.response.IngredientResponseModel
import ru.vsu.csf.bakebudget.services.deleteIngredientInProduct
import ru.vsu.csf.bakebudget.services.updateIngredientInProduct
import ru.vsu.csf.bakebudget.ui.theme.PrimaryBack
import ru.vsu.csf.bakebudget.ui.theme.SideBack
import ru.vsu.csf.bakebudget.utils.dataIncorrectToast
import ru.vsu.csf.bakebudget.utils.isWeightValid

@Composable
fun PasswordResetScreen(navController: NavHostController, isLogged: MutableState<Boolean>, retrofitAPI: RetrofitAPI) {
    val userEmail = remember {
        mutableStateOf("")
    }
    val userPassword = remember {
        mutableStateOf("")
    }
    val mContext = LocalContext.current
    val openAlertDialog = remember { mutableStateOf(false) }
    when {
        openAlertDialog.value -> {
            AlertDialogResetPassword(
                onDismissRequest = {
                    openAlertDialog.value = false
                },
                onConfirmation = {
                    openAlertDialog.value = false
                },
                dialogTitle = "Смена пароля",
                dialogText = "После перехода по ссылке в письме нажмите «ОК»",
                userEmail,
                userPassword,
                mContext,
                retrofitAPI
            )
        }
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
            PasswordTextForm(label = "Новый пароль", userPassword)
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 100.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            TextButton(
                onClick = {
                    navController.navigate("login")
                    mToast(mContext)
                    val eventParameters1 = "{\"button_clicked\":\"reset password\"}"
                    AppMetrica.reportEvent(
                        "Request for password reset",
                        eventParameters1
                    )
                    openAlertDialog.value = true
                }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.confirm_button),
                    contentDescription = "confirm"
                )
            }
        }
    }
}

private fun mToast(context: Context) {
    Toast.makeText(
        context,
        "Чтобы сбросить пароль, перейдите по ссылке в письме, отправленном на почту",
        Toast.LENGTH_LONG
    ).show()
}

@Composable
fun AlertDialogResetPassword(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    email: MutableState<String>,
    password: MutableState<String>,
    context: Context,
    retrofitAPI: RetrofitAPI
) {
    AlertDialog(
        containerColor = SideBack,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        ),
        modifier = Modifier.fillMaxWidth(0.9f),
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text("Изменить пароль")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Отмена")
            }
        }
    )
}