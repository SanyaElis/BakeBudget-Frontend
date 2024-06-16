package ru.vsu.csf.bakebudget.screens

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import io.appmetrica.analytics.AppMetrica
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ru.vsu.csf.bakebudget.R
import ru.vsu.csf.bakebudget.api.RetrofitAPI
import ru.vsu.csf.bakebudget.clearIsProUser
import ru.vsu.csf.bakebudget.components.InputTextField
import ru.vsu.csf.bakebudget.components.InputTextFieldGroup
import ru.vsu.csf.bakebudget.getIsProUser
import ru.vsu.csf.bakebudget.getToken
import ru.vsu.csf.bakebudget.models.MenuItemModel
import ru.vsu.csf.bakebudget.models.ProductModel
import ru.vsu.csf.bakebudget.models.response.ProductResponseModel
import ru.vsu.csf.bakebudget.saveIsProUser
import ru.vsu.csf.bakebudget.services.changeRole
import ru.vsu.csf.bakebudget.services.createCode
import ru.vsu.csf.bakebudget.services.deleteProduct
import ru.vsu.csf.bakebudget.services.getCode
import ru.vsu.csf.bakebudget.services.leaveGroup
import ru.vsu.csf.bakebudget.services.setCode
import ru.vsu.csf.bakebudget.ui.theme.PrimaryBack
import ru.vsu.csf.bakebudget.ui.theme.SideBack
import ru.vsu.csf.bakebudget.ui.theme.TextPrimary
import ru.vsu.csf.bakebudget.ui.theme.border
import ru.vsu.csf.bakebudget.ui.theme.sizeForSmallDevices
import ru.vsu.csf.bakebudget.utils.codeAlreadyGenerated
import ru.vsu.csf.bakebudget.utils.codeCopied
import java.util.Timer
import kotlin.concurrent.schedule

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun GroupsScreen(
    navController: NavHostController,
    isLogged: MutableState<Boolean>,
    retrofitAPI: RetrofitAPI,
    userRole: MutableState<String>
) {
    val mContext = LocalContext.current

    val clipboardManager: ClipboardManager = LocalClipboardManager.current

    val item = listOf(MenuItemModel(R.drawable.groups, "Группы"))
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val selectedItem = remember {
        mutableStateOf(item[0])
    }

    val code = remember {
        mutableStateOf("")
    }

    val isPro = remember {
        mutableStateOf(false)
    }

    val generatedCode = remember {
        mutableStateOf("")
    }

    val codeExists = remember {
        mutableStateOf(false)
    }

    val openAlertDialog = remember { mutableStateOf(false) }
    when {
        openAlertDialog.value -> {
            ConfirmationAlert(
                onDismissRequest = {
                    openAlertDialog.value = false
                },
                onConfirmation = {
                    changeRole(mContext, retrofitAPI, userRole)
                    val eventParameters3 = "{\"button_clicked\":\"advanced mode\"}"
                    AppMetrica.reportEvent(
                        "User enter advanced mode",
                        eventParameters3
                    )
                    Timer().schedule(2000) {
                        clearIsProUser(mContext)
                        saveIsProUser("y", mContext)
                        generatedCode.value = " "
                        generatedCode.value = ""
                        isPro.value = true
                    }
                    openAlertDialog.value = false
                },
                dialogTitle = "Переход на продвинутую версию",
                dialogText = " Вы не сможете вернуться в обычную версию приложения. Уверены, что хотите перейти на продвинутую версию?"
            )
        }
    }


    if (getToken(mContext) != null && !codeExists.value) {
        getCode(mContext, retrofitAPI, generatedCode)
        codeExists.value = true
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            SideMenu(
                navController = navController,
                drawerState = drawerState,
                scope = scope,
                selectedItem = selectedItem,
                isLogged = isLogged
            )
        },
        content = {
            Scaffold(bottomBar = {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.1f)
                        .background(SideBack)
                        .padding(start = 8.dp, end = 8.dp),
                    shape = RoundedCornerShape(10.dp, 10.dp, 0.dp, 0.dp),
                ) {
                    Box(
                        modifier = Modifier.background(PrimaryBack),
                        contentAlignment = Alignment.Center
                    ) {
                        TextButton(
                            onClick = {
                                if (getIsProUser(mContext).equals("y")) {
                                    if (generatedCode.value == "") {
                                        createCode(
                                            mContext,
                                            retrofitAPI,
                                            generatedCode,
                                            userRole
                                        )
                                        val eventParameters1 =
                                            "{\"button_clicked\":\"create group code\"}"
                                        AppMetrica.reportEvent(
                                            "Group code generated",
                                            eventParameters1
                                        )
                                    } else {
                                        codeAlreadyGenerated(mContext)
                                    }
                                } else {
                                    setCode(mContext, retrofitAPI, code, generatedCode)
                                    val eventParameters2 = "{\"button_clicked\":\"create group entered\"}"
                                    AppMetrica.reportEvent(
                                        "User enter group",
                                        eventParameters2
                                    )
                                }
                            }
                        ) {
                            if (getIsProUser(mContext).equals("y") || isPro.value) {
                                Image(
                                    painter = painterResource(id = R.drawable.button_generate),
                                    contentDescription = "generate"
                                )
                            } else {
                                Image(
                                    painter = painterResource(id = R.drawable.confirm_button),
                                    contentDescription = "confirm"
                                )
                            }
                        }
                    }
                }
            }) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .background(SideBack)
                        .padding(bottom = 10.dp)
                ) {
                    Column {
                        Header(scope = scope, drawerState = drawerState)
                        Box(
                            modifier = Modifier
                                .fillMaxHeight(0.91f)
                                .fillMaxWidth()
                                .background(SideBack),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                modifier = Modifier.fillMaxHeight(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                if (getIsProUser(mContext).equals("y")) {
                                    if (generatedCode.value == "") {
                                        Box(
                                            modifier = Modifier.padding(8.dp),
                                            contentAlignment = Alignment.TopCenter
                                        ) {
                                            Text(
                                                text = "Вы пользуетесь продвинутой версией приложения! Нажмите на кнопку «СГЕНЕРИРОВАТЬ», чтобы создать группу.",
                                                fontSize = 20.sp,
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                    }
                                    SelectionContainer(modifier = Modifier.padding(16.dp)) {
                                        Text(text = generatedCode.value, fontSize = 30.sp, maxLines = 2, textAlign = TextAlign.Center)
                                    }
                                    if (generatedCode.value != "") {
                                        IconButton(onClick = {
                                            clipboardManager.setText(AnnotatedString((generatedCode.value)))
                                            codeCopied(mContext)
                                        }) {
                                            Icon(
                                                imageVector = Icons.Default.ContentCopy,
                                                contentDescription = "copy",
                                                tint = Color.Gray
                                            )
                                        }
                                        Box(
                                            modifier = Modifier.padding(8.dp),
                                            contentAlignment = Alignment.BottomCenter
                                        ) {
                                            Text(
                                                text = "Другие пользователи смогут ввести данный код, чтобы присоединиться к группе",
                                                fontSize = 20.sp,
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                    }
                                } else {
                                    if (generatedCode.value != "") {
                                        Column {
                                            Box(
                                                modifier = Modifier.padding(8.dp),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = "Сейчас вы принадлежите группе с кодом: " + generatedCode.value,
                                                    fontSize = 20.sp,
                                                    textAlign = TextAlign.Center
                                                )
                                            }
                                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                                                Text(
                                                    text = "Выйти",
                                                    fontSize = 20.sp,
                                                    textAlign = TextAlign.Center
                                                )
                                                Icon(
                                                    modifier = Modifier
                                                        .padding(5.dp)
                                                        .clickable(onClick = {
                                                            generatedCode.value = ""
                                                            leaveGroup(mContext,retrofitAPI)
                                                        }),
                                                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                                                    tint = TextPrimary,
                                                    contentDescription = "exit group",
                                                )
                                            }
                                        }
                                    }
                                    //TODO:выйти из группы
                                    InputTextFieldGroup(
                                        placeholder = "Введите код группы",
                                        text = code,
                                        max = 50,
                                        true
                                    )
                                    Box(
                                        modifier = Modifier.padding(8.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "Введите код, чтобы присоединиться к группе",
                                            fontSize = 20.sp,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                    Box(
                                        modifier = Modifier.padding(8.dp),
                                        contentAlignment = Alignment.BottomCenter
                                    ) {
                                        Text(
                                            text = "Перейдите на продвинутую версию, чтобы получить возможность создать свою группу!",
                                            fontSize = 20.sp,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                    IconButton(onClick = {
                                        openAlertDialog.value = true
                                    }) {
                                        Icon(
                                            modifier = Modifier
                                                .background(PrimaryBack)
                                                .size(80.dp)
                                                .clip(
                                                    CircleShape
                                                ),
                                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                            contentDescription = "pro",
                                            tint = Color.White
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        })
}

@Composable
private fun Header(scope: CoroutineScope, drawerState: DrawerState) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.08f)
            .background(SideBack)
            .padding(start = 8.dp, end = 8.dp),
        shape = RoundedCornerShape(0.dp, 0.dp, 10.dp, 10.dp),
    ) {
        Column(modifier = Modifier.background(PrimaryBack)) {
            Row {
                Box(
                    modifier = Modifier
                        .background(PrimaryBack)
                ) {
                    TextButton(onClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    }) {
                        Image(
                            painter = painterResource(id = R.drawable.menu_open),
                            contentDescription = "menu",
                            modifier = Modifier.padding(5.dp)
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(PrimaryBack)
                        .padding(top = 8.dp, end = 64.dp),
                    contentAlignment = Alignment.TopCenter
                ) {
                    val configuration = LocalConfiguration.current
                    val width = configuration.screenWidthDp.dp
                    if (width < border) {
                        Text(text = "ГРУППЫ", fontSize = sizeForSmallDevices, color = Color.White)
                    } else {
                        Text(text = "ГРУППЫ", fontSize = 24.sp, color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun ConfirmationAlert(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String
) {
    androidx.compose.material3.AlertDialog(
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
                Text("Перейти")
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