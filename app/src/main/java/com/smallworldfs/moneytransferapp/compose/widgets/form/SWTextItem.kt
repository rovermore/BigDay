package com.smallworldfs.moneytransferapp.compose.widgets.form

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.compose.colors.blueAccentColor
import com.smallworldfs.moneytransferapp.compose.colors.colorRedError
import com.smallworldfs.moneytransferapp.compose.widgets.SWText
import com.smallworldfs.moneytransferapp.compose.widgets.SWTextField
import com.smallworldfs.moneytransferapp.domain.migrated.base.FieldAction
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Field
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import com.smallworldfs.moneytransferapp.utils.forms.SubType
import com.smallworldfs.moneytransferapp.utils.forms.Type

@Composable
fun SWTextItem(
    field: Field,
    onUpdateField: FieldAction
) {

    val hint = "${field.placeholder} ${if (field.isRequired) STRING_EMPTY else stringResource(id = R.string.optional_tag)}"

    val value = field.value ?: STRING_EMPTY

    val label = field.name ?: STRING_EMPTY

    val error = field.getError()

    val keyboardType =
        when (field.type) {
            Type.EMAIL -> KeyboardType.Email
            else ->
                when (field.subtype) {
                    SubType.ALPHA_NUM -> KeyboardType.Text
                    SubType.NUMERIC -> KeyboardType.Number
                    else -> KeyboardType.Text
                }
        }

    var textValue by rememberSaveable { mutableStateOf(value) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column {
            SWTextField(
                onValueChange = {
                    textValue = it
                    field.value = it
                    onUpdateField(field)
                },
                value = textValue,
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { this.contentDescription = label },
                label = { Text(hint) },
                colors = TextFieldDefaults.textFieldColors(
                    cursorColor = blueAccentColor,
                    focusedLabelColor = blueAccentColor,
                ),
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),

            )

            if (error.isNotEmpty())
                SWText(
                    text = error,
                    color = colorRedError,
                )
        }
    }
}

@Preview(showBackground = true, name = "Group phone item preview", widthDp = 420)
@Composable
fun SWTextItemPreview() {
    SWTextItem(Field(Type.GROUP, SubType.GROUP_DATE, "5477548757"), {})
}
