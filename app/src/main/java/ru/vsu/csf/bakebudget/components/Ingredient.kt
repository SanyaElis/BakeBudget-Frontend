package ru.vsu.csf.bakebudget.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ru.vsu.csf.bakebudget.R
import ru.vsu.csf.bakebudget.models.IngredientModel

@Composable
fun Ingredient(ingredient: IngredientModel, color: Color) {
    Card(
        modifier = Modifier.fillMaxWidth().background(color).padding(5.dp)) {
        Row(modifier = Modifier.fillMaxWidth().defaultMinSize(minHeight = 50.dp).background(color)
            .padding(start = 21.dp, end = 5.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.fillMaxWidth(0.33f),
                contentAlignment = Alignment.CenterStart) {
                Text(text = ingredient.name, maxLines = 3)
            }
            Box(modifier = Modifier.fillMaxWidth(0.5f),
                contentAlignment = Alignment.Center) {
                Text(text = ingredient.weight.toString(), maxLines = 3)
            }
            Box(modifier = Modifier.fillMaxWidth(0.75f),
                contentAlignment = Alignment.CenterEnd) {
                Text(text = ingredient.cost.toString(), maxLines = 3)
            }
            TextButton(
                onClick = {//TODO:edit and delete
                     }){
                Image(
                    painter = painterResource(id = R.drawable.options),
                    contentDescription = "options"
                )
            }
        }
    }
}