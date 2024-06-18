package ru.vsu.csf.bakebudget.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ru.vsu.csf.bakebudget.models.IngredientModel
import ru.vsu.csf.bakebudget.models.ProductModel
import ru.vsu.csf.bakebudget.ui.theme.Back2
import ru.vsu.csf.bakebudget.ui.theme.SideBack

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenuProducts(productsAll: MutableList<ProductModel>, selectedItemIndex: MutableIntState) {
    var expanded by remember { mutableStateOf(false) }
    val list = mutableListOf<String>()
    for (product in productsAll) {
        list.add(product.name)
    }


    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.padding(5.dp),
    ) {
        TextField(
            colors = ExposedDropdownMenuDefaults.textFieldColors(
                unfocusedContainerColor = SideBack,
                focusedContainerColor = Back2
            ),
            value = list[selectedItemIndex.intValue],
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor()
        )

        ExposedDropdownMenu(
            modifier = Modifier.background(SideBack),
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            list.forEachIndexed { index, item ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = item,
                            fontWeight = if (index == selectedItemIndex.intValue) FontWeight.Bold else null
                        )
                    },
                    onClick = {
                        selectedItemIndex.intValue = index
                        expanded = false
                    }
                )
            }
        }
    }
}