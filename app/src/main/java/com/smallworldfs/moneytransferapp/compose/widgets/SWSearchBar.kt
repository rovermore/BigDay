package com.smallworldfs.moneytransferapp.compose.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import com.smallworldfs.moneytransferapp.compose.colors.defaultGreyControl

@Composable
fun SWSearchBar(
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit,
    backgroundColor: Color = defaultGreyControl
) {
    var textValue by rememberSaveable { mutableStateOf(STRING_EMPTY) }

    LaunchedEffect(textValue) {
        if (textValue.isEmpty())
            onValueChange(textValue)
    }

    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SWTextField(
                value = textValue,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(stringResource(id = R.string.search_hint_text)) },
                onValueChange = { text ->
                    if (text != textValue)
                        textValue = text
                    onValueChange(text)
                },
                trailingIcon = {
                    Icon(
                        if (textValue.isNotEmpty()) Icons.Default.Clear else Icons.Default.Search,
                        contentDescription = "clear text",
                        modifier = Modifier
                            .clickable {
                                textValue = STRING_EMPTY
                            }
                    )
                },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = backgroundColor
                )
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 420)
@Composable
fun SearchBarPreview() {
    SWSearchBar(
        onValueChange = {}
    )
}
