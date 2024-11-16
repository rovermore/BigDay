package com.smallworldfs.moneytransferapp.compose.widgets.form

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.compose.widgets.SWTextGroup
import com.smallworldfs.moneytransferapp.domain.migrated.base.FieldAction
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Field
import com.smallworldfs.moneytransferapp.utils.INT_ONE
import com.smallworldfs.moneytransferapp.utils.INT_ZERO
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import com.smallworldfs.moneytransferapp.utils.forms.SubType
import com.smallworldfs.moneytransferapp.utils.forms.Type
import java.util.TreeMap

@Composable
fun SWGroupTextItem(
    field: Field,
    onUpdateField: FieldAction,
    onFieldClicked: FieldAction
) {

    val selectorValue = if (!field.childs[INT_ZERO].value.isNullOrEmpty()) {
        findValueFromKey(field.childs[INT_ZERO].data, field.childs[INT_ZERO].value)
    } else {
        field.childs[INT_ZERO].data[INT_ZERO]?.firstEntry()?.value ?: STRING_EMPTY
    }

    val hideSelector = selectorValue.isNullOrEmpty()

    val hint = "${field.placeholder} ${if (field.isRequired) STRING_EMPTY else stringResource(id = R.string.optional_tag)}"

    val label = field.name ?: STRING_EMPTY

    var textValue by rememberSaveable { mutableStateOf(field.childs[INT_ONE].value) }

    val onTextChanged: (String) -> Unit = {
        field.childs?.let { list ->
            list[INT_ONE].value = it
        }
        textValue = it
        onUpdateField(field)
    }

    SWTextGroup(
        onItemClicked = { onFieldClicked(field) },
        hint = hint,
        text = textValue,
        selectorValue = selectorValue,
        label = label,
        onTextChanged = onTextChanged,
        hideSelector = hideSelector
    )
}

/**
 * Check if the key give as a param exist in the list, then return the value, if not: return the key
 */
private fun findValueFromKey(data: ArrayList<TreeMap<String, String>>, key: String = STRING_EMPTY): String {
    val entry = data.firstOrNull { it.firstKey() == key }
    return if (entry != null) entry[key] ?: key else key
}

@Preview(showBackground = true, name = "Group phone item preview", widthDp = 420)
@Composable
fun SWGroupTextItemPreview() {
    SWGroupTextItem(Field(Type.GROUP, SubType.GROUP_DATE, "5477548757"), {}, {})
}
