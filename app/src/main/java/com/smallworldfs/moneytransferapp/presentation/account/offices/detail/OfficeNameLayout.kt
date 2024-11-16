package com.smallworldfs.moneytransferapp.presentation.account.offices.detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.presentation.account.offices.model.OfficeUIModel
import com.smallworldfs.moneytransferapp.compose.colors.blueAccentColor
import com.smallworldfs.moneytransferapp.compose.colors.greySeparator
import com.smallworldfs.moneytransferapp.compose.colors.lightGreyText
import com.smallworldfs.moneytransferapp.compose.colors.neutral0
import com.smallworldfs.moneytransferapp.compose.widgets.SWText

@Composable
fun OfficeName(office: OfficeUIModel, distance: String, officeCallbacks: OfficeDetailsCallbacks) {
    var showDetailView by rememberSaveable { mutableStateOf(false) }
    val showDetailText =
        if (!showDetailView) stringResource(id = R.string.office_detail_more_label)
        else stringResource(id = R.string.office_detail_less_label)

    Box(
        Modifier
            .background(color = neutral0)
            .fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {

            Row(modifier = Modifier.padding(bottom = 16.dp)) {
                Column {

                    Image(
                        modifier = Modifier.padding(start = 16.dp, top = 12.dp),
                        painter = painterResource(id = R.drawable.account_icn_office),
                        contentDescription = ""
                    )
                    SWText(
                        modifier = Modifier.padding(start = 8.dp, top = 4.dp),
                        text = distance,
                        color = lightGreyText
                    )
                }
                Column {

                    SWText(
                        modifier = Modifier.padding(start = 26.dp),
                        text = office.name,
                        color = Color.Black,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold
                    )

                    SWText(
                        modifier = Modifier.padding(start = 26.dp, top = 8.dp),
                        text = office.address,
                        color = lightGreyText,
                        fontSize = 16.sp
                    )
                }
            }

            Divider(
                color = greySeparator,
                thickness = 1.dp
            )

            Row(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(end = 12.dp)
                    .clickable {
                        showDetailView = !showDetailView
                        officeCallbacks.registerEvent(if (showDetailView) "click_more" else "click_less")
                    },
                verticalAlignment = Alignment.CenterVertically,
            ) {

                SWText(
                    modifier = Modifier
                        .padding(8.dp),
                    text = showDetailText,
                    textAlign = TextAlign.End,
                    color = blueAccentColor,
                    fontSize = 16.sp,
                )
                if (showDetailView)
                    Image(
                        painter = painterResource(id = R.drawable.ic_expand_coin_calculator),
                        contentDescription = "",
                    )
                else
                    Image(
                        modifier = Modifier.rotate(180f),
                        painter = painterResource(id = R.drawable.ic_expand_coin_calculator),
                        contentDescription = "",
                    )
            }
            AnimatedVisibility(showDetailView) {
                OfficeDetailView(
                    office,
                    { phone -> officeCallbacks.onPhoneClicked(phone) },
                    { mail -> officeCallbacks.onMailClicked(mail) },
                    { event -> officeCallbacks.registerEvent(event) }
                )
            }
        }
    }
}
