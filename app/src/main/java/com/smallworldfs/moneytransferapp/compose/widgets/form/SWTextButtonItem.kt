package com.smallworldfs.moneytransferapp.compose.widgets.form

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import com.smallworldfs.moneytransferapp.compose.widgets.SWText
import com.smallworldfs.moneytransferapp.domain.migrated.base.FieldAction
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Field
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import com.smallworldfs.moneytransferapp.utils.forms.SubType
import com.smallworldfs.moneytransferapp.utils.forms.Type

@Composable
fun SWTextButtonItem(
    field: Field,
    onFieldClicked: FieldAction
) {

    SWText(
        modifier = Modifier
            .semantics { this.contentDescription = field.name }
            .clickable { onFieldClicked(field) },
        text = field.value ?: STRING_EMPTY,
    )
}
@Preview(showBackground = true, name = "Text Button item preview", widthDp = 420)
@Composable
fun SWTextButtonItemPreview() {
    SWTextButtonItem(Field(Type.GROUP, SubType.GROUP_DATE, "5477548757")) {}
}
