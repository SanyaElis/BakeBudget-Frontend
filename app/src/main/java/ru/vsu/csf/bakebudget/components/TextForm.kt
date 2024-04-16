package ru.vsu.csf.bakebudget.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ru.vsu.csf.bakebudget.ui.theme.PrimaryBack
import ru.vsu.csf.bakebudget.ui.theme.UnfocusedField

@Composable
fun TextForm(label : String) {
    val textValue = remember {
        mutableStateOf("")
    }

    OutlinedTextField(modifier = Modifier
        .fillMaxWidth(0.75f)
        .background(PrimaryBack)
        .padding(10.dp)
        .requiredHeight(60.dp),
        value = textValue.value,
        label = { Text(text = label)},
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            focusedBorderColor = Color.White,
            unfocusedContainerColor = UnfocusedField,
            unfocusedBorderColor = UnfocusedField,
        ),
        keyboardOptions = KeyboardOptions.Default,
        shape = RoundedCornerShape(8.dp),
        onValueChange = {textValue.value = it})
}