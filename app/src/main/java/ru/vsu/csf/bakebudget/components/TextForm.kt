package ru.vsu.csf.bakebudget.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import ru.vsu.csf.bakebudget.ui.theme.PrimaryBack
import ru.vsu.csf.bakebudget.ui.theme.UnfocusedField

@Composable
fun TextForm(label : String, textValue: MutableState<String>, max: Int) {
    val focusManager = LocalFocusManager.current

    OutlinedTextField(modifier = Modifier
        .fillMaxWidth(0.75f)
        .background(PrimaryBack)
        .padding(10.dp)
//        .requiredHeight(65.dp)
        .onKeyEvent {
            if (it.key == Key.Enter){
                focusManager.clearFocus()
            }
            false
        },
        value = textValue.value,
        label = { Text(text = label)},
        singleLine = true,
        supportingText = { Text(text = "От 3 до 50 символов", fontSize = 10.sp)},
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            focusedBorderColor = Color.White,
            unfocusedContainerColor = UnfocusedField,
            unfocusedBorderColor = UnfocusedField,
            focusedLabelColor = Color.White,
        ),
        keyboardOptions = KeyboardOptions.Default,
        shape = RoundedCornerShape(8.dp),
        onValueChange = { if (it.length <= max) textValue.value = it })
}

@Composable
fun TextFormEmail(label : String, textValue: MutableState<String>, max: Int) {
    val focusManager = LocalFocusManager.current

    OutlinedTextField(modifier = Modifier
        .fillMaxWidth(0.75f)
        .background(PrimaryBack)
        .padding(10.dp)
        .requiredHeight(65.dp)
        .onKeyEvent {
            if (it.key == Key.Enter){
                focusManager.clearFocus()
            }
            false
        },
        value = textValue.value,
        label = { Text(text = label)},
        singleLine = true,
//        supportingText = { Text(text = "От 3 до 50 символов", fontSize = 10.sp)},
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            focusedBorderColor = Color.White,
            unfocusedContainerColor = UnfocusedField,
            unfocusedBorderColor = UnfocusedField,
            focusedLabelColor = Color.White,
        ),
        keyboardOptions = KeyboardOptions.Default,
        shape = RoundedCornerShape(8.dp),
        onValueChange = { if (it.length <= max) textValue.value = it })
}