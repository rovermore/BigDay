package com.smallworldfs.moneytransferapp.compose.widgets

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
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
import com.smallworldfs.moneytransferapp.compose.colors.colorRedError
import com.smallworldfs.moneytransferapp.compose.colors.darkGreyText

@Composable
fun SWErrorScreenLayout(
    retryListener: () -> Unit
) {
    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(id = R.drawable.transactional_icn_errorscreen),
            contentDescription = "error_icon",
        )

        OutlinedButton(
            modifier = Modifier.padding(top = 32.dp),
            onClick = { retryListener() },
            border = BorderStroke(1.dp, colorRedError),
            shape = RoundedCornerShape(70),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = colorRedError),
        ) {
            SWText(
                modifier = Modifier.padding(horizontal = 40.dp),
                text = stringResource(id = R.string.retry_text),
                fontWeight = FontWeight.ExtraBold,
                color = colorRedError,
                fontSize = 16.sp,
            )
        }

        SWText(
            modifier = Modifier.padding(start = 32.dp, end = 32.dp, top = 32.dp),
            text = stringResource(id = R.string.error_loading_description_label),
            fontSize = 18.sp,
        )

        SWText(
            modifier = Modifier.padding(top = 24.dp),
            text = stringResource(id = R.string.error_loading_generic_label),
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = darkGreyText,
        )
    }
}

@Preview(showBackground = true, widthDp = 420)
@Composable
fun SWErrorScreenLayoutPreview() {
    SWErrorScreenLayout(
        retryListener = {}
    )
}
