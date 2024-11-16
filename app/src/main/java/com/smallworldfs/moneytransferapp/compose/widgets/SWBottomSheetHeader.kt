package com.smallworldfs.moneytransferapp.compose.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.compose.colors.onSecondaryMediumEmphasis
import com.smallworldfs.moneytransferapp.compose.colors.onSurfaceHighEmphasis
import com.smallworldfs.moneytransferapp.compose.colors.onSurfaceMediumEmphasis
import com.smallworldfs.moneytransferapp.compose.colors.secondary
import com.smallworldfs.moneytransferapp.compose.colors.surfaceOverlay
import com.smallworldfs.moneytransferapp.compose.style.SWTextStyle

@Composable
fun BottomSheetHeader(headerTitle: String, darkTheme: Boolean, dismissListener: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (darkTheme) secondary else Color.Transparent)
            .padding(
                horizontal = 16.dp,
                vertical = 8.dp,
            ),
    ) {

        Divider(
            modifier = Modifier
                .padding(top = 16.dp, bottom = 8.dp)
                .width(32.dp)
                .height(4.dp)
                .align(Alignment.CenterHorizontally)
                .clip(RoundedCornerShape(100)),
            color = onSurfaceMediumEmphasis,
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                modifier = Modifier
                    .size(24.dp)
                    .clickable { dismissListener() },
                painter = painterResource(id = R.drawable.cross_icon_bottom_sheet), contentDescription = "close_icon_bottom_sheet",
                colorFilter = if (darkTheme)
                    ColorFilter.tint(
                        onSecondaryMediumEmphasis,
                    ) else null,

            )

            Column(
                modifier = Modifier
                    .padding(end = 24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                SWText(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                    text = headerTitle,
                    style = SWTextStyle.Heading,
                    color = if (darkTheme) surfaceOverlay else onSurfaceHighEmphasis,
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "Light Bottom Sheet Header", widthDp = 420)
@Composable
fun BottomSheetHeaderLightPreview() {
    BottomSheetHeader(headerTitle = "Title Section", false) {}
}

@Preview(showBackground = true, name = "Dark Bottom Sheet Header", widthDp = 420)
@Composable
fun BottomSheetHeaderDarkPreview() {
    BottomSheetHeader(headerTitle = "Title Section", true) {}
}
