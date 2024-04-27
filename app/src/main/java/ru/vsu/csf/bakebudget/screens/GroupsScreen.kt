package ru.vsu.csf.bakebudget.screens

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ru.vsu.csf.bakebudget.R
import ru.vsu.csf.bakebudget.components.Good
import ru.vsu.csf.bakebudget.components.InputTextField
import ru.vsu.csf.bakebudget.models.MenuItemModel
import ru.vsu.csf.bakebudget.ui.theme.PrimaryBack
import ru.vsu.csf.bakebudget.ui.theme.SideBack

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun GroupsScreen(
    navController: NavHostController,
    isLogged: MutableState<Boolean>,
    isPro: Boolean
) {
    val mContext = LocalContext.current
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
                                if (isPro) {
                                    generatedCode.value = getRandomString(14)
                                } else {
                                    if (code.value.length == 14) {
                                        mToast(context = mContext)
                                    } else {
                                        mToastWrong(context = mContext)
                                    }
                                }
                            }
                        ) {
                            if (isPro) {
                                Image(
                                    painter = painterResource(id = R.drawable.button_generate),
                                    contentDescription = "generate"
                                )
                            } else {
                                Image(
                                    painter = painterResource(id = R.drawable.button_confirm),
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
                        Box(modifier = Modifier
                            .fillMaxHeight(0.91f)
                            .fillMaxWidth()
                            .background(SideBack),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                if (isPro) {
                                    SelectionContainer {
                                        Text(text = generatedCode.value, fontSize = 40.sp)
                                    }
                                    if (generatedCode.value != "") {
                                        Box(
                                            modifier = Modifier.padding(8.dp),
                                            contentAlignment = Alignment.BottomCenter
                                        ) {
                                            Text(text = "Другие пользователи смогут ввести данный код, чтобы присоединиться к группе", fontSize = 20.sp,
                                                textAlign = TextAlign.Center)
                                        }
                                    }
                                } else {
                                    InputTextField(text = "Введите код группы", value = code, max = 14, 300)
                                    Box(
                                        modifier = Modifier.padding(8.dp),
                                        contentAlignment = Alignment.BottomCenter
                                    ) {
                                        Text(text = "Введите код, чтобы присоединиться к группе", fontSize = 20.sp,
                                            textAlign = TextAlign.Center)
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
                        .padding(top = 12.dp, end = 64.dp),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Text(text = "ГРУППЫ", fontSize = 24.sp, color = Color.White)
                }
            }
        }
    }
}

private fun mToast(context: Context) {
    Toast.makeText(
        context,
        "Теперь вы входите в состав группы",
        Toast.LENGTH_LONG
    ).show()
}

private fun mToastWrong(context: Context) {
    Toast.makeText(
        context,
        "Код не соответсвует ни одной группе",
        Toast.LENGTH_LONG
    ).show()
}

fun getRandomString(length: Int) : String {
    val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
    return (1..length)
        .map { allowedChars.random() }
        .joinToString("")
}