package ru.vsu.csf.bakebudget.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Divider
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import ru.vsu.csf.bakebudget.R
import ru.vsu.csf.bakebudget.models.OrderModel
import ru.vsu.csf.bakebudget.ui.theme.SideBack
import ru.vsu.csf.bakebudget.ui.theme.TextPrimary

@Composable
fun OrderStateRow(stateName: String,
                  stateOpen: MutableState<Boolean>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(SideBack)
    ) {
        Row(modifier = Modifier.padding(start = 16.dp, top = 15.dp, bottom = 8.dp)) {
            Text(
                text = stateName,
                modifier = Modifier.padding(end = 6.dp),
                fontSize = 12.sp,
                color = TextPrimary
            )
            Divider(
                color = TextPrimary, thickness = 1.dp, modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .padding(top = 8.dp)
            )
            if (stateOpen.value) {
                IconButton(modifier = Modifier.width(25.dp).height(15.dp), onClick = {
                    stateOpen.value = false
                }) {
                    Image(
                        painter = painterResource(id = R.drawable.button_open),
                        contentDescription = "open"
                    )
                }
            } else {
                IconButton(modifier = Modifier.width(25.dp).height(15.dp), onClick = {
                    stateOpen.value = true
                }) {
                    Image(
                        painter = painterResource(id = R.drawable.button_close),
                        contentDescription = "close"
                    )
                }
            }
        }
    }
}