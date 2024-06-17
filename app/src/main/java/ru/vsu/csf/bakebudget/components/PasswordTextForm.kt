package ru.vsu.csf.bakebudget.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.Visibility
import ru.vsu.csf.bakebudget.ui.theme.PrimaryBack
import ru.vsu.csf.bakebudget.ui.theme.UnfocusedField

@Composable
fun PasswordTextForm(label : String, textValue: MutableState<String>) {
    var passwordVisible = rememberSaveable { mutableStateOf(false) }
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
        label = { Text(text = label) },
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            focusedBorderColor = Color.White,
            unfocusedContainerColor = UnfocusedField,
            unfocusedBorderColor = UnfocusedField,
            focusedLabelColor = Color.White,
        ),
        visualTransformation = if (passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions.Default,
        shape = RoundedCornerShape(8.dp),
        trailingIcon = {
            val image = if (passwordVisible.value)
                Icons.Filled.Visibility
            else Icons.Filled.VisibilityOff

            val description = if (passwordVisible.value) "Hide password" else "Show password"

            IconButton(onClick = {passwordVisible.value = !passwordVisible.value}){
                Icon(imageVector  = image, description)
            }
        },
        onValueChange = {textValue.value = it})
}

