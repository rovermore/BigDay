package com.smallworldfs.moneytransferapp.compose.widgets.form

import android.text.TextUtils
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.smallworldfs.moneytransferapp.domain.migrated.base.FieldAction
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Field
import com.smallworldfs.moneytransferapp.utils.Constants
import com.smallworldfs.moneytransferapp.utils.INT_ZERO
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import com.smallworldfs.moneytransferapp.utils.forms.SubType
import com.smallworldfs.moneytransferapp.utils.forms.Type
import java.lang.Boolean
import kotlin.String
import kotlin.Unit
import kotlin.let

@Composable
fun SWFormLazyColumn(
    modifier: Modifier = Modifier,
    fieldList: List<Field>,
    onItemClicked: FieldAction = {},
    updateFields: (List<Field>, HashMap<String, String>) -> Unit,
    sendAnalytics: (String) -> Unit = {},
) {
    val updateField: (Field) -> Unit = { field ->
        fieldList.find { it.name == field.name }?.let { it.value = field.value }
        updateFields(fieldList, createHashMap(fieldList))
    }

    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
    ) {
        items(items = fieldList, key = { field -> field.name }) { field ->
            when (field.type) {
                Type.GROUP -> {
                    when (field.subtype) {
                        SubType.GROUP_DATE -> SWGroupDateItem(field, updateField, onItemClicked)
                        SubType.GROUP_PHONE -> SWGroupPhoneItem(field, updateField, onItemClicked)
                        SubType.GROUP_TEXT -> SWGroupTextItem(field, updateField, onItemClicked)
                    }
                }

                Type.SECTION_HEADER -> SWSectionHeaderItem(field)
                Type.TEXT -> SWTextItem(field, updateField)
                Type.PASSWORD -> SWPasswordItem(field, updateField)
                Type.EMAIL -> SWTextItem(field, updateField)
                Type.COMBO -> SWComboOwnItem(field, updateField, onItemClicked, sendAnalytics)
                Type.COMBO_COUNTRY -> SWComboCountryItem(field, onItemClicked)
                Type.CHECK_BOX -> SWCheckBoxItem(field, updateField)
                Type.WHITEBOX -> SWWhiteBoxItem(field)
                Type.SWITCH -> SWSwitchItem(field, updateField)
                Type.BUTTON -> {
                    if (field.subtype == SubType.ALPHA_NUM)
                        SWTextButtonItem(field, onItemClicked)
                    else
                        SWButtonItem(field, onItemClicked)
                }

                Type.FILE -> SWFileItem(field, onItemClicked)
            }
        }
    }
}

fun createHashMap(fieldList: List<Field>): HashMap<String, String> {
    val tempList = HashMap<String, String>()

    for (field: Field in fieldList) {
        if (!field.isHidden || field.name == "fallback" || field.name == "documentType") {
            when (field.type) {
                Constants.FIELD_TYPE.TEXT, Constants.FIELD_TYPE.TEXT_AREA, Type.FILE, Type.EMAIL, Type.PASSWORD ->
                    tempList[field.name ?: STRING_EMPTY] = field.value ?: STRING_EMPTY

                Type.COMBO, Type.COMBO_COUNTRY ->
                    if (field.subtype.equals(Constants.FIELD_SUBTYPES.COMBO_API, ignoreCase = true)) {
                        // Try to get keyValue data, and if is not present (Created beneficiary), try to get value from value attr
                        if (!TextUtils.isEmpty(field.name)) {
                            var keyValue = field.keyValue
                            if (TextUtils.isEmpty(keyValue)) {
                                keyValue = field.value
                            }
                            tempList[field.name] = keyValue ?: STRING_EMPTY
                        }
                    } else {
                        val value = if (field.keyValue == null) if (field.value == null) STRING_EMPTY else
                            field.value else field.keyValue
                        tempList[field.name ?: STRING_EMPTY] = value
                    }

                Constants.FIELD_TYPE.GROUP ->
                    if (field.subtype.equals(Constants.FIELD_SUBTYPES.TEXT_GROUP, ignoreCase = true)) {
                        for ((position, fieldAux) in field.childs.withIndex()) {
                            tempList[fieldAux.name ?: STRING_EMPTY] =
                                if (position == INT_ZERO) if (fieldAux.keyValue == null) STRING_EMPTY else
                                    fieldAux.keyValue else if (fieldAux.value == null) STRING_EMPTY else
                                    fieldAux.value
                        }
                    } else {
                        for (fieldAux in field.childs) {
                            tempList[fieldAux.name ?: STRING_EMPTY] = fieldAux.value ?: STRING_EMPTY
                        }
                    }

                Type.CHECK_BOX, Type.SWITCH, Constants.FIELD_TYPE.RADIO_BUTTON ->
                    // Convert String (true, false) to String (0, 1)
                    if (!TextUtils.isEmpty(field.name) && !TextUtils.isEmpty(field.value)) {
                        val value = if (Boolean.parseBoolean(field.value)) "1" else "0"
                        tempList[field.name] = value
                    }
            }
        }
    }
    return tempList
}

@Preview(showBackground = true, name = " Dynamic Form preview", widthDp = 420)
@Composable
fun SWFormLazyColumnPreview() {
    SWFormLazyColumn(modifier = Modifier, emptyList(), {}, { fields: List<Field>, hashMap: HashMap<String, String> -> }) {}
}
