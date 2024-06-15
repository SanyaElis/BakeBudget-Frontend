package ru.vsu.csf.bakebudget.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ru.vsu.csf.bakebudget.ui.theme.UnfocusedField

@Composable
fun InputTextField(
    placeholder: String,
    text: MutableState<String>, max: Int
) {
    OutlinedTextField(
        value = text.value,
        onValueChange = { if (it.length <= max) text.value = it },
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            focusedBorderColor = Color.White,
            unfocusedContainerColor = UnfocusedField,
            unfocusedBorderColor = UnfocusedField,
            focusedLabelColor = Color.White,
        ),
        label = { Text(placeholder) },
        maxLines = 1,
        modifier = Modifier
            .padding(5.dp)
            .requiredWidth(120.dp)
    )
}

@Composable
fun InputTextField(
    placeholder: String,
    text: MutableState<String>, max: Int,
    width: Int
) {
    OutlinedTextField(
        value = text.value,
        onValueChange = { if (it.length <= max) text.value = it },
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            focusedBorderColor = Color.White,
            unfocusedContainerColor = UnfocusedField,
            unfocusedBorderColor = UnfocusedField,
            focusedLabelColor = Color.White,
        ),
        label = { Text(placeholder) },
        maxLines = 1,
        modifier = Modifier
            .padding(3.dp)
            .requiredWidth(width.dp)
    )
}

@Composable
fun InputTextField(
    placeholder: String,
    text: MutableState<String>, max: Int,
    width: Boolean
) {
    OutlinedTextField(
        value = text.value,
        onValueChange = { if (it.length <= max) text.value = it },
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            focusedBorderColor = Color.White,
            unfocusedContainerColor = UnfocusedField,
            unfocusedBorderColor = UnfocusedField,
            focusedLabelColor = Color.White,
        ),
        label = { Text(placeholder) },
        maxLines = 1,
        modifier = Modifier
            .padding(3.dp, end = 8.dp)
            .fillMaxWidth()
    )
}

@Composable
fun InputTextFieldGroup(
    placeholder: String,
    text: MutableState<String>, max: Int,
    width: Boolean
) {
    OutlinedTextField(
        value = text.value,
        onValueChange = { if (it.length <= max) text.value = it },
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            focusedBorderColor = Color.White,
            unfocusedContainerColor = UnfocusedField,
            unfocusedBorderColor = UnfocusedField,
            focusedLabelColor = Color.White,
        ),
        label = { Text(placeholder) },
        maxLines = 1,
        modifier = Modifier
            .padding(start = 8.dp, end = 8.dp)
            .fillMaxWidth()
    )
}