package com.smallworldfs.moneytransferapp.compose.widgets.form

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.smallworldfs.moneytransferapp.compose.widgets.SWCheckbox
import com.smallworldfs.moneytransferapp.domain.migrated.base.FieldAction
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Field
import com.smallworldfs.moneytransferapp.utils.forms.SubType
import com.smallworldfs.moneytransferapp.utils.forms.Type

@Composable
fun SWCheckBoxItem(
    field: Field,
    updateField: FieldAction
) {

    var isChecked by rememberSaveable { mutableStateOf(field.value?.toBoolean() ?: false) }

    SWCheckbox(
        text = field.title,
        isChecked = isChecked,
        listener = { checked ->
            isChecked = checked
            field.value = isChecked.toString()
            updateField(field)
        },
        description = field.name
    )
}

@Preview(showBackground = true, name = "Group phone item preview", widthDp = 420)
@Composable
fun SWCheckBoxItemPreview() {
    SWCheckBoxItem(Field(Type.GROUP, SubType.GROUP_DATE, "5477548757"), {})
}
