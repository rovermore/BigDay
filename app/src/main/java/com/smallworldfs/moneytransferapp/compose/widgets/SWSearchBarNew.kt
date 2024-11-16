package com.smallworldfs.moneytransferapp.compose.widgets

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.compose.colors.defaultGreyLightBackground
import com.smallworldfs.moneytransferapp.compose.colors.info600
import com.smallworldfs.moneytransferapp.compose.colors.neutral0
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY

@Composable
fun SWSearchBarNew(
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit,
    backgroundColor: Color = neutral0,
    clearText: Boolean = false
) {

    val localFocusManager = LocalFocusManager.current
    var textValue by rememberSaveable { mutableStateOf(STRING_EMPTY) }

    LaunchedEffect(textValue) {
        if (textValue.isEmpty())
            onValueChange(textValue)
    }

    LaunchedEffect(clearText) {
        if (clearText) {
            textValue = STRING_EMPTY
        }
    }

    Box(
        modifier = modifier
            .border(
                width = 2.dp,
                color = defaultGreyLightBackground,
                shape = RoundedCornerShape(16.dp),
            )
            .clip(RoundedCornerShape(16.dp)),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            SWTextField(
                value = textValue,
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        localFocusManager.clearFocus()
                    },
                ),
                modifier = Modifier
                    .fillMaxWidth(),
                placeholder = { Text(stringResource(id = R.string.search_hint_text)) },
                onValueChange = { text ->
                    if (text != textValue)
                        textValue = text
                    onValueChange(text)
                },
                trailingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.search_normal_1),
                        contentDescription = "clear text",
                        modifier = Modifier
                            .clickable {
                                textValue = STRING_EMPTY
                            }
                            .size(24.dp),
                        tint = info600,
                    )
                },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = backgroundColor,
                ),
            )
        }
    }
}

@Preview(showBackground = false, widthDp = 420, backgroundColor = 4294967295)
@Composable
fun SWSearchBarNewPreview() {
    SWSearchBarNew(
        onValueChange = {},
    )
}
