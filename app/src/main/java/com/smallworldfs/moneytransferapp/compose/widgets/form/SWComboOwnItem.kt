package com.smallworldfs.moneytransferapp.compose.widgets.form

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.ScreenName
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
fun SWComboOwnItem(
    field: Field,
    onFieldUpdated: FieldAction,
    onFieldCLicked: FieldAction,
    sendAnalytics: (String) -> Unit
) {

    val hint = "${field.placeholder} ${if (field.isRequired) STRING_EMPTY else stringResource(id = R.string.optional_tag)}"

    val error = field.getError()

    var textValue by rememberSaveable { mutableStateOf(field.value ?: STRING_EMPTY) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable {
            onFieldCLicked(field)
            sendAnalytics(getAnalyticsScreenName(field.name))
        }
    ) {

        Column {

            SWTextField(
                onValueChange = {
                    textValue = it
                    field.value = it
                    onFieldUpdated(field)
                },
                value = textValue,
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { this.contentDescription = field.name },
                label = { Text(hint) },
                colors = TextFieldDefaults.textFieldColors(
                    cursorColor = blueAccentColor,
                    focusedLabelColor = blueAccentColor
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                readOnly = true
            )

            if (error.isNotEmpty())
                SWText(
                    text = error,
                    color = colorRedError,
                )
        }

        Icon(
            modifier = Modifier
                .width(24.dp)
                .height(24.dp),
            painter = painterResource(id = R.drawable.account_icn_arrowactivitycard),
            contentDescription = "logo",
            tint = blueAccentColor
        )
    }
}

private fun getAnalyticsScreenName(fieldName: String) =
    when (fieldName) {
        "mtn_purpose" -> ScreenName.TRANSACTION_PURPOSE_SCREEN.value
        "clientRelation" -> ScreenName.RELATIONSHIP_WITH_BENEFICIARY_SCREEN.value
        "state" -> ScreenName.REGION_SCREEN.value
        "paymentMethod" -> ScreenName.PAYMENT_METHOD_SCREEN.value
        "sourceoffunds" -> ScreenName.SOURCE_FUNDS_SCREEN.value
        "ocupation" -> ScreenName.OCCUPATION_SCREEN.value
        else -> STRING_EMPTY
    }

@Preview(showBackground = true, name = "Group phone item preview", widthDp = 420)
@Composable
fun SWComboOwnItemPreview() {
    SWComboOwnItem(Field(Type.GROUP, SubType.GROUP_DATE, "5477548757"), {}, {}, {})
}
