package ru.vsu.csf.bakebudget.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ru.vsu.csf.bakebudget.ui.theme.UnfocusedField

@Composable
fun InputTextField(
    text: String,
    value: MutableState<String>, max: Int
) {
    OutlinedTextField(
        value = value.value,
        onValueChange = { if (value.value.length <= max) value.value = it },
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            focusedBorderColor = Color.White,
            unfocusedContainerColor = UnfocusedField,
            unfocusedBorderColor = UnfocusedField,
            focusedLabelColor = Color.White,
        ),
        label = { Text(text) },
        maxLines = 1,
        modifier = Modifier
            .padding(5.dp)
            .requiredWidth(120.dp)
    )
}

@Composable
fun InputTextField(
    text: String,
    value: MutableState<String>, max: Int,
    width: Int
) {
    OutlinedTextField(
        value = value.value,
        onValueChange = { if (value.value.length <= max) value.value = it },
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            focusedBorderColor = Color.White,
            unfocusedContainerColor = UnfocusedField,
            unfocusedBorderColor = UnfocusedField,
            focusedLabelColor = Color.White,
        ),
        label = { Text(text) },
        maxLines = 1,
        modifier = Modifier
            .padding(5.dp)
            .requiredWidth(width.dp)
    )
}