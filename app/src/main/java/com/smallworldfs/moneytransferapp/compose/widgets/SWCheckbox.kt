package com.smallworldfs.moneytransferapp.compose.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY

@Composable
fun SWCheckbox(
    modifier: Modifier = Modifier,
    text: String = STRING_EMPTY,
    description: String = STRING_EMPTY,
    isChecked: Boolean = false,
    listener: (isChecked: Boolean) -> Unit
) {

    Row(
        modifier = modifier.semantics { this.contentDescription = description },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        SWText(modifier = Modifier.padding(start = 8.dp), text = text)

        Checkbox(
            checked = isChecked,
            onCheckedChange = { listener(it) },
        )
    }
}

@Preview(showBackground = true, name = "Group phone item preview", widthDp = 420)
@Composable
fun SWCheckboxPreview() {
    SWCheckbox(text = "text", isChecked = true) {}
}
