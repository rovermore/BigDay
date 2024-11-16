package com.smallworldfs.moneytransferapp.compose.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.compose.colors.neutral0

@Composable
fun SWPasswordInputTextField(
    modifier: Modifier,
    onPasswordInput: (String) -> Unit
) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var passwordText by rememberSaveable { mutableStateOf("") }

    Row(
        modifier = modifier,
    ) {
        SWTextField(
            value = passwordText,
            modifier = Modifier
                .background(neutral0)
                .fillMaxWidth()
                .padding(
                    top = 32.dp,
                    start = 16.dp,
                    end = 16.dp,
                ),
            onValueChange = {
                passwordText = it
                onPasswordInput(it)
            },
            singleLine = true,
            placeholder = { Text(stringResource(id = R.string.password)) },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = neutral0,
            ),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = if (passwordVisible) "Hide password" else "Show password"
                    )
                }
            }
        )
    }
}

@Preview(showBackground = true, widthDp = 420)
@Composable
fun SWPasswordInputPreview() {
    SWPasswordInputTextField(
        modifier = Modifier,
        onPasswordInput = {}
    )
}
