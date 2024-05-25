package ru.vsu.csf.bakebudget.screens

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import io.appmetrica.analytics.AppMetrica
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ru.vsu.csf.bakebudget.R
import ru.vsu.csf.bakebudget.api.RetrofitAPI
import ru.vsu.csf.bakebudget.components.BarGraph
import ru.vsu.csf.bakebudget.components.BarType
import ru.vsu.csf.bakebudget.components.DatePeriodField
import ru.vsu.csf.bakebudget.components.SwitchForm
import ru.vsu.csf.bakebudget.models.MenuItemModel
import ru.vsu.csf.bakebudget.models.request.ReportRequestModel
import ru.vsu.csf.bakebudget.services.createReportIncome
import ru.vsu.csf.bakebudget.services.createReportOrders
import ru.vsu.csf.bakebudget.ui.theme.PrimaryBack
import ru.vsu.csf.bakebudget.ui.theme.SideBack
import ru.vsu.csf.bakebudget.ui.theme.TextPrimary
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ReportsScreen(
    navController: NavHostController,
    isLogged: MutableState<Boolean>,
    isPro: MutableState<Boolean>,
    retrofitAPI: RetrofitAPI,
    userRole: MutableState<String>
) {
    val mContext = LocalContext.current

    val item = listOf(MenuItemModel(R.drawable.reports, "Отчеты"))
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val selectedItem = remember {
        mutableStateOf(item[0])
    }

    val eventParameters1 = "{\"button_clicked\":\"report created\"}"

    val openDatePicker1 = remember { mutableStateOf(false) }
    val openDatePicker2 = remember { mutableStateOf(false) }
    val dateStart = remember {
        mutableStateOf(LocalDate.now())
    }
    val dateEnd = remember {
        mutableStateOf(LocalDate.now())
    }

    val items = remember {
        listOf("Заказы", "Доходы")
    }

    val selectedIndex = remember {
        mutableIntStateOf(0)
    }

    val dataListOrders = remember {
        mutableStateListOf<Long>()
    }
    val dataListIncome = remember {
        mutableStateListOf<Long>()
    }

    val reportState = remember { mutableStateOf(false) }

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
                                reportState.value = false
                                val date1 = dateStart.value.format(
                                    DateTimeFormatter.ofPattern(
                                        "yyyy-MM-dd"
                                    )
                                )
                                val date2 = dateEnd.value.format(
                                    DateTimeFormatter.ofPattern(
                                        "yyyy-MM-dd"
                                    )
                                )
                                AppMetrica.reportEvent("Report created", eventParameters1)
                                createReportOrders(mContext, retrofitAPI, ReportRequestModel(date1, date2, date1, date2), dataListOrders, reportState)
                                createReportIncome(mContext, retrofitAPI, ReportRequestModel(date1, date2, date1, date2), dataListIncome, reportState)
                            }
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.button_make_report),
                                contentDescription = "make_report"
                            )
                        }
                    }
                }
            }) {
                Column {
                    Header(scope = scope, drawerState = drawerState)
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .background(SideBack)
                            .padding(bottom = 10.dp, top = 15.dp)
                    ) {
                        DatePeriodField(dateStart, openDatePicker1)
                        DatePeriodField(dateEnd, openDatePicker2)
                        Column(
                            modifier = Modifier
                                .fillMaxHeight(0.8f)
                                .background(SideBack)
                                .padding(start = 8.dp)
                        ) {
                            Box(modifier = Modifier.padding(start = 8.dp)) {
                                Text(
                                    text = "Выберите период",
                                    fontSize = 24.sp
                                )
                            }
                            Row(modifier = Modifier.background(SideBack)) {
                                TextButton(onClick = { openDatePicker1.value = true }) {
                                    Row {
                                        Icon(
                                            modifier = Modifier.padding(end = 3.dp),
                                            imageVector = Icons.Default.DateRange,
                                            contentDescription = "date"
                                        )
                                        Text(
                                            text = dateStart.value.format(
                                                DateTimeFormatter.ofPattern(
                                                    "d MMM YYYY"
                                                )
                                            ), fontSize = 20.sp,
                                            color = TextPrimary
                                        )
                                    }
                                }
                                Box(
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "—", fontSize = 30.sp)
                                }
                                TextButton(onClick = { openDatePicker2.value = true }) {
                                    Row {
                                        Icon(
                                            modifier = Modifier.padding(end = 3.dp),
                                            imageVector = Icons.Default.DateRange,
                                            contentDescription = "date"
                                        )
                                        Text(
                                            text = dateEnd.value.format(
                                                DateTimeFormatter.ofPattern(
                                                    "d MMM YYYY"
                                                )
                                            ), fontSize = 20.sp,
                                            color = TextPrimary
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.padding(12.dp))
                            Box(modifier = Modifier.padding(start = 8.dp)) {
                                Text(
                                    text = "Выберите тип отчета",
                                    fontSize = 24.sp
                                )
                            }
                            SwitchForm(
                                selectedIndex = selectedIndex,
                                items = items,
                                onSelectionChange = {
                                    selectedIndex.intValue = it
                                },
                            )
                            if (reportState.value) {
                                if (selectedIndex.intValue == 0 && dataListOrders.isNotEmpty()) {
                                    val floatValue = mutableListOf<Float>()
                                    val xList = mutableListOf("Принято", "Завершено", "Отменено")

                                    dataListOrders.forEachIndexed { index, value ->

                                        floatValue.add(
                                            index = index,
                                            element = value.toFloat() / if (dataListOrders.max().toFloat() == 0f) 1f else dataListOrders.max().toFloat()
                                        )

                                    }
                                    Column {
                                        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                                            Text(buildAnnotatedString {
                                                append("Завершено заказов: ")
                                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                                    append(dataListOrders[1].toString())
                                                }
                                            })
                                        }
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .fillMaxHeight(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            BarGraph(
                                                graphBarData = floatValue,
                                                xAxisScaleData = xList,
                                                barData_ = dataListOrders,
                                                height = 300.dp,
                                                roundType = BarType.TOP_CURVED,
                                                barWidth = 30.dp,
                                                barColor = PrimaryBack,
                                                barArrangement = Arrangement.SpaceEvenly
                                            )
                                        }
                                    }
                                } else if (dataListIncome.isNotEmpty()){
                                    val floatValue = mutableListOf<Float>()
                                    val xList = mutableListOf("Расходы", "Доход")

                                    dataListIncome.forEachIndexed { index, value ->

                                        floatValue.add(
                                            index = index,
                                            element = value.toFloat() / if (dataListIncome.max().toFloat() == 0f) 1f else dataListIncome.max().toFloat()
                                        )
                                    }
                                    Column {
                                        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                                            Text(buildAnnotatedString {
                                                append("Прибыль составила ")
                                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                                    append((dataListIncome[1] - dataListIncome[0]).toString())
                                                }
                                                append(" р.")
                                            })
                                        }
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .fillMaxHeight(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            BarGraph(
                                                graphBarData = floatValue,
                                                xAxisScaleData = xList,
                                                barData_ = dataListIncome,
                                                height = 300.dp,
                                                roundType = BarType.TOP_CURVED,
                                                barWidth = 40.dp,
                                                barColor = PrimaryBack,
                                                barArrangement = Arrangement.SpaceEvenly
                                            )
                                        }
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
                        .padding(top = 8.dp, end = 60.dp),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Text(text = "ОТЧЕТЫ", fontSize = 24.sp, color = Color.White)
                }
            }
        }
    }
}