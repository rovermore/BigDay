package com.smallworldfs.moneytransferapp.compose.widgets.form

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.smallworldfs.moneytransferapp.compose.widgets.SWRegionPicker
import com.smallworldfs.moneytransferapp.domain.migrated.base.FieldAction
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Field
import com.smallworldfs.moneytransferapp.utils.Constants
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import com.smallworldfs.moneytransferapp.utils.forms.SubType
import com.smallworldfs.moneytransferapp.utils.forms.Type
import java.util.TreeMap

@Composable
fun SWComboCountryItem(
    field: Field,
    onFieldClicked: FieldAction
) {

    var countryName = STRING_EMPTY
    if (field.data != null && field.value != null) {
        for (entry: TreeMap<String, String> in field.data) {
            if (entry.containsKey(field.value)) {
                countryName = entry[field.value].toString()
            }
        }
    }

    val flagUrl: String = if (field.value != null) Constants.COUNTRY.FLAG_IMAGE_ASSETS + field.value + Constants.COUNTRY.FLAG_IMAGE_EXTENSION else STRING_EMPTY

    SWRegionPicker(
        flagUrl = flagUrl,
        text = countryName,
        contentDescription = field.name
    ) {
        onFieldClicked(field)
    }
}

@Preview(showBackground = true, name = "Group phone item preview", widthDp = 420)
@Composable
fun SWComboCountryItemPreview() {
    SWComboCountryItem(Field(Type.GROUP, SubType.GROUP_DATE, "5477548757"), {})
}
