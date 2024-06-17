package ru.vsu.csf.bakebudget.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import ru.vsu.csf.bakebudget.ui.theme.PrimaryBack
import ru.vsu.csf.bakebudget.ui.theme.borderH

@Composable
fun IngredientAdd(
    name: MutableState<String>,
    weight: MutableState<String>,
    cost: MutableState<String>) {
    val configuration = LocalConfiguration.current
    val height = configuration.screenHeightDp.dp
    if (height > borderH + 20.dp) {
        Card(modifier = Modifier
            .fillMaxWidth()
            .requiredHeight(90.dp)
            .padding(5.dp)) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(PrimaryBack), horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically) {
                InputTextField(placeholder = "Название", name, 25, 110)
                InputTextField(placeholder = "Вес", weight, 8, 110)
                InputTextField(placeholder = "Цена", cost, 8, 110)
            }
        }
    } else {
        Card(modifier = Modifier
            .fillMaxWidth()
            .requiredHeight(60.dp)
            .padding(5.dp)) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(PrimaryBack), horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically) {
                InputTextFieldSmall(placeholder = "Название", name, 25, 90)
                InputTextFieldSmall(placeholder = "Вес", weight, 8, 90)
                InputTextFieldSmall(placeholder = "Цена", cost, 8, 90)
            }
        }
    }
}