package com.smallworldfs.moneytransferapp.presentation.status

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.compose.colors.colorBlueOcean
import com.smallworldfs.moneytransferapp.compose.colors.colorBlueOceanDark
import com.smallworldfs.moneytransferapp.compose.colors.colorGreenMain
import com.smallworldfs.moneytransferapp.compose.colors.darkGreyText
import com.smallworldfs.moneytransferapp.compose.widgets.SWButton
import com.smallworldfs.moneytransferapp.compose.widgets.SWText

@Composable
fun StatusNoTransactionEmptyView(onSendMoneyButtonClicked: () -> Unit) {
    Column(
        Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally,
        content = {
            Image(
                painter = painterResource(id = R.drawable.status_icn_emptyspace),
                contentDescription = "",
            )

            SWText(
                Modifier.padding(top = 25.dp),
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                color = darkGreyText,
                text = stringResource(id = R.string.status_empty_view_no_transactions_created),
            )

            SWText(
                Modifier.padding(top = 10.dp),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = darkGreyText,
                text = stringResource(id = R.string.status_empty_view_no_transactions_created_subtitle),
            )

            SWButton(
                modifier = Modifier
                    .width(180.dp)
                    .padding(top = 24.dp),
                backgroundColor = colorGreenMain,
                contentColor = colorBlueOceanDark,
                text = stringResource(id = R.string.send_money_button_text),
                textModifier = Modifier.padding(vertical = 4.dp),
                onClick = { onSendMoneyButtonClicked() },
                textColor = colorBlueOcean,
                fontSize = 14.sp,
                shape = RoundedCornerShape(50),
            )
        },
    )
}

@Preview
@Composable
fun StatusNoTransactionEmptyViewPreview() {
    StatusNoTransactionEmptyView {}
}
