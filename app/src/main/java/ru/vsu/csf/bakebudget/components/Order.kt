package ru.vsu.csf.bakebudget.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ru.vsu.csf.bakebudget.models.OrderModel

@Composable
fun Order(order: OrderModel) {
    Box(modifier = Modifier
        .background(Color.Gray)
        .padding(12.dp),
        contentAlignment = Alignment.Center) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = order.product.name)
            Text(text = order.costPrice.toString())
            Text(text = order.finalPrice.toString())
        }
    }
}