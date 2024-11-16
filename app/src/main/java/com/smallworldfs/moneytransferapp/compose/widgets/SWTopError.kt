package com.smallworldfs.moneytransferapp.compose.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.compose.colors.colorRedError
import com.smallworldfs.moneytransferapp.compose.colors.neutral0

@Composable
fun SWTopError(
    title: String = stringResource(id = R.string.generic_error_view_text),
    body: String = stringResource(id = R.string.generic_error_view_subtitle),
    onCloseIconClick: () -> Unit = {}
) {

    Row(
        modifier = Modifier
            .background(colorRedError)
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            SWText(
                text = title,
                color = neutral0,
                fontWeight = FontWeight.Bold
            )
            SWText(
                text = body,
                color = neutral0
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Column(
            modifier = Modifier
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                Icons.Default.Clear,
                contentDescription = "close",
                modifier = Modifier
                    .clickable {
                        onCloseIconClick()
                    }
                    .padding(
                        end = 16.dp
                    ),
                tint = neutral0
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 420)
@Composable
fun SWTopErrorPreview() {
    SWTopError()
}
