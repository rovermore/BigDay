package com.smallworldfs.moneytransferapp.compose.widgets.form

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.compose.widgets.SWPhoneNumber
import com.smallworldfs.moneytransferapp.domain.migrated.base.FieldAction
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Field
import com.smallworldfs.moneytransferapp.presentation.form.adapter.extractCountryPhonePrefix
import com.smallworldfs.moneytransferapp.utils.INT_ONE
import com.smallworldfs.moneytransferapp.utils.INT_ZERO
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import com.smallworldfs.moneytransferapp.utils.forms.SubType
import com.smallworldfs.moneytransferapp.utils.forms.Type
import java.util.TreeMap

@Composable
fun SWGroupPhoneItem(
    field: Field,
    onFieldUpdate: FieldAction,
    onFieldClicked: FieldAction
) {

    val hint = "${field.placeholder} ${if (field.isRequired) STRING_EMPTY else stringResource(id = R.string.optional_tag)}"

    var selectedCountryValue = "GBR"
    if (field.childs != null && field.childs.size > INT_ONE) {
        selectedCountryValue =
            if (field.value != null)
                getCountryById(field.data, field.value)?.value ?: field.childs[INT_ZERO].value
            else
                field.childs[INT_ZERO].value
    }

    val value = field.childs?.get(INT_ONE)?.value

    val prefix = field.childs?.get(INT_ZERO)?.let { it.data?.let { it1 -> extractCountryPhonePrefix(it1, selectedCountryValue) } } ?: STRING_EMPTY

    val errorText = field.getError()

    val onTextChanged: (String) -> Unit = {
        field.childs?.let { list ->
            list[INT_ONE].value = it
        }
        onFieldUpdate(field)
    }

    SWPhoneNumber(
        onItemClicked = { onFieldClicked(field) },
        hint = hint,
        errorText = errorText,
        value = value ?: STRING_EMPTY,
        contentDescription = field.name,
        selectedCountryValue = selectedCountryValue,
        prefix = prefix,
        onTextChanged = onTextChanged
    )
}

private fun getCountryById(countries: ArrayList<TreeMap<String?, String>>, id: String): Map.Entry<String?, String>? {
    countries.forEach {
        val countryData = it.firstEntry()
        if (countryData?.key != null && countryData.key == id) {
            return countryData
        }
    }
    return null
}

@Preview(showBackground = true, name = "Group phone item preview", widthDp = 420)
@Composable
fun SWGroupPhoneItemPreview() {
    SWGroupPhoneItem(Field(Type.GROUP, SubType.GROUP_DATE, "5477548757"), {}, {})
}
