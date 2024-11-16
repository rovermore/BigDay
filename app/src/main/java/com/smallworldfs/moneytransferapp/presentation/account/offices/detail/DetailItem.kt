package com.smallworldfs.moneytransferapp.presentation.account.offices.detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smallworldfs.moneytransferapp.compose.colors.blueAccentColor
import com.smallworldfs.moneytransferapp.compose.colors.lightGreyText
import com.smallworldfs.moneytransferapp.compose.widgets.SWText

@Composable
fun DetailItem(key: String, value: String, isClickable: Boolean, onClicked: (String) -> Unit) {
    Row(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SWText(
            text = key,
            color = lightGreyText,
            fontSize = 16.sp,
        )

        Spacer(modifier = Modifier.weight(1F))

        if (isClickable) {
            SWText(
                text = value,
                color = blueAccentColor,
                fontSize = 16.sp,
                modifier = Modifier.clickable { onClicked(value) }
            )
        } else {
            SWText(
                text = value,
                color = lightGreyText,
                fontSize = 16.sp,
            )
        }
    }
}
