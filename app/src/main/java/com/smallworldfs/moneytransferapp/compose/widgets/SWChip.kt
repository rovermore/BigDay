package com.smallworldfs.moneytransferapp.compose.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.compose.colors.onSurfaceMediumEmphasis
import com.smallworldfs.moneytransferapp.compose.state.SWChipState
import com.smallworldfs.moneytransferapp.compose.style.SWChipStyle
import com.smallworldfs.moneytransferapp.domain.migrated.base.Action
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY

@Composable
fun SWChip(
    modifier: Modifier = Modifier,
    state: SWChipState,
    leftBox: @Composable () -> Unit,
    rightIcon: Int? = null,
    text: String = STRING_EMPTY,
    onClick: Action = {}
) {

    with(state) {
        Box(
            modifier = modifier
                .clickable { onClick() },
        ) {
            Row(
                modifier = Modifier
                    .background(
                        color = backgroundColor,
                        shape = RoundedCornerShape(50.dp),
                    )
                    .border(
                        width = borderWidth.dp,
                        color = borderColor,
                        shape = RoundedCornerShape(50.dp),
                    )
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                leftBox()

                SWText(
                    modifier = Modifier
                        .padding(
                            horizontal = 8.dp,
                        ),
                    style = textStyle,
                    text = text,
                )

                rightIcon?.let {
                    Icon(
                        painter = painterResource(id = it),
                        tint = onSurfaceMediumEmphasis,
                        contentDescription = "right_icon",
                    )
                }
            }
        }
    }
}

@Preview(showBackground = false, widthDp = 420)
@Composable
fun SWChipPreview() {
    SWChip(
        state = SWChipState.Enabled,
        leftBox = { SWChipLeftBox(style = SWChipStyle.Star) },
        rightIcon = R.drawable.bold_close_circle,
        text = "Chip",
        onClick = {}
    )
}
