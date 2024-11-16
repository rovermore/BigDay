package com.smallworldfs.moneytransferapp.compose.widgets

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.smallworldfs.moneytransferapp.compose.colors.colorBlueOcean
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY

@Composable
fun SWTextField(
    modifier: Modifier = Modifier,
    value: String = STRING_EMPTY,
    onValueChange: (String) -> Unit,
    placeholder: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    singleLine: Boolean = false,
    colors: TextFieldColors = TextFieldDefaults.textFieldColors(cursorColor = colorBlueOcean, focusedIndicatorColor = colorBlueOcean),
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    label: @Composable (() -> Unit)? = null,
    readOnly: Boolean = false,
    textStyle: TextStyle = TextStyle.Default
) {

    TextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        placeholder = placeholder,
        trailingIcon = trailingIcon,
        singleLine = singleLine,
        colors = colors,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        label = label,
        readOnly = readOnly,
        textStyle = textStyle,
    )
}

@Preview(showBackground = true, name = "Select Destination Country preview", widthDp = 420)
@Composable
fun SWTextFieldPreview() {
    SWTextField(
        value = "Text",
        onValueChange = {},
        trailingIcon = {}
    )
}
