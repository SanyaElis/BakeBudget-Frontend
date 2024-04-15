package ru.vsu.csf.bakebudget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Ingredient(ingredient: IngredientModel) {
    Card(shape = RoundedCornerShape(15.dp),
        modifier = Modifier.fillMaxWidth().requiredHeight(50.dp).padding(5.dp)) {
        Row(modifier = Modifier.fillMaxWidth().fillMaxHeight(), horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically) {
            Text(text = ingredient.name)
            Text(text = ingredient.weight.toString())
            Text(text = ingredient.cost.toString())
        }
    }
}