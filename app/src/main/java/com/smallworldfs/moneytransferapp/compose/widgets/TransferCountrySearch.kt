package com.smallworldfs.moneytransferapp.compose.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
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
import com.smallworldfs.moneytransferapp.domain.migrated.base.Action
import com.smallworldfs.moneytransferapp.compose.colors.blueBackgroundWelcome
import com.smallworldfs.moneytransferapp.compose.colors.colorGrayLight

@Composable
fun TransferCountrySearch(
    modifier: Modifier = Modifier,
    onSearchClick: Action
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onSearchClick() }
        ) {
            SWText(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                text = stringResource(id = R.string.search_hint_text),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = colorGrayLight
            )
            Spacer(Modifier.weight(1f))
            Icon(
                modifier = Modifier.size(18.dp, 18.dp),
                painter = painterResource(id = R.drawable.icn_search_icon),
                contentDescription = "Search button",
                tint = blueBackgroundWelcome
            )
        }
        Spacer(
            modifier = Modifier
                .height(1.dp)
                .background(color = colorGrayLight)
                .fillMaxWidth()
        )
    }
}

@Preview(showBackground = true, widthDp = 420)
@Composable
fun TransferCountrySearchPreview() {
    TransferCountrySearch(
        onSearchClick = {}
    )
}
