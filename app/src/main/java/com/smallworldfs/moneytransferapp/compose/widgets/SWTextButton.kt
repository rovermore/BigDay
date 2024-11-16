package com.smallworldfs.moneytransferapp.compose.widgets

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smallworldfs.moneytransferapp.compose.colors.colorBlueOceanDark
import com.smallworldfs.moneytransferapp.domain.migrated.base.Action

@Composable
fun SWTextButton(
    text: String,
    modifier: Modifier = Modifier,
    colorText: Color = colorBlueOceanDark,
    fontSize: TextUnit = 18.sp,
    fontWeight: FontWeight = FontWeight.SemiBold,
    icon: Int? = null,
    iconColor: Color = colorBlueOceanDark,
    clickAction: Action
) {
    TextButton(
        modifier = modifier,
        onClick = { clickAction() },
    ) {
        icon?.let {
            Icon(
                modifier = Modifier
                    .width(24.dp)
                    .height(24.dp),
                painter = painterResource(id = it),
                contentDescription = "logo",
                tint = iconColor,
            )
        }

        SWText(
            text = text,
            color = colorText,
            fontSize = fontSize,
            fontWeight = fontWeight,
        )
    }
}

@Preview(showBackground = true, widthDp = 420)
@Composable
fun SWTextButtonPreview() {
    SWTextButton(
        "Text"
    ) {}
}
