package com.smallworldfs.moneytransferapp.compose.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.compose.colors.blockDisabledColor
import com.smallworldfs.moneytransferapp.compose.colors.darkGreyText
import com.smallworldfs.moneytransferapp.compose.colors.lightGreyText
import com.smallworldfs.moneytransferapp.compose.colors.transparent
import com.smallworldfs.moneytransferapp.compose.colors.neutral0
import com.smallworldfs.moneytransferapp.presentation.account.account.model.AccountMenuItemUIModel
import com.smallworldfs.moneytransferapp.presentation.account.account.model.MenuIcon

@Composable
fun SWMenuBlockItem(
    block: AccountMenuItemUIModel,
    onItemClicked: (AccountMenuItemUIModel) -> Unit
) {

    Card(
        modifier = Modifier
            .height(148.dp)
            .clickable { onItemClicked(block) }
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            contentColor = neutral0,
            containerColor = neutral0,
            disabledContainerColor = transparent,
            disabledContentColor = transparent
        )
    ) {

        Box(modifier = Modifier.fillMaxSize()) {

            Box(
                modifier = Modifier
                    .wrapContentSize()
                    .align(Alignment.Center),
                contentAlignment = Alignment.Center,
            ) {

                if (block.numNewInfo > 0)
                    BubbleIndicator(
                        text = block.numNewInfo.toString(),
                        modifier = Modifier.align(Alignment.TopEnd)
                    )

                Column(modifier = Modifier.align(Alignment.Center)) {

                    if (block.icon is MenuIcon.Icon)
                        Image(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            painter = painterResource(id = block.icon.resourceId),
                            contentDescription = "Icon Section"
                        )

                    SWText(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        text = block.title,
                        fontSize = 14.sp,
                        color = if (block.active) darkGreyText else blockDisabledColor,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )

                    SWText(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        text = block.description,
                        fontSize = 14.sp,
                        color = if (block.active) lightGreyText else blockDisabledColor,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 420)
@Composable
fun SWMenuBlockItemPreview() {
    SWMenuBlockItem(
        block = AccountMenuItemUIModel(
            title = "Title",
            description = "Description",
            numNewInfo = 3,
            icon = MenuIcon.Icon(resourceId = R.drawable.account_icn_settings2)
        ),
        onItemClicked = {}
    )
}
