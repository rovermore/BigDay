package com.smallworldfs.moneytransferapp.compose.widgets.transferdetails

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.compose.colors.black
import com.smallworldfs.moneytransferapp.compose.widgets.BubbleIndicator
import com.smallworldfs.moneytransferapp.compose.widgets.SWText
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY

@Composable
fun SWTransferSummaryItemHeader(
    transactionStatus: String,
    transactionIcon: Int?,
    showBubbleIndicator: Boolean
) {
    Row {
        Row(
            modifier = Modifier
                .padding(start = 8.dp, top = 8.dp, bottom = 8.dp, end = 4.dp)
                .wrapContentSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            transactionIcon?.let {
                Icon(
                    modifier = Modifier
                        .width(24.dp)
                        .height(24.dp),
                    painter = painterResource(id = transactionIcon),
                    contentDescription = "logo",
                    tint = black,
                )
            }

            SWText(
                modifier = Modifier
                    .padding(start = 4.dp),
                text = transactionStatus,
                color = black,
                fontWeight = FontWeight.Bold
            )
        }

        if (showBubbleIndicator) {
            BubbleIndicator(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .size(8.dp),
                text = STRING_EMPTY
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 420)
@Composable
fun SWTransferSummaryItemHeaderPreview() {
    SWTransferSummaryItemHeader(
        transactionStatus = "Pending bank transfer",
        transactionIcon = R.drawable.ic_action_close_white,
        showBubbleIndicator = true
    )
}
