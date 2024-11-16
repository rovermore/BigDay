package com.smallworldfs.moneytransferapp.compose.widgets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.compose.colors.onSurfaceMediumEmphasis
import com.smallworldfs.moneytransferapp.compose.style.SWChipStyle

@Composable
fun SWChipLeftBox(
    style: SWChipStyle
) {
    Box {
        when (style) {
            is SWChipStyle.Star -> {
                Icon(
                    painter = painterResource(id = R.drawable.linear_star),
                    tint = onSurfaceMediumEmphasis,
                    contentDescription = "left_icon",
                )
            }

            is SWChipStyle.Flag -> {
                SWImageFlag(
                    modifier = Modifier
                        .size(24.dp),
                    imageUrl = style.imageUrl,
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 420)
@Composable
fun SWChipLeftBoxPreview() {
    SWChipLeftBox(
        style = SWChipStyle.Star,
    )
}
