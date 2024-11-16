package com.smallworldfs.moneytransferapp.compose.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.compose.colors.defaultGreyLightBackground
import com.smallworldfs.moneytransferapp.compose.colors.info600
import com.smallworldfs.moneytransferapp.compose.colors.neutral0
import com.smallworldfs.moneytransferapp.compose.colors.onSurfaceMediumEmphasis
import com.smallworldfs.moneytransferapp.compose.style.SWTextStyle
import com.smallworldfs.moneytransferapp.domain.migrated.base.Action

@Composable
fun SWFormButton(
    modifier: Modifier = Modifier,
    text: String,
    onButtonClicked: Action = {}
) {

    Box(
        modifier = modifier
            .border(width = 2.dp, color = defaultGreyLightBackground, shape = RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp)),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onButtonClicked() }
                .background(neutral0),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            SWText(
                modifier = Modifier
                    .padding(start = 12.dp, end = 12.dp, top = 20.dp, bottom = 20.dp)
                    .weight(5F),
                style = SWTextStyle.Body1,
                text = text,
                color = onSurfaceMediumEmphasis
            )

            Icon(
                modifier = Modifier
                    .size(24.dp)
                    .weight(1F),
                painter = painterResource(id = R.drawable.arrow_down),
                contentDescription = "down arrow",
                tint = info600
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 420, backgroundColor = 4294967295)
@Composable
fun SWFormButtonPreview() {
    SWFormButton(
        text = "Sending money from"
    )
}
