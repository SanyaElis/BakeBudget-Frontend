package ru.vsu.csf.bakebudget.screens

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.ContentCopy
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import io.appmetrica.analytics.AppMetrica
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Response
import ru.vsu.csf.bakebudget.R
import ru.vsu.csf.bakebudget.api.RetrofitAPI
import ru.vsu.csf.bakebudget.components.InputTextField
import ru.vsu.csf.bakebudget.models.MenuItemModel
import ru.vsu.csf.bakebudget.services.changeRole
import ru.vsu.csf.bakebudget.services.createCode
import ru.vsu.csf.bakebudget.services.getCode
import ru.vsu.csf.bakebudget.services.setCode
import ru.vsu.csf.bakebudget.ui.theme.PrimaryBack
import ru.vsu.csf.bakebudget.ui.theme.SideBack
import ru.vsu.csf.bakebudget.utils.codeAlreadyGenerated
import ru.vsu.csf.bakebudget.utils.codeCopied
import java.util.Timer
import kotlin.concurrent.schedule

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun GroupsScreen(
    navController: NavHostController,
    isLogged: MutableState<Boolean>,
    isPro: MutableState<Boolean>,
    retrofitAPI: RetrofitAPI,
    jwtToken: MutableState<String>,
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
    val generatedCode = remember {
        mutableStateOf("")
    }

    val codeExists = remember {
        mutableStateOf(false)
    }

    if (jwtToken.value != "" && !codeExists.value) {
        getCode(mContext, retrofitAPI, jwtToken, generatedCode)
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
                isLogged = isLogged,
                jwtToken
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
                                if (isPro.value) {
                                    if (generatedCode.value == "") {
                                        createCode(
                                            mContext,
                                            retrofitAPI,
                                            jwtToken,
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
                                    setCode(mContext, retrofitAPI, jwtToken, code, generatedCode)
                                    val eventParameters2 = "{\"button_clicked\":\"create group entered\"}"
                                    AppMetrica.reportEvent(
                                        "User enter group",
                                        eventParameters2
                                    )
                                }
                            }
                        ) {
                            if (isPro.value) {
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
                                if (isPro.value) {
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
                                    }
                                    //TODO:выйти из группы
                                    InputTextField(
                                        placeholder = "Введите код группы",
                                        text = code,
                                        max = 50,
                                        300
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
                                        changeRole(mContext, retrofitAPI, jwtToken, isPro, userRole)
                                        val eventParameters3 = "{\"button_clicked\":\"advanced mode\"}"
                                        AppMetrica.reportEvent(
                                            "User enter advanced mode",
                                            eventParameters3
                                        )
                                        Timer().schedule(2000) {
                                            isPro.value = true
                                            generatedCode.value = ""
                                        }
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
                    Text(text = "ГРУППЫ", fontSize = 24.sp, color = Color.White)
                }
            }
        }
    }
}