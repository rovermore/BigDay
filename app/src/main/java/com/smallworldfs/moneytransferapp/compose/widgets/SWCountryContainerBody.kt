package com.smallworldfs.moneytransferapp.compose.widgets

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smallworldfs.moneytransferapp.compose.colors.info700
import com.smallworldfs.moneytransferapp.compose.colors.onSurfaceHighEmphasis
import com.smallworldfs.moneytransferapp.compose.state.SWCountryContainerState.Primary.bodyStyle
import com.smallworldfs.moneytransferapp.compose.style.SWCountryContainerStyle
import com.smallworldfs.moneytransferapp.compose.style.SWTextStyle
import com.smallworldfs.moneytransferapp.domain.migrated.base.Action
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY

@Composable
fun SWCountryContainerBody(
    text: String = STRING_EMPTY,
    textStyle: SWTextStyle,
    textColor: Color,
    containerStyle: SWCountryContainerStyle,
    onFirstTextClick: Action = {},
    onSecondTextClick: Action = {}
) {

    when (containerStyle) {
        is SWCountryContainerStyle.SmallFlag, is SWCountryContainerStyle.BigFlag -> {
            SWText(
                text = text,
                color = textColor,
                style = textStyle,
            )
        }

        is SWCountryContainerStyle.ClickableText -> {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                ClickableText(
                    text = AnnotatedString(containerStyle.firstText),
                    style = TextStyle(
                        color = info700,
                        fontSize = 12.sp,
                        fontFamily = textStyle.fontFamily,
                    ),
                    onClick = {
                        onFirstTextClick()
                    },
                )
                SWText(
                    modifier = Modifier
                        .padding(horizontal = 4.dp),
                    text = containerStyle.separator,
                    color = textColor,
                    style = textStyle,
                )
                ClickableText(
                    text = AnnotatedString(containerStyle.secondText),
                    style = TextStyle(
                        color = info700,
                        fontSize = 12.sp,
                        fontFamily = textStyle.fontFamily,
                    ),
                    onClick = {
                        onSecondTextClick()
                    },
                )
            }
        }
    }
}

@Preview(showBackground = false, widthDp = 420)
@Composable
fun SWCountryContainerBodyPreview() {
    SWCountryContainerBody(
        textStyle = bodyStyle,
        containerStyle = SWCountryContainerStyle.ClickableText(
            imageUrl = "",
            firstText = "Log in",
            separator = "or",
            secondText = "Sign up",
        ),
        textColor = onSurfaceHighEmphasis
    )
}
