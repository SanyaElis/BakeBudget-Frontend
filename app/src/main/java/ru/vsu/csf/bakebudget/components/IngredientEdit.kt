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
import androidx.compose.ui.unit.dp
import ru.vsu.csf.bakebudget.ui.theme.PrimaryBack

@Composable
fun IngredientEdit(
    name: MutableState<String>,
    weight: MutableState<String>,
    cost: MutableState<String>
) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .requiredHeight(90.dp)
        .padding(5.dp)) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(PrimaryBack), horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically) {
            InputTextField(text = "Название", name, 30, true)
            InputTextField(text = "Вес", weight, 30, true)
            InputTextField(text = "Цена", cost, 30, true)
        }
    }
}