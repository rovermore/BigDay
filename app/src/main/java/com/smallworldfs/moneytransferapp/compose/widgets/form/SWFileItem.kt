package com.smallworldfs.moneytransferapp.compose.widgets.form

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.smallworldfs.moneytransferapp.compose.widgets.SWFileUpload
import com.smallworldfs.moneytransferapp.domain.migrated.base.FieldAction
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Field
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import com.smallworldfs.moneytransferapp.utils.forms.SubType
import com.smallworldfs.moneytransferapp.utils.forms.Type

@Composable
fun SWFileItem(
    field: Field,
    onFieldClicked: FieldAction
) {
    SWFileUpload(
        name = field.name ?: STRING_EMPTY,
        errorMessage = field.errorMessage ?: STRING_EMPTY,
        value = field.value ?: STRING_EMPTY,
        placeholder = field.placeholder ?: STRING_EMPTY
    ) {
        onFieldClicked(field)
    }
}

@Preview(showBackground = true, widthDp = 420)
@Composable
fun SWFileItemPreview() {
    SWFileItem(Field(Type.GROUP, SubType.GROUP_DATE, "5477548757")) {}
}
