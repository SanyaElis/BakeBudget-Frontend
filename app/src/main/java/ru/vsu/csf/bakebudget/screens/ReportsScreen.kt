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
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.platform.LocalConfiguration
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
import ru.vsu.csf.bakebudget.getIsProUser
import ru.vsu.csf.bakebudget.models.MenuItemModel
import ru.vsu.csf.bakebudget.models.request.ReportRequestModel
import ru.vsu.csf.bakebudget.services.createReportIncome
import ru.vsu.csf.bakebudget.services.createReportOrders
import ru.vsu.csf.bakebudget.ui.theme.PrimaryBack
import ru.vsu.csf.bakebudget.ui.theme.SideBack
import ru.vsu.csf.bakebudget.ui.theme.TextPrimary
import ru.vsu.csf.bakebudget.ui.theme.border
import ru.vsu.csf.bakebudget.ui.theme.borderH
import ru.vsu.csf.bakebudget.ui.theme.sizeForSmallDevices
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Timer
import kotlin.concurrent.schedule

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ReportsScreen(
    navController: NavHostController,
    isLogged: MutableState<Boolean>,
    retrofitAPI: RetrofitAPI,
    userRole: MutableState<String>
) {
    if (!isLogged.value) {
        navController.navigate("home")
    }

    val configuration = LocalConfiguration.current
    val height = configuration.screenHeightDp.dp

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

    val selectedIndexGroup = remember {
        mutableIntStateOf(0)
    }

    val itemsGroup = remember {
        listOf("Личный", "Групповой")
    }

    val dataListOrders = remember {
        mutableStateListOf<Long>()
    }
    val dataListIncome = remember {
        mutableStateListOf<Long>()
    }

    val dataListOrdersGroup = remember {
        mutableStateListOf<Long>()
    }
    val dataListIncomeGroup = remember {
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
//                                if (selectedIndexGroup.intValue == 0) {
                                createReportOrders(
                                    mContext,
                                    retrofitAPI,
                                    ReportRequestModel(date1, date2, date1, date2),
                                    dataListOrders,
                                    reportState,
                                    false
                                )
                                createReportIncome(
                                    mContext,
                                    retrofitAPI,
                                    ReportRequestModel(date1, date2, date1, date2),
                                    dataListIncome,
                                    reportState,
                                    false
                                )
//                                } else {
                                if (getIsProUser(mContext).equals("y")) {
                                    createReportOrders(
                                        mContext,
                                        retrofitAPI,
                                        ReportRequestModel(date1, date2, date1, date2),
                                        dataListOrdersGroup,
                                        reportState,
                                        true
                                    )
                                    createReportIncome(
                                        mContext,
                                        retrofitAPI,
                                        ReportRequestModel(date1, date2, date1, date2),
                                        dataListIncomeGroup,
                                        reportState,
                                        true
                                    )
                                }
                            }
//                            }
                        ) {
                            Image(
                                painter = painterResource(id = if (height > borderH) R.drawable.button_make_report else R.drawable.make_report_button_small),
                                contentDescription = "make_report"
                            )
                        }
                    }
                }
            }) {
                Column(modifier = Modifier
                    .fillMaxHeight(0.9f)) {
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
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxHeight()
                                .background(SideBack)
                                .padding(start = 8.dp)
                        ) {
                            item {
                                Box(modifier = Modifier.padding(start = 8.dp)) {
                                    Text(
                                        text = "Выберите период",
                                        fontSize = 24.sp
                                    )
                                }
                            }
                            item {
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
                            }
                            item {
                                Spacer(modifier = Modifier.padding(12.dp))
                                Box(modifier = Modifier.padding(start = 8.dp)) {
                                    Text(
                                        text = "Выберите тип отчета",
                                        fontSize = 24.sp
                                    )
                                }
                            }
                            item {
                                SwitchForm(
                                    selectedIndex = selectedIndex,
                                    items = items,
                                    onSelectionChange = {
                                        selectedIndex.intValue = it
                                    },
                                )
                            }
                            item {
                                if (getIsProUser(mContext).equals("y")) {
                                    SwitchForm(
                                        selectedIndex = selectedIndexGroup,
                                        items = itemsGroup,
                                        onSelectionChange = {
                                            selectedIndexGroup.intValue = it
                                        },
                                    )
                                }
                            }
                            item {
                                if (reportState.value) {
                                    if (selectedIndex.intValue == 0 && dataListOrders.isNotEmpty()) {
                                        if (selectedIndexGroup.intValue == 0) {
                                            val floatValue = mutableListOf<Float>()
                                            val xList =
                                                mutableListOf("Принято", "Завершено", "Отменено")

                                            dataListOrders.forEachIndexed { index, value ->

                                                val max =
                                                    if (dataListOrders.max() % 3L == 0L) dataListOrders.max() else (if (dataListOrders.max() % 3L == 1L) (dataListOrders.max() + 2) else dataListOrders.max() + 1)
                                                floatValue.add(
                                                    index = index,
                                                    element = value.toFloat() / if (max.toFloat() == 0f) 1f else max.toFloat()
                                                )

                                            }
                                            Column {
                                                Spacer(modifier = Modifier.padding(12.dp))
                                                Box(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Text(buildAnnotatedString {
                                                        append("Завершено заказов: ")
                                                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                                            append(dataListOrders[1].toString())
                                                        }
                                                    })
                                                }
                                                Spacer(modifier = Modifier.padding(12.dp))
                                                Box(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .fillMaxHeight()
                                                        .padding(start = 8.dp, end = 8.dp),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    BarGraph(
                                                        graphBarData = floatValue,
                                                        xAxisScaleData = xList,
                                                        barData_ = dataListOrders,
                                                        height = 250.dp,
                                                        roundType = BarType.TOP_CURVED,
                                                        barWidth = 30.dp,
                                                        barColor = PrimaryBack,
                                                        barArrangement = Arrangement.SpaceEvenly,
                                                        true
                                                    )
                                                }
                                            }
                                        } else if (dataListOrdersGroup.isNotEmpty()) {
                                            val floatValue = mutableListOf<Float>()
                                            val xList =
                                                mutableListOf("Принято", "Завершено", "Отменено")

                                            dataListOrdersGroup.forEachIndexed { index, value ->

                                                val max =
                                                    if (dataListOrdersGroup.max() % 3L == 0L) dataListOrdersGroup.max() else (if (dataListOrdersGroup.max() % 3L == 1L) (dataListOrdersGroup.max() + 2) else dataListOrdersGroup.max() + 1)
                                                floatValue.add(
                                                    index = index,
                                                    element = value.toFloat() / if (max.toFloat() == 0f) 1f else max.toFloat()
                                                )

                                            }
                                            Column {
                                                Spacer(modifier = Modifier.padding(12.dp))
                                                Box(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Text(buildAnnotatedString {
                                                        append("Завершено заказов: ")
                                                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                                            append(dataListOrdersGroup[1].toString())
                                                        }
                                                    })
                                                }
                                                Spacer(modifier = Modifier.padding(12.dp))
                                                Box(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .fillMaxHeight()
                                                        .padding(start = 8.dp, end = 8.dp),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    BarGraph(
                                                        graphBarData = floatValue,
                                                        xAxisScaleData = xList,
                                                        barData_ = dataListOrdersGroup,
                                                        height = 250.dp,
                                                        roundType = BarType.TOP_CURVED,
                                                        barWidth = 30.dp,
                                                        barColor = PrimaryBack,
                                                        barArrangement = Arrangement.SpaceEvenly,
                                                        true
                                                    )
                                                }
                                            }
                                        }
                                    } else if (selectedIndexGroup.intValue == 0 && dataListIncome.isNotEmpty()) {
                                        val floatValue = mutableListOf<Float>()
                                        val xList = mutableListOf("Расходы", "Доход")

                                        dataListIncome.forEachIndexed { index, value ->

                                            floatValue.add(
                                                index = index,
                                                element = value.toFloat() / if (dataListIncome.max()
                                                        .toFloat() == 0f
                                                ) 1f else dataListIncome.max().toFloat()
                                            )
                                        }
                                        Column {
                                            Spacer(modifier = Modifier.padding(12.dp))
                                            Box(
                                                modifier = Modifier.fillMaxWidth(),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(buildAnnotatedString {
                                                    append("Прибыль составила ")
                                                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                                        append((dataListIncome[1] - dataListIncome[0]).toString())
                                                    }
                                                    append(" р.")
                                                })
                                            }
                                            Spacer(modifier = Modifier.padding(12.dp))
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .fillMaxHeight()
                                                    .padding(start = 8.dp, end = 8.dp),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                BarGraph(
                                                    graphBarData = floatValue,
                                                    xAxisScaleData = xList,
                                                    barData_ = dataListIncome,
                                                    height = 250.dp,
                                                    roundType = BarType.TOP_CURVED,
                                                    barWidth = 40.dp,
                                                    barColor = PrimaryBack,
                                                    barArrangement = Arrangement.SpaceEvenly,
                                                    false
                                                )
                                            }
                                        }
                                    } else if (dataListIncomeGroup.isNotEmpty()) {
                                        val floatValue = mutableListOf<Float>()
                                        val xList = mutableListOf("Расходы", "Доход")

                                        dataListIncomeGroup.forEachIndexed { index, value ->

                                            floatValue.add(
                                                index = index,
                                                element = value.toFloat() / if (dataListIncomeGroup.max()
                                                        .toFloat() == 0f
                                                ) 1f else dataListIncomeGroup.max().toFloat()
                                            )
                                        }
                                        Column {
                                            Spacer(modifier = Modifier.padding(12.dp))
                                            Box(
                                                modifier = Modifier.fillMaxWidth(),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(buildAnnotatedString {
                                                    append("Прибыль составила ")
                                                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                                        append((dataListIncomeGroup[1] - dataListIncomeGroup[0]).toString())
                                                    }
                                                    append(" р.")
                                                })
                                            }
                                            Spacer(modifier = Modifier.padding(12.dp))
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .fillMaxHeight()
                                                    .padding(start = 8.dp, end = 8.dp),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                BarGraph(
                                                    graphBarData = floatValue,
                                                    xAxisScaleData = xList,
                                                    barData_ = dataListIncomeGroup,
                                                    height = 250.dp,
                                                    roundType = BarType.TOP_CURVED,
                                                    barWidth = 40.dp,
                                                    barColor = PrimaryBack,
                                                    barArrangement = Arrangement.SpaceEvenly,
                                                    false
                                                )
                                            }
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
                    val configuration = LocalConfiguration.current
                    val width = configuration.screenWidthDp.dp
                    if (width < border) {
                        Text(text = "ОТЧЕТЫ", fontSize = sizeForSmallDevices, color = Color.White)
                    } else {
                        Text(text = "ОТЧЕТЫ", fontSize = 24.sp, color = Color.White)
                    }
                }
            }
        }
    }
}