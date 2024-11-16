package com.smallworldfs.moneytransferapp.compose.widgets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.compose.colors.darkGreyText
import com.smallworldfs.moneytransferapp.compose.colors.secondaryDarkGreyText

@Composable
fun SearchEmptyView() {

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            modifier = Modifier.padding(16.dp),
            painter = painterResource(id = R.drawable.getstarted_icn_failsearch),
            contentDescription = "",

        )

        Column(
            modifier = Modifier
        ) {
            SWText(
                text = stringResource(R.string.title_search_empty_view),
                fontSize = 16.sp,
                color = darkGreyText,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Left
            )
            SWText(
                text = stringResource(R.string.search_subtitle_empty_view),
                fontSize = 14.sp,
                color = secondaryDarkGreyText,
                textAlign = TextAlign.Left
            )
        }
    }
}

@Preview(showBackground = true, name = "Select Destination Country preview", widthDp = 420)
@Composable
fun SearchEmptyViewPreview() {
    SearchEmptyView()
}
