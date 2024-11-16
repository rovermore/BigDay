package com.smallworldfs.moneytransferapp.compose.widgets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.compose.state.SWCountryContainerState
import com.smallworldfs.moneytransferapp.compose.style.SWCountryContainerStyle
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY

@Composable
fun SWCountryContainer(
    title: String,
    body: String = STRING_EMPTY,
    state: SWCountryContainerState,
    style: SWCountryContainerStyle,
    isLoading: Boolean,
    icon: Int? = null
) {
    with(state) {
        Card(
            backgroundColor = backgroundColor,
            shape = RoundedCornerShape(8.dp),
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {

                if (isLoading) {
                    SWProgressSpinner(
                        modifier = Modifier
                            .size(48.dp)
                    )
                } else {
                    SWCountryContainerLeftFlag(
                        style = style,
                    )
                }

                Column(
                    modifier = Modifier
                        .padding(start = 16.dp),
                ) {
                    SWText(
                        text = title,
                        style = titleStyle,
                        color = titleColor,
                    )

                    if (body.isNotEmpty()) {
                        SWCountryContainerBody(
                            text = body,
                            textStyle = bodyStyle,
                            textColor = bodyColor,
                            containerStyle = style,
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                icon?.let {
                    Icon(
                        modifier = Modifier
                            .padding(end = 16.dp),
                        painter = painterResource(id = it),
                        contentDescription = "right_icon",
                        tint = iconTintColor,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = false, widthDp = 420)
@Composable
fun SWCountryContainerPrimarySmallPreview() {
    SWCountryContainer(
        title = "Title",
        body = "Second line",
        state = SWCountryContainerState.Primary,
        style = SWCountryContainerStyle.SmallFlag(""),
        isLoading = true,
        icon = R.drawable.linear_arrow_right,
    )
}

@Preview(showBackground = false, widthDp = 420)
@Composable
fun SWCountryContainerPrimaryBigPreview() {
    SWCountryContainer(
        title = "Title",
        body = "Second line",
        state = SWCountryContainerState.Primary,
        style = SWCountryContainerStyle.BigFlag(""),
        isLoading = false,
        icon = R.drawable.linear_arrow_right,
    )
}

@Preview(showBackground = false, widthDp = 420)
@Composable
fun SWCountryContainerSecondarySmallPreview() {
    SWCountryContainer(
        title = "Title",
        body = "Second line",
        state = SWCountryContainerState.Secondary,
        style = SWCountryContainerStyle.SmallFlag(""),
        isLoading = false,
        icon = R.drawable.linear_arrow_right,
    )
}

@Preview(showBackground = false, widthDp = 420)
@Composable
fun SWCountryContainerSecondaryBigPreview() {
    SWCountryContainer(
        title = "Title",
        body = "Second line",
        state = SWCountryContainerState.Secondary,
        style = SWCountryContainerStyle.BigFlag(""),
        isLoading = false,
        icon = R.drawable.linear_arrow_right,
    )
}

@Preview(showBackground = false, widthDp = 420)
@Composable
fun SWCountryContainerClickableTextPreview() {
    SWCountryContainer(
        title = "Title",
        body = "Second line",
        state = SWCountryContainerState.Primary,
        style = SWCountryContainerStyle.ClickableText(
            imageUrl = "",
            firstText = "Log in",
            separator = "or",
            secondText = "Sign up",
        ),
        isLoading = false,
        icon = R.drawable.linear_arrow_right,
    )
}
