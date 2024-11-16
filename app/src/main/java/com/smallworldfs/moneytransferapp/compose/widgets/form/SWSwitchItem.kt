package com.smallworldfs.moneytransferapp.compose.widgets.form

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.smallworldfs.moneytransferapp.compose.widgets.SWTitleDescriptionSwitch
import com.smallworldfs.moneytransferapp.domain.migrated.base.FieldAction
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Field
import com.smallworldfs.moneytransferapp.utils.forms.SubType
import com.smallworldfs.moneytransferapp.utils.forms.Type

@Composable
fun SWSwitchItem(
    field: Field,
    onFieldUpdated: FieldAction
) {

    var isChecked by rememberSaveable { mutableStateOf(field.value?.toBoolean() ?: false) }

    SWTitleDescriptionSwitch(
        Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        text = field.title,
        contentDescription = field.name,
        isChecked = isChecked,
        listener = { checked ->
            isChecked = checked
            field.value = isChecked.toString()
            onFieldUpdated(field)
        },
        isSwitchAlignedOnStart = true,
    )
}
@Preview(showBackground = true, name = "Switch preview", widthDp = 420)
@Composable
fun SWSwitchItemPreview() {
    SWSwitchItem(Field(Type.GROUP, SubType.GROUP_DATE, "5477548757"), {})
}
