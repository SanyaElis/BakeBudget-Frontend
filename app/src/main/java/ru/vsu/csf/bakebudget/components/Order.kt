package ru.vsu.csf.bakebudget.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.vsu.csf.bakebudget.models.OrderModel
import ru.vsu.csf.bakebudget.ui.theme.SideBack
import ru.vsu.csf.bakebudget.ui.theme.TextPrimary

@Composable
fun Order(order: OrderModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(SideBack)
            .padding(8.dp)
    ) {
        Image(
            modifier = Modifier
                .clip(RoundedCornerShape(14.dp)),
            contentScale = ContentScale.Fit,
            painter = painterResource(id = order.product.iconId), contentDescription = order.product.name
        )
        Column {
            Text(modifier = Modifier.padding(start = 8.dp, bottom = 6.dp), text = order.product.name, color = TextPrimary, fontSize = 16.sp)
            Text(modifier = Modifier.padding(start = 8.dp), text = order.finalPrice.toString() + " р.", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Text(modifier = Modifier.padding(start = 10.dp), text = "за " + order.weight.toString() + " г.", fontSize = 12.sp, color = TextPrimary)
        }
    }
}