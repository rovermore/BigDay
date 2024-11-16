package com.smallworldfs.moneytransferapp.compose.widgets.form

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
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
fun SWGroupDateItem(
    field: Field,
    onFieldUpdated: FieldAction,
    onFieldClicked: FieldAction
) {

    val hint = "${field.placeholder} ${if (field.isRequired) STRING_EMPTY else stringResource(id = R.string.optional_tag)}"

    val valueToShow = if (!field.value.isNullOrEmpty()) {
        field.value
    } else {
        val dayDate = field.childs?.firstOrNull { child -> child.name == "dayDate" || child.name == "dayExpirationDate" }?.value
        val monthDate = field.childs?.firstOrNull { child -> child.name == "monthDate" || child.name == "monthExpirationDate" }?.value
        val yearDate = field.childs?.firstOrNull { child -> child.name == "yearDate" || child.name == "yearExpirationDate" }?.value

        val dayToShow = try {
            dayDate?.substring(0, 2)
        } catch (e: Exception) {
            dayDate
        }
        if (dayToShow.isNullOrEmpty() || monthDate.isNullOrEmpty() || yearDate.isNullOrEmpty()) {
            STRING_EMPTY
        } else {
            "$dayToShow/$monthDate/$yearDate"
        }
    }

    Column {
        SWTextField(
            onValueChange = {
                field.value = valueToShow
                onFieldUpdated(field)
            },
            value = valueToShow,
            modifier = Modifier
                .fillMaxWidth()
                .semantics { this.contentDescription = field.name }
                .clickable { onFieldClicked(field) },
            label = { Text(hint) },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowForwardIos,
                    contentDescription = null,
                    tint = blueAccentColor
                )
            },
            colors = TextFieldDefaults.textFieldColors(
                cursorColor = blueAccentColor,
                focusedLabelColor = blueAccentColor
            )
        )
        field.getError().let { error ->
            if (error.isNotEmpty())
                SWText(
                    text = error,
                    color = colorRedError,
                )
        }
    }
}

@Preview(showBackground = true, name = "Group date item preview", widthDp = 420)
@Composable
fun SWGroupDateItemPreview() {
    SWGroupDateItem(Field(Type.GROUP, SubType.GROUP_DATE, "today"), {}, {})
}
