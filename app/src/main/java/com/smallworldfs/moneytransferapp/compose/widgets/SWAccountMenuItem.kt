package com.smallworldfs.moneytransferapp.compose.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.compose.colors.darkGreyText
import com.smallworldfs.moneytransferapp.compose.colors.neutral0
import com.smallworldfs.moneytransferapp.presentation.account.account.model.AccountMenuItemUIModel
import com.smallworldfs.moneytransferapp.presentation.account.account.model.MenuIcon

@Composable
fun SWAccountMenuItem(
    item: AccountMenuItemUIModel,
    modifier: Modifier = Modifier,
    onItemClicked: (AccountMenuItemUIModel) -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
            .clickable {
                onItemClicked(item)
            },
        elevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .background(neutral0),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (item.icon is MenuIcon.Icon)
                Icon(
                    modifier = Modifier
                        .padding(start = 8.dp, end = 8.dp),
                    tint = darkGreyText,
                    painter = painterResource(item.icon.resourceId),
                    contentDescription = item.description
                )
            SWText(
                text = item.description,
                color = darkGreyText,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 420)
@Composable
fun Preview() {
    SWAccountMenuItem(
        item = AccountMenuItemUIModel(
            title = "Title",
            description = "Description",
            icon = MenuIcon.Icon(resourceId = R.drawable.account_icn_settings2)
        ),
        onItemClicked = {}
    )
}
