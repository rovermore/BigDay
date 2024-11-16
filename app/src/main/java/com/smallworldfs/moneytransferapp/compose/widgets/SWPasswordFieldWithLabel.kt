package com.smallworldfs.moneytransferapp.compose.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.compose.colors.blueAccentColor
import com.smallworldfs.moneytransferapp.compose.colors.darkGreyText
import com.smallworldfs.moneytransferapp.compose.colors.neutral0

@Composable
fun SWPasswordFieldWithLabel(text: String, onValueChanged: (String) -> Unit) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    SWTextField(
        value = text,
        modifier = Modifier
            .padding(top = 10.dp)
            .background(neutral0)
            .fillMaxWidth(),
        onValueChange = {
            onValueChanged(it)
        },
        label = { Text(text = stringResource(id = R.string.password), fontSize = 14.sp) },
        trailingIcon = {
            val image = if (passwordVisible)
                Icons.Filled.VisibilityOff
            else Icons.Filled.Visibility

            val description = if (passwordVisible) "Hide password" else "Show password"

            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(imageVector = image, description, tint = if (passwordVisible) blueAccentColor else darkGreyText)
            }
        },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = neutral0,
            cursorColor = blueAccentColor,
            focusedIndicatorColor = blueAccentColor,
            focusedLabelColor = blueAccentColor,
            unfocusedLabelColor = darkGreyText,
        ),
        textStyle = TextStyle.Default.copy(fontSize = 16.sp),
        singleLine = true,
    )
}

@Preview(showBackground = true, widthDp = 420)
@Composable
fun SWPasswordFieldWithLabelPreview() {
    SWPasswordFieldWithLabel("", {})
}
