package com.smallworldfs.moneytransferapp.compose.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smallworldfs.moneytransferapp.compose.colors.colorBlueOceanDark

@Composable
fun PagerItem(
    modifier: Modifier = Modifier,
    title: String,
    textColor: Color,
    notificationNumber: Int = 0
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier.padding(top = 16.dp, bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            SWText(
                modifier = Modifier,
                text = title,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 14.sp,
                color = textColor,
            )
            if (notificationNumber != 0)
                BubbleIndicator(text = notificationNumber.toString())
        }
    }
}

@Preview(showBackground = true, widthDp = 420)
@Composable
fun PagerIndicatorPreview() {
    PagerItem(
        title = "SEND TO",
        textColor = colorBlueOceanDark,
        notificationNumber = 1,
    )
}
