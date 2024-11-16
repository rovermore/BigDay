package com.smallworldfs.moneytransferapp.compose.widgets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.Top
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.smallworldfs.moneytransferapp.compose.colors.onSurfaceMediumEmphasis
import com.smallworldfs.moneytransferapp.compose.style.SWStartListItemStyle
import com.smallworldfs.moneytransferapp.compose.style.SWStartListItemStyle.Star
import com.smallworldfs.moneytransferapp.compose.style.SWTextStyle.Body1
import com.smallworldfs.moneytransferapp.compose.style.SWTextStyle.Body2
import com.smallworldfs.moneytransferapp.compose.style.SWTextStyle.Overline
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY

@Composable
fun SWListItem(
    startListItem: SWStartListItemStyle,
    overlineText: String = STRING_EMPTY,
    body1: String = STRING_EMPTY,
    body2: String = STRING_EMPTY
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = if (startListItem is Star) Top else CenterVertically,
    ) {
        StartListItem(startListItem)

        Column {

            if (overlineText.isNotEmpty()) {
                SWText(
                    text = overlineText,
                    style = Overline,
                )
            }

            if (body1.isNotEmpty()) {
                SWText(
                    text = body1,
                    style = Body1,
                )
            }

            if (body2.isNotEmpty()) {
                SWText(
                    text = body2,
                    color = onSurfaceMediumEmphasis,
                    style = Body2,
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "List Item None", widthDp = 420)
@Composable
fun SWListItemPreviewNone() {
    SWListItem(
        startListItem = SWStartListItemStyle.None,
        overlineText = "OVERLINE",
        body1 = "Two-line item",
        body2 = "Second Line",
    )
}

@Preview(showBackground = true, name = "List Item Box", widthDp = 420)
@Composable
fun SWListItemPreviewBox() {
    SWListItem(
        startListItem = SWStartListItemStyle.PlaceHolder,
        overlineText = "OVERLINE",
        body1 = "Two-line item",
        body2 = "Second Line",
    )
}

@Preview(showBackground = true, name = "List Item TikBox", widthDp = 420)
@Composable
fun SWListItemPreviewCheckBox() {
    SWListItem(
        startListItem = SWStartListItemStyle.TickBox(
            isChecked = false,
            onCheckBoxClicked = {},
        ),
        overlineText = "OVERLINE",
        body1 = "Two-line item",
        body2 = "Second Line",
    )
}

@Preview(showBackground = true, name = "List Item TikBox", widthDp = 420)
@Composable
fun SWListItemPreviewFlagImage() {
    SWListItem(
        startListItem = SWStartListItemStyle.FlagImage(imageUrl = ""),
        overlineText = "OVERLINE",
        body1 = "Two-line item",
        body2 = "Second Line",
    )
}

@Preview(showBackground = true, name = "List Item Star", widthDp = 420)
@Composable
fun SWListItemPreviewStar() {
    SWListItem(
        startListItem = Star,
        overlineText = "OVERLINE",
        body1 = "Two-line item",
        body2 = "Second Line",
    )
}

@Preview(showBackground = true, name = "List Item Empty", widthDp = 420)
@Composable
fun SWListItemPreviewEmpty() {
    SWListItem(
        startListItem = SWStartListItemStyle.Empty,
        body1 = "Sorry! country not found.",
        body2 = "Please check the spelling or select another country to continue",
    )
}
