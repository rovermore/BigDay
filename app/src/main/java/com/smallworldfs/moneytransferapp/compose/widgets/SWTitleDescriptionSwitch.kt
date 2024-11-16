package com.smallworldfs.moneytransferapp.compose.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smallworldfs.moneytransferapp.compose.colors.blueAccentColor
import com.smallworldfs.moneytransferapp.compose.colors.darkGreyText
import com.smallworldfs.moneytransferapp.compose.colors.defaultGreyLightBackground
import com.smallworldfs.moneytransferapp.compose.colors.lightGreyText
import com.smallworldfs.moneytransferapp.compose.colors.secondaryDarkGreyText
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY

val headingTextSize = 16.sp
val defaultTextSize = 14.sp

@Composable
fun SWTitleDescriptionSwitch(
    modifier: Modifier = Modifier,
    text: String,
    description: String = STRING_EMPTY,
    contentDescription: String = STRING_EMPTY,
    isChecked: Boolean,
    listener: (isChecked: Boolean) -> Unit,
    isSwitchAlignedOnStart: Boolean = false
) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.SpaceBetween) {
        if (isSwitchAlignedOnStart) {
            SwitchSW(isChecked, listener, contentDescription)
            Spacer(modifier = Modifier.width(10.dp))
        }
        Column(
            modifier = Modifier
                .fillMaxWidth(if (isSwitchAlignedOnStart) 1f else 0.8f)
                .align(Alignment.CenterVertically),
        ) {
            SWText(text = text, color = darkGreyText, fontSize = headingTextSize, fontWeight = FontWeight.SemiBold)

            if (description.isNotEmpty()) {
                SWText(modifier = Modifier.padding(top = 8.dp), text = description, color = lightGreyText, fontSize = defaultTextSize, fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Start)
            }
        }

        if (!isSwitchAlignedOnStart) {
            SwitchSW(isChecked, listener, contentDescription)
        }
    }
}

@Composable
fun SwitchSW(
    isChecked: Boolean,
    listener: (isChecked: Boolean) -> Unit,
    contentDescription: String
) {
    Switch(
        modifier = Modifier.semantics { this.contentDescription = contentDescription },
        checked = isChecked,
        onCheckedChange = {
            listener(it)
        },
        colors = SwitchDefaults.colors(
            checkedThumbColor = blueAccentColor,
            uncheckedThumbColor = defaultGreyLightBackground,
            checkedTrackColor = blueAccentColor.copy(alpha = 0.1f),
            uncheckedTrackColor = secondaryDarkGreyText.copy(alpha = 0.2f),
        ),
    )
}

@Preview(showBackground = true, widthDp = 420)
@Composable
fun SWTitleDescriptionSwitchPreview() {
    SWTitleDescriptionSwitch(
        text = "Text",
        description = "Description",
        isChecked = true,
        listener = {}
    )
}
