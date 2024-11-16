package com.smallworldfs.moneytransferapp.presentation.home.dialog

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.compose.colors.black
import com.smallworldfs.moneytransferapp.compose.colors.colorBlueOceanDark
import com.smallworldfs.moneytransferapp.compose.colors.colorGrayLight
import com.smallworldfs.moneytransferapp.compose.colors.colorGreenMain
import com.smallworldfs.moneytransferapp.compose.colors.neutral0
import com.smallworldfs.moneytransferapp.compose.widgets.SWButton
import com.smallworldfs.moneytransferapp.compose.widgets.SWText
import com.smallworldfs.moneytransferapp.domain.migrated.base.Action

@Composable
fun MarketingPreferencesDialog(
    onNavigateToMarketingPref: Action,
    onDismiss: Action
) {

    Dialog(
        onDismissRequest = { onDismiss() },
    ) {
        ConstraintLayout {
            val (icon, layout) = createRefs()

            Image(
                painter = painterResource(R.drawable.popup_icn_app3x),
                contentDescription = "avatar",
                contentScale = ContentScale.Inside,
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .border(8.dp, neutral0, CircleShape)
                    .constrainAs(icon) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .zIndex(1f),
            )

            Column(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(8.dp))
                    .background(neutral0)
                    .fillMaxWidth()
                    .constrainAs(layout) {
                        top.linkTo(icon.top, 32.dp)
                    }
                    .clip(RoundedCornerShape(12.dp)),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.TopEnd
                ) {
                    Image(
                        painter = painterResource(R.drawable.checkout_icn_close),
                        contentDescription = "exit",
                        contentScale = ContentScale.Inside,
                        modifier = Modifier
                            .size(44.dp)
                            .padding(top = 16.dp, end = 16.dp)
                            .clickable { onDismiss() }
                    )
                }

                SWText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    text = stringResource(id = R.string.manage_your_settings_title),
                    color = black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                )

                Image(
                    painter = painterResource(R.drawable.popup_icn_email),
                    contentDescription = "avatar",
                    modifier = Modifier
                        .size(44.dp)
                )

                SWText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    text = stringResource(id = R.string.manage_your_settings_body),
                    color = colorGrayLight,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )

                SWButton(
                    modifier = Modifier
                        .padding(top = 44.dp, bottom = 88.dp),
                    shape = RoundedCornerShape(8.dp),
                    onClick = {
                        onNavigateToMarketingPref()
                    },
                    text = stringResource(id = R.string.settings),
                    backgroundColor = colorGreenMain,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textModifier = Modifier.padding(0.dp),
                    textColor = colorBlueOceanDark
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 420)
@Composable
fun MarketingPreferencesDialogPreview() {
    MarketingPreferencesDialog(
        { },
        { }
    )
}
