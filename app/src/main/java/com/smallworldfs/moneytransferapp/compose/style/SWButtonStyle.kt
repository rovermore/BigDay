package com.smallworldfs.moneytransferapp.compose.style

import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.smallworldfs.moneytransferapp.compose.colors.colorGreenMain
import com.smallworldfs.moneytransferapp.compose.colors.defaultGreyLightBackground
import com.smallworldfs.moneytransferapp.compose.colors.neutral0

sealed class SWButtonStyle(
    val modifier: Modifier = Modifier,
    val contentColor: Color = neutral0,
    val backgroundColor: Color = colorGreenMain,
    val shape: Shape = RoundedCornerShape(50),
    val textAlign: TextAlign = TextAlign.Center,
    val textColor: Color = neutral0
) {

    class Stroked(borderColor: Color = defaultGreyLightBackground) : SWButtonStyle(
        modifier = Modifier
            .border(width = 2.dp, color = borderColor, shape = RoundedCornerShape(50))
            .clip(RoundedCornerShape(16.dp)),
    )

    class Flat(backgroundColor: Color = colorGreenMain, textColor: Color = neutral0) : SWButtonStyle(
        backgroundColor = backgroundColor,
        textColor = textColor
    )

    class Full(backgroundColor: Color = colorGreenMain, textColor: Color = neutral0) : SWButtonStyle(
        backgroundColor = backgroundColor,
        textColor = textColor,
        shape = RoundedCornerShape(0.dp)
    )
}
