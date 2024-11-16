package com.smallworldfs.moneytransferapp.compose.widgets

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.compose.colors.colorBlueOceanDark
import com.smallworldfs.moneytransferapp.compose.colors.colorGreenMain
import com.smallworldfs.moneytransferapp.domain.migrated.base.Action

@Composable
fun LoginToSendFooter(
    modifier: Modifier = Modifier,
    navigateToWelcomeActivity: Action
) {
    SWButton(
        modifier = modifier
            .fillMaxWidth(),
        backgroundColor = colorGreenMain,
        shape = RoundedCornerShape(0),
        text = stringResource(id = R.string.login_to_send_money),
        fontSize = 18.sp,
        textColor = colorBlueOceanDark,
        onClick = { navigateToWelcomeActivity() },
    ) {
        Icon(
            modifier = Modifier
                .size(18.dp, 18.dp)
                .rotate(90f),
            painter = painterResource(id = R.drawable.ic_chevron_right),
            contentDescription = null,
            tint = colorBlueOceanDark,
        )
    }
}

@Preview(showBackground = true, widthDp = 420)
@Composable
fun LoginToSendFooterPreview() {
    LoginToSendFooter(
        Modifier,
        {},
    )
}
