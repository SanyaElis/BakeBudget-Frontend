package ru.vsu.csf.bakebudget.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ru.vsu.csf.bakebudget.models.GoodModel
import ru.vsu.csf.bakebudget.ui.theme.SideBack

@Composable
fun Good(good: GoodModel) {
    Column(modifier = Modifier.fillMaxWidth().background(SideBack).padding(8.dp)) {
        Image(modifier = Modifier
            .clip(RoundedCornerShape(14.dp)),
            contentScale = ContentScale.Fit,
            painter = painterResource(id = good.iconId), contentDescription = good.name)
        Text(text = good.name, modifier = Modifier.padding(start = 8.dp))
    }
}