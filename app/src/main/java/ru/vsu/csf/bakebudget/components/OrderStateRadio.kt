package ru.vsu.csf.bakebudget.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import ru.vsu.csf.bakebudget.R

@Composable
fun OrderStateRadio(selectedValue: MutableState<Int>) {
    MaterialTheme {
        val items = listOf("НЕ НАЧАТ", "В ПРОЦЕССЕ", "ЗАВЕРШЕН", "ОТМЕНЕН")
        Column(Modifier.padding(start = 8.dp, end = 8.dp, top = 8.dp)) {
            items.forEachIndexed() { idx, item ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.selectable(
                        selected = (selectedValue.value == idx),
                        onClick = { selectedValue.value = idx },
                        role = Role.RadioButton
                    ).padding(8.dp)
                ) {
                    IconToggleButton(
                        checked = selectedValue.value == idx,
                        onCheckedChange = { selectedValue.value = idx },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Image(
                            painter = painterResource(id = if (selectedValue.value == idx) R.drawable.radio_2 else R.drawable.radio_1),
                            contentDescription = item
                        )
                    }
                    Text(
                        text = item,
                        modifier = Modifier.fillMaxWidth().padding(start = 8.dp)
                    )
                }
            }
        }
    }
}