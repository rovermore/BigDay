package com.smallworldfs.moneytransferapp.presentation.status

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.compose.colors.mainBlue
import com.smallworldfs.moneytransferapp.compose.colors.mainBlueDark1
import com.smallworldfs.moneytransferapp.compose.colors.neutral0
import com.smallworldfs.moneytransferapp.compose.widgets.SWText

@Composable
fun TransactionsHeader(title: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(mainBlueDark1, mainBlue),
                    start = Offset(0f, 0f),
                    end = Offset.Infinite,
                ),
            ),
    ) {
        SWText(
            Modifier.padding(vertical = 20.dp, horizontal = 25.dp),
            color = neutral0,
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Preview
@Composable
fun TransactionHeaderPreview() {
    TransactionsHeader(stringResource(id = R.string.my_transactions_label))
}
