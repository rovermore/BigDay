package com.smallworldfs.moneytransferapp.compose.widgets.form

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.smallworldfs.moneytransferapp.compose.widgets.SWPasswordInputTextField
import com.smallworldfs.moneytransferapp.compose.widgets.SWPasswordStrengthBar
import com.smallworldfs.moneytransferapp.domain.migrated.base.FieldAction
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Field
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import com.smallworldfs.moneytransferapp.utils.forms.SubType
import com.smallworldfs.moneytransferapp.utils.forms.Type

@Composable
fun SWPasswordItem(
    field: Field,
    onFieldUpdated: FieldAction
) {

    Row(verticalAlignment = Alignment.CenterVertically) {
        Column {
            SWPasswordInputTextField(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                onPasswordInput = {
                    field.value = it
                    onFieldUpdated(field)
                },
            )

            if (field.subtype == SubType.STRONG_PASSWORD)
                SWPasswordStrengthBar(
                    modifier = Modifier
                        .wrapContentHeight()
                        .fillMaxWidth()
                        .padding(
                            top = 8.dp,
                            start = 16.dp,
                            end = 16.dp,
                        ),
                    password = field.value ?: STRING_EMPTY,
                    attributes = field.attributes,
                    auxText = field.attributes.textRequirements.ifEmpty { "Your password must have at least: 8 characters, one uppercase letter, one number and one special character" }
                )
        }
    }
}

@Preview(showBackground = true, widthDp = 420)
@Composable
fun SWPasswordItemPreview() {
    SWPasswordItem(Field(Type.GROUP, SubType.GROUP_DATE, "5477548757"), {})
}
