package com.smallworldfs.moneytransferapp.compose.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smallworldfs.moneytransferapp.compose.colors.black
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY

@Composable
fun SWSectionHeader(
    value: String = STRING_EMPTY,
    label: String = STRING_EMPTY,
    drawableId: Int
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.semantics { this.contentDescription = label },
    ) {
        Image(
            modifier = Modifier
                .width(24.dp)
                .height(24.dp),
            painter = painterResource(id = drawableId),
            contentDescription = "logo",
        )

        SWText(
            text = value,
            fontSize = 16.sp,
            color = black,
            modifier = Modifier.padding(all = 16.dp),
        )
    }
}

@Preview(showBackground = true, name = "Group phone item preview", widthDp = 420)
@Composable
fun SWSectionHeaderPreview() {
    SWSectionHeader(drawableId = 0)
}
