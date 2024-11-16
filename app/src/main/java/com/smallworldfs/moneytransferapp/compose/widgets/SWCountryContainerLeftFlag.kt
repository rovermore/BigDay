package com.smallworldfs.moneytransferapp.compose.widgets

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.compose.style.SWCountryContainerStyle

@Composable
fun SWCountryContainerLeftFlag(
    style: SWCountryContainerStyle
) {
    when (style) {
        is SWCountryContainerStyle.SmallFlag -> {
            SWImageFlag(
                size = 32.dp,
                imageUrl = style.imageUrl,
                errorImage = painterResource(id = R.drawable.linear_global_search),
                fallbackImage = painterResource(id = R.drawable.linear_global_search),
            )
        }

        is SWCountryContainerStyle.BigFlag, is SWCountryContainerStyle.ClickableText -> {
            SWImageFlag(
                size = 48.dp,
                imageUrl = style.imageUrl,
                errorImage = painterResource(id = R.drawable.linear_global_search),
                fallbackImage = painterResource(id = R.drawable.linear_global_search),
            )
        }
    }
}
