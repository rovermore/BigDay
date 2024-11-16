package com.smallworldfs.moneytransferapp.presentation.myactivity

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import com.smallworldfs.moneytransferapp.compose.colors.defaultGreyLightBackground
import com.smallworldfs.moneytransferapp.compose.colors.defaultTextColor
import com.smallworldfs.moneytransferapp.compose.style.SWTextStyle
import com.smallworldfs.moneytransferapp.compose.widgets.SWButton
import com.smallworldfs.moneytransferapp.compose.widgets.SWText

@Composable
fun EmptyActivityView(onSendMoneyButtonClicked: () -> Unit) {
    Column(
        Modifier
            .fillMaxSize()
            .background(defaultGreyLightBackground),
        verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally,
        content = {
            Image(
                painter = painterResource(id = R.drawable.account_img_myactivitywelcome),
                contentDescription = "",
            )

            SWText(
                Modifier.padding(top = 40.dp),
                style = SWTextStyle.HeadingBold,
                text = stringResource(id = R.string.my_activity_empty_view_text1),
            )

            SWText(
                Modifier.padding(top = 10.dp),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = defaultTextColor,
                text = stringResource(id = R.string.my_activity_empty_view_text2),
            )

            SWText(
                Modifier.padding(top = 80.dp),
                style = SWTextStyle.Heading,
                text = stringResource(id = R.string.my_activity_empty_view_text3),
            )

            SWButton(
                modifier = Modifier
                    .width(180.dp)
                    .padding(top = 30.dp),
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
fun EmptyActivityViewPreview() {
    EmptyActivityView {}
}
