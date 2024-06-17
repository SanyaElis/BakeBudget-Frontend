package ru.vsu.csf.bakebudget.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.vsu.csf.bakebudget.ui.theme.UnfocusedField

@Composable
fun InputTextField(
    placeholder: String,
    text: MutableState<String>, max: Int
) {
    val focusManager = LocalFocusManager.current
    OutlinedTextField(
        value = text.value,
        onValueChange = { if (it.length <= max) text.value = it },
        shape = RoundedCornerShape(16.dp),
        singleLine = true,
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
            .onKeyEvent {
                if (it.key == Key.Enter){
                    focusManager.clearFocus()
                }
                false
            }
    )
}

@Composable
fun InputTextField(
    placeholder: String,
    text: MutableState<String>, max: Int,
    width: Int
) {
    val focusManager = LocalFocusManager.current
    OutlinedTextField(
        value = text.value,
        onValueChange = { if (it.length <= max) text.value = it },
        shape = RoundedCornerShape(16.dp),
        singleLine = true,
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
            .onKeyEvent {
                if (it.key == Key.Enter){
                    focusManager.clearFocus()
                }
                false
            }
    )
}

@Composable
fun InputTextField(
    placeholder: String,
    text: MutableState<String>, max: Int,
    width: Boolean
) {
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        value = text.value,
        onValueChange = { if (it.length <= max) text.value = it },
        shape = RoundedCornerShape(16.dp),
        singleLine = true,
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
            .onKeyEvent {
                if (it.key == Key.Enter){
                    focusManager.clearFocus()
                }
                false
            }
    )
}

@Composable
fun InputTextFieldGroup(
    placeholder: String,
    text: MutableState<String>, max: Int,
    width: Boolean
) {
    val focusManager = LocalFocusManager.current

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
        singleLine = true,
        maxLines = 1,
        modifier = Modifier
            .padding(start = 8.dp, end = 8.dp)
            .fillMaxWidth()
            .onKeyEvent {
                if (it.key == Key.Enter){
                    focusManager.clearFocus()
                }
                false
            }
    )
}

@Composable
fun InputTextFieldSmall(
    placeholder: String,
    text: MutableState<String>, max: Int,
    width: Int
) {
    val focusManager = LocalFocusManager.current
    OutlinedTextField(
        value = text.value,
        onValueChange = { if (it.length <= max) text.value = it },
        shape = RoundedCornerShape(16.dp),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            focusedBorderColor = Color.White,
            unfocusedContainerColor = UnfocusedField,
            unfocusedBorderColor = UnfocusedField,
            focusedLabelColor = Color.White,
        ),
        label = { Text(placeholder, fontSize = 10.sp) },
        maxLines = 1,
        modifier = Modifier
            .requiredWidth(width.dp)
            .requiredHeight(50.dp)
            .onKeyEvent {
                if (it.key == Key.Enter){
                    focusManager.clearFocus()
                }
                false
            }
    )
}