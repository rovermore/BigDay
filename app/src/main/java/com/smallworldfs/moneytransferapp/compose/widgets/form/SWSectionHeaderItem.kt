package com.smallworldfs.moneytransferapp.compose.widgets.form

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.compose.widgets.SWSectionHeader
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Field
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import com.smallworldfs.moneytransferapp.utils.forms.SubType
import com.smallworldfs.moneytransferapp.utils.forms.Type

@Composable
fun SWSectionHeaderItem(
    field: Field
) {

    val value = field.value ?: STRING_EMPTY

    val label = field.name ?: STRING_EMPTY

    val drawableId = when (field.subtype) {
        SubType.SECTION_HEADER_PROFILE -> R.drawable.hardregister_icn_profile
        SubType.SECTION_HEADER_ADDRESS -> R.drawable.hardregister_icn_address
        else -> R.drawable.hardregister_icn_profile
    }

    SWSectionHeader(
        value = value,
        label = label,
        drawableId = drawableId
    )
}
@Preview(showBackground = true, name = "Group phone item preview", widthDp = 420)
@Composable
fun SWSectionHeaderItemPreview() {
    SWSectionHeaderItem(Field(Type.GROUP, SubType.GROUP_DATE, "5477548757"))
}
