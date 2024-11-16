package com.smallworldfs.moneytransferapp.compose.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.domain.migrated.base.Action

@Composable
fun SWWhiteToolbar(
    onBackAction: Action
) {
    Row {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            Image(
                modifier = Modifier
                    .width(48.dp)
                    .height(48.dp)
                    .padding(start = 16.dp)
                    .clickable {
                        onBackAction()
                    },
                painter = painterResource(id = R.drawable.ic_action_arrow_back_grey),
                contentDescription = "back_button",
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 420)
@Composable
fun SWWhiteToolbarPreview() {
    SWWhiteToolbar(
        onBackAction = {}
    )
}
