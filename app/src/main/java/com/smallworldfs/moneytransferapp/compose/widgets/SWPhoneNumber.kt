package com.smallworldfs.moneytransferapp.compose.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.compose.colors.blueAccentColor
import com.smallworldfs.moneytransferapp.compose.colors.colorRedError
import com.smallworldfs.moneytransferapp.domain.migrated.base.Action
import com.smallworldfs.moneytransferapp.utils.Constants
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY

@Composable
fun SWPhoneNumber(
    onItemClicked: Action,
    hint: String = STRING_EMPTY,
    errorText: String = STRING_EMPTY,
    value: String = STRING_EMPTY,
    contentDescription: String = STRING_EMPTY,
    selectedCountryValue: String = STRING_EMPTY,
    prefix: String = STRING_EMPTY,
    onTextChanged: (String) -> Unit = {}
) {

    var textValue by rememberSaveable {
        mutableStateOf(value)
    }

    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .clickable {
                    onItemClicked()
                },
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                SWImageFlag(
                    modifier = Modifier
                        .width(24.dp)
                        .height(24.dp),
                    imageUrl = Constants.COUNTRY.FLAG_IMAGE_ASSETS + selectedCountryValue +
                        Constants.COUNTRY.FLAG_IMAGE_EXTENSION,
                )

                SWText(
                    modifier = Modifier
                        .semantics { this.contentDescription = "prefix_number_txt" },
                    text = prefix,
                )

                Icon(
                    modifier = Modifier
                        .width(24.dp)
                        .height(24.dp),
                    painter = painterResource(id = R.drawable.account_icn_arrowactivitycard),
                    contentDescription = "logo",
                    tint = blueAccentColor,
                )
            }
        }

        Box {

            SWTextField(
                onValueChange = {
                    textValue = it
                    onTextChanged(it)
                },
                value = textValue,
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { this.contentDescription = contentDescription },
                label = { Text(hint) },
                colors = TextFieldDefaults.textFieldColors(
                    cursorColor = blueAccentColor,
                    focusedLabelColor = blueAccentColor,
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),

            )

            if (errorText.isNotEmpty())
                SWText(
                    text = errorText,
                    color = colorRedError,
                )
        }
    }
}

@Preview(showBackground = true, name = "Group phone item preview", widthDp = 420)
@Composable
fun SWPhoneNumberPreview() {
    SWPhoneNumber({})
}
